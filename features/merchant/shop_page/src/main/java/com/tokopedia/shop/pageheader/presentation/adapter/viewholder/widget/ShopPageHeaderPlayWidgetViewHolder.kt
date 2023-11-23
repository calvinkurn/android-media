package com.tokopedia.shop.pageheader.presentation.adapter.viewholder.widget

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.creation.common.presentation.bottomsheet.ContentCreationBottomSheet
import com.tokopedia.creation.common.presentation.customviews.ContentCreationEntryPointWidget
import com.tokopedia.creation.common.presentation.model.ContentCreationEntryPointSource
import com.tokopedia.shop.R
import com.tokopedia.shop.analytic.ShopPageTrackingSGCPlayWidget
import com.tokopedia.shop.databinding.LayoutShopHeaderPlayWidgetBinding
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
        )

        fun onImpressionPlayWidgetComponent(
            componentModel: ShopPageHeaderPlayWidgetButtonComponentUiModel,
            shopPageHeaderWidgetUiModel: ShopPageHeaderWidgetUiModel
        )

        fun getContentCreationListener(): ContentCreationBottomSheet.Listener
    }

    private val viewBinding: LayoutShopHeaderPlayWidgetBinding? by viewBinding()
    private val widgetPlayRootContainer: ContentCreationEntryPointWidget? = viewBinding?.root

    override fun bind(shopPageHeaderWidgetUiModel: ShopPageHeaderWidgetUiModel) {
        val modelComponent =
            shopPageHeaderWidgetUiModel.componentPages.filterIsInstance<ShopPageHeaderPlayWidgetButtonComponentUiModel>()
                .firstOrNull()

        modelComponent?.let {
            widgetPlayRootContainer?.widgetSource = ContentCreationEntryPointSource.Shop
            widgetPlayRootContainer?.creationBottomSheetListener =
                listener.getContentCreationListener()
            widgetPlayRootContainer?.onClickListener = {
                shopPageTrackingSGCPlayWidget?.onClickSGCContent(shopId = it.shopPageHeaderDataModel?.shopId.orEmpty())
                listener.onStartLiveStreamingClicked(
                    modelComponent,
                    shopPageHeaderWidgetUiModel
                )
            }
            widgetPlayRootContainer?.fetchConfig()

            shopPageTrackingSGCPlayWidget?.onImpressionSGCContent(shopId = it.shopPageHeaderDataModel?.shopId.orEmpty())
        }
    }
}
