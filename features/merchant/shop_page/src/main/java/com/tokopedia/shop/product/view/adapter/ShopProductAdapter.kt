package com.tokopedia.shop.product.view.adapter

import android.os.Handler
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.LoadingMoreViewHolder
import com.tokopedia.shop.analytic.OldShopPageTrackingConstant.ALL_ETALASE
import com.tokopedia.shop.common.constant.ShopPageConstant.*
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

    val shopProductViewModelList: MutableList<ShopProductViewModel> = mutableListOf()
    val shopProductSortFilterUiViewModel: ShopProductSortFilterUiModel?
        get() = mapOfDataModel[KEY_SORT_FILTER_DATA_MODEL] as? ShopProductSortFilterUiModel
    val shopProductSortFilterPosition: Int
        get() = shopProductSortFilterUiViewModel?.let {
            visitables.indexOf(it)
        } ?: 0
    val shopProductFirstViewModelPosition: Int
        get() = shopProductFirstViewModel?.let {
            visitables.indexOf(it)
        } ?: 0
    val shopChangeProductGridSegment: Int
        get() = visitables.indexOfFirst {
            it.javaClass == ShopProductChangeGridSectionUiModel::class.java
        }.takeIf { it != -1 } ?: 0
    private var onStickySingleHeaderViewListener: OnStickySingleHeaderListener? = null
    private var recyclerView: RecyclerView? = null
    private var mapOfDataModel = mutableMapOf<String, Visitable<*>>()
    private val shopProductEtalaseHighlightViewModel: ShopProductEtalaseHighlightViewModel?
        get() = mapOfDataModel[KEY_ETALASE_HIGHLIGHT_DATA_MODEL] as? ShopProductEtalaseHighlightViewModel
    private val membershipStampViewModel: MembershipStampProgressViewModel?
        get() = mapOfDataModel[KEY_MEMBERSHIP_DATA_MODEL] as? MembershipStampProgressViewModel
    private val shopMerchantVoucherViewModel: ShopMerchantVoucherViewModel?
        get() = mapOfDataModel[KEY_MERCHANT_VOUCHER_DATA_MODEL] as? ShopMerchantVoucherViewModel
    private val shopProductFeaturedViewModel: ShopProductFeaturedViewModel?
        get() = mapOfDataModel[KEY_FEATURED_PRODUCT_DATA_MODEL] as? ShopProductFeaturedViewModel
    private val sellerEmptyProductAllEtalaseDataModel: ShopSellerEmptyProductAllEtalaseViewModel?
        get() = mapOfDataModel[KEY_SHOP_SELLER_EMPTY_PRODUCT_ALL_ETALASE_DATA_MODEL] as? ShopSellerEmptyProductAllEtalaseViewModel
    private val shopEmptyProductViewModel: ShopEmptyProductViewModel?
        get() = mapOfDataModel[KEY_SHOP_BUYER_EMPTY_PRODUCT_DATA_MODEL] as? ShopEmptyProductViewModel
    private val shopProductAddViewModel: ShopProductAddViewModel?
        get() = mapOfDataModel[KEY_SHOP_PRODUCT_ADD_DATA_MODEL] as? ShopProductAddViewModel
    private val shopProductFirstViewModel: ShopProductViewModel?
        get() = mapOfDataModel[KEY_SHOP_PRODUCT_FIRST_DATA_MODEL] as? ShopProductViewModel

    override fun showErrorNetwork(message: String, onRetryListener: ErrorNetworkModel.OnRetryListener) {
        errorNetworkModel.errorMessage = message
        errorNetworkModel.onRetryListener = onRetryListener
        this.shopProductViewModelList.clear()
        clearAllNonDataElement()
        visitables.add(errorNetworkModel)
        notifyDataSetChanged()
        mapDataModel()
    }

    override fun isShowLoadingMore(): Boolean {
        return shopProductViewModelList.size > 0
    }

    override fun isItemClickableByDefault(): Boolean {
        return false
    }

    override fun getEndlessDataSize(): Int {
        return shopProductViewModelList.size
    }

    override fun getStickyHeaderPosition(): Int {
        return visitables.indexOf(shopProductSortFilterUiViewModel)
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

    override fun createStickyViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val stickyViewType = getItemViewType(stickyHeaderPosition)
        val view = onCreateViewItem(parent, stickyViewType)
        return shopProductAdapterTypeFactory.createViewHolder(view, stickyViewType)
    }

    override fun bindSticky(viewHolder: RecyclerView.ViewHolder) {
        if (viewHolder is ShopProductSortFilterViewHolder) {
            (mapOfDataModel[KEY_SORT_FILTER_DATA_MODEL] as? ShopProductSortFilterUiModel)?.let {
                viewHolder.bind(it)
            }
        }
    }

    override fun onStickyHide() {
        notifyChangedItem(shopProductSortFilterPosition)
    }

    override fun setListener(onStickySingleHeaderViewListener: OnStickySingleHeaderListener) {
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
        shopEmptyProductViewModel?.let {
            val position = visitables.indexOf(it)
            visitables.remove(it)
            notifyRemovedItem(position)
        }
        shopProductAddViewModel?.let {
            val position = visitables.indexOf(it)
            visitables.remove(it)
            notifyRemovedItem(position)
        }
        mapDataModel()
    }

    fun getEtalaseNameHighLight(shopProductViewModel: ShopProductViewModel): String {
        shopProductEtalaseHighlightViewModel?.let {
            val etalaseHighlightCarouselViewModelList = it.etalaseHighlightCarouselViewModelList
            var i = 0
            val sizei = etalaseHighlightCarouselViewModelList.size
            while (i < sizei) {
                val shopProductViewModelList = etalaseHighlightCarouselViewModelList[i].shopProductViewModelList
                var j = 0
                val sizej = shopProductViewModelList.size
                while (j < sizej) {
                    val shopProductViewModelEtalase = shopProductViewModelList[j]
                    if (shopProductViewModelEtalase.id == shopProductViewModel.id && shopProductViewModel.etalaseId == etalaseHighlightCarouselViewModelList[i].shopEtalaseViewModel.etalaseId) {
                        return etalaseHighlightCarouselViewModelList[i].shopEtalaseViewModel.etalaseName
                    }
                    j++
                }
                i++
            }
            return ALL_ETALASE
        } ?: return ALL_ETALASE
    }

    fun getEtalaseNameHighLightType(shopProductViewModel: ShopProductViewModel): Int? {
        return shopProductEtalaseHighlightViewModel?.let {
            val matchEtalaseHighlight = it.etalaseHighlightCarouselViewModelList.firstOrNull {
                it.shopProductViewModelList.firstOrNull { it.id == shopProductViewModel.id } != null
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

    fun clearProductList() {
        if (shopProductViewModelList.isNotEmpty()) {
            val totalData = visitables.size
            var indexStart = 0
            if (mapOfDataModel.isNotEmpty()) {
                indexStart = if (null != shopProductAddViewModel) {
                    visitables.indexOf(shopProductAddViewModel)
                } else {
                    visitables.indexOf(mapOfDataModel[KEY_SHOP_PRODUCT_FIRST_DATA_MODEL])
                }
            }
            if (indexStart >= 0 && totalData <= visitables.size && indexStart < totalData) {
                visitables.subList(indexStart, totalData).clear()
                notifyRemovedItemRange(indexStart, totalData)
                shopProductViewModelList.clear()
                mapDataModel()
            }
        }
    }

    fun clearMerchantVoucherData() {
        shopMerchantVoucherViewModel?.let {
            val position = visitables.indexOf(it)
            visitables.remove(it)
            notifyRemovedItem(position)
            mapDataModel()
        }
    }

    fun clearMembershipData() {
        membershipStampViewModel?.let {
            val position = visitables.indexOf(it)
            visitables.remove(it)
            notifyRemovedItem(position)
            mapDataModel()
        }
    }

    fun updateWishListStatus(productId: String, wishList: Boolean) {
        var i = 0
        val sizei = shopProductViewModelList.size
        while (i < sizei) {
            val shopProductViewModel = shopProductViewModelList[i]
            if (shopProductViewModel.id.equals(productId, ignoreCase = true)) {
                shopProductViewModel.isWishList = wishList
                notifyChangedItem(visitables.indexOf(shopProductViewModel))
                break
            }
            i++
        }
        shopProductFeaturedViewModel?.let {
            val isFeaturedChanged = it.updateWishListStatus(productId, wishList)
            if (isFeaturedChanged) {
                notifyChangedItem(visitables.indexOf(it))
            }
        }

        shopProductEtalaseHighlightViewModel?.let {
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
            val listWithoutProductListData = getListWithoutProductCardDataAndLoadingMoreModel()
            visitables.add(listWithoutProductListData.size, data)
            notifyDataSetChanged()
        } else {
            val indexObject = visitables.indexOf(mapOfDataModel[KEY_SORT_FILTER_DATA_MODEL])
            visitables[indexObject] = data
            notifyChangedItem(indexObject)
        }
        mapDataModel()
    }

    fun setMembershipDataModel(data: MembershipStampProgressViewModel) {
        if (!mapOfDataModel.containsKey(KEY_MEMBERSHIP_DATA_MODEL)) {
            val listWithoutProductListData = getListWithoutProductCardDataAndLoadingMoreModel()
            visitables.add(listWithoutProductListData.size, data)
            notifyDataSetChanged()
        } else {
            val indexObject = visitables.indexOf(mapOfDataModel[KEY_MEMBERSHIP_DATA_MODEL])
            visitables[indexObject] = data
        }
        mapDataModel()
    }

    fun setMerchantVoucherDataModel(data: ShopMerchantVoucherViewModel) {
        if (!mapOfDataModel.containsKey(KEY_MERCHANT_VOUCHER_DATA_MODEL)) {
            val listWithoutProductListData = getListWithoutProductCardDataAndLoadingMoreModel()
            visitables.add(listWithoutProductListData.size, data)
            notifyDataSetChanged()
        } else {
            val indexObject = visitables.indexOf(mapOfDataModel[KEY_MERCHANT_VOUCHER_DATA_MODEL])
            visitables[indexObject] = data
        }
        mapDataModel()
    }

    fun setShopProductFeaturedDataModel(data: ShopProductFeaturedViewModel) {
        if (!mapOfDataModel.containsKey(KEY_FEATURED_PRODUCT_DATA_MODEL)) {
            val listWithoutProductListData = getListWithoutProductCardDataAndLoadingMoreModel()
            visitables.add(listWithoutProductListData.size, data)
            notifyDataSetChanged()
        } else {
            val indexObject = visitables.indexOf(mapOfDataModel[KEY_FEATURED_PRODUCT_DATA_MODEL])
            visitables[indexObject] = data
        }
        mapDataModel()
    }

    fun setShopProductEtalaseHighlightDataModel(data: ShopProductEtalaseHighlightViewModel) {
        if (!mapOfDataModel.containsKey(KEY_ETALASE_HIGHLIGHT_DATA_MODEL)) {
            val listWithoutProductListData = getListWithoutProductCardDataAndLoadingMoreModel()
            visitables.add(listWithoutProductListData.size, data)
            notifyDataSetChanged()
        } else {
            val indexObject = visitables.indexOf(mapOfDataModel[KEY_ETALASE_HIGHLIGHT_DATA_MODEL])
            visitables[indexObject] = data
        }
        mapDataModel()
    }

    fun setProductListDataModel(productList: List<ShopProductViewModel>) {
        visitables.addAll(productList)
        shopProductViewModelList.addAll(productList)
        notifyInsertedItemRange(lastIndex, productList.size)
        mapDataModel()
    }

    fun addSellerAddProductDataModel() {
        val shopProductAddViewModel = ShopProductAddViewModel()
        visitables.add(shopProductAddViewModel)
        notifyInsertedItem(visitables.size - 1)
        mapDataModel()
    }

    fun refreshMembershipData() {
        membershipStampViewModel?.let {
            notifyChangedItem(visitables.indexOf(it))
        }
    }

    fun refreshMerchantVoucherData() {
        shopMerchantVoucherViewModel?.let {
            notifyChangedItem(visitables.indexOf(it))
        }
    }

    fun addEmptyDataModel(emptyDataViewModel: Visitable<*>) {
        visitables.add(emptyDataViewModel)
        notifyChangedDataSet()
        mapDataModel()
    }

    fun addEmptyStateData(productList: List<ShopProductViewModel>) {
        if(productList.isNotEmpty()) {
            if (visitables.getOrNull(lastIndex) !is ShopProductEmptySearchUiModel) {
                visitables.add(ShopProductEmptySearchUiModel())
                notifyInsertedItem(lastIndex)
            }
            if (visitables.getOrNull(lastIndex) !is ShopProductTitleEmptyUiModel) {
                visitables.add(ShopProductTitleEmptyUiModel())
                notifyInsertedItem(lastIndex)
            }
            if(visitables.getOrNull(lastIndex) !is ShopProductViewModel) {
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
                is MembershipStampProgressViewModel -> {
                    mutableMapDataModelPosition[KEY_MEMBERSHIP_DATA_MODEL] = data
                }
                is ShopMerchantVoucherViewModel -> {
                    mutableMapDataModelPosition[KEY_MERCHANT_VOUCHER_DATA_MODEL] = data
                }
                is ShopProductFeaturedViewModel -> {
                    mutableMapDataModelPosition[KEY_FEATURED_PRODUCT_DATA_MODEL] = data
                }
                is ShopProductEtalaseHighlightViewModel -> {
                    mutableMapDataModelPosition[KEY_ETALASE_HIGHLIGHT_DATA_MODEL] = data
                }
                is ShopProductEtalaseTitleViewModel -> {
                    mutableMapDataModelPosition[KEY_ETALASE_TITLE_DATA_MODEL] = data
                }
                is ShopProductAddViewModel -> {
                    mutableMapDataModelPosition[KEY_SHOP_PRODUCT_ADD_DATA_MODEL] = data
                }
                is ShopSellerEmptyProductAllEtalaseViewModel -> {
                    mutableMapDataModelPosition[KEY_SHOP_SELLER_EMPTY_PRODUCT_ALL_ETALASE_DATA_MODEL] = data
                }
                is ShopEmptyProductViewModel -> {
                    mutableMapDataModelPosition[KEY_SHOP_BUYER_EMPTY_PRODUCT_DATA_MODEL] = data
                }
                is ShopProductViewModel -> {
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

    private fun getListWithoutProductCardDataAndLoadingMoreModel(): List<Visitable<*>> {
        return visitables.filter {
            it::class.java != ShopProductViewModel::class.java &&
                    it::class.java != ShopProductAddViewModel::class.java &&
                    it::class.java != ShopSellerEmptyProductAllEtalaseViewModel::class.java &&
                    it::class.java != ShopEmptyProductViewModel::class.java &&
                    it::class.java != LoadingMoreModel::class.java
        }
    }

    fun addShopPageProductChangeGridSection(data: ShopProductChangeGridSectionUiModel) {
        visitables.add(getListWithoutProductCardDataAndLoadingMoreModel().size, data)
        notifyChangedDataSet()
    }

    fun updateShopPageProductChangeGridSection(gridType: ShopProductViewGridType) {
        visitables.filterIsInstance<ShopProductChangeGridSectionUiModel>().firstOrNull()?.apply {
            this.gridType = gridType
            notifyChangedItem(visitables.indexOf(this))
        }
    }
}
