package com.tokopedia.purchase_platform.features.checkout.view

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.tokopedia.dialog.DialogUnify

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

}