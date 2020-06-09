package com.tokopedia.checkout.view.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection
import com.tokopedia.checkout.domain.model.cartshipmentform.CampaignTimerUi

class ExpiredTimeDialog : DialogFragment() {

    var analytics: CheckoutAnalyticsCourierSelection? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val timerModel: CampaignTimerUi = arguments?.getParcelable(ARGUMENT_CAMPAIGN_TIMER)
                ?: CampaignTimerUi()
        analytics?.eventViewCampaignDialog(timerModel.gtmProductId, timerModel.gtmUserId)
        return activity?.let {
            val dialogUnify = DialogUnify(it, DialogUnify.SINGLE_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(timerModel.dialogTitle)
                setDescription(timerModel.dialogDescription)
                setPrimaryCTAText(timerModel.dialogButton)
                setPrimaryCTAClickListener {
                    analytics?.eventClickBelanjaLagiOnDialog(timerModel.gtmProductId, timerModel.gtmUserId)
                    it.finish()
                }
                setCancelable(false)
                setOverlayClose(false)
            }
            dialogUnify
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    companion object {
        private const val ARGUMENT_CAMPAIGN_TIMER = "ARGUMENT_CAMPAIGN_TIMER"

        @JvmStatic
        fun newInstance(model: CampaignTimerUi, gtm: CheckoutAnalyticsCourierSelection)
                : ExpiredTimeDialog = ExpiredTimeDialog().apply {
            arguments = Bundle().apply {
                putParcelable(ARGUMENT_CAMPAIGN_TIMER, model)
            }
            isCancelable = false
            analytics = gtm
        }
    }

}