package com.tokopedia.autocomplete.suggestion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.RouteManager
import com.tokopedia.autocomplete.OnScrollListenerAutocomplete
import com.tokopedia.autocomplete.R
import com.tokopedia.autocomplete.analytics.AppScreen
import com.tokopedia.autocomplete.analytics.AutocompleteTracking
import com.tokopedia.autocomplete.suggestion.di.DaggerSuggestionComponent
import com.tokopedia.autocomplete.suggestion.di.SuggestionComponent
import com.tokopedia.autocomplete.suggestion.di.SuggestionContextModule
import com.tokopedia.autocomplete.util.getModifiedApplink
import com.tokopedia.discovery.common.model.SearchParameter
import kotlinx.android.synthetic.main.fragment_suggestion.*
import javax.inject.Inject

class SuggestionFragment : BaseDaggerFragment(), SuggestionContract.View, SuggestionClickListener {

    private val SEARCH_PARAMETER = "SEARCH_PARAMETER"
    private val MP_SEARCH_AUTOCOMPLETE = "mp_search_autocomplete"

    @Inject
    lateinit var presenter: SuggestionPresenter

    private lateinit var networkErrorMessage: String

    private var performanceMonitoring: PerformanceMonitoring? = null

    private var searchParameter: SearchParameter? = null

    private lateinit var adapter: SuggestionAdapter

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
        val component: SuggestionComponent = DaggerSuggestionComponent.builder()
                .baseAppComponent(getBaseAppComponent())
                .suggestionContextModule(activity?.let { SuggestionContextModule(it) })
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
        prepareView(view)
        presenter.attachView(this)
    }

    private fun prepareView(view: View) {
        val typeFactory = SuggestionAdapterTypeFactory(this)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = SuggestionAdapter(typeFactory)
        recyclerViewSuggestion?.adapter = adapter
        recyclerViewSuggestion?.layoutManager = layoutManager
        recyclerViewSuggestion?.isNestedScrollingEnabled = false
        recyclerViewSuggestion?.setHasFixedSize(true)
        recyclerViewSuggestion?.addOnScrollListener(OnScrollListenerAutocomplete(view.context, view))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detachView()
    }

    override fun getScreenName(): String {
        return AppScreen.SCREEN_UNIVERSEARCH
    }

    override fun showSuggestionResult(list: MutableList<Visitable<*>>) {
        stopTracePerformanceMonitoring()
        adapter.clearData()
        adapter.addAll(list)

        suggestionViewUpdateListener?.showSuggestionView()
    }

    private fun stopTracePerformanceMonitoring() {
        performanceMonitoring?.stopTrace()
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

    fun setIsTyping(isTyping: Boolean) {
        presenter.setIsTyping(isTyping)
    }

    override fun onItemClicked(item: BaseSuggestionViewModel) {
        presenter.onSuggestionItemClicked(item)
    }

    override fun dropKeyBoard() {
        suggestionViewUpdateListener?.dropKeyboard()
    }

    override fun route(applink: String) {
        activity?.let {
            val modifiedApplink = getModifiedApplink(applink, searchParameter)
            RouteManager.route(it, modifiedApplink)
        }
    }

    override fun finish() {
        activity?.finish()
    }

    override fun copyTextToSearchView(text: String) {
        suggestionViewUpdateListener?.setSearchQuery("$text ")
    }

    fun setSearchParameter(searchParameter: SearchParameter) {
        this.searchParameter = searchParameter
    }

    fun setSuggestionViewUpdateListener(suggestionViewUpdateListener: SuggestionViewUpdateListener) {
        this.suggestionViewUpdateListener = suggestionViewUpdateListener
    }

    override fun trackEventClickKeyword(eventLabel: String) {
        AutocompleteTracking.eventClickKeyword(eventLabel)
    }

    override fun trackEventClickCurated(eventLabel: String, campaignCode: String) {
        AutocompleteTracking.eventClickCurated(eventLabel, campaignCode)
    }

    override fun trackEventClickShop(eventLabel: String) {
        AutocompleteTracking.eventClickShop(eventLabel)
    }

    override fun trackEventClickProfile(eventLabel: String) {
        AutocompleteTracking.eventClickProfile(eventLabel)
    }

    override fun trackEventClickRecentKeyword(eventLabel: String) {
        AutocompleteTracking.eventClickRecentKeyword(eventLabel)
    }
}
