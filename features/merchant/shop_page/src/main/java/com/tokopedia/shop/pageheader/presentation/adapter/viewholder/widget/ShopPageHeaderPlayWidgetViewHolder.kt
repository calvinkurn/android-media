package com.tokopedia.shop.pageheader.presentation.adapter.viewholder.widget

import android.view.View
import android.widget.FrameLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.shop.R
import com.tokopedia.shop.analytic.ShopPageTrackingSGCPlayWidget
import com.tokopedia.shop.common.graphql.data.shopinfo.Broadcaster
import com.tokopedia.shop.databinding.LayoutShopHeaderPlayWidgetBinding
import com.tokopedia.shop.pageheader.data.model.ShopPageHeaderDataModel
import com.tokopedia.shop.pageheader.presentation.uimodel.component.ShopPageHeaderPlayWidgetButtonComponentUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopPageHeaderWidgetUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ShopPageHeaderPlayWidgetViewHolder(
    itemView: View,
    private val shopPageTrackingSGCPlayWidget: ShopPageTrackingSGCPlayWidget?,
    private val listener: Listener

) : AbstractViewHolder<ShopPageHeaderWidgetUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.layout_shop_header_play_widget
    }

    interface Listener {
        fun onStartLiveStreamingClicked(
            componentModel: ShopPageHeaderPlayWidgetButtonComponentUiModel,
            shopPageHeaderWidgetUiModel: ShopPageHeaderWidgetUiModel,
            broadcasterConfig: Broadcaster.Config
        )

        fun onImpressionPlayWidgetComponent(
            componentModel: ShopPageHeaderPlayWidgetButtonComponentUiModel,
            shopPageHeaderWidgetUiModel: ShopPageHeaderWidgetUiModel
        )
    }

    private val viewBinding: LayoutShopHeaderPlayWidgetBinding? by viewBinding()
    private val playSgcWidgetContainer = viewBinding?.playSgcWidgetContainer
    private val tvStartCreateContentDesc = viewBinding?.tvStartCreateContentDesc
    private val playSgcBtnStartLive = viewBinding?.playSgcBtnStartLive
    private val widgetPlayRootContainer: FrameLayout? = viewBinding?.widgetPlayRootContainer

    override fun bind(shopPageHeaderWidgetUiModel: ShopPageHeaderWidgetUiModel) {
        val modelComponent = shopPageHeaderWidgetUiModel.componentPages.filterIsInstance<ShopPageHeaderPlayWidgetButtonComponentUiModel>().firstOrNull()
        modelComponent?.shopPageHeaderDataModel?.let { shopPageHeaderDataModel ->
            if (allowContentCreation(shopPageHeaderDataModel)) {
                showPlayWidget()
                setupTextContentSgcWidget()
                shopPageTrackingSGCPlayWidget?.onImpressionSGCContent(shopId = shopPageHeaderDataModel.shopId)
                playSgcBtnStartLive?.setOnClickListener {
                    shopPageTrackingSGCPlayWidget?.onClickSGCContent(shopId = shopPageHeaderDataModel.shopId)
                    listener.onStartLiveStreamingClicked(
                        modelComponent,
                        shopPageHeaderWidgetUiModel,
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
        return (isStreamAllowed(dataModel) || isShortsVideoAllowed(dataModel))
    }

    private fun setupTextContentSgcWidget() {
        if (tvStartCreateContentDesc?.text?.isNotBlank() == true) return
        tvStartCreateContentDesc?.text = MethodChecker.fromHtml(getString(R.string.shop_page_play_widget_desription))
    }

    private fun isStreamAllowed(dataModel: ShopPageHeaderDataModel): Boolean {
        return dataModel.broadcaster.streamAllowed
    }

    private fun isShortsVideoAllowed(dataModel: ShopPageHeaderDataModel): Boolean {
        return dataModel.broadcaster.shortVideoAllowed
    }
}
