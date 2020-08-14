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
import com.tokopedia.shop.product.view.datamodel.*
import com.tokopedia.shop.product.view.listener.ShopCarouselSeeAllClickedListener
import com.tokopedia.shop.product.view.listener.ShopProductClickedListener
import com.tokopedia.shop.product.view.listener.ShopProductEmptySearchListener
import com.tokopedia.shop.product.view.listener.ShopProductImpressionListener
import com.tokopedia.shop.product.view.viewholder.*

class ShopProductAdapterTypeFactory(private val membershipStampAdapterListener: MembershipStampAdapter.MembershipStampAdapterListener?,
                                    private val shopProductClickedListener: ShopProductClickedListener?,
                                    private val shopProductImpressionListener: ShopProductImpressionListener?,
                                    private val shopCarouselSeeAllClickedListener: ShopCarouselSeeAllClickedListener?,
                                    private val emptyProductOnClickListener: BaseEmptyViewHolder.Callback?,
                                    private val shopProductEtalaseListViewHolderListener: ShopProductSortFilterViewHolder.ShopProductEtalaseChipListViewHolderListener?,
                                    private val onMerchantVoucherListWidgetListener: MerchantVoucherListWidget.OnMerchantVoucherListWidgetListener?,
                                    private val shopProductAddViewHolderListener: ShopProductAddViewHolder.ShopProductAddViewHolderListener?,
                                    private val shopProductsEmptyViewHolderListener: ShopProductsEmptyViewHolder.ShopProductsEmptyViewHolderListener?,
                                    private val shopProductEmptySearchListener: ShopProductEmptySearchListener?,
                                    private val isGridSquareLayout: Boolean,
                                    private val deviceWidth: Int,
                                    @param:ShopTrackProductTypeDef @field:ShopTrackProductTypeDef
                                    private val shopTrackType: Int) : BaseAdapterTypeFactory() {
    private var shopProductAdapter: ShopProductAdapter? = null

    fun attachAdapter(shopProductAdapter: ShopProductAdapter) {
        this.shopProductAdapter = shopProductAdapter
    }

    override fun type(viewModel: LoadingModel): Int {
        return LoadingShimmeringGridViewHolder.LAYOUT
    }

    override fun type(emptyModel: EmptyModel): Int {
        return ShopProductListEmptyViewHolder.LAYOUT
    }

    fun type(shopEmptyProductViewModel: ShopEmptyProductViewModel): Int {
        return ShopProductsEmptyViewHolder.LAYOUT
    }

    fun type(shopProductEtalaseHighlightViewModel: ShopProductEtalaseHighlightViewModel): Int {
        return ShopProductEtalaseHighlightViewHolder.LAYOUT
    }

    fun type(etalaseHighlightCarouselViewModel: EtalaseHighlightCarouselViewModel): Int {
        return ShopProductCarouselViewHolder.LAYOUT
    }

    fun type(membershipStampProgressViewModel: MembershipStampProgressViewModel): Int {
        return MembershipStampProgressViewHolder.LAYOUT
    }

    fun type(shopProductFeaturedViewModel: ShopProductFeaturedViewModel): Int {
        return ShopProductCarouselViewHolder.LAYOUT
    }

    fun type(shopMerchantVoucherViewModel: ShopMerchantVoucherViewModel): Int {
        return ShopMerchantVoucherViewHolder.LAYOUT
    }

    fun type(shopProductViewModel: ShopProductViewModel): Int {
        return ShopProductViewHolder.GRID_LAYOUT
    }

    override fun type(errorNetworkModel: ErrorNetworkModel): Int {
        return ErrorNetworkWrapViewHolder.LAYOUT
    }

    fun type(etalaseLabelViewModel: ShopProductSortFilterUiModel): Int {
        return ShopProductSortFilterViewHolder.LAYOUT
    }

    fun type(viewModel: HideViewModel): Int {
        return HideViewHolder.LAYOUT
    }

    fun type(shopProductEtalaseTitleViewModel: ShopProductEtalaseTitleViewModel): Int {
        return ShopProductEtalaseTitleViewHolder.LAYOUT
    }

    fun type(shopProductAddViewModel: ShopProductAddViewModel): Int {
        return ShopProductAddViewHolder.LAYOUT
    }

    fun type(shopSellerEmptyProductAllEtalaseViewModel: ShopSellerEmptyProductAllEtalaseViewModel): Int {
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
            ShopProductTitleEmptyViewHolder.LAYOUT -> ShopProductTitleEmptyViewHolder(parent)
            ShopProductEmptySearchViewHolder.LAYOUT -> ShopProductEmptySearchViewHolder(parent, shopProductEmptySearchListener)
            ErrorNetworkWrapViewHolder.LAYOUT -> return ErrorNetworkWrapViewHolder(parent)
            ShopProductEtalaseTitleViewHolder.LAYOUT -> return ShopProductEtalaseTitleViewHolder(parent)
            ShopProductSortFilterViewHolder.LAYOUT -> return ShopProductSortFilterViewHolder(parent, shopProductEtalaseListViewHolderListener)
            ShopProductAddViewHolder.LAYOUT -> return ShopProductAddViewHolder(parent, shopProductAddViewHolderListener)
            ShopProductSellerAllEtalaseEmptyViewHolder.LAYOUT -> return ShopProductSellerAllEtalaseEmptyViewHolder(parent)
            ShopMerchantVoucherViewHolder.LAYOUT -> return ShopMerchantVoucherViewHolder(parent, onMerchantVoucherListWidgetListener)
            ShopProductCarouselViewHolder.LAYOUT -> return ShopProductCarouselViewHolder(parent, deviceWidth, shopProductClickedListener, shopProductImpressionListener,
                    parent.context.getString(R.string.shop_page_label_featured_product), ShopTrackProductTypeDef.FEATURED, null)
            ShopProductEtalaseHighlightViewHolder.LAYOUT -> return ShopProductEtalaseHighlightViewHolder(parent, deviceWidth, shopProductClickedListener, shopProductImpressionListener, shopCarouselSeeAllClickedListener)
            ShopProductViewHolder.GRID_LAYOUT -> return ShopProductViewHolder(parent, shopProductClickedListener, shopProductImpressionListener, !isGridSquareLayout, deviceWidth, shopTrackType, type)
            MembershipStampProgressViewHolder.LAYOUT -> return MembershipStampProgressViewHolder(parent, membershipStampAdapterListener)
            else -> return if (type == HideViewHolder.LAYOUT) {
                HideViewHolder(parent)
            } else {
                super.createViewHolder(parent, type)
            }
        }
    }

}