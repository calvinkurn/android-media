package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.databinding.WidgetPmCancelDeactivationSubmissionBinding
import com.tokopedia.power_merchant.subscribe.view.model.WidgetCancelDeactivationSubmissionUiModel
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created By @ilhamsuaib on 04/03/21
 */

class CancelDeactivationSubmissionWidget(
    itemView: View,
    private val listener: Listener
) : AbstractViewHolder<WidgetCancelDeactivationSubmissionUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.widget_pm_cancel_deactivation_submission
    }

    private val binding: WidgetPmCancelDeactivationSubmissionBinding? by viewBinding()

    override fun bind(element: WidgetCancelDeactivationSubmissionUiModel) {
        binding?.run {
            tvPmQuitPmMessage.text = root.context.getString(
                R.string.pm_deactivate_power_merchant_pro_message_info,
                element.deactivatedStatusName,
                element.expiredTime
            ).parseAsHtml()

            btnPmCancelQuitSubmission.isLoading = false
            btnPmCancelQuitSubmission.setOnClickListener {
                listener.cancelPmDeactivationSubmission(adapterPosition)
                btnPmCancelQuitSubmission.isLoading = true
            }
        }
    }

    interface Listener {
        fun cancelPmDeactivationSubmission(position: Int)
    }
}