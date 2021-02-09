package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.shop.R
import com.tokopedia.shop.common.constant.ShopPagePerformanceConstant.SHOP_HOME_IMAGE_MULTIPLE_COLUMN_TRACE
import com.tokopedia.shop.home.view.listener.ShopHomeDisplayWidgetListener
import com.tokopedia.shop.home.view.model.ShopHomeDisplayWidgetUiModel
import kotlinx.android.synthetic.main.layout_shop_home_campaign_npl_tnc_item.view.*


class ShopHomeCampaignNplTncItemViewHolder(
        itemView: View
) : RecyclerView.ViewHolder(itemView) {

    fun bind(position: Int, message: String) {
        itemView.tv_message_counter?.text = position.plus(1).toString()
        itemView.tv_message?.text = message
    }
}