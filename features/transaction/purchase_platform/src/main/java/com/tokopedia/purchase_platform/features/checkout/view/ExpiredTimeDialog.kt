package com.tokopedia.purchase_platform.features.checkout.view

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.tokopedia.dialog.DialogUnify

class ExpiredTimeDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogUnify = DialogUnify(context!!, DialogUnify.SINGLE_ACTION, DialogUnify.NO_IMAGE)
        dialogUnify.setTitle("Waktu Pembayaran Habis")
        dialogUnify.setDescription("bla bla bafdjlasfj badslfjsladfj sd")
        dialogUnify.setPrimaryCTAText("Belanja Lagi")
        dialogUnify.setPrimaryCTAClickListener {
            if (activity != null) {
                activity!!.finish()
            }
        }
        dialogUnify.setCancelable(false)
        dialogUnify.setOverlayClose(false)
        return dialogUnify
    }

}