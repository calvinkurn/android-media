package com.tokopedia.shop.home.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder
import com.tokopedia.play.widget.PlayWidgetViewHolder
import com.tokopedia.play.widget.ui.coordinator.PlayWidgetCoordinator
import com.tokopedia.shop.common.util.ShopProductViewGridType
import com.tokopedia.shop.common.view.listener.ShopProductChangeGridSectionListener
import com.tokopedia.shop.home.WidgetName.BUY_AGAIN
import com.tokopedia.shop.home.WidgetName.DISPLAY_DOUBLE_COLUMN
import com.tokopedia.shop.home.WidgetName.DISPLAY_SINGLE_COLUMN
import com.tokopedia.shop.home.WidgetName.DISPLAY_TRIPLE_COLUMN
import com.tokopedia.shop.home.WidgetName.NEW_PRODUCT_LAUNCH_CAMPAIGN
import com.tokopedia.shop.home.WidgetName.PLAY_CAROUSEL_WIDGET
import com.tokopedia.shop.home.WidgetName.PRODUCT
import com.tokopedia.shop.home.WidgetName.RECENT_ACTIVITY
import com.tokopedia.shop.home.WidgetName.REMINDER
import com.tokopedia.shop.home.WidgetName.SLIDER_BANNER
import com.tokopedia.shop.home.WidgetName.SLIDER_SQUARE_BANNER
import com.tokopedia.shop.home.WidgetName.VIDEO
import com.tokopedia.shop.home.WidgetName.VOUCHER_STATIC
import com.tokopedia.shop.home.view.adapter.viewholder.*
import com.tokopedia.shop.home.view.listener.ShopHomeCampaignNplWidgetListener
import com.tokopedia.shop.home.view.listener.ShopHomeCarouselProductListener
import com.tokopedia.shop.home.view.listener.ShopHomeDisplayWidgetListener
import com.tokopedia.shop.home.view.listener.ShopHomeEndlessProductListener
import com.tokopedia.shop.home.view.model.*
import com.tokopedia.shop.product.view.datamodel.ShopProductSortFilterUiModel
import com.tokopedia.shop.product.view.viewholder.ShopProductSortFilterViewHolder

class ShopHomeAdapterTypeFactory(
        private val listener: ShopHomeDisplayWidgetListener,
        private val onMerchantVoucherListWidgetListener: ShopHomeVoucherViewHolder.ShopHomeVoucherViewHolderListener,
        private val shopHomeEndlessProductListener: ShopHomeEndlessProductListener,
        private val shopHomeCarouselProductListener: ShopHomeCarouselProductListener,
        private val shopProductEtalaseListViewHolderListener: ShopProductSortFilterViewHolder.ShopProductSortFilterViewHolderListener?,
        private val shopHomeCampaignNplWidgetListener: ShopHomeCampaignNplWidgetListener,
        private val shopProductChangeGridSectionListener: ShopProductChangeGridSectionListener,
        private val playWidgetCoordinator: PlayWidgetCoordinator
) : BaseAdapterTypeFactory(), TypeFactoryShopHome {
    var productCardType: ShopProductViewGridType = ShopProductViewGridType.SMALL_GRID
    private var previousViewHolder: AbstractViewHolder<*>? = null

    override fun type(baseShopHomeWidgetUiModel: BaseShopHomeWidgetUiModel): Int {
        return if(isShowHomeWidgetPlaceHolder(baseShopHomeWidgetUiModel))
            ShopHomeShimmeringGeneralViewHolder.LAYOUT
        else {
            return when (baseShopHomeWidgetUiModel.name) {
                DISPLAY_SINGLE_COLUMN, DISPLAY_DOUBLE_COLUMN, DISPLAY_TRIPLE_COLUMN -> ShopHomeMultipleImageColumnViewHolder.LAYOUT_RES
                SLIDER_SQUARE_BANNER -> ShopHomeSliderSquareViewHolder.LAYOUT_RES
                SLIDER_BANNER -> ShopHomeSliderBannerViewHolder.LAYOUT_RES
                VIDEO -> ShopHomeVideoViewHolder.LAYOUT_RES
                PRODUCT -> ShopHomeCarousellProductViewHolder.LAYOUT
                VOUCHER_STATIC -> ShopHomeVoucherViewHolder.LAYOUT
                RECENT_ACTIVITY, BUY_AGAIN, REMINDER -> ShopHomeCarouselProductPersonalizationViewHolder.LAYOUT
                NEW_PRODUCT_LAUNCH_CAMPAIGN -> ShopHomeNplCampaignViewHolder.LAYOUT
                PLAY_CAROUSEL_WIDGET -> CarouselPlayWidgetViewHolder.LAYOUT
                else -> HideViewHolder.LAYOUT
            }
        }
    }

    private fun isShowHomeWidgetPlaceHolder(model: BaseShopHomeWidgetUiModel): Boolean {
        return model.widgetState == WidgetState.PLACEHOLDER || model.widgetState == WidgetState.LOADING
    }

    override fun type(shopHomeProductEtalaseTitleUiModel: ShopHomeProductEtalaseTitleUiModel): Int {
        return ShopHomeProductEtalaseTitleViewHolder.LAYOUT
    }

    override fun type(etalaseLabelViewModel: ShopProductSortFilterUiModel): Int {
        return ShopProductSortFilterViewHolder.LAYOUT
    }

    override fun type(carouselPlayCardViewModel: CarouselPlayWidgetUiModel): Int {
        return CarouselPlayWidgetViewHolder.LAYOUT
    }

    override fun type(shopHomeProductViewModel: ShopHomeProductUiModel): Int {
        return when(productCardType) {
            ShopProductViewGridType.SMALL_GRID -> {
                ShopHomeProductViewHolder.LAYOUT
            }
            ShopProductViewGridType.BIG_GRID -> {
                ShopHomeProductItemBigGridViewHolder.LAYOUT
            }
            ShopProductViewGridType.LIST -> {
                ShopHomeProductItemListViewHolder.LAYOUT
            }
        }
    }

    override fun type(viewModel: LoadingModel?): Int {
        return ShopHomeLoadingShimmerViewHolder.LAYOUT
    }

    fun type(shopHomeProductChangeGridSectionUiModel: ShopHomeProductChangeGridSectionUiModel): Int {
        return ShopHomeProductChangeGridSectionViewHolder.LAYOUT
    }

    fun type(model: ProductGridListPlaceholderUiModel): Int {
        return ProductGridListPlaceholderViewHolder.LAYOUT
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
                ShopHomeProductViewHolder(parent, shopHomeEndlessProductListener)
            }
            ShopHomeProductItemBigGridViewHolder.LAYOUT -> {
                ShopHomeProductItemBigGridViewHolder(parent, shopHomeEndlessProductListener)
            }
            ShopHomeProductItemListViewHolder.LAYOUT -> {
                ShopHomeProductItemListViewHolder(parent, shopHomeEndlessProductListener)
            }
            ShopHomeProductEtalaseTitleViewHolder.LAYOUT -> {
                ShopHomeProductEtalaseTitleViewHolder(parent)
            }
            ShopHomeCarousellProductViewHolder.LAYOUT -> {
                ShopHomeCarousellProductViewHolder(parent, shopHomeCarouselProductListener)
            }
            ShopHomeVoucherViewHolder.LAYOUT -> {
                ShopHomeVoucherViewHolder(parent, onMerchantVoucherListWidgetListener)
            }
            ShopHomeLoadingShimmerViewHolder.LAYOUT -> {
                ShopHomeLoadingShimmerViewHolder(parent)
            }
            ShopProductSortFilterViewHolder.LAYOUT -> return ShopProductSortFilterViewHolder(parent, shopProductEtalaseListViewHolderListener)
            ShopHomeNplCampaignViewHolder.LAYOUT -> {
                ShopHomeNplCampaignViewHolder(parent, shopHomeCampaignNplWidgetListener)
            }
            ShopHomeProductChangeGridSectionViewHolder.LAYOUT -> ShopHomeProductChangeGridSectionViewHolder(parent, shopProductChangeGridSectionListener)
            CarouselPlayWidgetViewHolder.LAYOUT -> CarouselPlayWidgetViewHolder(PlayWidgetViewHolder(parent, playWidgetCoordinator))
            ShopHomeCarouselProductPersonalizationViewHolder.LAYOUT -> ShopHomeCarouselProductPersonalizationViewHolder(parent, shopHomeCarouselProductListener)
            ShopHomeShimmeringGeneralViewHolder.LAYOUT -> ShopHomeShimmeringGeneralViewHolder(parent)
            ProductGridListPlaceholderViewHolder.LAYOUT -> ProductGridListPlaceholderViewHolder(parent)
            else -> return super.createViewHolder(parent, type)
        }
        previousViewHolder = viewHolder
        return viewHolder
    }
}