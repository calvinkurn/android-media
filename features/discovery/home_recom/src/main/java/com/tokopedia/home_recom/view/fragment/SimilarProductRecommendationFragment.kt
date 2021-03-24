package com.tokopedia.home_recom.view.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.fragment.annotations.FragmentInflater
import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.discovery.common.manager.ProductCardOptionsWishlistCallback
import com.tokopedia.discovery.common.manager.handleProductCardOptionsActivityResult
import com.tokopedia.discovery.common.manager.showProductCardOptions
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.analytics.RecommendationPageTracking
import com.tokopedia.home_recom.analytics.SimilarProductRecommendationTracking
import com.tokopedia.home_recom.di.HomeRecommendationComponent
import com.tokopedia.home_recom.model.datamodel.*
import com.tokopedia.home_recom.util.*
import com.tokopedia.home_recom.view.adapter.SimilarProductRecommendationAdapter
import com.tokopedia.home_recom.view.adapter.SimilarProductRecommendationTypeFactoryImpl
import com.tokopedia.home_recom.view.viewholder.RecommendationEmptyViewHolder
import com.tokopedia.home_recom.viewmodel.SimilarProductRecommendationViewModel
import com.tokopedia.home_recom.viewmodel.SimilarProductRecommendationViewModel.Companion.DEFAULT_VALUE_SORT
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.sortfilter.SortFilter
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.trackingoptimizer.TrackingQueue
import kotlinx.android.synthetic.main.fragment_simillar_recommendation.view.*
import java.lang.StringBuilder
import javax.inject.Inject

/**
 * Created by Lukas on 26/08/19
 */
open class SimilarProductRecommendationFragment : BaseListFragment<HomeRecommendationDataModel, SimilarProductRecommendationTypeFactoryImpl>(),
        RecommendationListener,
        RecommendationErrorListener,
        RecommendationEmptyViewHolder.RecommendationEmptyStateListener,
        SortFilterBottomSheet.Callback {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val adapterFactory by lazy { SimilarProductRecommendationTypeFactoryImpl(this, this, this) }
    private val viewModelProvider by lazy{ ViewModelProviders.of(this, viewModelFactory) }
    private val recommendationViewModel by lazy { viewModelProvider.get(SimilarProductRecommendationViewModel::class.java) }
    private val adapter by lazy { SimilarProductRecommendationAdapter(adapterFactory) }
    private var sortFilterView: SortFilter? = null
    private var filterSortBottomSheet: SortFilterBottomSheet? = null
    private val staggeredGrid by lazy { StaggeredGridLayoutManager(SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL) }
    private var trackingQueue: TrackingQueue? = null
    private var ref: String = ""
    private var source: String = ""
    private var productId: String = ""
    private var internalRef: String = ""
    private var hasNextPage: Boolean = true

    companion object{
        private const val SPAN_COUNT = 2
        private const val WIHSLIST_STATUS_IS_WISHLIST = "isWishlist"
        private const val PDP_EXTRA_UPDATED_POSITION = "wishlistUpdatedPosition"
        private const val SAVED_PRODUCT_ID = "saved_product_id"
        private const val SAVED_REF = "saved_ref"
        private const val SAVED_SOURCE = "saved_source"
        private const val REQUEST_FROM_PDP = 399

        @SuppressLint("SyntheticAccessor")
        fun newInstance(productId: String = "", ref: String = "", source: String = "", internalRef: String = "", @FragmentInflater fragmentInflater: String = FragmentInflater.DEFAULT) = SimilarProductRecommendationFragment().apply {
            this.ref = ref
            this.source = source
            this.productId = productId
            this.internalRef = internalRef
            this.fragmentInflater = fragmentInflater
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run{
            (this as AppCompatActivity).supportActionBar?.title = getString(R.string.recom_similar_recommendation)
        }
        savedInstanceState?.let{
            productId = it.getString(SAVED_PRODUCT_ID) ?: ""
            ref = it.getString(SAVED_REF) ?: ""
            source = it.getString(SAVED_SOURCE) ?: ""
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_simillar_recommendation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let { trackingQueue = TrackingQueue(it) }
        sortFilterView = view.findViewById(R.id.filter_sort_recommendation)
        setupRecyclerView(view)
        setupBackToTop(view)
        enableLoadMore()
        observeLiveData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_FROM_PDP) {
            data?.let {
                val wishlistStatusFromPdp = data.getBooleanExtra(WIHSLIST_STATUS_IS_WISHLIST,
                        false)
                val position = data.getIntExtra(PDP_EXTRA_UPDATED_POSITION, -1)
                if(position >= 0 && adapter.data.size > position) {
                    (adapter.data[position] as RecommendationItemDataModel).productItem.isWishlist = wishlistStatusFromPdp
                    adapter.notifyItemChanged(position, wishlistStatusFromPdp)
                }
            }
        }
        handleProductCardOptionsActivityResult(requestCode, resultCode, data,
                object : ProductCardOptionsWishlistCallback {
                    override fun onReceiveWishlistResult(productCardOptionsModel: ProductCardOptionsModel) {
                        handleWishlistAction(productCardOptionsModel)
                    }
                })
    }

    override fun onGetListErrorWithEmptyData(throwable: Throwable) {
        if (activity != null) {
            adapter.showRecommendationError(RecommendationErrorDataModel(throwable))
            if (swipeToRefresh != null) {
                swipeToRefresh.isEnabled = false
            }
        }
    }

    override fun showLoading() {
        if(hasNextPage){
            super.showLoading()
        }
    }

    override fun showEmpty() {
        adapter.clearAllElements()
        adapter.addElement(RecommendationEmptyDataModel())
    }

    override fun getSwipeRefreshLayoutResourceId(): Int = com.tokopedia.home_recom.R.id.swipe_refresh_layout

    private fun setupRecyclerView(view: View){
        view.filter_sort_recommendation?.hide()
        getRecyclerView(view)?.apply {
            if(this is VerticalRecyclerView) clearItemDecoration()
            layoutManager = recyclerViewLayoutManager
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val lastItems = staggeredGrid.findFirstCompletelyVisibleItemPositions(null)
                    if (lastItems.isNotEmpty() && lastItems[0] >= 2) {
                        if(!view.recom_back_to_top.isShown) {
                            view.recom_back_to_top?.show()
                            view.recom_back_to_top.visible()
                        }
                    } else {
                        view.recom_back_to_top.gone()
                        view.recom_back_to_top?.hide()
                    }
                }
            })
        }
        RecommendationPageTracking.sendScreenSimilarProductRecommendationPage("/rekomendasi/d", ref, productId)
    }

    private fun setupBackToTop(view: View){
        view.recom_back_to_top?.circleMainMenu?.setOnClickListener {
            view.recycler_view?.smoothScrollToPosition(0)
        }
    }

    private fun observeLiveData(){
        recommendationViewModel.recommendationItem.observe(viewLifecycleOwner, Observer {
            it?.let {
                when {
                    it.status.isLoading() -> {
                        adapter.clearAllElements()
                        showLoading()
                    }
                    it.status.isLoadMore() -> showLoading()
                    it.status.isEmpty() -> showEmpty()
                    it.status.isError() -> showGetListError(it.exception)
                    it.status.isSuccess() -> {
                        it.data?.let { pair ->
                            val recommendationItems = pair.first
                            if (recommendationItems.isNotEmpty()) {
                                recommendationItems.getOrNull(0)?.let {
                                    activity?.run {
                                        (this as AppCompatActivity).supportActionBar?.title = if (it.header.isNotEmpty()) it.header else getString(R.string.recom_similar_recommendation)
                                    }
                                }
                                hasNextPage = pair.second
                                renderList(mapDataModel(recommendationItems), pair.second)
                                if(!hasNextPage) showToastSuccess(getString(R.string.recom_msg_empty_next_page))
                            }else{
                                hideLoading()
                                hasNextPage = false
                                updateScrollListenerState(false)
                                showToastSuccess(getString(R.string.recom_msg_empty_next_page))
                            }
                        }
                    }
                    else -> {}
                }
            }
        })

        recommendationViewModel.filterSortChip.observe(viewLifecycleOwner, Observer {
            if (it.status.isSuccess()) {
                it.data?.let { data ->
                    sortFilterView?.show()
                    setRecommendationFilterAndSort(data.quickFilterList.mapToUnifyFilterModel(this::onQuickFilterClick), data.filterAndSort.mapToFullFilterModel())
                }
            } else if(it.status.isLoading()){
                sortFilterView?.hide()
            }
        })
    }

    private fun handleWishlistAction(productCardOptionsModel: ProductCardOptionsModel?) {
        if (productCardOptionsModel == null) return
        val wishlistResult = productCardOptionsModel.wishlistResult
        if (wishlistResult.isUserLoggedIn) {
            if (wishlistResult.isSuccess) {
                if (wishlistResult.isAddWishlist) {
                    if(productId.isNotBlank() || productId.isNotEmpty()){
                        RecommendationPageTracking.eventUserClickRecommendationWishlistForLoginWithProductId(true, ref)
                    }else {
                        RecommendationPageTracking.eventUserClickRecommendationWishlistForLogin(true, productCardOptionsModel.screenName, ref)
                    }
                    showMessageSuccessAddWishlist()
                } else {
                    if(productId.isNotBlank() || productId.isNotEmpty()){
                        RecommendationPageTracking.eventUserClickRecommendationWishlistForLoginWithProductId(false, ref)
                    }else {
                        RecommendationPageTracking.eventUserClickRecommendationWishlistForLogin(false, productCardOptionsModel.screenName, ref)
                    }
                    showMessageSuccessRemoveWishlist()
                }
                updateWishlist(wishlistResult.isAddWishlist, productCardOptionsModel.productPosition)
            } else {
                showMessageFailedWishlistAction()
            }
        } else {
            RouteManager.route(context, ApplinkConst.LOGIN)
        }
    }

    private fun updateWishlist(isWishlist: Boolean, position: Int) {
        if(position > -1 && adapter.itemCount > 0 &&
                adapter.itemCount > position) {
            (adapter.data[position] as RecommendationItemDataModel).productItem.isWishlist = isWishlist
            adapter.notifyItemChanged(position, isWishlist)
        }
    }

    private fun showMessageSuccessAddWishlist() {
        showToastSuccessWithAction(getString(R.string.recom_msg_success_add_wishlist), getString(R.string.home_recom_go_to_wishlist)){
            View.OnClickListener { goToWishlist() }
        }
    }

    private fun goToWishlist() {
        if (activity == null) return
        RouteManager.route(activity, ApplinkConst.NEW_WISHLIST)
    }

    private fun showMessageSuccessRemoveWishlist() {
        showToastSuccess(getString(R.string.recom_msg_success_remove_wishlist))
    }

    private fun showMessageFailedWishlistAction() {
        showToastError()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SAVED_PRODUCT_ID, productId)
        outState.putString(SAVED_REF, ref)
    }

    /**
     * [onPause] is override from [BaseListFragment]
     * this void override with added extra sendAllTracking
     */
    override fun onPause() {
        super.onPause()
        trackingQueue?.sendAll()
    }

    override fun getAdapterTypeFactory(): SimilarProductRecommendationTypeFactoryImpl = adapterFactory

    override fun createAdapterInstance(): BaseListAdapter<HomeRecommendationDataModel, SimilarProductRecommendationTypeFactoryImpl> {
        return adapter
    }

    override fun onItemClicked(item: HomeRecommendationDataModel?) {}

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(HomeRecommendationComponent::class.java).inject(this)
    }

    override fun loadData(page: Int) {
        recommendationViewModel.getSimilarProductRecommendation(page, source, productId, ref)
    }

    override fun hasInitialSwipeRefresh(): Boolean {
        return true
    }

    override fun getRecyclerViewResourceId(): Int {
        return com.tokopedia.home_recom.R.id.recycler_view
    }

    override fun getRecyclerViewLayoutManager(): RecyclerView.LayoutManager {
        return staggeredGrid
    }

    override fun onRefreshRecommendation() {
        showLoading()
        loadInitialData()
    }

    override fun onCloseRecommendation() {
        activity?.finish()
    }

    private fun setRecommendationFilterAndSort(filters: List<SortFilterItem>, dynamicFilterModel: DynamicFilterModel){
        sortFilterView?.let { sortFilterView ->
            if(dynamicFilterModel.data.filter.isEmpty() && dynamicFilterModel.data.sort.isEmpty()){
                sortFilterView.sortFilterPrefix.hide()
                sortFilterView.hide()
            } else {
                if(!sortFilterView.isVisible){
                    sortFilterView.resetAllFilters()
                    sortFilterView.show()
                    sortFilterView.sortFilterPrefix.show()
                }
                sortFilterView.addItem(filters as ArrayList<SortFilterItem>)
            }
            val sortChip = recommendationViewModel.filterSortChip.value?.data?.filterAndSort?.sortChip?.find { it.isSelected }?.value
            val selectedSort = if(sortChip != null && sortChip != DEFAULT_VALUE_SORT) 1 else 0
            sortFilterView.parentListener = { openBottomSheetFilterRevamp(dynamicFilterModel) }
            sortFilterView.indicatorCounter = dynamicFilterModel.data.filter.getOptions().getCountSelected() + selectedSort
        }
    }

    private fun openBottomSheetFilterRevamp(dynamicFilterModel: DynamicFilterModel){
        filterSortBottomSheet = SortFilterBottomSheet()
        filterSortBottomSheet?.show(
                requireFragmentManager(),
                recommendationViewModel.getSelectedSortFilter(),
                dynamicFilterModel,
                this
        )

        filterSortBottomSheet?.setOnDismissListener {
            filterSortBottomSheet = null
        }
    }

    private fun onQuickFilterClick(item: SortFilterItem, recom: RecommendationFilterChipsEntity.RecommendationFilterChip){
        adapter.clearAllElements()
        recommendationViewModel.getRecommendationFromQuickFilter(item.title.toString(), source, productId)
        SimilarProductRecommendationTracking.eventUserClickQuickFilterChip(recommendationViewModel.userId(), "${recom.options.firstOrNull()?.key ?: ""}=${recom.options.firstOrNull()?.value ?: ""}")
    }

    /**
     * =================================================================================
     * Listener from [RecommendationEmptyViewHolder.RecommendationEmptyStateListener]
     * =================================================================================
     */
    override fun onResetFilterClick() {
        onRefreshRecommendation()
    }

    /**
     * =================================================================================
     * Listener from [RecommendationListener]
     * =================================================================================
     */

    /**
     * This void from Callback [RecommendationListener]
     * It handling item click
     * @param item the item clicked
     * @param layoutType the layoutType is type layout where item placed
     * @param position list of position of the item at Adapter, can be [1] or [1,2] for dynamic nested item
     */
    override fun onProductClick(item: RecommendationItem, layoutType: String?, vararg position: Int) {
        try {
            if(recommendationViewModel.isLoggedIn()) SimilarProductRecommendationTracking.eventClick(item, item.position.toString(), ref, internalRef)
            else SimilarProductRecommendationTracking.eventClickNonLogin(item, item.position.toString(), ref, internalRef)
            RouteManager.getIntent(activity, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, item.productId.toString()).run {
                putExtra(PDP_EXTRA_UPDATED_POSITION, position.first())
                startActivityForResult(this, REQUEST_FROM_PDP)
            }
        }catch (ex: Exception){

        }
    }

    /**
     * This void from Callback [RecommendationListener]
     * It handling product impression item
     * @param item the item clicked
     */
    override fun onProductImpression(item: RecommendationItem) {
        trackingQueue?.let { trackingQueue ->
            if(recommendationViewModel.isLoggedIn()) SimilarProductRecommendationTracking.eventImpression(trackingQueue, item, item.position.toString(), ref, internalRef)
            else SimilarProductRecommendationTracking.eventImpressionNonLogin(trackingQueue, item, item.position.toString(), ref, internalRef)
        }
    }

    /**
     * This void from Callback [RecommendationListener]
     * It handling wishlist click from item
     * @param item the item clicked
     * @param isAddWishlist the wishlist is selected or not
     * @param callback the callback for notify when success or not, there are have 2 params [Boolean] and [Throwable]
     */
    override fun onWishlistClick(item: RecommendationItem, isAddWishlist: Boolean, callback: (Boolean, Throwable?) -> Unit) {
        if(recommendationViewModel.isLoggedIn()){
            SimilarProductRecommendationTracking.eventClickWishlist(isAddWishlist)
            if(isAddWishlist){
                recommendationViewModel.addWishlist(item, callback)
            } else {
                recommendationViewModel.removeWishlist(item, callback)
            }
        }else{
            SimilarProductRecommendationTracking.eventClickWishlistNonLogin()
            RouteManager.route(context, ApplinkConst.LOGIN)
        }
    }

    /**
     * This void from Callback [RecommendationListener]
     * It handling three dots click from product card
     * @param item the item clicked
     * @param position is array position, which mean more than 1 is nested
     */
    override fun onThreeDotsClick(item: RecommendationItem, vararg position: Int) {
        showProductCardOptions(
                this,
                createProductCardOptionsModel(item, position[0])
        )
    }

    /**
     * =================================================================================
     * Listener from [SortFilterBottomSheet.Callback]
     * =================================================================================
     */
    override fun onApplySortFilter(applySortFilterModel: SortFilterBottomSheet.ApplySortFilterModel) {
        adapter.clearAllElements()
        recommendationViewModel.getRecommendationFromFullFilter(applySortFilterModel.selectedSortMapParameter, applySortFilterModel.selectedFilterMapParameter, source, productId)
        filterSortBottomSheet = null
        val mapFilter = mutableMapOf<String, String>()
        applySortFilterModel.selectedFilterMapParameter.forEach {
            if(mapFilter.containsKey(it.key)) mapFilter[it.key] = mapFilter[it.key] + "&" + it.value
            else mapFilter[it.key] = it.value
        }
        val selectedFilterString = mapFilter.map { it.key + "=" + it.value }.joinToString("&")

        val selectedSortString = applySortFilterModel.selectedSortMapParameter.map { "${it.key}=${it.value}" }.joinToString("&")
        applySortFilterModel.mapParameter.forEach {
            SimilarProductRecommendationTracking.eventUserClickFullFilterChip(recommendationViewModel.userId(), "${it.key}=${it.value}")
        }
        var trackerParam = selectedSortString
        if(selectedFilterString.isNotEmpty()){
            trackerParam += if(trackerParam.isNotEmpty()) "&" else "" + selectedFilterString
        }
        SimilarProductRecommendationTracking.eventUserClickShowProduct(recommendationViewModel.userId(), trackerParam)
    }

    override fun getResultCount(mapParameter: Map<String, String>) {
        filterSortBottomSheet?.setResultCountText(getString(R.string.recom_filter_sort_apply))
    }

    /**
     * =================================================================================
     * Private function
     * =================================================================================
     */

    /**
     * Function [mapDataModel]
     * It handling mapper pojo into dataModel
     * @param listRecommendationModel list pojo recommendationWidget from API
     * @return list of dataModel
     */
    private fun mapDataModel(listRecommendationModel: List<RecommendationItem>): List<RecommendationItemDataModel>{
        return listRecommendationModel.map { RecommendationItemDataModel(it) }
    }

    private fun createProductCardOptionsModel(recommendationItem: RecommendationItem, position: Int): ProductCardOptionsModel {
        val productCardOptionsModel = ProductCardOptionsModel()
        productCardOptionsModel.hasWishlist = true
        productCardOptionsModel.isWishlisted = recommendationItem.isWishlist
        productCardOptionsModel.productId = recommendationItem.productId.toString()
        productCardOptionsModel.isTopAds = recommendationItem.isTopAds
        productCardOptionsModel.topAdsWishlistUrl = recommendationItem.wishlistUrl
        productCardOptionsModel.productPosition = position
        productCardOptionsModel.screenName = recommendationItem.header
        return productCardOptionsModel
    }
}