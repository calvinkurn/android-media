package com.tokopedia.purchase_platform.features.checkout.view.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.purchase_platform.features.checkout.domain.model.cartshipmentform.CampaignTimerUi

class ExpiredTimeDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val dialogUnify = DialogUnify(it, DialogUnify.SINGLE_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle("Waktu Pembayaran Habis")
                setDescription("bla bla bafdjlasfj badslfjsladfj sd")
                setPrimaryCTAText("Belanja Lagi")
                setPrimaryCTAClickListener {
                    it.finish()
                }
                setCancelable(false)
                setOverlayClose(false)
            }
            dialogUnify
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    companion object {
        @JvmStatic
        fun newInstance(model: CampaignTimerUi): ExpiredTimeDialog = ExpiredTimeDialog().apply {
            arguments = Bundle().apply {
                putParcelable("ARGUMENT_CAMPAIGN_TIMER", model)
            }
            isCancelable = false
        }
    }

}