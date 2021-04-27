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
import com.tokopedia.autocomplete.suggestion.topshop.SuggestionTopShopCardDataView
import com.tokopedia.autocomplete.suggestion.topshop.SuggestionTopShopListener
import com.tokopedia.autocomplete.util.getModifiedApplink
import com.tokopedia.discovery.common.model.SearchParameter
import kotlinx.android.synthetic.main.fragment_suggestion.*
import javax.inject.Inject

class SuggestionFragment :
        BaseDaggerFragment(), SuggestionContract.View,
        SuggestionClickListener, SuggestionTopShopListener {
    private val SEARCH_PARAMETER = "SEARCH_PARAMETER"
    private val MP_SEARCH_AUTOCOMPLETE = "mp_search_autocomplete"

    @Inject
    lateinit var presenter: SuggestionPresenter

    private lateinit var networkErrorMessage: String

    private var performanceMonitoring: PerformanceMonitoring? = null

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
        val typeFactory = SuggestionAdapterTypeFactory(this, this)
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

    override fun showSuggestionResult(list: List<Visitable<*>>) {
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
            val searchParameter = savedInstanceState.getSerializable(SEARCH_PARAMETER) as HashMap<String, String>
            setSearchParameter(searchParameter)
            presenter.search()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(SEARCH_PARAMETER, HashMap<String, Any>(presenter.getSearchParameter()))
    }

    fun search(searchParameter: SearchParameter) {
        performanceMonitoring = PerformanceMonitoring.start(MP_SEARCH_AUTOCOMPLETE)
        setSearchParameter(searchParameter.getSearchParameterHashMap())
        presenter.search()
    }

    fun setIsTyping(isTyping: Boolean) {
        presenter.setIsTyping(isTyping)
    }

    override fun onItemClicked(item: BaseSuggestionDataView) {
        presenter.onSuggestionItemClicked(item)
    }

    override fun dropKeyBoard() {
        suggestionViewUpdateListener?.dropKeyboard()
    }

    override fun route(applink: String, searchParameter: Map<String, String>) {
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

    fun setSearchParameter(searchParameter: HashMap<String, String> ) {
        presenter.setSearchParameter(searchParameter)
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

    override fun onTopShopCardClicked(topShopData: SuggestionTopShopCardDataView) {
        presenter.onTopShopCardClicked(topShopData)
    }

    override fun onTopShopSeeMoreClicked(topShopData: SuggestionTopShopCardDataView) {
        presenter.onTopShopCardClicked(topShopData)
    }

    override fun trackEventClickTopShopCard(eventLabel: String) {
        AutocompleteTracking.eventClickTopShop(eventLabel)
    }

    override fun trackEventClickTopShopSeeMore(eventLabel: String) {
        AutocompleteTracking.eventClickTopShopSeeMore(eventLabel)
    }

    override fun trackEventClickLocalKeyword(eventLabel: String, userId: String) {
        AutocompleteTracking.eventClickLocalKeyword(eventLabel, userId)
    }

    override fun trackEventClickGlobalKeyword(eventLabel: String, userId: String) {
        AutocompleteTracking.eventClickGlobalKeyword(eventLabel, userId)
    }
}
