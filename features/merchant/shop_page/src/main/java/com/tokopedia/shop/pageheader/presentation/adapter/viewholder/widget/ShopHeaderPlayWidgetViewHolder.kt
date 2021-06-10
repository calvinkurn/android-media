package com.tokopedia.shop.pageheader.presentation.adapter.viewholder.widget

import android.view.View
import android.widget.FrameLayout
import androidx.cardview.widget.CardView
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieCompositionFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.shop.R
import com.tokopedia.shop.analytic.ShopPageTrackingSGCPlayWidget
import com.tokopedia.shop.pageheader.data.model.ShopPageHeaderDataModel
import com.tokopedia.shop.pageheader.presentation.uimodel.component.ShopHeaderPlayWidgetButtonComponentUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopHeaderWidgetUiModel
import com.tokopedia.unifyprinciples.Typography

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


    private val playSgcWidgetContainer = itemView.findViewById<CardView>(R.id.play_sgc_widget_container)
    private val playSgcLetsTryLiveTypography = itemView.findViewById<Typography>(R.id.play_sgc_lets_try_live)
    private val playSgcBtnStartLiveLottieAnimationView = itemView.findViewById<LottieAnimationView>(R.id.play_sgc_btn_start_live)
    private val widgetPlayRootContainer: FrameLayout? = itemView.findViewById(R.id.widget_play_root_container)

    override fun bind(shopHeaderWidgetUiModel: ShopHeaderWidgetUiModel) {
        val modelComponent = shopHeaderWidgetUiModel.components.filterIsInstance<ShopHeaderPlayWidgetButtonComponentUiModel>().firstOrNull()
        modelComponent?.shopPageHeaderDataModel?.let { shopPageHeaderDataModel ->
            if (allowLiveStreaming(shopPageHeaderDataModel)) {
                showPlayWidget()
                setupTextContentSgcWidget()
                setLottieAnimationFromUrl(itemView.context.getString(R.string.shop_page_lottie_sgc_url))
                shopPageTrackingSGCPlayWidget?.onImpressionSGCContent(shopId = shopPageHeaderDataModel.shopId)
                playSgcBtnStartLiveLottieAnimationView.setOnClickListener {
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

    private fun allowLiveStreaming(dataModel: ShopPageHeaderDataModel) = dataModel.broadcaster.streamAllowed && GlobalConfig.isSellerApp()

    private fun setupTextContentSgcWidget() {
        if (playSgcLetsTryLiveTypography.text.isBlank()) playSgcLetsTryLiveTypography.text = MethodChecker.fromHtml(itemView.context.getString(R.string.shop_page_play_widget_title))
    }

    /**
     * Fetch the animation from http URL and play the animation
     */
    private fun setLottieAnimationFromUrl(animationUrl: String) {
        LottieCompositionFactory.fromUrl(itemView.context, animationUrl).addListener { result ->
            playSgcBtnStartLiveLottieAnimationView.setComposition(result)
            playSgcBtnStartLiveLottieAnimationView.playAnimation()
        }
    }
}