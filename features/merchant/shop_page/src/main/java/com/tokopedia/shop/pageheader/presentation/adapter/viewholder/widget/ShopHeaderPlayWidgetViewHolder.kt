package com.tokopedia.shop.pageheader.presentation.adapter.viewholder.widget

import android.text.Spannable
import android.text.SpannableString
import android.view.View
import android.widget.FrameLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.shop.R
import com.tokopedia.shop.analytic.ShopPageTrackingSGCPlayWidget
import com.tokopedia.shop.common.graphql.data.shopinfo.Broadcaster
import com.tokopedia.shop.databinding.LayoutShopHeaderPlayWidgetBinding
import com.tokopedia.shop.pageheader.data.model.ShopPageHeaderDataModel
import com.tokopedia.shop.pageheader.presentation.customview.CenteredImageSpan
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
                shopHeaderWidgetUiModel: ShopHeaderWidgetUiModel,
                broadcasterConfig: Broadcaster.Config,
        )

        fun onImpressionPlayWidgetComponent(
            componentModel: ShopHeaderPlayWidgetButtonComponentUiModel,
            shopHeaderWidgetUiModel: ShopHeaderWidgetUiModel
        )
    }

    private val viewBinding: LayoutShopHeaderPlayWidgetBinding? by viewBinding()
    private val playSgcWidgetContainer = viewBinding?.playSgcWidgetContainer
    private val tvStartCreateContentDesc = viewBinding?.tvStartCreateContentDesc
    private val playSgcBtnStartLive = viewBinding?.playSgcBtnStartLive
    private val widgetPlayRootContainer: FrameLayout? = viewBinding?.widgetPlayRootContainer

    override fun bind(shopHeaderWidgetUiModel: ShopHeaderWidgetUiModel) {
        viewBinding?.tvStartCreateContent?.setCompoundDrawablesWithIntrinsicBounds(
            MethodChecker.getDrawable(itemView.context, R.drawable.ic_content_creation),
            null,
            null,
            null
        )

        val modelComponent = shopHeaderWidgetUiModel.components.filterIsInstance<ShopHeaderPlayWidgetButtonComponentUiModel>().firstOrNull()
        modelComponent?.shopPageHeaderDataModel?.let { shopPageHeaderDataModel ->
            if (allowContentCreation(shopPageHeaderDataModel)) {
                showPlayWidget()
                setupTextContentSgcWidget(shopPageHeaderDataModel)
                shopPageTrackingSGCPlayWidget?.onImpressionSGCContent(shopId = shopPageHeaderDataModel.shopId)
                playSgcBtnStartLive?.setOnClickListener {
                    shopPageTrackingSGCPlayWidget?.onClickSGCContent(shopId = shopPageHeaderDataModel.shopId)
                    listener.onStartLiveStreamingClicked(
                            modelComponent,
                            shopHeaderWidgetUiModel,
                            shopPageHeaderDataModel.broadcaster
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

    private fun allowContentCreation(dataModel: ShopPageHeaderDataModel): Boolean {
        return (isStreamAllowed(dataModel) || isShortsVideoAllowed(dataModel)) && GlobalConfig.isSellerApp()
    }

    private fun setupTextContentSgcWidget(dataModel: ShopPageHeaderDataModel) {
        if(tvStartCreateContentDesc?.text?.isNotBlank() == true) return

        val betaTemplate = getString(R.string.shop_page_play_widget_beta_template)

        val imgBeta = MethodChecker.getDrawable(itemView.context, R.drawable.ic_play_beta_badge)?.apply {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
        }
        val imgBetaSpan = imgBeta?.let { CenteredImageSpan(it) }

        val span = SpannableString(
            MethodChecker.fromHtml(
                when {
                    isStreamAllowed(dataModel) && isShortsVideoAllowed(dataModel) -> {
                        getString(R.string.shop_page_play_widget_livestream_and_shorts_label)
                    }
                    isStreamAllowed(dataModel) -> {
                        getString(R.string.shop_page_play_widget_livestream_only_label)
                    }
                    isShortsVideoAllowed(dataModel) -> {
                        getString(R.string.shop_page_play_widget_shorts_only_label)
                    }
                    else -> {
                        ""
                    }
                }
            )
        )

        span.setSpan(
            imgBetaSpan,
            span.indexOf(betaTemplate),
            span.indexOf(betaTemplate) + betaTemplate.length,
            Spannable.SPAN_INCLUSIVE_EXCLUSIVE
        )

        tvStartCreateContentDesc?.text = span
    }

    private fun isStreamAllowed(dataModel: ShopPageHeaderDataModel): Boolean {
        return dataModel.broadcaster.streamAllowed
    }

    private fun isShortsVideoAllowed(dataModel: ShopPageHeaderDataModel): Boolean {
        return dataModel.broadcaster.shortVideoAllowed
    }
}
