package com.tokopedia.autocomplete.suggestion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.RouteManager
import com.tokopedia.autocomplete.*

import com.tokopedia.autocomplete.adapter.ItemClickListener
import com.tokopedia.autocomplete.adapter.SearchPageAdapter
import com.tokopedia.autocomplete.analytics.AppScreen
import com.tokopedia.autocomplete.di.AutoCompleteComponent
import com.tokopedia.autocomplete.di.DaggerAutoCompleteComponent
import com.tokopedia.autocomplete.AutoCompleteActivity
import com.tokopedia.discovery.common.model.SearchParameter
import kotlinx.android.synthetic.main.fragment_suggestion.*
import javax.inject.Inject

class SuggestionFragment : BaseDaggerFragment(), SuggestionContract.View, ItemClickListener {

    private val SEARCH_PARAMETER = "SEARCH_PARAMETER"
    private val MP_SEARCH_AUTOCOMPLETE = "mp_search_autocomplete"
    private val PAGER_POSITION_PRODUCT = 0
    private val PAGER_POSITION_SHOP = 1

    @Inject
    lateinit var presenter: SuggestionPresenter

    private lateinit var networkErrorMessage: String

    private var performanceMonitoring: PerformanceMonitoring? = null

    private var searchParameter: SearchParameter? = null

    private var onTabShop: Boolean = false

    private lateinit var pageAdapter: SearchPageAdapter

    private var suggestionViewUpdateListener: SuggestionViewUpdateListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initResources()
        retainInstance = true
    }

    private fun initResources() {
        networkErrorMessage = getString(com.tokopedia.abstraction.R.string.msg_network_error)
    }

    override fun initInjector() {
        val component: AutoCompleteComponent = DaggerAutoCompleteComponent.builder()
                .baseAppComponent(getBaseAppComponent())
                .build()
        component.inject(this)
        component.inject(presenter)

    }

    private fun getBaseAppComponent(): BaseAppComponent? {
        return if (activity == null || activity?.application == null) null else
            (activity?.application as BaseMainApplication).baseAppComponent

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_suggestion, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        prepareView()
        presenter.attachView(this)
    }

    private fun prepareView() {
        pageAdapter = SearchPageAdapter(fragmentManager, context, this)
        suggestionViewPager?.offscreenPageLimit = 3
        suggestionViewPager?.adapter = pageAdapter
        suggestionTabLayout.setupWithViewPager(suggestionViewPager)
    }

    fun getCurrentTab(): Int {
        return if (isOnTabShop()) PAGER_POSITION_SHOP else PAGER_POSITION_PRODUCT
    }

    private fun isOnTabShop(): Boolean {
        return onTabShop
    }

    override fun setOnTabShop(onTabShop: Boolean) {
        this.onTabShop = onTabShop
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detachView()
    }

    override fun getScreenName(): String {
        return AppScreen.SCREEN_UNIVERSEARCH
    }

    override fun showSuggestionResult(allFragmentList: MutableList<Visitable<*>>, productFragmentList: MutableList<Visitable<*>>, shopFragmentList: MutableList<Visitable<*>>) {
        stopTracePerformanceMonitoring()
        setSuggestionViewPagerOnPageChangeListener()
        clearData()
        addBulkSearchResult(allFragmentList, productFragmentList, shopFragmentList)
        suggestionViewUpdateListener?.showSuggestionView()
    }

    private fun stopTracePerformanceMonitoring() {
        performanceMonitoring?.stopTrace()
    }

    private fun setSuggestionViewPagerOnPageChangeListener() {
        suggestionViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                setOnTabShop(position == 2)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    private fun clearData() {
        pageAdapter.getRegisteredFragment(0).clearData()
        pageAdapter.getRegisteredFragment(1).clearData()
        pageAdapter.getRegisteredFragment(2).clearData()
    }

    private fun addBulkSearchResult(allFragmentList: MutableList<Visitable<*>>, productFragmentList: MutableList<Visitable<*>>, shopFragmentList: MutableList<Visitable<*>>) {
        pageAdapter.getRegisteredFragment(0).addBulkSearchResult(allFragmentList)
        pageAdapter.getRegisteredFragment(1).addBulkSearchResult(productFragmentList)
        pageAdapter.getRegisteredFragment(2).addBulkSearchResult(shopFragmentList)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        if (savedInstanceState != null) {
            searchParameter = savedInstanceState.getParcelable(SEARCH_PARAMETER)
            searchParameter?.let { presenter.search(it) }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(SEARCH_PARAMETER, searchParameter)
    }

    fun search(searchParameter: SearchParameter) {
        performanceMonitoring = PerformanceMonitoring.start(MP_SEARCH_AUTOCOMPLETE)
        presenter.search(searchParameter)
    }

    override fun onItemClicked(applink: String, webUrl: String?) {
        dropKeyBoard()
        startActivityFromAutoComplete(applink)
    }

    private fun dropKeyBoard() {
        if (activity != null && activity is AutoCompleteActivity) {
            (activity as AutoCompleteActivity).dropKeyboard()
        }
    }

    private fun startActivityFromAutoComplete(applink: String) {
        if (activity == null) return

        RouteManager.route(activity, applink)
        activity?.finish()
    }

    override fun copyTextToSearchView(text: String?) {
        (activity as AutoCompleteActivity).setSearchQuery("$text ")
    }

    fun setSearchParameter(searchParameter: SearchParameter) {
        this.searchParameter = searchParameter
    }

    fun setSuggestionViewUpdateListener(suggestionViewUpdateListener: SuggestionViewUpdateListener){
        this.suggestionViewUpdateListener = suggestionViewUpdateListener
    }
}
