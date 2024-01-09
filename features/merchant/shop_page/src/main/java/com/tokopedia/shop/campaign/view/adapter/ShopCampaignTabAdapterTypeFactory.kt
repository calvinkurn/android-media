package com.tokopedia.shop.campaign.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder
import com.tokopedia.play.widget.PlayWidgetViewHolder
import com.tokopedia.play.widget.ui.coordinator.PlayWidgetCoordinator
import com.tokopedia.shop.R
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
import com.tokopedia.shop.home.util.RecyclerviewPoolListener
import com.tokopedia.shop.home.WidgetNameEnum
import com.tokopedia.shop.home.view.adapter.ShopWidgetTypeFactory
import com.tokopedia.shop.home.view.adapter.viewholder.ShopCarouselProductWidgetPlaceholderViewHolder
import com.tokopedia.shop.home.view.listener.ShopHomeDisplayWidgetListener
import com.tokopedia.shop.home.view.listener.ShopHomeReimagineDisplayBannerTimerWidgetListener
import com.tokopedia.shop.home.view.model.BaseShopHomeWidgetUiModel
import com.tokopedia.shop_widget.common.util.WidgetState

class ShopCampaignTabAdapterTypeFactory(
    private val shopHomeDisplayWidgetListener: ShopHomeDisplayWidgetListener,
    private val shopCampaignDisplayBannerTimerWidgetListener: ShopHomeReimagineDisplayBannerTimerWidgetListener,
    private val shopCampaignCarouselProductListener: ShopCampaignCarouselProductListener,
    private val playWidgetCoordinator: PlayWidgetCoordinator,
    private val shopPlayWidgetListener: ShopCampaignPlayWidgetListener,
    private val shopCampaignInterface: ShopCampaignInterface,
    private val sliderBannerHighlightListener: ShopCampaignDisplaySliderBannerHighlightViewHolder.Listener,
    private val shopCampaignVoucherSliderListener: ShopCampaignVoucherSliderViewHolder.Listener,
    private val shopCampaignVoucherSliderItemListener: ShopCampaignVoucherSliderItemViewHolder.Listener,
    private val shopCampaignVoucherSliderMoreItemListener: ShopCampaignVoucherSliderMoreItemViewHolder.Listener,
    private val recyclerviewPoolListener: RecyclerviewPoolListener
) : BaseAdapterTypeFactory(), ShopWidgetTypeFactory {

    override fun type(baseShopHomeWidgetUiModel: BaseShopHomeWidgetUiModel): Int {
        return when (baseShopHomeWidgetUiModel.name) {
            WidgetNameEnum.VOUCHER.value -> getShopCampaignVoucherSliderViewHolder(baseShopHomeWidgetUiModel)
            WidgetNameEnum.BANNER_TIMER.value -> getShopCampaignDisplayBannerTimerViewHolder(baseShopHomeWidgetUiModel)
            WidgetNameEnum.PRODUCT_HIGHLIGHT.value -> getShopCampaignCarouselProductViewHolder(baseShopHomeWidgetUiModel)
            WidgetNameEnum.DISPLAY_SINGLE_COLUMN.value,
            WidgetNameEnum.DISPLAY_DOUBLE_COLUMN.value,
            WidgetNameEnum.DISPLAY_TRIPLE_COLUMN.value -> getShopCampaignMultipleImageColumnViewHolder(
                baseShopHomeWidgetUiModel
            )
            WidgetNameEnum.SLIDER_SQUARE_BANNER.value -> getShopCampaignSliderSquareViewHolder(baseShopHomeWidgetUiModel)
            WidgetNameEnum.SLIDER_BANNER.value -> getShopCampaignSliderBannerViewHolder(baseShopHomeWidgetUiModel)
            WidgetNameEnum.PLAY_CAROUSEL_WIDGET.value -> ShopCampaignCarouselPlayWidgetViewHolder.LAYOUT
            WidgetNameEnum.VIDEO.value -> getShopCampaignVideoViewHolder(baseShopHomeWidgetUiModel)
            WidgetNameEnum.SLIDER_BANNER_HIGHLIGHT.value -> getShopCampaignDisplaySliderBannerHighlight(
                baseShopHomeWidgetUiModel
            )

            else -> HideViewHolder.LAYOUT
        }
    }

    private fun getShopCampaignVoucherSliderViewHolder(baseShopHomeWidgetUiModel: BaseShopHomeWidgetUiModel): Int {
        return if (isShowWidgetPlaceHolder(baseShopHomeWidgetUiModel)) {
            ShopCampaignVoucherSliderPlaceholderViewHolder.LAYOUT
        } else {
            ShopCampaignVoucherSliderViewHolder.LAYOUT
        }
    }

    private fun getShopCampaignSliderSquareViewHolder(baseShopHomeWidgetUiModel: BaseShopHomeWidgetUiModel): Int {
        return if (isShowWidgetPlaceHolder(baseShopHomeWidgetUiModel)) {
            ShopCampaignSliderSquarePlaceholderViewHolder.LAYOUT_RES
        } else {
            ShopCampaignSliderSquareViewHolder.LAYOUT_RES
        }
    }

    private fun getShopCampaignSliderBannerViewHolder(baseShopHomeWidgetUiModel: BaseShopHomeWidgetUiModel): Int {
        return if (isShowWidgetPlaceHolder(baseShopHomeWidgetUiModel)) {
            ShopCampaignSliderBannerPlaceholderViewHolder.LAYOUT_RES
        } else {
            ShopCampaignSliderBannerViewHolder.LAYOUT_RES
        }
    }

    private fun getShopCampaignDisplaySliderBannerHighlight(baseShopHomeWidgetUiModel: BaseShopHomeWidgetUiModel): Int {
        return if (isShowWidgetPlaceHolder(baseShopHomeWidgetUiModel)) {
            ShopCampaignDisplaySliderBannerHighlightPlaceholderViewHolder.LAYOUT
        } else {
            ShopCampaignDisplaySliderBannerHighlightViewHolder.LAYOUT
        }
    }

    private fun getShopCampaignDisplayBannerTimerViewHolder(baseShopHomeWidgetUiModel: BaseShopHomeWidgetUiModel): Int {
        return if (isShowWidgetPlaceHolder(baseShopHomeWidgetUiModel)) {
            ShopCampaignDisplayBannerTimerPlaceholderViewHolder.LAYOUT
        } else {
            ShopCampaignDisplayBannerTimerViewHolder.LAYOUT
        }
    }

    private fun getShopCampaignCarouselProductViewHolder(baseShopHomeWidgetUiModel: BaseShopHomeWidgetUiModel): Int {
        return if (isShowWidgetPlaceHolder(baseShopHomeWidgetUiModel)) {
            ShopCarouselProductWidgetPlaceholderViewHolder.LAYOUT
        } else {
            ShopCampaignCarouselProductHighlightViewHolder.LAYOUT
        }
    }

    private fun getShopCampaignMultipleImageColumnViewHolder(baseShopHomeWidgetUiModel: BaseShopHomeWidgetUiModel): Int {
        return if (isShowWidgetPlaceHolder(baseShopHomeWidgetUiModel)) {
            ShopCampaignMultipleImageColumnPlaceholderViewHolder.LAYOUT
        } else {
            ShopCampaignMultipleImageColumnViewHolder.LAYOUT
        }
    }

    private fun getShopCampaignVideoViewHolder(baseShopHomeWidgetUiModel: BaseShopHomeWidgetUiModel): Int {
        return if (isShowWidgetPlaceHolder(baseShopHomeWidgetUiModel)) {
            ShopCampaignVideoPlaceholderViewHolder.LAYOUT
        } else {
            ShopCampaignVideoViewHolder.LAYOUT
        }
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
                shopCampaignInterface,
                recyclerviewPoolListener
            )

            ShopCarouselProductWidgetPlaceholderViewHolder.LAYOUT -> ShopCarouselProductWidgetPlaceholderViewHolder(
                parent,
                recyclerviewPoolListener
            )

            ShopCampaignMultipleImageColumnPlaceholderViewHolder.LAYOUT -> ShopCampaignMultipleImageColumnPlaceholderViewHolder(
                parent
            )

            ShopCampaignMultipleImageColumnViewHolder.LAYOUT -> ShopCampaignMultipleImageColumnViewHolder(
                parent,
                shopHomeDisplayWidgetListener,
                shopCampaignInterface,
                recyclerviewPoolListener
            )

            ShopCampaignVoucherSliderPlaceholderViewHolder.LAYOUT -> ShopCampaignVoucherSliderPlaceholderViewHolder(parent)
            ShopCampaignVoucherSliderViewHolder.LAYOUT -> ShopCampaignVoucherSliderViewHolder(
                parent,
                shopCampaignInterface,
                shopCampaignVoucherSliderListener,
                shopCampaignVoucherSliderItemListener,
                shopCampaignVoucherSliderMoreItemListener,
                recyclerviewPoolListener
            )

            ShopCampaignSliderSquarePlaceholderViewHolder.LAYOUT_RES -> ShopCampaignSliderSquarePlaceholderViewHolder(
                parent
            )

            ShopCampaignSliderSquareViewHolder.LAYOUT_RES -> ShopCampaignSliderSquareViewHolder(
                parent,
                shopHomeDisplayWidgetListener,
                shopCampaignInterface,
                recyclerviewPoolListener
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
                shopPlayWidgetListener,
                shopCampaignInterface
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
