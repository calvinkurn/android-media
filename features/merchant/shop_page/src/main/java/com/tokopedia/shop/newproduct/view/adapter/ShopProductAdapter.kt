package com.tokopedia.shop.newproduct.view.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.ALL_ETALASE
import com.tokopedia.shop.newproduct.view.viewholder.ShopProductEtalaseListViewHolder
import com.tokopedia.shop.product.view.adapter.scrolllistener.DataEndlessScrollListener
import com.tokopedia.shop.product.view.widget.OnStickySingleHeaderListener
import com.tokopedia.shop.product.view.widget.StickySingleHeaderView

import com.tokopedia.shop.common.constant.ShopPageConstant.*
import com.tokopedia.shop.newproduct.view.datamodel.*
import com.tokopedia.shop.newproduct.view.viewholder.ShopProductAddInfoViewHolder
import com.tokopedia.shop.newproduct.view.viewholder.ShopProductAddViewHolder
import com.tokopedia.shop.newproduct.view.viewholder.ShopProductViewHolder


class ShopProductAdapter(private val shopProductAdapterTypeFactory: ShopProductAdapterTypeFactory) : BaseListAdapter<BaseShopProductViewModel, ShopProductAdapterTypeFactory>(shopProductAdapterTypeFactory, null), DataEndlessScrollListener.OnDataEndlessScrollListener, StickySingleHeaderView.OnStickySingleHeaderAdapter {

    init {
        shopProductAdapterTypeFactory.attachAdapter(this)
    }

    val shopProductViewModelList: MutableList<ShopProductViewModel> = mutableListOf()
    val shopProductEtalaseListViewModel: ShopProductEtalaseListViewModel?
        get() = mapOfDataModel[KEY_ETALASE_DATA_MODEL] as? ShopProductEtalaseListViewModel
    val shopProductEtalaseTitlePosition: Int
        get() = shopProductEtalaseTitleViewModel?.let {
            visitables.indexOf(shopProductEtalaseTitleViewModel)
        } ?: 0
    private var onStickySingleHeaderViewListener: OnStickySingleHeaderListener? = null
    private var recyclerView: RecyclerView? = null
    private var mapOfDataModel = mutableMapOf<String, Visitable<*>>()
    private val shopProductEtalaseTitleViewModel: ShopProductEtalaseTitleViewModel?
        get() = mapOfDataModel[KEY_ETALASE_TITLE_DATA_MODEL] as? ShopProductEtalaseTitleViewModel
    private val shopProductEtalaseHighlightViewModel: ShopProductEtalaseHighlightViewModel?
        get() = mapOfDataModel[KEY_ETALASE_HIGHLIGHT_DATA_MODEL] as? ShopProductEtalaseHighlightViewModel
    private val membershipStampViewModel: MembershipStampProgressViewModel?
        get() = mapOfDataModel[KEY_MEMBERSHIP_DATA_MODEL] as? MembershipStampProgressViewModel
    private val shopMerchantVoucherViewModel: ShopMerchantVoucherViewModel?
        get() = mapOfDataModel[KEY_MERCHANT_VOUCHER_DATA_MODEL] as? ShopMerchantVoucherViewModel
    private val shopProductFeaturedViewModel: ShopProductFeaturedViewModel?
        get() = mapOfDataModel[KEY_FEATURED_PRODUCT_DATA_MODEL] as? ShopProductFeaturedViewModel
    private val sellerEmptyProductDataModel: ShopSellerEmptyProductViewModel?
        get() = mapOfDataModel[KEY_SHOP_SELLER_EMPTY_PRODUCT_DATA_MODEL] as? ShopSellerEmptyProductViewModel
    private val buyerEmptyProductDataModel: EmptyOwnShopModel?
        get() = mapOfDataModel[KEY_SHOP_BUYER_EMPTY_PRODUCT_DATA_MODEL] as? EmptyOwnShopModel
    private val shopProductAddViewModel: ShopProductAddViewModel?
        get() = mapOfDataModel[KEY_SHOP_PRODUCT_ADD_DATA_MODEL] as? ShopProductAddViewModel

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
        return visitables.indexOf(shopProductEtalaseListViewModel)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<*>, position: Int) {
        if (holder.itemView.layoutParams is StaggeredGridLayoutManager.LayoutParams) {
            val staggeredLayoutParams = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
            staggeredLayoutParams.isFullSpan = !(getItemViewType(position) == ShopProductViewHolder.GRID_LAYOUT ||
                    getItemViewType(position) == ShopProductAddViewHolder.LAYOUT ||
                    getItemViewType(position) == ShopProductAddInfoViewHolder.LAYOUT)
        }
        super.onBindViewHolder(holder, position)
    }

    override fun createStickyViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val stickyViewType = getItemViewType(stickyHeaderPosition)
        val view = onCreateViewItem(parent, stickyViewType)
        return shopProductAdapterTypeFactory.createViewHolder(view, stickyViewType)
    }

    override fun bindSticky(viewHolder: RecyclerView.ViewHolder) {
        if (viewHolder is ShopProductEtalaseListViewHolder) {
            (mapOfDataModel[KEY_ETALASE_DATA_MODEL] as? ShopProductEtalaseListViewModel)?.let {
                viewHolder.bind(it)
            }
        }
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

    override fun hideLoading() {
        if (visitables.contains(loadingModel)) {
            visitables.remove(loadingModel)
        } else if (visitables.contains(loadingMoreModel)) {
            visitables.remove(loadingMoreModel)
        }
    }

    override fun isLoading(): Boolean {
        return visitables.contains(loadingModel) || visitables.contains(loadingMoreModel)
    }

    override fun clearAllNonDataElement() {
        super.clearAllNonDataElement()
        sellerEmptyProductDataModel?.let {
            visitables.remove(it)
        }
        buyerEmptyProductDataModel?.let {
            visitables.remove(it)
        }
        shopProductAddViewModel?.let {
            visitables.remove(it)
        }
        mapDataModel()
    }

    fun getEtalaseNameHighLight(shopProductViewModel: ShopProductViewModel): String {
        shopProductEtalaseHighlightViewModel?.let {
            val etalaseHighlightCarouselViewModelList = shopProductEtalaseHighlightViewModel!!.etalaseHighlightCarouselViewModelList
            var i = 0
            val sizei = etalaseHighlightCarouselViewModelList.size
            while (i < sizei) {
                val shopProductViewModelList = etalaseHighlightCarouselViewModelList[i].shopProductViewModelList
                var j = 0
                val sizej = shopProductViewModelList.size
                while (j < sizej) {
                    val shopProductViewModelEtalase = shopProductViewModelList[j]
                    if (shopProductViewModelEtalase.id == shopProductViewModel.id) {
                        return etalaseHighlightCarouselViewModelList[i].shopEtalaseViewModel.etalaseName
                    }
                    j++
                }
                i++
            }
            return ALL_ETALASE
        } ?: return ALL_ETALASE
    }

    fun changeSelectedEtalaseId(shopProductEtalaseChipItemViewModel: ShopProductEtalaseChipItemViewModel) {
        shopProductEtalaseListViewModel?.apply {
            selectedEtalaseId = shopProductEtalaseChipItemViewModel.etalaseId
            selectedEtalaseName = shopProductEtalaseChipItemViewModel.etalaseName
            selectedEtalaseBadge = shopProductEtalaseChipItemViewModel.etalaseBadge
        }
        notifyItemChanged(visitables.indexOf(shopProductEtalaseListViewModel))
        updateShopEtalaseTitle()
    }

    fun isEtalaseInChip(etalaseId: String): Boolean {
        val shopEtalaseViewModelList = shopProductEtalaseListViewModel!!.etalaseModelList
        shopEtalaseViewModelList.filterIsInstance(ShopProductEtalaseChipItemViewModel::class.java).map {
            if (it.etalaseId.equals(etalaseId, ignoreCase = true)) {
                return true
            }
        }
        return false
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
            visitables.subList(indexStart, totalData).clear()
            notifyItemRangeRemoved(indexStart, totalData)
            shopProductViewModelList.clear()
            mapDataModel()
        }
    }

    fun getProductViewModelRealPosition(shopProductViewModel: ShopProductViewModel): Int {
        return shopProductViewModelList.indexOf(shopProductViewModel)
    }

    fun replaceProductList(shopProductViewModelArrayList: List<ShopProductViewModel>) {
        if (this.shopProductViewModelList === shopProductViewModelArrayList) {
            return
        }
        if (this.shopProductViewModelList.size > 0) {
            clearProductList()
        }
        addProductList(shopProductViewModelArrayList)
    }

    fun clearMerchantVoucherData() {
        shopMerchantVoucherViewModel?.let {
            val position = visitables.indexOf(it)
            visitables.remove(it)
            notifyItemRemoved(position)
            mapDataModel()
        }
    }

    fun clearMembershipData() {
        membershipStampViewModel?.let {
            val position = visitables.indexOf(it)
            visitables.remove(it)
            notifyItemRemoved(position)
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
                notifyItemChanged(visitables.indexOf(shopProductViewModel))
                break
            }
            i++
        }
        shopProductFeaturedViewModel?.let {
            val isFeaturedChanged = it.updateWishListStatus(productId, wishList)
            if (isFeaturedChanged) {
                notifyItemChanged(visitables.indexOf(it))
            }
        }

        shopProductEtalaseHighlightViewModel?.let {
            val isEtalaseChanged = it.updateWishListStatus(productId, wishList)
            if (isEtalaseChanged) {
                notifyItemChanged(visitables.indexOf(it))
            }
        }
    }


    fun refreshSticky() {
        if (onStickySingleHeaderViewListener != null) {
            recyclerView!!.post { onStickySingleHeaderViewListener!!.refreshSticky() }
        }
    }

    fun setEtalaseDataModel(data: ShopProductEtalaseListViewModel) {
        if (!mapOfDataModel.containsKey(KEY_ETALASE_DATA_MODEL)) {
            visitables.add(data)
        } else {
            val indexObject = visitables.indexOf(mapOfDataModel[KEY_ETALASE_DATA_MODEL])
            visitables[indexObject] = data
        }
        mapDataModel()
    }

    fun setMembershipDataModel(data: MembershipStampProgressViewModel) {
        if (!mapOfDataModel.containsKey(KEY_MEMBERSHIP_DATA_MODEL)) {
            visitables.add(data)
        } else {
            val indexObject = visitables.indexOf(mapOfDataModel[KEY_MEMBERSHIP_DATA_MODEL])
            visitables[indexObject] = data
        }
        mapDataModel()
    }

    fun setMerchantVoucherDataModel(data: ShopMerchantVoucherViewModel) {
        if (!mapOfDataModel.containsKey(KEY_MERCHANT_VOUCHER_DATA_MODEL)) {
            visitables.add(data)
        } else {
            val indexObject = visitables.indexOf(mapOfDataModel[KEY_MERCHANT_VOUCHER_DATA_MODEL])
            visitables[indexObject] = data
        }
        mapDataModel()
    }

    fun setShopProductFeaturedDataModel(data: ShopProductFeaturedViewModel) {
        if (!mapOfDataModel.containsKey(KEY_FEATURED_PRODUCT_DATA_MODEL)) {
            visitables.add(data)
        } else {
            val indexObject = visitables.indexOf(mapOfDataModel[KEY_FEATURED_PRODUCT_DATA_MODEL])
            visitables[indexObject] = data
        }
        mapDataModel()
    }

    fun setShopProductEtalaseHighlightDataModel(data: ShopProductEtalaseHighlightViewModel) {
        if (!mapOfDataModel.containsKey(KEY_ETALASE_HIGHLIGHT_DATA_MODEL)) {
            visitables.add(data)
        } else {
            val indexObject = visitables.indexOf(mapOfDataModel[KEY_ETALASE_HIGHLIGHT_DATA_MODEL])
            visitables[indexObject] = data
        }
        mapDataModel()
    }

    fun setShopProductEtalaseTitleData(data: ShopProductEtalaseTitleViewModel) {
        if (!mapOfDataModel.containsKey(KEY_ETALASE_TITLE_DATA_MODEL)) {
            visitables.add(data)
        } else {
            val indexObject = visitables.indexOf(mapOfDataModel[KEY_ETALASE_TITLE_DATA_MODEL])
            visitables[indexObject] = data
        }
        mapDataModel()
    }

    fun setProductListDataModel(productList: List<ShopProductViewModel>) {
        visitables.addAll(productList)
        shopProductViewModelList.addAll(productList)
        mapDataModel()
    }

    fun addSellerAddProductDataModel() {
        visitables.add(ShopProductAddViewModel())
        mapDataModel()
    }

    fun refreshMembershipData() {
        membershipStampViewModel?.let {
            notifyItemChanged(visitables.indexOf(it))
        }
    }

    fun refreshMerchantVoucherData() {
        shopMerchantVoucherViewModel?.let {
            notifyItemChanged(visitables.indexOf(it))
        }
    }

    fun addEmptyDataModel(emptyDataViewModel: Visitable<*>) {
        visitables.add(emptyDataViewModel)
        mapDataModel()
    }

    private fun updateShopEtalaseTitle() {
        shopProductEtalaseTitleViewModel?.apply {
            shopProductEtalaseListViewModel?.let {
                etalaseName = it.selectedEtalaseName
                etalaseBadge = it.selectedEtalaseBadge
            }
            notifyItemChanged(visitables.indexOf(shopProductEtalaseTitleViewModel))
        }
    }

    private fun addProductList(shopProductViewModelArrayList: List<ShopProductViewModel>) {
        this.shopProductViewModelList.addAll(shopProductViewModelArrayList)
        visitables.addAll(shopProductViewModelArrayList)
        mapDataModel()
    }

    private fun mapDataModel() {
        val mutableMapDataModelPosition = mutableMapOf<String, Visitable<*>>()
        for (data in visitables) {
            when (data) {
                is ShopProductEtalaseListViewModel -> {
                    mutableMapDataModelPosition[KEY_ETALASE_DATA_MODEL] = data
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
                is ShopSellerEmptyProductViewModel -> {
                    mutableMapDataModelPosition[KEY_SHOP_SELLER_EMPTY_PRODUCT_DATA_MODEL] = data
                }
                is EmptyOwnShopModel -> {
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
}
