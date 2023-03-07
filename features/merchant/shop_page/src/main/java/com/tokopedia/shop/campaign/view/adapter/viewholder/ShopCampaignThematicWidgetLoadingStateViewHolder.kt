package com.tokopedia.shop.campaign.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.R
import com.tokopedia.shop_widget.thematicwidget.uimodel.ThematicWidgetUiModel

class ShopCampaignThematicWidgetLoadingStateViewHolder(
    itemView: View
) : AbstractViewHolder<ThematicWidgetUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_campaign_thematic_widget_loading_state
    }

    override fun bind(element: ThematicWidgetUiModel) {
        /* nothing to do */
    }
}
