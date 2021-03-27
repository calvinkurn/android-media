package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.view.View
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view.model.WidgetErrorStateUiModel

/**
 * Created By @ilhamsuaib on 27/03/21
 */

class ErrorStateWidget(itemView: View) : BaseViewHolder<WidgetErrorStateUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.widget_pm_error_state
    }

    override fun bind(element: WidgetErrorStateUiModel) {}
}