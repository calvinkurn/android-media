package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view.model.WidgetQuitSubmissionUiModel
import kotlinx.android.synthetic.main.widget_pm_quit_submission.view.*

/**
 * Created By @ilhamsuaib on 04/03/21
 */

class QuitSubmissionWidget(itemView: View) : AbstractViewHolder<WidgetQuitSubmissionUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.widget_pm_quit_submission
    }

    override fun bind(element: WidgetQuitSubmissionUiModel) {
        with(itemView) {
            tvPmQuitPmMessage.text = getString(R.string.pm_deactivate_power_merchant_message_info)
        }
    }
}