package com.tokopedia.shop.product.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.LoadingShimmeringGridViewHolder
import com.tokopedia.merchantvoucher.voucherList.widget.MerchantVoucherListWidget
import com.tokopedia.shop.R
import com.tokopedia.shop.analytic.model.ShopTrackProductTypeDef
import com.tokopedia.shop.common.view.adapter.MembershipStampAdapter
import com.tokopedia.shop.common.view.listener.ShopProductChangeGridSectionListener
import com.tokopedia.shop.common.util.ShopProductViewGridType
import com.tokopedia.shop.product.view.viewholder.ErrorNetworkWrapViewHolder
import com.tokopedia.shop.product.view.viewholder.ShopProductListEmptyViewHolder

import com.tokopedia.shop.product.view.datamodel.*
import com.tokopedia.shop.product.view.listener.*
import com.tokopedia.shop.product.view.viewholder.*

class ShopProductAdapterTypeFactory(private val membershipStampAdapterListener: MembershipStampAdapter.MembershipStampAdapterListener?,
                                    private val shopProductClickedListener: ShopProductClickedListener?,
                                    private val shopProductImpressionListener: ShopProductImpressionListener?,
                                    private val shopCarouselSeeAllClickedListener: ShopCarouselSeeAllClickedListener?,
                                    private val emptyProductOnClickListener: BaseEmptyViewHolder.Callback?,
                                    private val shopProductEtalaseListViewHolderListener: ShopProductSortFilterViewHolder.ShopProductSortFilterViewHolderListener?,
                                    private val shopProductAddViewHolderListener: ShopProductAddViewHolder.ShopProductAddViewHolderListener?,
                                    private val shopProductsEmptyViewHolderListener: ShopProductsEmptyViewHolder.ShopProductsEmptyViewHolderListener?,
                                    private val shopProductEmptySearchListener: ShopProductEmptySearchListener?,
                                    private val shopProductChangeGridSectionListener: ShopProductChangeGridSectionListener,
                                    private val isGridSquareLayout: Boolean,
                                    private val deviceWidth: Int,
                                    @param:ShopTrackProductTypeDef @field:ShopTrackProductTypeDef
                                    private val shopTrackType: Int) : BaseAdapterTypeFactory() {
    private var shopProductAdapter: ShopProductAdapter? = null
    var productCardType: ShopProductViewGridType = ShopProductViewGridType.SMALL_GRID

    fun attachAdapter(shopProductAdapter: ShopProductAdapter) {
        this.shopProductAdapter = shopProductAdapter
    }

    override fun type(viewModel: LoadingModel): Int {
        return LoadingShimmeringGridViewHolder.LAYOUT
    }

    override fun type(emptyModel: EmptyModel): Int {
        return ShopProductListEmptyViewHolder.LAYOUT
    }

    fun type(shopEmptyProductUiModel: ShopEmptyProductUiModel): Int {
        return ShopProductsEmptyViewHolder.LAYOUT
    }

    fun type(shopProductEtalaseHighlightUiModel: ShopProductEtalaseHighlightUiModel): Int {
        return ShopProductEtalaseHighlightViewHolder.LAYOUT
    }

    fun type(etalaseHighlightCarouselUiModel: EtalaseHighlightCarouselUiModel): Int {
        return ShopProductCarouselViewHolder.LAYOUT
    }

    fun type(membershipStampProgressUiModel: MembershipStampProgressUiModel): Int {
        return MembershipStampProgressViewHolder.LAYOUT
    }

    fun type(shopProductFeaturedUiModel: ShopProductFeaturedUiModel): Int {
        return ShopProductCarouselViewHolder.LAYOUT
    }

    fun type(shopMerchantVoucherUiModel: ShopMerchantVoucherUiModel): Int {
        return ShopMerchantVoucherViewHolder.LAYOUT
    }

    fun type(shopProductUiModel: ShopProductUiModel): Int {
        return when(productCardType) {
            ShopProductViewGridType.SMALL_GRID -> {
                ShopProductViewHolder.GRID_LAYOUT
            }
            ShopProductViewGridType.BIG_GRID -> {
                ShopProductItemBigGridViewHolder.LAYOUT
            }
            ShopProductViewGridType.LIST -> {
                ShopProductItemListViewHolder.LAYOUT
            }
        }
    }

    override fun type(errorNetworkModel: ErrorNetworkModel): Int {
        return ErrorNetworkWrapViewHolder.LAYOUT
    }

    fun type(etalaseLabelViewModel: ShopProductSortFilterUiModel): Int {
        return ShopProductSortFilterViewHolder.LAYOUT
    }

    fun type(uiModel: HideUiModel): Int {
        return HideViewHolder.LAYOUT
    }

    fun type(shopProductEtalaseTitleUiModel: ShopProductEtalaseTitleUiModel): Int {
        return ShopProductEtalaseTitleViewHolder.LAYOUT
    }

    fun type(shopProductChangeGridSectionUiModel: ShopProductChangeGridSectionUiModel): Int {
        return ShopProductChangeGridSectionViewHolder.LAYOUT
    }

    fun type(shopProductAddUiModel: ShopProductAddUiModel): Int {
        return ShopProductAddViewHolder.LAYOUT
    }

    fun type(shopSellerEmptyProductAllEtalaseUiModel: ShopSellerEmptyProductAllEtalaseUiModel): Int {
        return ShopProductSellerAllEtalaseEmptyViewHolder.LAYOUT
    }

    fun type(shopProductTitleEmptyUiModel: ShopProductTitleEmptyUiModel): Int {
        return ShopProductTitleEmptyViewHolder.LAYOUT
    }

    fun type(shopProductEmptySearchUiModel: ShopProductEmptySearchUiModel): Int {
        return ShopProductEmptySearchViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        when (type) {
            LoadingShimmeringGridViewHolder.LAYOUT -> return LoadingShimmeringGridViewHolder(parent)
            ShopProductListEmptyViewHolder.LAYOUT -> return ShopProductListEmptyViewHolder(parent, emptyProductOnClickListener)
            ShopProductsEmptyViewHolder.LAYOUT -> return ShopProductsEmptyViewHolder(parent, shopProductsEmptyViewHolderListener)
            ShopProductTitleEmptyViewHolder.LAYOUT -> return ShopProductTitleEmptyViewHolder(parent)
            ShopProductEmptySearchViewHolder.LAYOUT -> return ShopProductEmptySearchViewHolder(parent, shopProductEmptySearchListener)
            ErrorNetworkWrapViewHolder.LAYOUT -> return ErrorNetworkWrapViewHolder(parent)
            ShopProductEtalaseTitleViewHolder.LAYOUT -> return ShopProductEtalaseTitleViewHolder(parent)
            ShopProductSortFilterViewHolder.LAYOUT -> return ShopProductSortFilterViewHolder(parent, shopProductEtalaseListViewHolderListener)
            ShopProductAddViewHolder.LAYOUT -> return ShopProductAddViewHolder(parent, shopProductAddViewHolderListener)
            ShopProductSellerAllEtalaseEmptyViewHolder.LAYOUT -> return ShopProductSellerAllEtalaseEmptyViewHolder(parent)
            ShopMerchantVoucherViewHolder.LAYOUT -> return ShopMerchantVoucherViewHolder(parent)
            ShopProductCarouselViewHolder.LAYOUT -> return ShopProductCarouselViewHolder(parent, deviceWidth, shopProductClickedListener, shopProductImpressionListener,
                    parent.context.getString(R.string.shop_page_label_featured_product), ShopTrackProductTypeDef.FEATURED, null)
            ShopProductEtalaseHighlightViewHolder.LAYOUT -> return ShopProductEtalaseHighlightViewHolder(parent, deviceWidth, shopProductClickedListener, shopProductImpressionListener, shopCarouselSeeAllClickedListener)
            ShopProductViewHolder.GRID_LAYOUT -> return ShopProductViewHolder(parent, shopProductClickedListener, shopProductImpressionListener, !isGridSquareLayout, deviceWidth, shopTrackType, type)
            ShopProductItemListViewHolder.LAYOUT -> return ShopProductItemListViewHolder(parent, shopProductClickedListener, shopProductImpressionListener, ShopTrackProductTypeDef.PRODUCT)
            ShopProductItemBigGridViewHolder.LAYOUT -> return ShopProductItemBigGridViewHolder(parent, shopProductClickedListener, shopProductImpressionListener, ShopTrackProductTypeDef.PRODUCT)
            MembershipStampProgressViewHolder.LAYOUT -> return MembershipStampProgressViewHolder(parent, membershipStampAdapterListener)
            ShopProductChangeGridSectionViewHolder.LAYOUT -> return ShopProductChangeGridSectionViewHolder(parent, shopProductChangeGridSectionListener)
            else -> return if (type == HideViewHolder.LAYOUT) {
                HideViewHolder(parent)
            } else {
                super.createViewHolder(parent, type)
            }
        }
    }

}