package com.tokopedia.shop.campaign.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder
import com.tokopedia.play.widget.PlayWidgetViewHolder
import com.tokopedia.play.widget.ui.coordinator.PlayWidgetCoordinator
import com.tokopedia.shop.campaign.view.adapter.viewholder.ShopCampaignCarouselPlayWidgetViewHolder
import com.tokopedia.shop.campaign.view.adapter.viewholder.ShopCampaignCarouselProductHighlightViewHolder
import com.tokopedia.shop.campaign.view.adapter.viewholder.ShopCampaignDisplayBannerTimerPlaceholderViewHolder
import com.tokopedia.shop.campaign.view.adapter.viewholder.ShopCampaignDisplayBannerTimerViewHolder
import com.tokopedia.shop.campaign.view.adapter.viewholder.ShopCampaignDisplaySliderBannerHighlightPlaceholderViewHolder
import com.tokopedia.shop.campaign.view.adapter.viewholder.ShopCampaignDisplaySliderBannerHighlightViewHolder
import com.tokopedia.shop.campaign.view.adapter.viewholder.ShopCampaignLayoutLoadingShimmerViewHolder
import com.tokopedia.shop.campaign.view.adapter.viewholder.ShopCampaignMultipleImageColumnPlaceholderViewHolder
import com.tokopedia.shop.campaign.view.adapter.viewholder.ShopCampaignMultipleImageColumnViewHolder
import com.tokopedia.shop.campaign.view.adapter.viewholder.ShopCampaignSliderBannerPlaceholderViewHolder
import com.tokopedia.shop.campaign.view.adapter.viewholder.ShopCampaignSliderBannerViewHolder
import com.tokopedia.shop.campaign.view.adapter.viewholder.ShopCampaignSliderSquarePlaceholderViewHolder
import com.tokopedia.shop.campaign.view.adapter.viewholder.ShopCampaignSliderSquareViewHolder
import com.tokopedia.shop.campaign.view.adapter.viewholder.ShopCampaignVideoPlaceholderViewHolder
import com.tokopedia.shop.campaign.view.adapter.viewholder.ShopCampaignVideoViewHolder
import com.tokopedia.shop.campaign.view.adapter.viewholder.ShopCampaignVoucherSliderItemViewHolder
import com.tokopedia.shop.campaign.view.adapter.viewholder.ShopCampaignVoucherSliderMoreItemViewHolder
import com.tokopedia.shop.campaign.view.adapter.viewholder.ShopCampaignVoucherSliderPlaceholderViewHolder
import com.tokopedia.shop.campaign.view.adapter.viewholder.ShopCampaignVoucherSliderViewHolder
import com.tokopedia.shop.campaign.view.listener.ShopCampaignCarouselProductListener
import com.tokopedia.shop.campaign.view.listener.ShopCampaignInterface
import com.tokopedia.shop.campaign.view.listener.ShopCampaignPlayWidgetListener
import com.tokopedia.shop.home.WidgetName.BANNER_TIMER
import com.tokopedia.shop.home.WidgetName.DISPLAY_DOUBLE_COLUMN
import com.tokopedia.shop.home.WidgetName.DISPLAY_SINGLE_COLUMN
import com.tokopedia.shop.home.WidgetName.DISPLAY_TRIPLE_COLUMN
import com.tokopedia.shop.home.WidgetName.PLAY_CAROUSEL_WIDGET
import com.tokopedia.shop.home.WidgetName.PRODUCT_HIGHLIGHT
import com.tokopedia.shop.home.WidgetName.SLIDER_BANNER
import com.tokopedia.shop.home.WidgetName.SLIDER_BANNER_HIGHLIGHT
import com.tokopedia.shop.home.WidgetName.SLIDER_SQUARE_BANNER
import com.tokopedia.shop.home.WidgetName.VIDEO
import com.tokopedia.shop.home.WidgetName.VOUCHER
import com.tokopedia.shop.home.view.adapter.ShopWidgetTypeFactory
import com.tokopedia.shop.home.view.adapter.viewholder.ShopCarouselProductWidgetPlaceholderViewHolder
import com.tokopedia.shop.home.view.listener.ShopHomeDisplayBannerTimerWidgetListener
import com.tokopedia.shop.home.view.listener.ShopHomeDisplayWidgetListener
import com.tokopedia.shop.home.view.model.BaseShopHomeWidgetUiModel
import com.tokopedia.shop_widget.common.util.WidgetState
import com.tokopedia.shop.R

class ShopCampaignTabAdapterTypeFactory(
    private val shopHomeDisplayWidgetListener: ShopHomeDisplayWidgetListener,
    private val shopCampaignDisplayBannerTimerWidgetListener: ShopHomeDisplayBannerTimerWidgetListener,
    private val shopCampaignCarouselProductListener: ShopCampaignCarouselProductListener,
    private val playWidgetCoordinator: PlayWidgetCoordinator,
    private val shopPlayWidgetListener: ShopCampaignPlayWidgetListener,
    private val shopCampaignInterface: ShopCampaignInterface,
    private val sliderBannerHighlightListener: ShopCampaignDisplaySliderBannerHighlightViewHolder.Listener,
    private val shopCampaignVoucherSliderListener: ShopCampaignVoucherSliderViewHolder.Listener,
    private val shopCampaignVoucherSliderItemListener: ShopCampaignVoucherSliderItemViewHolder.Listener,
    private val shopCampaignVoucherSliderMoreItemListener: ShopCampaignVoucherSliderMoreItemViewHolder.Listener
) : BaseAdapterTypeFactory(), ShopWidgetTypeFactory {

    override fun type(baseShopHomeWidgetUiModel: BaseShopHomeWidgetUiModel): Int {
        return when (baseShopHomeWidgetUiModel.name) {
            VOUCHER -> getShopCampaignVoucherSliderViewHolder(baseShopHomeWidgetUiModel)
            BANNER_TIMER -> getShopCampaignDisplayBannerTimerViewHolder(baseShopHomeWidgetUiModel)
            PRODUCT_HIGHLIGHT -> getShopCampaignCarouselProductViewHolder(baseShopHomeWidgetUiModel)
            DISPLAY_SINGLE_COLUMN, DISPLAY_DOUBLE_COLUMN, DISPLAY_TRIPLE_COLUMN -> getShopCampaignMultipleImageColumnViewHolder(
                baseShopHomeWidgetUiModel
            )

            SLIDER_SQUARE_BANNER -> getShopCampaignSliderSquareViewHolder(baseShopHomeWidgetUiModel)
            SLIDER_BANNER -> getShopCampaignSliderBannerViewHolder(baseShopHomeWidgetUiModel)
            PLAY_CAROUSEL_WIDGET -> ShopCampaignCarouselPlayWidgetViewHolder.LAYOUT
            VIDEO -> getShopCampaignVideoViewHolder(baseShopHomeWidgetUiModel)
            SLIDER_BANNER_HIGHLIGHT -> getShopCampaignDisplaySliderBannerHighlight(
                baseShopHomeWidgetUiModel
            )

            else -> HideViewHolder.LAYOUT
        }
    }

    private fun getShopCampaignVoucherSliderViewHolder(baseShopHomeWidgetUiModel: BaseShopHomeWidgetUiModel): Int {
        return if (isShowWidgetPlaceHolder(baseShopHomeWidgetUiModel))
            ShopCampaignVoucherSliderPlaceholderViewHolder.LAYOUT
        else
            ShopCampaignVoucherSliderViewHolder.LAYOUT

    }

    private fun getShopCampaignSliderSquareViewHolder(baseShopHomeWidgetUiModel: BaseShopHomeWidgetUiModel): Int {
        return if (isShowWidgetPlaceHolder(baseShopHomeWidgetUiModel))
            ShopCampaignSliderSquarePlaceholderViewHolder.LAYOUT_RES
        else
            ShopCampaignSliderSquareViewHolder.LAYOUT_RES
    }

    private fun getShopCampaignSliderBannerViewHolder(baseShopHomeWidgetUiModel: BaseShopHomeWidgetUiModel): Int {
        return if (isShowWidgetPlaceHolder(baseShopHomeWidgetUiModel))
            ShopCampaignSliderBannerPlaceholderViewHolder.LAYOUT_RES
        else
            ShopCampaignSliderBannerViewHolder.LAYOUT_RES
    }

    private fun getShopCampaignDisplaySliderBannerHighlight(baseShopHomeWidgetUiModel: BaseShopHomeWidgetUiModel): Int {
        return if (isShowWidgetPlaceHolder(baseShopHomeWidgetUiModel))
            ShopCampaignDisplaySliderBannerHighlightPlaceholderViewHolder.LAYOUT
        else
            ShopCampaignDisplaySliderBannerHighlightViewHolder.LAYOUT
    }

    private fun getShopCampaignDisplayBannerTimerViewHolder(baseShopHomeWidgetUiModel: BaseShopHomeWidgetUiModel): Int {
        return if (isShowWidgetPlaceHolder(baseShopHomeWidgetUiModel))
            ShopCampaignDisplayBannerTimerPlaceholderViewHolder.LAYOUT
        else
            ShopCampaignDisplayBannerTimerViewHolder.LAYOUT
    }

    private fun getShopCampaignCarouselProductViewHolder(baseShopHomeWidgetUiModel: BaseShopHomeWidgetUiModel): Int {
        return if (isShowWidgetPlaceHolder(baseShopHomeWidgetUiModel))
            ShopCarouselProductWidgetPlaceholderViewHolder.LAYOUT
        else
            ShopCampaignCarouselProductHighlightViewHolder.LAYOUT
    }

    private fun getShopCampaignMultipleImageColumnViewHolder(baseShopHomeWidgetUiModel: BaseShopHomeWidgetUiModel): Int {
        return if (isShowWidgetPlaceHolder(baseShopHomeWidgetUiModel))
            ShopCampaignMultipleImageColumnPlaceholderViewHolder.LAYOUT
        else
            ShopCampaignMultipleImageColumnViewHolder.LAYOUT
    }

    private fun getShopCampaignVideoViewHolder(baseShopHomeWidgetUiModel: BaseShopHomeWidgetUiModel): Int {
        return if (isShowWidgetPlaceHolder(baseShopHomeWidgetUiModel))
            ShopCampaignVideoPlaceholderViewHolder.LAYOUT
        else
            ShopCampaignVideoViewHolder.LAYOUT
    }

    override fun type(viewModel: LoadingModel?): Int {
        return ShopCampaignLayoutLoadingShimmerViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        val viewHolder = when (type) {
            ShopCampaignDisplayBannerTimerPlaceholderViewHolder.LAYOUT -> ShopCampaignDisplayBannerTimerPlaceholderViewHolder(
                parent
            )

            ShopCampaignDisplayBannerTimerViewHolder.LAYOUT -> ShopCampaignDisplayBannerTimerViewHolder(
                parent,
                shopCampaignDisplayBannerTimerWidgetListener,
                shopCampaignInterface
            )

            ShopCampaignCarouselProductHighlightViewHolder.LAYOUT -> ShopCampaignCarouselProductHighlightViewHolder(
                parent,
                shopCampaignCarouselProductListener,
                shopCampaignInterface
            )

            ShopCarouselProductWidgetPlaceholderViewHolder.LAYOUT -> ShopCarouselProductWidgetPlaceholderViewHolder(
                parent
            )

            ShopCampaignMultipleImageColumnPlaceholderViewHolder.LAYOUT -> ShopCampaignMultipleImageColumnPlaceholderViewHolder(
                parent
            )

            ShopCampaignMultipleImageColumnViewHolder.LAYOUT -> ShopCampaignMultipleImageColumnViewHolder(
                parent,
                shopHomeDisplayWidgetListener,
                shopCampaignInterface
            )

            ShopCampaignVoucherSliderPlaceholderViewHolder.LAYOUT -> ShopCampaignVoucherSliderPlaceholderViewHolder(parent)
            ShopCampaignVoucherSliderViewHolder.LAYOUT -> ShopCampaignVoucherSliderViewHolder(
                parent,
                shopCampaignInterface,
                shopCampaignVoucherSliderListener,
                shopCampaignVoucherSliderItemListener,
                shopCampaignVoucherSliderMoreItemListener
            )

            ShopCampaignSliderSquarePlaceholderViewHolder.LAYOUT_RES -> ShopCampaignSliderSquarePlaceholderViewHolder(
                parent
            )

            ShopCampaignSliderSquareViewHolder.LAYOUT_RES -> ShopCampaignSliderSquareViewHolder(
                parent,
                shopHomeDisplayWidgetListener,
                shopCampaignInterface
            )

            ShopCampaignSliderBannerPlaceholderViewHolder.LAYOUT_RES -> ShopCampaignSliderBannerPlaceholderViewHolder(
                parent
            )

            ShopCampaignSliderBannerViewHolder.LAYOUT_RES -> ShopCampaignSliderBannerViewHolder(
                parent,
                shopHomeDisplayWidgetListener,
                shopCampaignInterface
            )

            ShopCampaignCarouselPlayWidgetViewHolder.LAYOUT -> ShopCampaignCarouselPlayWidgetViewHolder(
                parent,
                PlayWidgetViewHolder(
                    parent.findViewById(R.id.play_widget_view),
                    playWidgetCoordinator
                ),
                shopPlayWidgetListener, shopCampaignInterface
            )

            ShopCampaignVideoPlaceholderViewHolder.LAYOUT -> ShopCampaignVideoPlaceholderViewHolder(
                parent
            )

            ShopCampaignVideoViewHolder.LAYOUT -> ShopCampaignVideoViewHolder(
                parent,
                shopHomeDisplayWidgetListener,
                shopCampaignInterface
            )

            ShopCampaignDisplaySliderBannerHighlightPlaceholderViewHolder.LAYOUT -> ShopCampaignDisplaySliderBannerHighlightPlaceholderViewHolder(
                parent
            )

            ShopCampaignDisplaySliderBannerHighlightViewHolder.LAYOUT -> ShopCampaignDisplaySliderBannerHighlightViewHolder(
                parent,
                sliderBannerHighlightListener,
                shopCampaignInterface
            )

            ShopCampaignLayoutLoadingShimmerViewHolder.LAYOUT -> {
                ShopCampaignLayoutLoadingShimmerViewHolder(parent)
            }

            else -> return super.createViewHolder(parent, type)
        }
        return viewHolder
    }

    private fun isShowWidgetPlaceHolder(model: BaseShopHomeWidgetUiModel): Boolean {
        return model.widgetState == WidgetState.PLACEHOLDER || model.widgetState == WidgetState.LOADING
    }

}
