package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view.model.WidgetLoadingStateUiModel

/**
 * Created By @ilhamsuaib on 27/03/21
 */

class LoadingStateWidget(itemView: View) : AbstractViewHolder<WidgetLoadingStateUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.widget_pm_loading_state
    }

    override fun bind(element: WidgetLoadingStateUiModel) {}
}