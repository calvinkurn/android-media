package com.tokopedia.shop.home.view.adapter

import android.os.Parcelable
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.LoadingMoreViewHolder
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.play.widget.ui.PlayWidgetState
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel
import com.tokopedia.shop.R
import com.tokopedia.shop.common.data.model.ShopPageWidgetUiModel
import com.tokopedia.shop.common.util.ShopProductViewGridType
import com.tokopedia.shop.common.util.ShopUtil.setElement
import com.tokopedia.shop.home.WidgetName
import com.tokopedia.shop.home.view.adapter.viewholder.*
import com.tokopedia.shop.home.view.adapter.viewholder.advance_carousel_banner.ShopHomeDisplayAdvanceCarouselBannerViewHolder
import com.tokopedia.shop.home.view.model.*
import com.tokopedia.shop.product.view.adapter.scrolllistener.DataEndlessScrollListener
import com.tokopedia.shop.product.view.datamodel.ShopProductSortFilterUiModel
import com.tokopedia.shop.product.view.viewholder.ShopProductSortFilterViewHolder
import com.tokopedia.shop.product.view.widget.OnStickySingleHeaderListener
import com.tokopedia.shop.product.view.widget.StickySingleHeaderView
import com.tokopedia.shop_widget.common.util.WidgetState
import com.tokopedia.shop_widget.thematicwidget.uimodel.ThematicWidgetUiModel
import com.tokopedia.youtube_common.data.model.YoutubeVideoDetailModel

/**
 * Created by rizqiaryansa on 2020-02-21.
 */
open class ShopHomeAdapter(
    private val shopHomeAdapterTypeFactory: AdapterTypeFactory
) : BaseListAdapter<Visitable<*>, AdapterTypeFactory>(shopHomeAdapterTypeFactory),
    DataEndlessScrollListener.OnDataEndlessScrollListener,
    StickySingleHeaderView.OnStickySingleHeaderAdapter {

    companion object {
        private const val INVALID_INDEX = -1
        private const val ALL_PRODUCT_STRING = "Semua Produk"
    }

    override val stickyHeaderPosition: Int
        get() = visitables.indexOfFirst {
            it::class.java == ShopProductSortFilterUiModel::class.java
        }

    private var onStickySingleHeaderViewListener: OnStickySingleHeaderListener? = null
    var isOwner: Boolean = false
    private var recyclerView: RecyclerView? = null
    var productListViewModel: MutableList<ShopHomeProductUiModel> = mutableListOf()
    val shopHomeEtalaseTitlePosition: Int
        get() = visitables.indexOfFirst {
            it.javaClass == ShopHomeProductEtalaseTitleUiModel::class.java
        }.takeIf { it != -1 } ?: 0

    override fun onViewAttachedToWindow(holder: AbstractViewHolder<out Visitable<*>>) {
        super.onViewAttachedToWindow(holder)
        if (holder is ShopHomeSliderBannerViewHolder) {
            holder.resumeTimer()
        }
        else if (holder is ShopHomeDisplayAdvanceCarouselBannerViewHolder) {
            holder.resumeTimer()
        }
    }

    override fun onViewDetachedFromWindow(holder: AbstractViewHolder<out Visitable<*>>) {
        if (holder is ShopHomeSliderBannerViewHolder) {
            holder.pauseTimer()
        }
        else if(holder is ShopHomeDisplayAdvanceCarouselBannerViewHolder){
            holder.pauseTimer()
        }
        super.onViewDetachedFromWindow(holder)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<*>, position: Int) {
        val layoutParams = holder.itemView.layoutParams
        if (layoutParams is StaggeredGridLayoutManager.LayoutParams) {
            layoutParams.isFullSpan = !(
                getItemViewType(position) == ShopHomeProductViewHolder.LAYOUT ||
                    getItemViewType(position) == ShopHomeProductItemBigGridViewHolder.LAYOUT ||
                    getItemViewType(position) == ShopHomeProductItemListViewHolder.LAYOUT ||
                    getItemViewType(position) == LoadingMoreViewHolder.LAYOUT
                )
        }
        super.onBindViewHolder(holder, position)
    }

    override fun clearAllElements() {
        super.clearAllElements()
        refreshSticky()
    }

    fun setProductListData(productList: List<ShopHomeProductUiModel>, isOwner: Boolean) {
        val newList = getNewVisitableItems()
        productListViewModel.addAll(productList)
        newList.remove(ShopHomeProductListEmptyUiModel(isOwner))
        newList.addAll(productList)
        submitList(newList)
    }

    fun setProductListEmptyState(isOwner: Boolean) {
        val newList = getNewVisitableItems()
        if (!newList.contains(ShopHomeProductListEmptyUiModel(isOwner)))
            newList.add(ShopHomeProductListEmptyUiModel(isOwner))
        submitList(newList)
    }

    fun setEtalaseTitleData() {
        val newList = getNewVisitableItems()
        if (newList.filterIsInstance(ShopHomeProductEtalaseTitleUiModel::class.java).isEmpty()) {
            val etalaseTitleUiModel = ShopHomeProductEtalaseTitleUiModel(ALL_PRODUCT_STRING, "")
            newList.add(etalaseTitleUiModel)
            submitList(newList)
        }
    }

    fun setSortFilterData(shopProductSortFilterUiModel: ShopProductSortFilterUiModel) {
        val newList = getNewVisitableItems()
        if (newList.filterIsInstance(ShopProductSortFilterUiModel::class.java).isEmpty()) {
            newList.add(shopProductSortFilterUiModel)
            submitList(newList)
        }
    }

    fun setHomeLayoutData(data: List<Visitable<*>>) {
        val newList = getNewVisitableItems()
        newList.clear()
        newList.addAll(
            data.onEach {
                if (it is BaseShopHomeWidgetUiModel) {
                    it.widgetState = WidgetState.PLACEHOLDER
                } else if (it is ThematicWidgetUiModel) {
                    it.widgetState = WidgetState.PLACEHOLDER
                }
            }
        )
        newList.add(ProductGridListPlaceholderUiModel(WidgetState.PLACEHOLDER))
        submitList(newList)
    }

    fun addProductGridListPlaceHolder() {
        val newList = getNewVisitableItems()
        newList.add(ProductGridListPlaceholderUiModel(WidgetState.PLACEHOLDER))
        submitList(newList)
    }

    fun setHomeYouTubeData(widgetId: String, data: YoutubeVideoDetailModel) {
        val newList = getNewVisitableItems()
        newList.filterIsInstance<ShopHomeDisplayWidgetUiModel>()
            .find {
                it.widgetId == widgetId
            }?.let {
                it.data?.firstOrNull()?.youTubeVideoDetail = data
                it.isNewData = true
            }
        submitList(newList)
    }

    fun setHomeMerchantVoucherData(shopHomeVoucherUiModel: ShopHomeVoucherUiModel) {
        val newList = getNewVisitableItems()
        newList.indexOfFirst { it is ShopHomeVoucherUiModel }.let { index ->
            if (index >= 0) {
                if ((shopHomeVoucherUiModel.data == null && !shopHomeVoucherUiModel.isError) || shopHomeVoucherUiModel.data?.isShown == false) {
                    newList.removeAt(index)
                } else {
                    shopHomeVoucherUiModel.widgetState = WidgetState.FINISH
                    shopHomeVoucherUiModel.isNewData = true
                    newList.setElement(index, shopHomeVoucherUiModel)
                }
            }
        }
        submitList(newList)
    }

    fun setProductComparisonData(uiModel: ShopHomePersoProductComparisonUiModel) {
        val newList = getNewVisitableItems()
        newList.indexOfFirst { it is ShopHomePersoProductComparisonUiModel }.let { index ->
            if (index >= 0) {
                if ((uiModel.recommendationWidget == null && !uiModel.isError)) {
                    newList.removeAt(index)
                } else {
                    uiModel.widgetState = WidgetState.FINISH
                    uiModel.isNewData = true
                    newList.setElement(index, uiModel)
                }
            }
        }
        submitList(newList)
    }

    fun removeProductComparisonWidget() {
        val newList = getNewVisitableItems()
        newList.indexOfFirst { it is ShopHomePersoProductComparisonUiModel }.let { index ->
            if (index >= 0) {
                newList.removeAt(index)
            }
        }
        submitList(newList)
    }

    override fun hideLoading() {
        val newList = getNewVisitableItems()
        if (newList.contains(loadingModel)) {
            newList.remove(loadingModel)
            submitList(newList)
        } else if (newList.contains(loadingMoreModel)) {
            newList.remove(loadingMoreModel)
            submitList(newList)
        }
    }

    override fun showLoading() {
        if (!isLoading) {
            val newList = getNewVisitableItems()
            if (isShowLoadingMore) {
                newList.add(loadingMoreModel)
            } else {
                newList.clear()
                newList.add(loadingModel)
            }
            submitList(newList)
        }
    }

    override fun getEndlessDataSize(): Int {
        return productListViewModel.size
    }

    fun getAllProductWidgetPosition(): Int {
        return visitables.indexOfFirst { it is ShopHomeProductUiModel }
    }

    fun updateProductWidgetData(shopHomeCarousellProductUiModel: ShopHomeCarousellProductUiModel) {
        val newList = getNewVisitableItems()
        val position = newList.indexOf(shopHomeCarousellProductUiModel)
        shopHomeCarousellProductUiModel.copy()
        shopHomeCarousellProductUiModel.isNewData = true
        newList.setElement(position, shopHomeCarousellProductUiModel)
        submitList(newList)
    }

    fun updateWishlistProduct(productId: String, isWishlist: Boolean) {
        val newList = getNewVisitableItems()
        newList.filterIsInstance<ShopHomeProductUiModel>().onEach {
            if (it.id == productId) {
                it.isWishList = isWishlist
            }
        }
        newList.filterIsInstance<ShopHomeCarousellProductUiModel>().onEach { shopHomeCarousellProductUiModel ->
            shopHomeCarousellProductUiModel.productList.filter {
                it.id == productId
            }.onEach {
                it.isWishList = isWishlist
            }.also {
                shopHomeCarousellProductUiModel.isNewData = true
            }
        }
        submitList(newList)
    }

    override fun onStickyHide() {
        val newList = getNewVisitableItems()
        submitList(newList)
    }

    override fun createStickyViewHolder(parent: ViewGroup?): RecyclerView.ViewHolder {
        val stickyViewType = getItemViewType(stickyHeaderPosition)
        val view = onCreateViewItem(parent, stickyViewType)
        return shopHomeAdapterTypeFactory.createViewHolder(view, stickyViewType)
    }

    override fun setListener(onStickySingleHeaderViewListener: OnStickySingleHeaderListener?) {
        this.onStickySingleHeaderViewListener = onStickySingleHeaderViewListener
    }

    override fun bindSticky(viewHolder: RecyclerView.ViewHolder?) {
        if (viewHolder is ShopProductSortFilterViewHolder) {
            visitables.filterIsInstance(ShopProductSortFilterUiModel::class.java).firstOrNull()?.let {
                viewHolder.bind(it)
            }
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        this.recyclerView = null
    }

    override fun isShowLoadingMore(): Boolean {
        return visitables.filterIsInstance<ShopHomeProductUiModel>().isNotEmpty()
    }

    fun pauseSliderBannerAutoScroll() {
        val listSliderBannerViewModel = visitables.filterIsInstance<ShopHomeDisplayWidgetUiModel>().filter {
            it.name == WidgetName.SLIDER_BANNER
        }
        listSliderBannerViewModel.forEach {
            (recyclerView?.findViewHolderForAdapterPosition(visitables.indexOf(it)) as? ShopHomeSliderBannerViewHolder)?.pauseTimer()
            (recyclerView?.findViewHolderForAdapterPosition(visitables.indexOf(it)) as? ShopHomeDisplayAdvanceCarouselBannerViewHolder)?.pauseTimer()
        }
    }

    fun resumeSliderBannerAutoScroll() {
        val listSliderBannerViewModel = visitables.filterIsInstance<ShopHomeDisplayWidgetUiModel>().filter {
            it.name == WidgetName.SLIDER_BANNER
        }
        listSliderBannerViewModel.forEach {
            (recyclerView?.findViewHolderForAdapterPosition(visitables.indexOf(it)) as? ShopHomeSliderBannerViewHolder)?.resumeTimer()
            (recyclerView?.findViewHolderForAdapterPosition(visitables.indexOf(it)) as? ShopHomeDisplayAdvanceCarouselBannerViewHolder)?.resumeTimer()
        }
    }

    fun refreshSticky() {
        if (onStickySingleHeaderViewListener != null) {
            recyclerView?.post { onStickySingleHeaderViewListener?.refreshSticky() }
        }
    }

    fun changeSelectedSortFilter(sortId: String, sortName: String) {
        val newList = getNewVisitableItems()
        val shopProductSortFilterUiViewModel = newList
            .filterIsInstance<ShopProductSortFilterUiModel>().firstOrNull()
        shopProductSortFilterUiViewModel?.apply {
            selectedSortId = sortId
            selectedSortName = sortName
        }
        submitList(newList)
    }

    fun changeSortFilterIndicatorCounter(filterIndicatorCounter: Int) {
        val newList = getNewVisitableItems()
        val shopProductSortFilterUiViewModel = newList
            .filterIsInstance<ShopProductSortFilterUiModel>().firstOrNull()
        shopProductSortFilterUiViewModel?.apply {
            this.filterIndicatorCounter = filterIndicatorCounter
        }
        submitList(newList)
    }

    fun removeProductList() {
        val newList = getNewVisitableItems()
        val firstProductViewModelIndex = newList.indexOfFirst {
            it::class.java == ShopHomeProductUiModel::class.java
        }
        val totalProductViewModelData = newList.filterIsInstance<ShopHomeProductUiModel>().size
        if (firstProductViewModelIndex >= 0 && totalProductViewModelData <= newList.size) {
            newList.removeAll(newList.filterIsInstance<ShopHomeProductUiModel>())
            productListViewModel.clear()
            submitList(newList)
        }
    }

    private fun setLayoutManagerSpanCount() {
        (recyclerView?.layoutManager as? StaggeredGridLayoutManager)?.spanCount = when ((shopHomeAdapterTypeFactory as? ShopHomeAdapterTypeFactory)?.productCardType) {
            ShopProductViewGridType.BIG_GRID -> {
                recyclerView?.context?.resources?.getInteger(R.integer.span_count_big_grid) ?: 1
            }
            ShopProductViewGridType.SMALL_GRID -> {
                recyclerView?.context?.resources?.getInteger(R.integer.span_count_small_grid) ?: 2
            }
            ShopProductViewGridType.LIST -> {
                recyclerView?.context?.resources?.getInteger(R.integer.span_count_list) ?: 1
            }
            else -> {
                Int.ONE
            }
        }
    }

    fun updateRemindMeStatusCampaignNplWidgetData(
        campaignId: String,
        isRemindMe: Boolean? = null,
        isClickRemindMe: Boolean = false
    ) {
        val newList = getNewVisitableItems()
        newList.filterIsInstance<ShopHomeNewProductLaunchCampaignUiModel>().onEach { nplCampaignUiModel ->
            nplCampaignUiModel.data?.firstOrNull { it.campaignId == campaignId }?.let {
                isRemindMe?.let { isRemindMe ->
                    it.isRemindMe = isRemindMe
                    if (isClickRemindMe) {
                        if (isRemindMe)
                            ++it.totalNotify
                        else
                            --it.totalNotify
                    }
                }
                it.showRemindMeLoading = false
                nplCampaignUiModel.isNewData = true
            }
        }
        submitList(newList)
    }

    fun updateRemindMeStatusCampaignFlashSaleWidgetData(
        campaignId: String,
        isRemindMe: Boolean? = null,
        isClickRemindMe: Boolean = false
    ) {
        val newList = getNewVisitableItems()
        newList.filterIsInstance<ShopHomeFlashSaleUiModel>().onEach { flashSaleCampaignUiModel ->
            flashSaleCampaignUiModel.data?.firstOrNull { it.campaignId == campaignId }?.let {
                isRemindMe?.let { isRemindMe ->
                    it.isRemindMe = isRemindMe
                    if (isClickRemindMe) {
                        if (isRemindMe)
                            ++it.totalNotify
                        else
                            --it.totalNotify
                    }
                }
                flashSaleCampaignUiModel.isNewData = true
            }
        }
        submitList(newList)
    }

    fun removeWidget(model: Visitable<*>) {
        val newList = getNewVisitableItems()
        val modelIndex = newList.indexOf(model)
        if (modelIndex != INVALID_INDEX) {
            newList.remove(model)
            submitList(newList)
        }
    }

    fun showNplRemindMeLoading(campaignId: String) {
        val newList = getNewVisitableItems()
        newList.filterIsInstance<ShopHomeNewProductLaunchCampaignUiModel>().onEach { nplCampaignUiModel ->
            nplCampaignUiModel.data?.firstOrNull { it.campaignId == campaignId }?.let {
                it.showRemindMeLoading = true
                nplCampaignUiModel.isNewData = true
            }
        }
        submitList(newList)
    }

    fun showBannerTimerRemindMeLoading() {
        val newList = getNewVisitableItems()
        newList.filterIsInstance<ShopWidgetDisplayBannerTimerUiModel>().onEach { nplCampaignUiModel ->
            nplCampaignUiModel.let {
                it.data?.showRemindMeLoading = true
                it.isNewData = true
            }
        }
        submitList(newList)
    }

    // ========== Shop Home Revamp V4 ============== //
    fun getTerlarisWidgetUiModel(): ShopHomeV4TerlarisUiModel? {
        return visitables.filterIsInstance<ShopHomeV4TerlarisUiModel>().firstOrNull()
    }

    fun setTerlarisWidgetData(uiModel: ShopHomeV4TerlarisUiModel) {
        val newList = getNewVisitableItems()
        submitList(newList)
    }

    // ========== Shop Home Revamp V4 ============== //

    fun getNplCampaignUiModel(campaignId: String): ShopHomeNewProductLaunchCampaignUiModel? {
        return visitables.filterIsInstance<ShopHomeNewProductLaunchCampaignUiModel>().firstOrNull {
            it.data?.firstOrNull()?.campaignId == campaignId
        }
    }

    fun getFlashSaleCampaignUiModel(campaignId: String): ShopHomeFlashSaleUiModel? {
        return visitables.filterIsInstance<ShopHomeFlashSaleUiModel>().firstOrNull {
            it.data?.firstOrNull()?.campaignId == campaignId
        }
    }

    fun changeProductCardGridType(gridType: ShopProductViewGridType) {
        (shopHomeAdapterTypeFactory as? ShopHomeAdapterTypeFactory)?.productCardType = gridType
        setLayoutManagerSpanCount()
        recyclerView?.requestLayout()
    }

    fun updateShopPageProductChangeGridSectionIcon(
        isProductListEmpty: Boolean,
        totalProductData: Int,
        gridType: ShopProductViewGridType = ShopProductViewGridType.SMALL_GRID
    ) {
        val newList = getNewVisitableItems()
        val gridSectionModel = newList.filterIsInstance<ShopHomeProductChangeGridSectionUiModel>().firstOrNull()
        if (gridSectionModel == null) {
            if (!isProductListEmpty) {
                newList.add(ShopHomeProductChangeGridSectionUiModel(totalProductData, gridType))
            }
        } else {
            gridSectionModel.apply {
                if (isProductListEmpty) {
                    newList.remove(this)
                } else {
                    this.totalProduct = totalProductData
                }
            }
        }
        submitList(newList)
    }

    fun updateShopPageProductChangeGridSectionIcon(gridType: ShopProductViewGridType) {
        val newList = getNewVisitableItems()
        newList.filterIsInstance<ShopHomeProductChangeGridSectionUiModel>().firstOrNull()?.apply {
            this.gridType = gridType
        }
        submitList(newList)
    }

    /**
     * Play Widget
     */
    fun updatePlayWidget(playWidgetState: PlayWidgetState?) {
        val newList = getNewVisitableItems()
        newList.indexOfFirst { it is CarouselPlayWidgetUiModel }.let { position ->
            if (position == -1) return@let
            if (playWidgetState == null || playWidgetState.isLoading || isPlayWidgetEmpty(playWidgetState.model)) {
                newList.removeAt(position)
            } else {
                (newList.getOrNull(position) as? CarouselPlayWidgetUiModel)?.copy(playWidgetState = playWidgetState)?.apply {
                    widgetState = WidgetState.FINISH
                    isNewData = true
                }?.also {
                    newList.setElement(position, it)
                }
            }
        }
        submitList(newList)
    }

    private fun isPlayWidgetEmpty(widget: PlayWidgetUiModel): Boolean {
        return widget.items.isEmpty()
    }

    fun updateShopHomeWidgetContentData(listWidgetContentData: Map<Pair<String, String>, Visitable<*>?>) {
        val newList = getNewVisitableItems()
        listWidgetContentData.onEach { widgetContentData ->
            newList.filterIsInstance<Visitable<*>>().indexOfFirst {
                when (it) {
                    is BaseShopHomeWidgetUiModel -> {
                        widgetContentData.key.first == it.widgetId
                    }
                    is ThematicWidgetUiModel -> {
                        widgetContentData.key.first == it.widgetId
                    }
                    else -> {
                        false
                    }
                }
            }.let { position ->
                if (position >= 0 && position < newList.size) {
                    when (widgetContentData.value) {
                        null -> {
                            newList.removeAt(position)
                        }
                        is BaseShopHomeWidgetUiModel -> {
                            (widgetContentData.value as BaseShopHomeWidgetUiModel).widgetState = WidgetState.FINISH
                            (widgetContentData.value as BaseShopHomeWidgetUiModel).isNewData = true
                            newList.setElement(position, widgetContentData.value)
                        }
                        is ThematicWidgetUiModel -> {
                            (widgetContentData.value as ThematicWidgetUiModel).widgetState = WidgetState.FINISH
                            (widgetContentData.value as ThematicWidgetUiModel).isNewData = true
                            newList.setElement(position, widgetContentData.value)
                        }
                    }
                }
            }
        }
        submitList(newList)
    }

    fun updateShopHomeWidgetStateToLoading(listWidgetLayout: MutableList<ShopPageWidgetUiModel>) {
        listWidgetLayout.onEach { widgetLayout ->
            visitables.filterIsInstance<Visitable<*>>().firstOrNull {
                when (it) {
                    is BaseShopHomeWidgetUiModel -> {
                        widgetLayout.widgetId == it.widgetId
                    }
                    is ThematicWidgetUiModel -> {
                        widgetLayout.widgetId == it.widgetId
                    }
                    else -> {
                        false
                    }
                }
            }?.let {
                when (it) {
                    is BaseShopHomeWidgetUiModel -> {
                        it.widgetState = WidgetState.LOADING
                    }
                    is ThematicWidgetUiModel -> {
                        it.widgetState = WidgetState.LOADING
                    }
                }
            }
        }
    }

    fun isLoadNextHomeWidgetData(position: Int): Boolean {
        return visitables.filterIsInstance<BaseShopHomeWidgetUiModel>().getOrNull(position)?.widgetState == WidgetState.PLACEHOLDER ||
            visitables.filterIsInstance<ThematicWidgetUiModel>().getOrNull(position)?.widgetState == WidgetState.PLACEHOLDER
    }

    fun isLoadProductGridListData(position: Int): Boolean {
        val isProductGridListPlaceholderUiMode = (visitables.getOrNull(position) as? ProductGridListPlaceholderUiModel)?.widgetState == WidgetState.PLACEHOLDER
        val isSortFilterDataUiModelEmpty = visitables.filterIsInstance(ShopProductSortFilterUiModel::class.java).isEmpty()
        return isProductGridListPlaceholderUiMode && isSortFilterDataUiModelEmpty
    }

    fun updateProductGridListPlaceholderStateToLoadingState() {
        visitables.filterIsInstance<ProductGridListPlaceholderUiModel>().firstOrNull()?.let {
            it.widgetState = WidgetState.LOADING
        }
    }

    fun removeProductGridListPlaceholder() {
        val newList = getNewVisitableItems()
        newList.indexOfFirst { it is ProductGridListPlaceholderUiModel }.let { position ->
            if (position >= 0 && position < newList.size) {
                newList.removeAt(position)
            }
        }
        submitList(newList)
    }

    fun isProductGridListPlaceholderExists(): Boolean {
        return visitables.filterIsInstance<ProductGridListPlaceholderUiModel>().isNotEmpty()
    }

    fun isLoadFirstWidgetContentData(): Boolean {
        return visitables.filterIsInstance<Visitable<*>>().none {
            when (it) {
                is BaseShopHomeWidgetUiModel -> it.widgetState == WidgetState.LOADING || it.widgetState == WidgetState.FINISH
                is ThematicWidgetUiModel -> it.widgetState == WidgetState.LOADING || it.widgetState == WidgetState.FINISH
                else -> false
            }
        }
    }

    fun getPlayWidgetUiModel(): CarouselPlayWidgetUiModel? {
        return visitables.filterIsInstance<CarouselPlayWidgetUiModel>().firstOrNull()
    }

    fun getMvcWidgetUiModel(): ShopHomeVoucherUiModel? {
        return visitables.filterIsInstance<ShopHomeVoucherUiModel>().firstOrNull()
    }

    fun getPersoProductComparisonWidgetUiModel(): ShopHomePersoProductComparisonUiModel? {
        return visitables.filterIsInstance<ShopHomePersoProductComparisonUiModel>().firstOrNull()
    }

    fun removeShopHomeWidget(listShopWidgetLayout: List<ShopPageWidgetUiModel>) {
        val newList = getNewVisitableItems()
        listShopWidgetLayout.onEach { shopWidgetLayout ->
            newList.filterIsInstance<Visitable<*>>().indexOfFirst {
                when (it) {
                    is BaseShopHomeWidgetUiModel -> {
                        shopWidgetLayout.widgetId == it.widgetId
                    }
                    is ThematicWidgetUiModel -> {
                        shopWidgetLayout.widgetId == it.widgetId
                    }
                    else -> {
                        false
                    }
                }
            }.let { position ->
                newList.removeAt(position)
            }
        }
        submitList(newList)
    }

    fun getNewVisitableItems() = visitables.toMutableList()

    fun submitList(newList: List<Visitable<*>>) {
        val currentRecyclerViewState: Parcelable? = recyclerView?.layoutManager?.onSaveInstanceState()
        val diffCallback = ShopPageHomeDiffUtilCallback(visitables, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        visitables.clear()
        newList.forEach {
            when (it) {
                is BaseShopHomeWidgetUiModel -> it.isNewData = false
                is ThematicWidgetUiModel -> it.isNewData = false
            }
        }
        visitables.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
        currentRecyclerViewState?.let {
            recyclerView?.layoutManager?.onRestoreInstanceState(it)
        }
    }

    fun isCampaignFollower(campaignId: String): Boolean {
        return visitables.filterIsInstance<ShopHomeNewProductLaunchCampaignUiModel>().any {
            val campaignItem = it.data?.firstOrNull()
            val nplItemCampaignId = campaignItem?.campaignId.orEmpty()
            val dynamicRule = campaignItem?.dynamicRule
            val dynamicRuleDescription = dynamicRule?.descriptionHeader.orEmpty()
            nplItemCampaignId == campaignId && dynamicRuleDescription.isNotEmpty()
        }
    }

    fun getLastVisibleShopWidgetPosition(lastVisibleItemPosition: Int): Int {
        return when (visitables.getOrNull(lastVisibleItemPosition)) {
            is BaseShopHomeWidgetUiModel, is ThematicWidgetUiModel -> {
                lastVisibleItemPosition
            }
            else -> {
                val lastShopWidgetUiModel = visitables.lastOrNull {
                    it is BaseShopHomeWidgetUiModel || it is ThematicWidgetUiModel
                }
                visitables.lastIndexOf(lastShopWidgetUiModel)
            }
        }
    }

    fun getShopHomeWidgetData(): List<BaseShopHomeWidgetUiModel> {
        return visitables.filterIsInstance<BaseShopHomeWidgetUiModel>()
    }

    fun anyFestivityOnShopHomeWidget(): Boolean {
        return visitables.filterIsInstance<Visitable<*>>().any {
            when (it) {
                is BaseShopHomeWidgetUiModel -> it.isFestivity
                is ThematicWidgetUiModel -> it.isFestivity
                else -> false
            }
        }
    }
}
