package com.tokopedia.buyerorderdetail.presentation.adapter.callback

import android.content.Context
import com.tokopedia.applink.RouteManager
import com.tokopedia.scp_rewards_touchpoints.touchpoints.adapter.viewholder.ScpRewardsMedalTouchPointWidgetViewHolder

class ScpRewardsMedalTouchPointWidgetCallback: ScpRewardsMedalTouchPointWidgetViewHolder.ScpRewardsMedalTouchPointWidgetListener {
    override fun onClickWidgetListener(
        context: Context,
        appLink: String
    ) {
        RouteManager.route(context, appLink)
    }
}
