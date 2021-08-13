package com.tokopedia.shop.product.view.adapter

import android.os.Handler
import android.view.ViewGroup
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
    val shopProductSortFilterUiViewModel: ShopProductSortFilterUiModel?
        get() = mapOfDataModel[KEY_SORT_FILTER_DATA_MODEL] as? ShopProductSortFilterUiModel
    val shopProductSortFilterPosition: Int
        get() = shopProductSortFilterUiViewModel?.let {
            visitables.indexOf(it)
        } ?: 0
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
    private val shopProductFeaturedUiModel: ShopProductFeaturedUiModel?
        get() = mapOfDataModel[KEY_FEATURED_PRODUCT_DATA_MODEL] as? ShopProductFeaturedUiModel
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
        notifyDataSetChanged()
        mapDataModel()
    }

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
            staggeredLayoutParams.isFullSpan = !(getItemViewType(position) == ShopProductViewHolder.GRID_LAYOUT ||
                    getItemViewType(position) == ShopProductItemBigGridViewHolder.LAYOUT ||
                    getItemViewType(position) == ShopProductItemListViewHolder.LAYOUT ||
                    getItemViewType(position) == ShopProductAddViewHolder.LAYOUT ||
                    getItemViewType(position) == ShopProductSellerAllEtalaseEmptyViewHolder.LAYOUT ||
                    getItemViewType(position) == LoadingMoreViewHolder.LAYOUT)
        }
        super.onBindViewHolder(holder, position)
    }

    private fun setLayoutManagerSpanCount() {
        (recyclerView?.layoutManager as? StaggeredGridLayoutManager)?.spanCount = when (shopProductAdapterTypeFactory.productCardType) {
            ShopProductViewGridType.BIG_GRID -> {
                1
            }
            ShopProductViewGridType.SMALL_GRID -> {
                2
            }
            ShopProductViewGridType.LIST -> {
                1
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
        notifyChangedItem(shopProductSortFilterPosition)
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
            if (isShowLoadingMore) {
                visitables.add(loadingMoreModel)
            } else {
                visitables.add(loadingModel)
            }
            notifyInsertedItem(visitables.size -1)
        }
    }

    override fun hideLoading() {
        if (visitables.contains(loadingModel)) {
            val itemPosition = visitables.indexOf(loadingModel)
            visitables.remove(loadingModel)
            notifyRemovedItem(itemPosition)
        } else if (visitables.contains(loadingMoreModel)) {
            val itemPosition = visitables.indexOf(loadingMoreModel)
            visitables.remove(loadingMoreModel)
            notifyRemovedItem(itemPosition)
        }
    }

    override fun isLoading(): Boolean {
        return visitables.contains(loadingModel) || visitables.contains(loadingMoreModel)
    }

    override fun clearAllNonDataElement() {
        super.clearAllNonDataElement()
        sellerEmptyProductAllEtalaseDataModel?.let {
            val position = visitables.indexOf(it)
            visitables.remove(it)
            notifyRemovedItem(position)
        }
        shopEmptyProductUiModel?.let {
            val position = visitables.indexOf(it)
            visitables.remove(it)
            notifyRemovedItem(position)
        }
        shopProductAddUiModel?.let {
            val position = visitables.indexOf(it)
            visitables.remove(it)
            notifyRemovedItem(position)
        }
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
        shopProductSortFilterUiViewModel?.apply {
            selectedSortId = sortId
            selectedSortName = sortName
        }
        notifyChangedItem(visitables.indexOf(shopProductSortFilterUiViewModel))
    }

    fun changeSelectedEtalaseFilter(etalaseId: String, etalaseName: String) {
        shopProductSortFilterUiViewModel?.apply {
            selectedEtalaseId = etalaseId
            selectedEtalaseName = etalaseName
        }
        notifyChangedItem(visitables.indexOf(shopProductSortFilterUiViewModel))
    }

    fun changeSortFilterIndicatorCounter(filterIndicatorCounter: Int) {
        shopProductSortFilterUiViewModel?.apply {
            this.filterIndicatorCounter = filterIndicatorCounter
        }
        notifyChangedItem(visitables.indexOf(shopProductSortFilterUiViewModel))
    }

    fun clearProductList() {
        if (shopProductUiModelList.isNotEmpty()) {
            val totalData = visitables.size
            var indexStart = 0
            if (mapOfDataModel.isNotEmpty()) {
                indexStart = if (null != shopProductAddUiModel) {
                    visitables.indexOf(shopProductAddUiModel)
                } else {
                    visitables.indexOf(mapOfDataModel[KEY_SHOP_PRODUCT_FIRST_DATA_MODEL])
                }
            }
            if (indexStart >= 0 && totalData <= visitables.size && indexStart < totalData) {
                visitables.subList(indexStart, totalData).clear()
                notifyRemovedItemRange(indexStart, totalData)
                shopProductUiModelList.clear()
                mapDataModel()
            }
        }
    }

    fun clearMerchantVoucherData() {
        shopMerchantVoucherUiModel?.let {
            val position = visitables.indexOf(it)
            visitables.remove(it)
            notifyRemovedItem(position)
            mapDataModel()
        }
    }

    fun clearMembershipData() {
        membershipStampUiModel?.let {
            val position = visitables.indexOf(it)
            visitables.remove(it)
            notifyRemovedItem(position)
            mapDataModel()
        }
    }

    fun updateWishListStatus(productId: String, wishList: Boolean) {
        var i = 0
        val sizei = shopProductUiModelList.size
        while (i < sizei) {
            val shopProductViewModel = shopProductUiModelList[i]
            if (shopProductViewModel.id.equals(productId, ignoreCase = true)) {
                shopProductViewModel.isWishList = wishList
                notifyChangedItem(visitables.indexOf(shopProductViewModel))
                break
            }
            i++
        }
        shopProductFeaturedUiModel?.let {
            val isFeaturedChanged = it.updateWishListStatus(productId, wishList)
            if (isFeaturedChanged) {
                notifyChangedItem(visitables.indexOf(it))
            }
        }

        shopProductEtalaseHighlightUiModel?.let {
            val isEtalaseChanged = it.updateWishListStatus(productId, wishList)
            if (isEtalaseChanged) {
                notifyChangedItem(visitables.indexOf(it))
            }
        }
    }

    fun refreshSticky() {
        if (onStickySingleHeaderViewListener != null) {
            recyclerView?.post { onStickySingleHeaderViewListener?.refreshSticky() }
        }
    }

    fun setSortFilterData(data: ShopProductSortFilterUiModel) {
        if (!mapOfDataModel.containsKey(KEY_SORT_FILTER_DATA_MODEL)) {
            val listWithoutProductListData = getListWithoutProductCardDataAndLoadingModel()
            visitables.add(listWithoutProductListData.size, data)
            notifyDataSetChanged()
        } else {
            val indexObject = visitables.indexOf(mapOfDataModel[KEY_SORT_FILTER_DATA_MODEL])
            visitables[indexObject] = data
            notifyChangedItem(indexObject)
        }
        mapDataModel()
    }

    fun setMembershipDataModel(data: MembershipStampProgressUiModel) {
        if (!mapOfDataModel.containsKey(KEY_MEMBERSHIP_DATA_MODEL)) {
            val listWithoutProductListData = getListWithoutProductCardDataAndLoadingModel()
            visitables.add(listWithoutProductListData.size, data)
            notifyDataSetChanged()
        } else {
            val indexObject = visitables.indexOf(mapOfDataModel[KEY_MEMBERSHIP_DATA_MODEL])
            visitables[indexObject] = data
        }
        mapDataModel()
    }

    fun setMerchantVoucherDataModel(data: ShopMerchantVoucherUiModel) {
        if (!mapOfDataModel.containsKey(KEY_MERCHANT_VOUCHER_DATA_MODEL)) {
            val listWithoutProductListData = getListWithoutProductCardDataAndLoadingModel()
            visitables.add(listWithoutProductListData.size, data)
            notifyDataSetChanged()
        } else {
            val indexObject = visitables.indexOf(mapOfDataModel[KEY_MERCHANT_VOUCHER_DATA_MODEL])
            visitables[indexObject] = data
        }
        mapDataModel()
    }

    fun setShopProductFeaturedDataModel(data: ShopProductFeaturedUiModel) {
        if (!mapOfDataModel.containsKey(KEY_FEATURED_PRODUCT_DATA_MODEL)) {
            val listWithoutProductListData = getListWithoutProductCardDataAndLoadingModel()
            visitables.add(listWithoutProductListData.size, data)
            notifyDataSetChanged()
        } else {
            val indexObject = visitables.indexOf(mapOfDataModel[KEY_FEATURED_PRODUCT_DATA_MODEL])
            visitables[indexObject] = data
        }
        mapDataModel()
    }

    fun setShopProductEtalaseHighlightDataModel(data: ShopProductEtalaseHighlightUiModel) {
        if (!mapOfDataModel.containsKey(KEY_ETALASE_HIGHLIGHT_DATA_MODEL)) {
            val listWithoutProductListData = getListWithoutProductCardDataAndLoadingModel()
            visitables.add(listWithoutProductListData.size, data)
            notifyDataSetChanged()
        } else {
            val indexObject = visitables.indexOf(mapOfDataModel[KEY_ETALASE_HIGHLIGHT_DATA_MODEL])
            visitables[indexObject] = data
        }
        mapDataModel()
    }

    fun setShopProductEtalaseTitleData(data: ShopProductEtalaseTitleUiModel) {
        if (!mapOfDataModel.containsKey(KEY_ETALASE_TITLE_DATA_MODEL)) {
            visitables.add(lastIndex, data)
            notifyDataSetChanged()
        } else {
            val indexObject = visitables.indexOf(mapOfDataModel[KEY_ETALASE_TITLE_DATA_MODEL])
            visitables[indexObject] = data
        }
        mapDataModel()
    }

    fun setProductListDataModel(productList: List<ShopProductUiModel>) {
        visitables.addAll(productList)
        shopProductUiModelList.addAll(productList)
        notifyInsertedItemRange(lastIndex, productList.size)
        mapDataModel()
    }

    fun addSellerAddProductDataModel() {
        val shopProductAddViewModel = ShopProductAddUiModel()
        visitables.add(shopProductAddViewModel)
        notifyInsertedItem(visitables.size - 1)
        mapDataModel()
    }

    fun refreshMembershipData() {
        membershipStampUiModel?.let {
            notifyChangedItem(visitables.indexOf(it))
        }
    }

    fun refreshMerchantVoucherData() {
        shopMerchantVoucherUiModel?.let {
            notifyChangedItem(visitables.indexOf(it))
        }
    }

    fun addEmptyDataModel(emptyDataViewModel: Visitable<*>) {
        visitables.add(emptyDataViewModel)
        notifyChangedDataSet()
        mapDataModel()
    }

    fun addEmptyStateData(productList: List<ShopProductUiModel>) {
        if(productList.isNotEmpty()) {
            if (visitables.getOrNull(lastIndex) !is ShopProductEmptySearchUiModel) {
                visitables.add(ShopProductEmptySearchUiModel())
                notifyInsertedItem(lastIndex)
            }
            if (visitables.getOrNull(lastIndex) !is ShopProductTitleEmptyUiModel) {
                visitables.add(ShopProductTitleEmptyUiModel())
                notifyInsertedItem(lastIndex)
            }
            if(visitables.getOrNull(lastIndex) !is ShopProductUiModel) {
                val lastIndex = visitables.size
                visitables.addAll(productList)
                notifyItemRangeInserted(lastIndex, productList.size)
            }
        } else {
            if (visitables.getOrNull(lastIndex) !is ShopProductEmptySearchUiModel) {
                visitables.add(ShopProductEmptySearchUiModel())
                notifyInsertedItem(lastIndex)
            }
        }
    }

    fun changeProductCardGridType(gridType: ShopProductViewGridType){
        shopProductAdapterTypeFactory.productCardType =  gridType
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


    private fun notifyChangedItem(position: Int) {
        recyclerView?.isComputingLayout?.let {
            if (isAllowedNotify(it, position)) {
                notifyItemChanged(position)
            } else {
                notifyChangedDataSet()
            }
        }
    }

    private fun notifyRemovedItem(position: Int) {
        recyclerView?.isComputingLayout?.let {
            if (isAllowedNotify(it, position)) {
                notifyItemRemoved(position)
            } else {
                notifyChangedDataSet()
            }
        }
    }

    private fun notifyRemovedItemRange(startPosition: Int, totalItem: Int) {
        recyclerView?.isComputingLayout?.let {
            if (isAllowedNotify(it, startPosition)) {
                notifyItemRangeRemoved(startPosition, totalItem)
            } else {
                notifyChangedDataSet()
            }
        }
    }

    private fun notifyInsertedItemRange(startPosition: Int, totalItem: Int) {
        recyclerView?.isComputingLayout?.let {
            if (isAllowedNotify(it, startPosition)) {
                notifyItemRangeInserted(startPosition, totalItem)
            } else {
                notifyChangedDataSet()
            }
        }
    }

    private fun notifyInsertedItem(position: Int) {
        recyclerView?.isComputingLayout?.let {
            if (isAllowedNotify(it, position)) {
                notifyItemInserted(position)
            } else {
                notifyChangedDataSet()
            }
        }
    }

    private fun notifyChangedDataSet(){
        Handler().post {
            notifyDataSetChanged()
        }
    }

    private fun isAllowedNotify(isComputingLayout: Boolean, position: Int): Boolean {
        return !isComputingLayout && position >= 0
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

    fun updateShopPageProductChangeGridSectionIcon(totalProductData: Int, gridType: ShopProductViewGridType = ShopProductViewGridType.SMALL_GRID) {
        val gridSectionModel = visitables.filterIsInstance<ShopProductChangeGridSectionUiModel>().firstOrNull()
        if (gridSectionModel == null) {
            if(totalProductData != 0) {
                visitables.add(getListWithoutProductCardDataAndLoadingModel().size, ShopProductChangeGridSectionUiModel(totalProductData, gridType))
                notifyChangedDataSet()
            }
        } else {
            gridSectionModel.apply {
                val index = visitables.indexOf(this)
                if(totalProductData == 0){
                    visitables.remove(this)
                    notifyRemovedItem(index)
                }else{
                    this.totalProduct = totalProductData
                    notifyChangedItem(index)
                }
            }
        }
    }

    fun updateShopPageProductChangeGridSectionIcon(gridType: ShopProductViewGridType) {
        visitables.filterIsInstance<ShopProductChangeGridSectionUiModel>().firstOrNull()?.apply {
            this.gridType = gridType
            notifyChangedItem(visitables.indexOf(this))
        }
    }
}
