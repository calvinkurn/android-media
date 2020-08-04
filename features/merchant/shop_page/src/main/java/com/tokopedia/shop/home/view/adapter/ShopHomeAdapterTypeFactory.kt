package com.tokopedia.shop.home.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.home.WidgetName.DISPLAY_DOUBLE_COLUMN
import com.tokopedia.shop.home.WidgetName.DISPLAY_SINGLE_COLUMN
import com.tokopedia.shop.home.WidgetName.DISPLAY_TRIPLE_COLUMN
import com.tokopedia.shop.home.WidgetName.PLAY_CAROUSEL_WIDGET
import com.tokopedia.shop.home.WidgetName.PRODUCT
import com.tokopedia.shop.home.WidgetName.SLIDER_BANNER
import com.tokopedia.shop.home.WidgetName.SLIDER_SQUARE_BANNER
import com.tokopedia.shop.home.WidgetName.VIDEO
import com.tokopedia.shop.home.WidgetName.VOUCHER
import com.tokopedia.shop.home.view.adapter.viewholder.*
import com.tokopedia.shop.home.view.listener.ShopHomeDisplayWidgetListener
import com.tokopedia.shop.home.view.listener.ShopPageHomePlayCarouselListener
import com.tokopedia.shop.home.view.listener.ShopPageHomeProductClickListener
import com.tokopedia.shop.home.view.model.BaseShopHomeWidgetUiModel
import com.tokopedia.shop.home.view.model.ShopHomePlayCarouselUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductEtalaseTitleUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductViewModel
import com.tokopedia.shop.product.view.datamodel.ShopProductSortFilterUiModel
import com.tokopedia.shop.product.view.viewholder.ShopProductSortFilterViewHolder

class ShopHomeAdapterTypeFactory(
        private val listener: ShopHomeDisplayWidgetListener,
        private val onMerchantVoucherListWidgetListener: ShopHomeVoucherViewHolder.ShopHomeVoucherViewHolderListener,
        private val shopPageHomeProductClickListener: ShopPageHomeProductClickListener,
        private val shopPageHomePlayCarouselListener: ShopPageHomePlayCarouselListener,
        private val shopProductEtalaseListViewHolderListener: ShopProductSortFilterViewHolder.ShopProductEtalaseChipListViewHolderListener?
) : BaseAdapterTypeFactory(), TypeFactoryShopHome {
    var adapter: ShopHomeAdapter? = null
    private var previousViewHolder: AbstractViewHolder<*>? = null

    override fun type(baseShopHomeWidgetUiModel: BaseShopHomeWidgetUiModel): Int {
        when (baseShopHomeWidgetUiModel.name) {
            DISPLAY_SINGLE_COLUMN, DISPLAY_DOUBLE_COLUMN, DISPLAY_TRIPLE_COLUMN -> return ShopHomeMultipleImageColumnViewHolder.LAYOUT_RES
            SLIDER_SQUARE_BANNER -> return ShopHomeSliderSquareViewHolder.LAYOUT_RES
            PLAY_CAROUSEL_WIDGET -> return ShopHomePlayCarouselViewHolder.LAYOUT
            SLIDER_BANNER -> return ShopHomeSliderBannerViewHolder.LAYOUT_RES
            VIDEO -> return ShopHomeVideoViewHolder.LAYOUT_RES
            PRODUCT -> return ShopHomeCarousellProductViewHolder.LAYOUT
            VOUCHER -> return ShopHomeVoucherViewHolder.LAYOUT
        }
        return -1
    }

    override fun type(shopHomeProductEtalaseTitleUiModel: ShopHomeProductEtalaseTitleUiModel): Int {
        return ShopHomeProductEtalaseTitleViewHolder.LAYOUT
    }

    override fun type(shopHomePlayCarouselUiModel: ShopHomePlayCarouselUiModel): Int {
        return ShopHomePlayCarouselViewHolder.LAYOUT
    }

    override fun type(etalaseLabelViewModel: ShopProductSortFilterUiModel): Int {
        return ShopProductSortFilterViewHolder.LAYOUT
    }

    override fun type(shopHomeProductViewModel: ShopHomeProductViewModel): Int {
        return ShopHomeProductViewHolder.LAYOUT
    }

    override fun type(viewModel: LoadingModel?): Int {
        return ShopHomeLoadingShimmerViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        val viewHolder = when (type) {
            ShopHomeMultipleImageColumnViewHolder.LAYOUT_RES -> ShopHomeMultipleImageColumnViewHolder(
                    parent,
                    previousViewHolder,
                    listener
            )
            ShopHomeSliderSquareViewHolder.LAYOUT_RES -> ShopHomeSliderSquareViewHolder(
                    parent,
                    previousViewHolder,
                    listener
            )
            ShopHomeSliderBannerViewHolder.LAYOUT_RES -> ShopHomeSliderBannerViewHolder(
                    parent,
                    previousViewHolder,
                    listener
            )
            ShopHomeVideoViewHolder.LAYOUT_RES -> ShopHomeVideoViewHolder(
                    parent,
                    previousViewHolder,
                    listener
            )
            ShopHomeProductViewHolder.LAYOUT -> {
                ShopHomeProductViewHolder(parent, shopPageHomeProductClickListener)
            }
            ShopHomeProductEtalaseTitleViewHolder.LAYOUT -> {
                ShopHomeProductEtalaseTitleViewHolder(parent)
            }
            ShopHomeCarousellProductViewHolder.LAYOUT -> {
                ShopHomeCarousellProductViewHolder(parent, shopPageHomeProductClickListener)
            }
            ShopHomeVoucherViewHolder.LAYOUT -> {
                ShopHomeVoucherViewHolder(parent, adapter?.isOwner
                        ?: false, onMerchantVoucherListWidgetListener)
            }
            ShopHomeLoadingShimmerViewHolder.LAYOUT -> {
                ShopHomeLoadingShimmerViewHolder(parent)
            }
            ShopHomePlayCarouselViewHolder.LAYOUT -> {
                ShopHomePlayCarouselViewHolder(parent, shopPageHomePlayCarouselListener)
            }
            ShopProductSortFilterViewHolder.LAYOUT -> return ShopProductSortFilterViewHolder(parent, shopProductEtalaseListViewHolderListener)

            else -> return super.createViewHolder(parent, type)
        }
        previousViewHolder = viewHolder
        return viewHolder
    }
}