package com.tokopedia.shop.pageheader.presentation.adapter.viewholder.widget

import android.view.View
import android.widget.FrameLayout
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieCompositionFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
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

    private val playWidgetContainer: CardView? = itemView.findViewById(R.id.play_seller_widget_container)
    private val shopPageSgcTitle: Typography? = itemView.findViewById(R.id.shop_page_sgc_title)
    private val containerLottie: FrameLayout? = itemView.findViewById(R.id.container_lottie)
    private val lottieAnimation: LottieAnimationView? = itemView.findViewById(R.id.lottie)
    private val widgetPlayRootContainer:  FrameLayout? = itemView.findViewById(R.id.widget_play_root_container)

    override fun bind(shopHeaderWidgetUiModel: ShopHeaderWidgetUiModel) {
        val modelComponent = shopHeaderWidgetUiModel.components.filterIsInstance<ShopHeaderPlayWidgetButtonComponentUiModel>().firstOrNull()
        modelComponent?.shopPageHeaderDataModel?.let { shopPageHeaderDataModel ->
            if (isShowPlayWidget(shopPageHeaderDataModel)) {
                showPlayWidget()
                setupTextContentSgcWidget()
                setLottieAnimationFromUrl(itemView.context.getString(R.string.shop_page_lottie_sgc_url))
                if (shopPageHeaderDataModel.broadcaster.streamAllowed) shopPageTrackingSGCPlayWidget?.onImpressionSGCContent(shopId = shopPageHeaderDataModel.shopId)
                containerLottie?.setOnClickListener {
                    shopPageTrackingSGCPlayWidget?.onClickSGCContent(shopId = shopPageHeaderDataModel.shopId)
                    listener.onStartLiveStreamingClicked(
                            modelComponent,
                            shopHeaderWidgetUiModel
                    )
                }
                playWidgetContainer?.addOnImpressionListener(modelComponent){
                    listener.onImpressionPlayWidgetComponent(
                            modelComponent,
                            shopHeaderWidgetUiModel
                    )
                }
            } else {
                hidePlayWidget()
            }
        }
    }

    private fun showPlayWidget(){
        widgetPlayRootContainer?.show()
        playWidgetContainer?.show()
    }

    private fun hidePlayWidget(){
        widgetPlayRootContainer?.hide()
        playWidgetContainer?.hide()
    }

    private fun isShowPlayWidget(shopPageHeaderDataModel: ShopPageHeaderDataModel) = shopPageHeaderDataModel.broadcaster.streamAllowed && GlobalConfig.isSellerApp()

    private fun setupTextContentSgcWidget() {
        if (shopPageSgcTitle?.text?.isBlank() == true) {
            val text = itemView.context.getString(R.string.shop_page_play_widget_title)
            shopPageSgcTitle.text = MethodChecker.fromHtml(text)
        }
    }

    /**
     * Fetch the animation from http URL and play the animation
     */
    private fun setLottieAnimationFromUrl(animationUrl: String) {
        itemView.context?.let {
            val lottieCompositionLottieTask = LottieCompositionFactory.fromUrl(it, animationUrl)

            lottieCompositionLottieTask.addListener { result ->
                lottieAnimation?.setComposition(result)
                lottieAnimation?.playAnimation()
            }

            lottieCompositionLottieTask.addFailureListener { }
        }
    }
}