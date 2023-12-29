package com.tokopedia.autocompletecomponent.initialstate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
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
import com.tokopedia.autocompletecomponent.initialstate.chips.InitialStateChipListener
import com.tokopedia.autocompletecomponent.initialstate.curatedcampaign.CuratedCampaignDataView
import com.tokopedia.autocompletecomponent.initialstate.curatedcampaign.CuratedCampaignListener
import com.tokopedia.autocompletecomponent.initialstate.di.InitialStateComponent
import com.tokopedia.autocompletecomponent.initialstate.dynamic.DynamicInitialStateItemTrackingModel
import com.tokopedia.autocompletecomponent.initialstate.dynamic.DynamicInitialStateListener
import com.tokopedia.autocompletecomponent.initialstate.dynamic.DynamicInitialStateSearchDataView
import com.tokopedia.autocompletecomponent.initialstate.mps.MpsInitialStateListener
import com.tokopedia.autocompletecomponent.initialstate.popularsearch.PopularSearchDataView
import com.tokopedia.autocompletecomponent.initialstate.popularsearch.PopularSearchListener
import com.tokopedia.autocompletecomponent.initialstate.productline.ProductLineListener
import com.tokopedia.autocompletecomponent.initialstate.recentsearch.RecentSearchDataView
import com.tokopedia.autocompletecomponent.initialstate.recentsearch.RecentSearchListener
import com.tokopedia.autocompletecomponent.initialstate.recentview.RecentViewDataView
import com.tokopedia.autocompletecomponent.initialstate.recentview.RecentViewListener
import com.tokopedia.autocompletecomponent.initialstate.searchbareducation.SearchBarEducationListener
import com.tokopedia.autocompletecomponent.searchbar.SearchBarViewModel
import com.tokopedia.autocompletecomponent.util.HasViewModelFactory
import com.tokopedia.autocompletecomponent.util.OnScrollListenerAutocomplete
import com.tokopedia.autocompletecomponent.util.SCREEN_UNIVERSEARCH
import com.tokopedia.autocompletecomponent.util.getModifiedApplink
import com.tokopedia.discovery.common.reimagine.ReimagineRollence
import com.tokopedia.discovery.common.reimagine.Search1InstAuto
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressConstant
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.track.TrackApp
import javax.inject.Inject

class InitialStateFragment:
    TkpdBaseV4Fragment(),
    InitialStateContract.View,
    RecentViewListener,
    RecentSearchListener,
    ProductLineListener,
    PopularSearchListener,
    DynamicInitialStateListener,
    CuratedCampaignListener,
    InitialStateChipListener,
    SearchBarEducationListener,
    MpsInitialStateListener {

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

    private val viewModel: SearchBarViewModel? by lazy {
        val activity = activity ?: return@lazy null
        if (activity !is HasViewModelFactory) return@lazy null
        val factory = activity.viewModelFactory ?: return@lazy null
        ViewModelProvider(activity, factory).get()
    }

    private var performanceMonitoring: PerformanceMonitoring? = null

    var reimagineRollence: ReimagineRollence? = null
        @Inject set

    private val initialStateAdapter by lazy {
        val initialStateAdapterTypeFactory = InitialStateAdapterTypeFactory(
            recentViewListener = this,
            recentSearchListener = this,
            productLineListener = this,
            popularSearchListener = this,
            dynamicInitialStateListener = this,
            curatedCampaignListener = this,
            chipListener = this,
            searchBarEducationListener = this,
            mpsChipListener = this,
            isReimagine = reimagineRollence?.search1InstAuto() != Search1InstAuto.CONTROL
        )
        InitialStateAdapter(initialStateAdapterTypeFactory)
    }

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
        showMps()
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

    override fun onRecentViewImpressed(recentViewDataView: RecentViewDataView, list: List<Any>) {
        initialStateTracking?.impressedRecentView(recentViewDataView.list, list)
    }

    override fun onRecentSearchImpressed(
        recentSearchList: List<BaseItemInitialStateSearch>,
        list: List<Any>
    ) {
        val keyword = presenter?.getQueryKey() ?: ""
        initialStateTracking?.impressedRecentSearch(recentSearchList, list, keyword)
    }

    override fun onPopularSearchImpressed(
        popularSearchDataView: PopularSearchDataView,
        model: DynamicInitialStateItemTrackingModel
    ) {
        initialStateTracking?.impressedDynamicSection(
            popularSearchDataView.list,
            model
        )
    }

    override fun onSeeMoreRecentSearchImpressed(userId: String) {
        initialStateTracking?.impressedSeeMoreRecentSearch(userId)
    }

    override fun trackEventClickRecentSearch(item: BaseItemInitialStateSearch, label: String) {
        initialStateTracking?.eventClickRecentSearch(item, label, item.dimension90)
    }

    override fun trackEventClickRecentShop(
        item: BaseItemInitialStateSearch,
        label: String,
        userId: String
    ) {
        initialStateTracking?.eventClickRecentShop(item, label, userId, item.dimension90)
    }

    override fun trackEventClickSeeMoreRecentSearch(userId: String) {
        initialStateTracking?.eventClickSeeMoreRecentSearch(userId)
    }

    override fun onDynamicSectionItemClicked(item: BaseItemInitialStateSearch) {
        presenter?.onDynamicSectionItemClicked(item)
    }

    override fun trackEventClickDynamicSectionItem(
        userId: String,
        label: String,
        item: BaseItemInitialStateSearch,
        type: String,
        pageSource: String
    ) {
        initialStateTracking?.eventClickDynamicSection(item, userId, label, type, pageSource)
    }

    override fun refreshViewWithPosition(position: Int) {
        initialStateAdapter.refreshPopularSection(position)
    }

    override fun onDynamicSectionImpressed(
        dynamicInitialStateSearchDataView: DynamicInitialStateSearchDataView,
        model: DynamicInitialStateItemTrackingModel
    ) {
        initialStateTracking?.impressedDynamicSection(
            dynamicInitialStateSearchDataView.list,
            model
        )
    }

    override fun dropKeyBoard() {
        activity?.let {
            KeyboardHandler.hideSoftKeyboard(it)
        }
    }

    override fun onCuratedCampaignCardClicked(curatedCampaignDataView: CuratedCampaignDataView) {
        presenter?.onCuratedCampaignCardClicked(curatedCampaignDataView)
    }

    override fun trackEventClickCuratedCampaignCard(
        userId: String,
        label: String,
        item: BaseItemInitialStateSearch,
        type: String,
        campaignCode: String,
    ) {
        initialStateTracking?.eventClickCuratedCampaignCard(
            item,
            userId,
            label,
            type,
            campaignCode,
        )
    }

    override fun onCuratedCampaignCardImpressed(
        userId: String,
        label: String,
        item: BaseItemInitialStateSearch,
        type: String,
        campaignCode: String
    ) {
        initialStateTracking?.impressedCuratedCampaign(item, userId, label, type, campaignCode)
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

    override fun trackEventClickTokoNowDynamicSectionItem(
        label: String,
        item: BaseItemInitialStateSearch,
    ) {
        initialStateTracking?.eventClickTokoNowPopularSearch(item, label)
    }

    override fun onChipClicked(item: BaseItemInitialStateSearch) {
        presenter?.onChipClicked(item)
    }

    override fun trackEventClickChip(
        userId: String,
        label: String,
        item: BaseItemInitialStateSearch,
        type: String,
        pageSource: String
    ) {
        initialStateTracking?.eventClickDynamicSection(item, userId, label, type, pageSource)
    }

    interface InitialStateViewUpdateListener {

        fun showInitialStateView()
    }

    override fun onSearchBarEducationClick(item: BaseItemInitialStateSearch) {
        presenter?.onSearchBarEducationClick(item)
    }

    override fun trackEventClickSearchBarEducation(item: BaseItemInitialStateSearch) {
        initialStateTracking?.eventClickSearchBarEducation(item)
    }

    override fun onMpsChipClicked(item: BaseItemInitialStateSearch) {
        item.click(TrackApp.getInstance().gtm)
        viewModel?.onInitialStateItemSelected(item)
    }

    private fun showMps() {
        viewModel?.showMps()
    }
}
