package com.tokopedia.shop.pageheader.presentation.adapter.viewholder.widget

import android.view.View
import android.widget.FrameLayout
import com.airbnb.lottie.LottieCompositionFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.shop.R
import com.tokopedia.shop.analytic.ShopPageTrackingSGCPlayWidget
import com.tokopedia.shop.databinding.LayoutShopHeaderPlayWidgetBinding
import com.tokopedia.shop.pageheader.data.model.ShopPageHeaderDataModel
import com.tokopedia.shop.pageheader.presentation.uimodel.component.ShopHeaderPlayWidgetButtonComponentUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopHeaderWidgetUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ShopHeaderPlayWidgetViewHolder(
    itemView: View,
    private val shopPageTrackingSGCPlayWidget: ShopPageTrackingSGCPlayWidget?,
    private val listener: Listener

) : AbstractViewHolder<ShopHeaderWidgetUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.layout_shop_header_play_widget
    }

    interface Listener {
        fun onStartLiveStreamingClicked(
            componentModel: ShopHeaderPlayWidgetButtonComponentUiModel,
            shopHeaderWidgetUiModel: ShopHeaderWidgetUiModel
        )

        fun onImpressionPlayWidgetComponent(
            componentModel: ShopHeaderPlayWidgetButtonComponentUiModel,
            shopHeaderWidgetUiModel: ShopHeaderWidgetUiModel
        )
    }

    private val viewBinding: LayoutShopHeaderPlayWidgetBinding? by viewBinding()
    private val playSgcWidgetContainer = viewBinding?.playSgcWidgetContainer
    private val playSgcLetsTryLiveTypography = viewBinding?.playSgcLetsTryLive
    private val playSgcBtnStartLiveLottieAnimationView = viewBinding?.playSgcBtnStartLive
    private val widgetPlayRootContainer: FrameLayout? = viewBinding?.widgetPlayRootContainer

    override fun bind(shopHeaderWidgetUiModel: ShopHeaderWidgetUiModel) {
        val modelComponent = shopHeaderWidgetUiModel.components.filterIsInstance<ShopHeaderPlayWidgetButtonComponentUiModel>().firstOrNull()
        modelComponent?.shopPageHeaderDataModel?.let { shopPageHeaderDataModel ->
            if (allowLiveStreaming(shopPageHeaderDataModel)) {
                showPlayWidget()
                setupTextContentSgcWidget()
                setLottieAnimationFromUrl(itemView.context.getString(R.string.shop_page_lottie_sgc_url))
                shopPageTrackingSGCPlayWidget?.onImpressionSGCContent(shopId = shopPageHeaderDataModel.shopId)
                playSgcBtnStartLiveLottieAnimationView?.setOnClickListener {
                    shopPageTrackingSGCPlayWidget?.onClickSGCContent(shopId = shopPageHeaderDataModel.shopId)
                    listener.onStartLiveStreamingClicked(
                        modelComponent,
                        shopHeaderWidgetUiModel
                    )
                }
            } else {
                hidePlayWidget()
            }
        }
    }

    private fun showPlayWidget() {
        widgetPlayRootContainer?.show()
        playSgcWidgetContainer?.show()
    }

    private fun hidePlayWidget() {
        widgetPlayRootContainer?.hide()
        playSgcWidgetContainer?.hide()
    }

    private fun allowLiveStreaming(dataModel: ShopPageHeaderDataModel): Boolean {
        return dataModel.broadcaster.streamAllowed && GlobalConfig.isSellerApp()
    }

    private fun setupTextContentSgcWidget() {
        if (playSgcLetsTryLiveTypography?.text?.isBlank() == true) playSgcLetsTryLiveTypography.text = MethodChecker.fromHtml(itemView.context.getString(R.string.shop_page_play_widget_title))
    }

    /**
     * Fetch the animation from http URL and play the animation
     */
    private fun setLottieAnimationFromUrl(animationUrl: String) {
        LottieCompositionFactory.fromUrl(itemView.context, animationUrl).addListener { result ->
            playSgcBtnStartLiveLottieAnimationView?.setComposition(result)
            playSgcBtnStartLiveLottieAnimationView?.playAnimation()
        }
    }
}
