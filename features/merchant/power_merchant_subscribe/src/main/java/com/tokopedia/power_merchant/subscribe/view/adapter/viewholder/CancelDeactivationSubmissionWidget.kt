package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.tracking.PowerMerchantTracking
import com.tokopedia.power_merchant.subscribe.view.model.WidgetCancelDeactivationSubmissionUiModel
import kotlinx.android.synthetic.main.widget_pm_cancel_deactivation_submission.view.*

/**
 * Created By @ilhamsuaib on 04/03/21
 */

class CancelDeactivationSubmissionWidget(
        itemView: View,
        private val listener: Listener,
        private val powerMerchantTracking: PowerMerchantTracking
) : AbstractViewHolder<WidgetCancelDeactivationSubmissionUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.widget_pm_cancel_deactivation_submission
    }

    override fun bind(element: WidgetCancelDeactivationSubmissionUiModel) {
        with(itemView) {
            tvPmQuitPmMessage.text = getString(R.string.pm_deactivate_power_merchant_message_info, element.expiredTime).parseAsHtml()

            btnPmCancelQuitSubmission.isLoading = false
            btnPmCancelQuitSubmission.setOnClickListener {
                listener.cancelPmDeactivationSubmission(adapterPosition)
                btnPmCancelQuitSubmission.isLoading = true
                powerMerchantTracking.sendEventClickCancelOptOutPowerMerchant()
            }
        }
    }

    interface Listener {
        fun cancelPmDeactivationSubmission(position: Int)
    }
}