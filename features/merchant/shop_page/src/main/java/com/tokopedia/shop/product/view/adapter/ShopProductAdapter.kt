package com.tokopedia.shop.product.view.adapter

import android.os.Parcelable
import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.LoadingMoreViewHolder
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.shop.R
import com.tokopedia.shop.analytic.OldShopPageTrackingConstant.ALL_ETALASE
import com.tokopedia.shop.common.constant.ShopPageConstant.KEY_ETALASE_HIGHLIGHT_DATA_MODEL
import com.tokopedia.shop.common.constant.ShopPageConstant.KEY_ETALASE_TITLE_DATA_MODEL
import com.tokopedia.shop.common.constant.ShopPageConstant.KEY_FEATURED_PRODUCT_DATA_MODEL
import com.tokopedia.shop.common.constant.ShopPageConstant.KEY_MEMBERSHIP_DATA_MODEL
import com.tokopedia.shop.common.constant.ShopPageConstant.KEY_MERCHANT_VOUCHER_DATA_MODEL
import com.tokopedia.shop.common.constant.ShopPageConstant.KEY_SHOP_BUYER_EMPTY_PRODUCT_DATA_MODEL
import com.tokopedia.shop.common.constant.ShopPageConstant.KEY_SHOP_PRODUCT_ADD_DATA_MODEL
import com.tokopedia.shop.common.constant.ShopPageConstant.KEY_SHOP_PRODUCT_FIRST_DATA_MODEL
import com.tokopedia.shop.common.constant.ShopPageConstant.KEY_SHOP_SELLER_EMPTY_PRODUCT_ALL_ETALASE_DATA_MODEL
import com.tokopedia.shop.common.constant.ShopPageConstant.KEY_SORT_FILTER_DATA_MODEL
import com.tokopedia.shop.common.util.ShopProductViewGridType
import com.tokopedia.shop.product.view.adapter.scrolllistener.DataEndlessScrollListener
import com.tokopedia.shop.product.view.datamodel.*
import com.tokopedia.shop.product.view.viewholder.*
import com.tokopedia.shop.product.view.widget.OnStickySingleHeaderListener
import com.tokopedia.shop.product.view.widget.StickySingleHeaderView

class ShopProductAdapter(private val shopProductAdapterTypeFactory: ShopProductAdapterTypeFactory) : BaseListAdapter<BaseShopProductViewModel, ShopProductAdapterTypeFactory>(shopProductAdapterTypeFactory, null), DataEndlessScrollListener.OnDataEndlessScrollListener, StickySingleHeaderView.OnStickySingleHeaderAdapter {

    init {
        shopProductAdapterTypeFactory.attachAdapter(this)
    }

    val shopProductUiModelList: MutableList<ShopProductUiModel> = mutableListOf()
    private val shopProductSortFilterUiViewModel: ShopProductSortFilterUiModel?
        get() = mapOfDataModel[KEY_SORT_FILTER_DATA_MODEL] as? ShopProductSortFilterUiModel
    val shopProductFirstViewModelPosition: Int
        get() = shopProductFirstUiModel?.let {
            visitables.indexOf(it)
        } ?: 0
    val shopChangeProductGridSegment: Int
        get() = visitables.indexOfFirst {
            it.javaClass == ShopProductChangeGridSectionUiModel::class.java
        }.takeIf { it != -1 } ?: 0
    private var onStickySingleHeaderViewListener: OnStickySingleHeaderListener? = null
    private var recyclerView: RecyclerView? = null
    private var mapOfDataModel = mutableMapOf<String, Visitable<*>>()
    private val shopProductEtalaseHighlightUiModel: ShopProductEtalaseHighlightUiModel?
        get() = mapOfDataModel[KEY_ETALASE_HIGHLIGHT_DATA_MODEL] as? ShopProductEtalaseHighlightUiModel
    private val membershipStampUiModel: MembershipStampProgressUiModel?
        get() = mapOfDataModel[KEY_MEMBERSHIP_DATA_MODEL] as? MembershipStampProgressUiModel
    private val shopMerchantVoucherUiModel: ShopMerchantVoucherUiModel?
        get() = mapOfDataModel[KEY_MERCHANT_VOUCHER_DATA_MODEL] as? ShopMerchantVoucherUiModel
    private val sellerEmptyProductAllEtalaseDataModel: ShopSellerEmptyProductAllEtalaseUiModel?
        get() = mapOfDataModel[KEY_SHOP_SELLER_EMPTY_PRODUCT_ALL_ETALASE_DATA_MODEL] as? ShopSellerEmptyProductAllEtalaseUiModel
    private val shopEmptyProductUiModel: ShopEmptyProductUiModel?
        get() = mapOfDataModel[KEY_SHOP_BUYER_EMPTY_PRODUCT_DATA_MODEL] as? ShopEmptyProductUiModel
    private val shopProductAddUiModel: ShopProductAddUiModel?
        get() = mapOfDataModel[KEY_SHOP_PRODUCT_ADD_DATA_MODEL] as? ShopProductAddUiModel
    private val shopProductFirstUiModel: ShopProductUiModel?
        get() = mapOfDataModel[KEY_SHOP_PRODUCT_FIRST_DATA_MODEL] as? ShopProductUiModel
    override val stickyHeaderPosition: Int
        get() = visitables.indexOf(shopProductSortFilterUiViewModel)

    override fun showErrorNetwork(message: String, onRetryListener: ErrorNetworkModel.OnRetryListener) {
        errorNetworkModel.errorMessage = message
        errorNetworkModel.onRetryListener = onRetryListener
        this.shopProductUiModelList.clear()
        clearAllNonDataElement()
        visitables.add(errorNetworkModel)
        val newList = getNewVisitableItems()
        newList.add(errorNetworkModel)
        submitList(newList)
        mapDataModel()
    }

    private fun getNewVisitableItems() = visitables.toMutableList()

    override fun isShowLoadingMore(): Boolean {
        return shopProductUiModelList.size > 0
    }

    override fun isItemClickableByDefault(): Boolean {
        return false
    }

    override fun getEndlessDataSize(): Int {
        return shopProductUiModelList.size
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<*>, position: Int) {
        if (holder.itemView.layoutParams is StaggeredGridLayoutManager.LayoutParams) {
            val staggeredLayoutParams = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
            staggeredLayoutParams.isFullSpan = !(
                getItemViewType(position) == ShopProductViewHolder.GRID_LAYOUT ||
                    getItemViewType(position) == ShopProductItemBigGridViewHolder.LAYOUT ||
                    getItemViewType(position) == ShopProductItemListViewHolder.LAYOUT ||
                    getItemViewType(position) == ShopProductAddViewHolder.LAYOUT ||
                    getItemViewType(position) == ShopProductSellerAllEtalaseEmptyViewHolder.LAYOUT ||
                    getItemViewType(position) == LoadingMoreViewHolder.LAYOUT
                )
        }
        super.onBindViewHolder(holder, position)
    }

    private fun setLayoutManagerSpanCount() {
        (recyclerView?.layoutManager as? StaggeredGridLayoutManager)?.spanCount = when (shopProductAdapterTypeFactory.productCardType) {
            ShopProductViewGridType.BIG_GRID -> {
                recyclerView?.context?.resources?.getInteger(R.integer.span_count_big_grid) ?: 1
            }
            ShopProductViewGridType.SMALL_GRID -> {
                recyclerView?.context?.resources?.getInteger(R.integer.span_count_small_grid) ?: 2
            }
            ShopProductViewGridType.LIST -> {
                recyclerView?.context?.resources?.getInteger(R.integer.span_count_list) ?: 1
            }
        }
    }

    override fun createStickyViewHolder(parent: ViewGroup?): RecyclerView.ViewHolder {
        val stickyViewType = getItemViewType(stickyHeaderPosition)
        val view = onCreateViewItem(parent, stickyViewType)
        return shopProductAdapterTypeFactory.createViewHolder(view, stickyViewType)
    }

    override fun bindSticky(viewHolder: RecyclerView.ViewHolder?) {
        if (viewHolder is ShopProductSortFilterViewHolder) {
            (mapOfDataModel[KEY_SORT_FILTER_DATA_MODEL] as? ShopProductSortFilterUiModel)?.let {
                viewHolder.bind(it)
            }
        }
    }

    override fun onStickyHide() {
        val newList = getNewVisitableItems()
        submitList(newList)
    }

    override fun setListener(onStickySingleHeaderViewListener: OnStickySingleHeaderListener?) {
        this.onStickySingleHeaderViewListener = onStickySingleHeaderViewListener
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        this.recyclerView = null
    }

    override fun clearAllElements() {
        super.clearAllElements()
        refreshSticky()
        mapDataModel()
    }

    override fun showLoading() {
        if (!isLoading) {
            val newList = getNewVisitableItems()
            if (isShowLoadingMore) {
                newList.add(loadingMoreModel)
            } else {
                newList.add(loadingModel)
            }
            submitList(newList)
        }
    }

    override fun hideLoading() {
        val newList = getNewVisitableItems()
        if (visitables.contains(loadingModel)) {
            newList.remove(loadingModel)
        } else if (visitables.contains(loadingMoreModel)) {
            newList.remove(loadingMoreModel)
        }
        submitList(newList)
    }

    override fun isLoading(): Boolean {
        return visitables.contains(loadingModel) || visitables.contains(loadingMoreModel)
    }

    override fun clearAllNonDataElement() {
        super.clearAllNonDataElement()
        val newList = getNewVisitableItems()
        sellerEmptyProductAllEtalaseDataModel?.let {
            newList.remove(it)
        }
        shopEmptyProductUiModel?.let {
            newList.remove(it)
        }
        shopProductAddUiModel?.let {
            newList.remove(it)
        }
        submitList(newList)
        mapDataModel()
    }

    fun getEtalaseNameHighLight(shopProductUiModel: ShopProductUiModel): String {
        shopProductEtalaseHighlightUiModel?.let {
            val etalaseHighlightCarouselViewModelList = it.getEtalaseHighlightCarouselUiModelList()
            var i = 0
            val sizei = etalaseHighlightCarouselViewModelList?.size.orZero()
            while (i < sizei) {
                val shopProductViewModelList = etalaseHighlightCarouselViewModelList?.getOrNull(i)?.shopProductUiModelList
                var j = 0
                val sizej = shopProductViewModelList?.size.orZero()
                while (j < sizej) {
                    val shopProductViewModelEtalase = shopProductViewModelList?.getOrNull(j)
                    if (shopProductViewModelEtalase?.id == shopProductUiModel.id && shopProductUiModel.etalaseId == etalaseHighlightCarouselViewModelList?.getOrNull(i)?.shopEtalaseViewModel?.etalaseId) {
                        return etalaseHighlightCarouselViewModelList.getOrNull(i)?.shopEtalaseViewModel?.etalaseName.orEmpty()
                    }
                    j++
                }
                i++
            }
            return ALL_ETALASE
        } ?: return ALL_ETALASE
    }

    fun getEtalaseNameHighLightType(shopProductUiModel: ShopProductUiModel): Int? {
        return shopProductEtalaseHighlightUiModel?.let {
            val matchEtalaseHighlight = it.getEtalaseHighlightCarouselUiModelList()?.firstOrNull {
                it.shopProductUiModelList?.firstOrNull { it.id == shopProductUiModel.id } != null
            }
            matchEtalaseHighlight?.shopEtalaseViewModel?.type
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
        mapDataModel()
    }

    fun changeSelectedEtalaseFilter(etalaseId: String, etalaseName: String) {
        val newList = getNewVisitableItems()
        val shopProductSortFilterUiViewModel = newList
            .filterIsInstance<ShopProductSortFilterUiModel>().firstOrNull()
        shopProductSortFilterUiViewModel?.apply {
            selectedEtalaseId = etalaseId
            selectedEtalaseName = etalaseName
        }
        submitList(newList)
        mapDataModel()
    }

    fun changeSortFilterIndicatorCounter(filterIndicatorCounter: Int) {
        val newList = getNewVisitableItems()
        val shopProductSortFilterUiViewModel = newList
            .filterIsInstance<ShopProductSortFilterUiModel>().firstOrNull()
        shopProductSortFilterUiViewModel?.apply {
            this.filterIndicatorCounter = filterIndicatorCounter
        }
        submitList(newList)
        mapDataModel()
    }

    fun clearProductList() {
        if (shopProductUiModelList.isNotEmpty()) {
            val newList = getNewVisitableItems()
            val totalData = newList.size
            var indexStart = 0
            if (mapOfDataModel.isNotEmpty()) {
                indexStart = if (null != shopProductAddUiModel) {
                    newList.indexOf(shopProductAddUiModel)
                } else {
                    newList.indexOf(mapOfDataModel[KEY_SHOP_PRODUCT_FIRST_DATA_MODEL])
                }
            }
            if (indexStart >= 0 && totalData <= newList.size && indexStart < totalData) {
                newList.subList(indexStart, totalData).clear()
                submitList(newList)
                shopProductUiModelList.clear()
                mapDataModel()
            }
        }
    }

    fun clearMerchantVoucherData() {
        shopMerchantVoucherUiModel?.let {
            val newList = getNewVisitableItems()
            newList.remove(it)
            submitList(newList)
            mapDataModel()
        }
    }

    fun clearMembershipData() {
        membershipStampUiModel?.let {
            val newList = getNewVisitableItems()
            newList.remove(it)
            submitList(newList)
            mapDataModel()
        }
    }

    fun updateWishListStatus(productId: String, wishList: Boolean) {
        val newList = getNewVisitableItems()
        newList.filterIsInstance<ShopProductUiModel>().onEach {
            if (it.id == productId) {
                it.isWishList = wishList
                it.isNewData = true
            }
        }
        newList.filterIsInstance<ShopProductFeaturedUiModel>().firstOrNull()?.let {
            val isFeaturedChanged = it.updateWishListStatus(productId, wishList)
            if (isFeaturedChanged) {
                it.isNewData = true
            }
        }
        newList.filterIsInstance<ShopProductEtalaseHighlightUiModel>().firstOrNull()?.let {
            val isFeaturedChanged = it.updateWishListStatus(productId, wishList)
            if (isFeaturedChanged) {
                it.isNewData = true
            }
        }
        submitList(newList)
        mapDataModel()
    }

    fun refreshSticky() {
        if (onStickySingleHeaderViewListener != null) {
            recyclerView?.post { onStickySingleHeaderViewListener?.refreshSticky() }
        }
    }

    fun setSortFilterData(data: ShopProductSortFilterUiModel) {
        val newList = getNewVisitableItems()
        if (!mapOfDataModel.containsKey(KEY_SORT_FILTER_DATA_MODEL)) {
            val listWithoutProductListData = getListWithoutProductCardDataAndLoadingModel()
            newList.add(listWithoutProductListData.size, data)
        } else {
            val indexObject = visitables.indexOf(mapOfDataModel[KEY_SORT_FILTER_DATA_MODEL])
            newList[indexObject] = data
        }
        submitList(newList)
        mapDataModel()
    }

    fun setMembershipDataModel(data: MembershipStampProgressUiModel) {
        val newList = getNewVisitableItems()
        if (!mapOfDataModel.containsKey(KEY_MEMBERSHIP_DATA_MODEL)) {
            val listWithoutProductListData = getListWithoutProductCardDataAndLoadingModel()
            newList.add(listWithoutProductListData.size, data)
        } else {
            val indexObject = visitables.indexOf(mapOfDataModel[KEY_MEMBERSHIP_DATA_MODEL])
            newList[indexObject] = data
        }
        submitList(newList)
        mapDataModel()
    }

    fun setMerchantVoucherDataModel(data: ShopMerchantVoucherUiModel) {
        val newList = getNewVisitableItems()
        if (!mapOfDataModel.containsKey(KEY_MERCHANT_VOUCHER_DATA_MODEL)) {
            val listWithoutProductListData = getListWithoutProductCardDataAndLoadingModel()
            newList.add(listWithoutProductListData.size, data)
        } else {
            val indexObject = visitables.indexOf(mapOfDataModel[KEY_MERCHANT_VOUCHER_DATA_MODEL])
            newList[indexObject] = data
        }
        submitList(newList)
        mapDataModel()
    }

    fun setShopProductFeaturedDataModel(data: ShopProductFeaturedUiModel) {
        val newList = getNewVisitableItems()
        if (!mapOfDataModel.containsKey(KEY_FEATURED_PRODUCT_DATA_MODEL)) {
            val listWithoutProductListData = getListWithoutProductCardDataAndLoadingModel()
            newList.add(listWithoutProductListData.size, data)
        } else {
            val indexObject = visitables.indexOf(mapOfDataModel[KEY_FEATURED_PRODUCT_DATA_MODEL])
            newList[indexObject] = data
        }
        submitList(newList)
        mapDataModel()
    }

    fun setShopProductEtalaseHighlightDataModel(data: ShopProductEtalaseHighlightUiModel) {
        val newList = getNewVisitableItems()
        if (!mapOfDataModel.containsKey(KEY_ETALASE_HIGHLIGHT_DATA_MODEL)) {
            val listWithoutProductListData = getListWithoutProductCardDataAndLoadingModel()
            newList.add(listWithoutProductListData.size, data)
        } else {
            val indexObject = visitables.indexOf(mapOfDataModel[KEY_ETALASE_HIGHLIGHT_DATA_MODEL])
            newList[indexObject] = data
        }
        submitList(newList)
        mapDataModel()
    }

    fun setProductListDataModel(productList: List<ShopProductUiModel>) {
        val newList = getNewVisitableItems()
        newList.addAll(productList)
        shopProductUiModelList.addAll(productList)
        submitList(newList)
        mapDataModel()
    }

    fun addSellerAddProductDataModel() {
        val newList = getNewVisitableItems()
        val shopProductAddViewModel = ShopProductAddUiModel()
        newList.add(shopProductAddViewModel)
        submitList(newList)
        mapDataModel()
    }

    fun refreshMembershipData() {
        val newList = getNewVisitableItems()
        newList.filterIsInstance<MembershipStampProgressUiModel>().firstOrNull()?.let {
            val index = newList.indexOf(it)
            newList.set(index, it.copy())
        }
        submitList(newList)
        mapDataModel()
    }

    fun refreshMerchantVoucherData() {
        val newList = getNewVisitableItems()
        newList.filterIsInstance<ShopMerchantVoucherUiModel>().firstOrNull()?.let {
            val index = newList.indexOf(it)
            newList.set(index, it.copy())
        }
        submitList(newList)
        mapDataModel()
    }

    fun addEmptyDataModel(emptyDataViewModel: Visitable<*>) {
        val newList = getNewVisitableItems()
        newList.add(emptyDataViewModel)
        submitList(newList)
        mapDataModel()
    }

    fun changeProductCardGridType(gridType: ShopProductViewGridType) {
        shopProductAdapterTypeFactory.productCardType = gridType
        setLayoutManagerSpanCount()
        recyclerView?.requestLayout()
    }

    private fun mapDataModel() {
        val mutableMapDataModelPosition = mutableMapOf<String, Visitable<*>>()
        for (data in visitables) {
            when (data) {
                is ShopProductSortFilterUiModel -> {
                    mutableMapDataModelPosition[KEY_SORT_FILTER_DATA_MODEL] = data
                }
                is MembershipStampProgressUiModel -> {
                    mutableMapDataModelPosition[KEY_MEMBERSHIP_DATA_MODEL] = data
                }
                is ShopMerchantVoucherUiModel -> {
                    mutableMapDataModelPosition[KEY_MERCHANT_VOUCHER_DATA_MODEL] = data
                }
                is ShopProductFeaturedUiModel -> {
                    mutableMapDataModelPosition[KEY_FEATURED_PRODUCT_DATA_MODEL] = data
                }
                is ShopProductEtalaseHighlightUiModel -> {
                    mutableMapDataModelPosition[KEY_ETALASE_HIGHLIGHT_DATA_MODEL] = data
                }
                is ShopProductEtalaseTitleUiModel -> {
                    mutableMapDataModelPosition[KEY_ETALASE_TITLE_DATA_MODEL] = data
                }
                is ShopProductAddUiModel -> {
                    mutableMapDataModelPosition[KEY_SHOP_PRODUCT_ADD_DATA_MODEL] = data
                }
                is ShopSellerEmptyProductAllEtalaseUiModel -> {
                    mutableMapDataModelPosition[KEY_SHOP_SELLER_EMPTY_PRODUCT_ALL_ETALASE_DATA_MODEL] = data
                }
                is ShopEmptyProductUiModel -> {
                    mutableMapDataModelPosition[KEY_SHOP_BUYER_EMPTY_PRODUCT_DATA_MODEL] = data
                }
                is ShopProductUiModel -> {
                    if (!mutableMapDataModelPosition.containsKey(KEY_SHOP_PRODUCT_FIRST_DATA_MODEL)) {
                        mutableMapDataModelPosition[KEY_SHOP_PRODUCT_FIRST_DATA_MODEL] = data
                    }
                }
            }
        }
        setmapDataModel(mutableMapDataModelPosition)
    }

    private fun setmapDataModel(mutableMapDataModelPosition: MutableMap<String, Visitable<*>>) {
        this.mapOfDataModel = mutableMapDataModelPosition
    }

    private fun getListWithoutProductCardDataAndLoadingModel(): List<Visitable<*>> {
        return visitables.filter {
            it::class.java != ShopProductUiModel::class.java &&
                it::class.java != ShopProductAddUiModel::class.java &&
                it::class.java != ShopSellerEmptyProductAllEtalaseUiModel::class.java &&
                it::class.java != ShopEmptyProductUiModel::class.java &&
                it::class.java != ShopProductEtalaseTitleUiModel::class.java &&
                it::class.java != ShopProductChangeGridSectionUiModel::class.java &&
                it::class.java != LoadingMoreModel::class.java &&
                it::class.java != LoadingModel::class.java
        }
    }

    fun updateShopPageProductChangeGridSectionIcon(isProductListEmpty: Boolean, totalProductData: Int, gridType: ShopProductViewGridType = ShopProductViewGridType.SMALL_GRID) {
        val newList = getNewVisitableItems()
        val gridSectionModel = newList.filterIsInstance<ShopProductChangeGridSectionUiModel>().firstOrNull()
        if (gridSectionModel == null) {
            if (!isProductListEmpty) {
                newList.add(getListWithoutProductCardDataAndLoadingModel().size, ShopProductChangeGridSectionUiModel(totalProductData, gridType))
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
        newList.filterIsInstance<ShopProductChangeGridSectionUiModel>().firstOrNull()?.apply {
            this.gridType = gridType
        }
        submitList(newList)
    }

    fun addSuggestionSearchTextSection(suggestionText: String, suggestionQuery: String) {
        val newList = getNewVisitableItems()
        newList.remove(ShopProductSearchResultSuggestionUiModel(suggestionText, suggestionQuery))
        newList.add(ShopProductSearchResultSuggestionUiModel(suggestionText, suggestionQuery))
        submitList(newList)
    }

    fun addEmptySearchResultState() {
        val newList = getNewVisitableItems()
        newList.add(ShopProductEmptySearchUiModel())
        submitList(newList)
        mapDataModel()
    }

    fun addEmptyShowcaseResultState() {
        val newList = getNewVisitableItems()
        newList.add(ShopProductEmptyShowcaseUiModel())
        submitList(newList)
        mapDataModel()
    }

    fun addProductSuggestion(productList: List<ShopProductUiModel>) {
        val newList = getNewVisitableItems()
        shopProductUiModelList.addAll(productList)
        newList.add(ShopProductTitleEmptyUiModel())
        newList.addAll(productList)
        submitList(newList)
        mapDataModel()
    }

    fun clearShopPageProductResultEmptyState() {
        val newList = getNewVisitableItems()
        newList.firstOrNull {
            it is ShopProductEmptySearchUiModel
        }?.let {
            val position = newList.indexOf(it)
            newList.removeAt(position)
        }
        newList.firstOrNull {
            it is ShopProductEmptyShowcaseUiModel
        }?.let {
            val position = newList.indexOf(it)
            newList.removeAt(position)
        }
        newList.firstOrNull {
            it is ShopProductTitleEmptyUiModel
        }?.let {
            val position = newList.indexOf(it)
            newList.removeAt(position)
        }
        submitList(newList)
    }

    fun clearShopPageChangeGridSection() {
        val newList = getNewVisitableItems()
        newList.firstOrNull {
            it is ShopProductChangeGridSectionUiModel
        }?.let {
            val position = newList.indexOf(it)
            newList.removeAt(position)
            submitList(newList)
        }
    }

    fun updateProductTabWidget(productTabWidget: MutableList<Visitable<*>>?) {
        productTabWidget?.let {
            submitList(productTabWidget)
            mapDataModel()
        }
    }

    private fun submitList(newList: List<Visitable<*>>) {
        val currentRecyclerViewState: Parcelable? = recyclerView?.layoutManager?.onSaveInstanceState()
        val diffCallback = ShopPageProductDiffUtilCallback(visitables, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        visitables.clear()
        newList.forEach {
            when (it) {
                is ShopProductUiModel -> it.isNewData = false
                is ShopProductFeaturedUiModel -> it.isNewData = false
                is ShopProductEtalaseHighlightUiModel -> it.isNewData = false
                else -> {}
            }
        }
        visitables.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
        currentRecyclerViewState?.let {
            recyclerView?.layoutManager?.onRestoreInstanceState(it)
        }
    }
}
