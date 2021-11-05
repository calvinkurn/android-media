package com.tokopedia.autocompletecomponent.initialstate

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
import com.tokopedia.autocompletecomponent.initialstate.analytics.InitialStateTracking
import com.tokopedia.autocompletecomponent.initialstate.curatedcampaign.CuratedCampaignDataView
import com.tokopedia.autocompletecomponent.initialstate.di.InitialStateComponent
import com.tokopedia.autocompletecomponent.initialstate.dynamic.DynamicInitialStateItemTrackingModel
import com.tokopedia.autocompletecomponent.initialstate.recentsearch.RecentSearchDataView
import com.tokopedia.autocompletecomponent.util.OnScrollListenerAutocomplete
import com.tokopedia.autocompletecomponent.util.SCREEN_UNIVERSEARCH
import com.tokopedia.autocompletecomponent.util.getModifiedApplink
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressConstant
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import javax.inject.Inject

class InitialStateFragment:
    TkpdBaseV4Fragment(),
    InitialStateContract.View,
    InitialStateItemClickListener {

    companion object {
        const val INITIAL_STATE_FRAGMENT_TAG = "INITIAL_STATE_FRAGMENT"
        private const val SEARCH_PARAMETER = "SEARCH_PARAMETER"
        private const val MP_SEARCH_AUTOCOMPLETE = "mp_search_autocomplete"

        @JvmStatic
        fun create(
            component: InitialStateComponent,
        ) = InitialStateFragment().apply {
            component.inject(this)
        }
    }

    var presenter: InitialStateContract.Presenter? = null
        @Inject set

    var initialStateViewUpdateListener: InitialStateViewUpdateListener? = null
        @Inject set

    var initialStateTracking: InitialStateTracking? = null
        @Inject set

    private var performanceMonitoring: PerformanceMonitoring? = null
    private val initialStateAdapterTypeFactory = InitialStateAdapterTypeFactory(this)
    private val initialStateAdapter = InitialStateAdapter(initialStateAdapterTypeFactory)

    private val recyclerViewInitialState by lazy {
        view?.findViewById<RecyclerView?>(R.id.recyclerViewInitialState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_initial_state, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        prepareView()
        presenter?.attachView(this)
    }

    private fun prepareView() {
        recyclerViewInitialState?.also {
            it.adapter = initialStateAdapter
            it.layoutManager = LinearLayoutManager(context, VERTICAL, false)
            it.addOnScrollListener(OnScrollListenerAutocomplete(activity))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter?.detachView()
    }

    override fun getScreenName(): String {
        return SCREEN_UNIVERSEARCH
    }

    override fun showInitialStateResult(list: List<Visitable<*>>) {
        notifyAdapter(list)
    }

    private fun notifyAdapter(list: List<Visitable<*>>) {
        stopTracePerformanceMonitoring()
        initialStateAdapter.clearData()
        initialStateAdapter.addAll(list)

        initialStateViewUpdateListener?.showInitialStateView()
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

        presenter?.showInitialState(searchParameter)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        val presenter = presenter ?: return

        outState.putSerializable(
            SEARCH_PARAMETER,
            HashMap<String, Any>(presenter.getSearchParameter())
        )
    }

    fun show(searchParameter: Map<String, String>) {
        performanceMonitoring = PerformanceMonitoring.start(MP_SEARCH_AUTOCOMPLETE)

        presenter?.showInitialState(searchParameter)
    }

    override fun onProductLineClicked(item: BaseItemInitialStateSearch) {
        presenter?.onProductLineClicked(item)
    }

    override fun onRecentSearchItemClicked(item: BaseItemInitialStateSearch) {
        presenter?.onRecentSearchItemClicked(item)
    }

    override fun onRecentSearchSeeMoreClicked() {
        presenter?.recentSearchSeeMoreClicked()
    }

    override fun renderCompleteRecentSearch(recentSearchDataView: RecentSearchDataView) {
        val presenter = presenter ?: return

        initialStateAdapter.removeSeeMoreButton(presenter.seeMoreButtonPosition)
        initialStateAdapter.renderRecentSearch(recentSearchDataView, presenter.recentSearchPosition)
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

    override fun onDeleteRecentSearchItem(item: BaseItemInitialStateSearch) {
        deleteRecentSearch(item)
    }

    private fun deleteRecentSearch(item: BaseItemInitialStateSearch) {
        presenter?.deleteRecentSearchItem(item)
    }

    override fun onDeleteAllRecentSearch() {
        deleteAllRecentSearch()
    }

    private fun deleteAllRecentSearch() {
        presenter?.deleteAllRecentSearch()
    }

    override fun onRefreshPopularSearch(featureId: String) {
        refreshPopularSearch(featureId)
    }

    private fun refreshPopularSearch(featureId: String) {
        presenter?.refreshPopularSearch(featureId)
    }

    override fun onRefreshDynamicSection(featureId: String) {
        presenter?.refreshDynamicSection(featureId)
    }

    override fun onRecentViewImpressed(list: List<Any>) {
        initialStateTracking?.impressedRecentView(list)
    }

    override fun onRecentSearchImpressed(list: List<Any>) {
        val keyword = presenter?.getQueryKey() ?: ""
        initialStateTracking?.impressedRecentSearch(list, keyword)
    }

    override fun onPopularSearchImpressed(model: DynamicInitialStateItemTrackingModel) {
        initialStateTracking?.impressedDynamicSection(model)
    }

    override fun onSeeMoreRecentSearchImpressed(userId: String) {
        initialStateTracking?.impressedSeeMoreRecentSearch(userId)
    }

    override fun trackEventClickRecentSearch(label: String, pageSource: String) {
        initialStateTracking?.eventClickRecentSearch(label, pageSource)
    }

    override fun trackEventClickRecentShop(label: String, userId: String, pageSource: String) {
        initialStateTracking?.eventClickRecentShop(label, userId, pageSource)
    }

    override fun trackEventClickSeeMoreRecentSearch(userId: String) {
        initialStateTracking?.eventClickSeeMoreRecentSearch(userId)
    }

    override fun onDynamicSectionItemClicked(item: BaseItemInitialStateSearch) {
        presenter?.onDynamicSectionItemClicked(item)
    }

    override fun trackEventClickDynamicSectionItem(userId: String, label: String, type: String, pageSource: String) {
        initialStateTracking?.eventClickDynamicSection(userId, label, type, pageSource)
    }

    override fun refreshViewWithPosition(position: Int) {
        initialStateAdapter.refreshPopularSection(position)
    }

    override fun onDynamicSectionImpressed(model: DynamicInitialStateItemTrackingModel) {
        initialStateTracking?.impressedDynamicSection(model)
    }

    override fun dropKeyBoard() {
        activity?.let {
            KeyboardHandler.hideSoftKeyboard(it)
        }
    }

    override fun onCuratedCampaignCardClicked(curatedCampaignDataView: CuratedCampaignDataView) {
        presenter?.onCuratedCampaignCardClicked(curatedCampaignDataView)
    }

    override fun trackEventClickCuratedCampaignCard(userId: String, label: String, type: String, campaignCode: String) {
        initialStateTracking?.eventClickCuratedCampaignCard(userId, label, type, campaignCode)
    }

    override fun onCuratedCampaignCardImpressed(userId: String, label: String, type: String, campaignCode: String) {
        initialStateTracking?.impressedCuratedCampaign(userId, label, type, campaignCode)
    }

    override fun onRecentViewClicked(item: BaseItemInitialStateSearch) {
        presenter?.onRecentViewClicked(item)
    }

    override fun trackEventClickRecentView(item: BaseItemInitialStateSearch, label: String) {
        val productDataLayer = item.getRecentViewAsObjectDataLayer()
        initialStateTracking?.eventClickRecentView(productDataLayer, label)
    }

    override fun trackEventClickProductLine(
        item: BaseItemInitialStateSearch,
        userId: String,
        label: String
    ) {
        val productDataLayer = item.getProductLineAsObjectDataLayer()
        initialStateTracking?.eventClickInitialStateProductLine(
            productDataLayer,
            userId,
            label,
            item.dimension90,
        )
    }

    override val chooseAddressData: LocalCacheModel
        get() = context?.let {
            try {
                ChooseAddressUtils.getLocalizingAddressData(it)
            } catch (e: Throwable) {
                ChooseAddressConstant.emptyAddress
            }
        } ?: ChooseAddressConstant.emptyAddress

    override fun onRefreshPopularSearch() {
        initialStateTracking?.eventClickRefreshPopularSearch()
    }

    override fun onRefreshTokoNowPopularSearch() {
        initialStateTracking?.eventClickRefreshTokoNowPopularSearch()
    }

    override fun trackEventClickTokoNowDynamicSectionItem(label: String) {
        initialStateTracking?.eventClickTokoNowPopularSearch(label)
    }

    override fun trackEventClickChip(userId: String, label: String, type: String, pageSource: String) {
        initialStateTracking?.eventClickDynamicSection(userId, label, type, pageSource)
    }

    override fun onChipClicked(item: BaseItemInitialStateSearch) {
        presenter?.onChipClicked(item)
    }

    interface InitialStateViewUpdateListener {

        fun showInitialStateView()
    }
}
