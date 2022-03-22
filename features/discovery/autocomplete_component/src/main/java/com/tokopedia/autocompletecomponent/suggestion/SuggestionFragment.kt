package com.tokopedia.autocompletecomponent.suggestion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.RouteManager
import com.tokopedia.autocompletecomponent.R
import com.tokopedia.autocompletecomponent.suggestion.analytics.SuggestionTracking
import com.tokopedia.autocompletecomponent.suggestion.chips.SuggestionChipListener
import com.tokopedia.autocompletecomponent.suggestion.di.SuggestionComponent
import com.tokopedia.autocompletecomponent.suggestion.topshop.SuggestionTopShopCardDataView
import com.tokopedia.autocompletecomponent.suggestion.topshop.SuggestionTopShopListener
import com.tokopedia.autocompletecomponent.util.OnScrollListenerAutocomplete
import com.tokopedia.autocompletecomponent.util.SCREEN_UNIVERSEARCH
import com.tokopedia.autocompletecomponent.util.getModifiedApplink
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressConstant
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import javax.inject.Inject

class SuggestionFragment :
    TkpdBaseV4Fragment(),
    SuggestionContract.View,
    SuggestionListener,
    SuggestionTopShopListener,
    SuggestionChipListener {

    companion object {
        const val SUGGESTION_FRAGMENT_TAG = "SUGGESTION_FRAGMENT_TAG"
        private const val SEARCH_PARAMETER = "SEARCH_PARAMETER"
        private const val MP_SEARCH_AUTOCOMPLETE = "mp_search_autocomplete"

        @JvmStatic
        fun create(
            component: SuggestionComponent,
        ) = SuggestionFragment().apply {
            component.inject(this)
        }
    }

    var presenter: SuggestionContract.Presenter? = null
        @Inject set

    var suggestionViewUpdateListener: SuggestionViewUpdateListener? = null
        @Inject set

    var suggestionTracking: SuggestionTracking? = null
        @Inject set

    override val className: String
        get() = activity?.javaClass?.name ?: ""

    private var performanceMonitoring: PerformanceMonitoring? = null
    private val suggestionTypeFactory = SuggestionAdapterTypeFactory(
        suggestionListener = this,
        suggestionTopShopListener = this,
        suggestionChipListener = this,
    )
    private val suggestionAdapter = SuggestionAdapter(suggestionTypeFactory)

    private val recyclerViewSuggestion by lazy {
        view?.findViewById<RecyclerView?>(R.id.recyclerViewSuggestion)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_suggestion, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        prepareView()
        presenter?.attachView(this)
    }

    private fun prepareView() {
        recyclerViewSuggestion?.apply {
            adapter = suggestionAdapter
            layoutManager = LinearLayoutManager(context, VERTICAL, false)
            isNestedScrollingEnabled = false
            setHasFixedSize(true)
            addOnScrollListener(OnScrollListenerAutocomplete(activity))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.detachView()
    }

    override fun getScreenName(): String {
        return SCREEN_UNIVERSEARCH
    }

    override fun showSuggestionResult(list: List<Visitable<*>>) {
        stopTracePerformanceMonitoring()
        suggestionAdapter.replaceData(list)

        suggestionViewUpdateListener?.showSuggestionView()
    }

    private fun stopTracePerformanceMonitoring() {
        performanceMonitoring?.stopTrace()
        performanceMonitoring = null
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        if (savedInstanceState == null) return

        val searchParameter = savedInstanceState.getSerializable(SEARCH_PARAMETER)
            as HashMap<String, String>

        presenter?.getSuggestion(searchParameter)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        val presenter = presenter ?: return

        outState.putSerializable(
            SEARCH_PARAMETER,
            HashMap<String, Any>(presenter.getSearchParameter())
        )
    }

    fun getSuggestion(searchParameter: Map<String, String>) {
        performanceMonitoring = PerformanceMonitoring.start(MP_SEARCH_AUTOCOMPLETE)

        presenter?.getSuggestion(searchParameter)
    }

    fun setIsTyping(isTyping: Boolean) {
        presenter?.setIsTyping(isTyping)
    }

    override fun onItemClicked(item: BaseSuggestionDataView) {
        presenter?.onSuggestionItemClicked(item)
    }

    override fun onChipImpressed(item: BaseSuggestionDataView) {
        suggestionTracking?.eventImpressionSuggestion(item)
    }

    override fun onChipClicked(
        baseSuggestionDataView: BaseSuggestionDataView,
        childItem: BaseSuggestionDataView.ChildItem
    ) {
        presenter?.onSuggestionChipClicked(baseSuggestionDataView, childItem)
    }

    override fun onItemImpressed(item: BaseSuggestionDataView) {
        presenter?.onSuggestionItemImpressed(item)
    }

    override fun dropKeyBoard() {
        activity?.let {
            KeyboardHandler.hideSoftKeyboard(it)
        }
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

    override fun trackEventClickKeyword(
        eventLabel: String,
        dimension90: String,
        baseSuggestionDataView: BaseSuggestionDataView,
    ) {
        suggestionTracking?.eventClickKeyword(eventLabel, dimension90, baseSuggestionDataView)
    }

    override fun trackEventClickCurated(
        eventLabel: String,
        campaignCode: String,
        dimension90: String,
        baseSuggestionDataView: BaseSuggestionDataView,
    ) {
        suggestionTracking?.eventClickCurated(
            eventLabel,
            campaignCode,
            dimension90,
            baseSuggestionDataView
        )
    }

    override fun trackEventClickShop(
        eventLabel: String,
        dimension90: String,
        baseSuggestionDataView: BaseSuggestionDataView,
    ) {
        suggestionTracking?.eventClickShop(eventLabel, dimension90, baseSuggestionDataView)
    }

    override fun trackEventClickProfile(
        eventLabel: String,
        baseSuggestionDataView: BaseSuggestionDataView
    ) {
        suggestionTracking?.eventClickProfile(eventLabel, baseSuggestionDataView)
    }

    override fun trackEventClickRecentKeyword(
        eventLabel: String,
        dimension90: String,
        baseSuggestionDataView: BaseSuggestionDataView
    ) {
        suggestionTracking?.eventClickRecentKeyword(eventLabel, dimension90, baseSuggestionDataView)
    }

    override fun onTopShopCardClicked(topShopData: SuggestionTopShopCardDataView) {
        presenter?.onTopShopCardClicked(topShopData)
    }

    override fun onTopShopSeeMoreClicked(topShopData: SuggestionTopShopCardDataView) {
        presenter?.onTopShopCardClicked(topShopData)
    }

    override fun trackEventClickTopShopCard(eventLabel: String) {
        suggestionTracking?.eventClickTopShop(eventLabel)
    }

    override fun trackEventClickTopShopSeeMore(eventLabel: String) {
        suggestionTracking?.eventClickTopShopSeeMore(eventLabel)
    }

    override fun trackEventClickLocalKeyword(
        eventLabel: String,
        userId: String,
        dimension90: String,
        baseSuggestionDataView: BaseSuggestionDataView
    ) {
        suggestionTracking?.eventClickLocalKeyword(
            eventLabel,
            userId,
            dimension90,
            baseSuggestionDataView,
        )
    }

    override fun trackEventClickGlobalKeyword(
        eventLabel: String,
        userId: String,
        dimension90: String,
        baseSuggestionDataView: BaseSuggestionDataView
    ) {
        suggestionTracking?.eventClickGlobalKeyword(
            eventLabel,
            userId,
            dimension90,
            baseSuggestionDataView,
        )
    }

    override fun trackEventClickProductLine(item: BaseSuggestionDataView, eventLabel: String, userId: String) {
        val productDataLayer = item.getProductLineAsObjectDataLayer()
        suggestionTracking?.eventClickSuggestionProductLine(productDataLayer, eventLabel, userId)
    }

    override val chooseAddressData: LocalCacheModel
        get() = context?.let {
            try {
                ChooseAddressUtils.getLocalizingAddressData(it)
            } catch (e: Throwable) {
                ChooseAddressConstant.emptyAddress
            }
        } ?: ChooseAddressConstant.emptyAddress

    override fun trackTokoNowEventClickCurated(
        eventLabel: String,
        baseSuggestionDataView: BaseSuggestionDataView
    ) {
        suggestionTracking?.eventClickTokoNowCurated(eventLabel, baseSuggestionDataView)
    }

    override fun trackTokoNowEventClickKeyword(
        eventLabel: String,
        baseSuggestionDataView: BaseSuggestionDataView
    ) {
        suggestionTracking?.eventClickTokoNowKeyword(eventLabel, baseSuggestionDataView)
    }

    override fun trackClickChip(
        eventLabel: String,
        dimension90: String,
        baseSuggestionDataView: BaseSuggestionDataView,
    ) {
        suggestionTracking?.eventClickChipSuggestion(
            eventLabel,
            dimension90,
            baseSuggestionDataView,
        )
    }

    override fun trackEventImpressCurated(
        label: String,
        campaignCode: String,
        pageSource: String,
        baseSuggestionDataView: BaseSuggestionDataView
    ) {
        suggestionTracking?.eventImpressCurated(
            label,
            campaignCode,
            pageSource,
            baseSuggestionDataView,
        )
    }

    override fun trackEventImpression(item: BaseSuggestionDataView) {
        suggestionTracking?.eventImpressionSuggestion(item)
    }

    override fun trackEventClick(item: BaseSuggestionDataView) {
        suggestionTracking?.eventClickSuggestion(item)
    }

    interface SuggestionViewUpdateListener {

        fun showSuggestionView()
        fun setSearchQuery(keyword: String)
    }
}
