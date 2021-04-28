package com.tokopedia.sellerorder.list.presentation.dialogs

import android.content.Context
import android.view.View
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.sellerorder.analytics.SomAnalytics
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.dialog_multi_print_awb.*

class SomListBulkPrintDialog(context: Context) {

    private var dialogUnify: DialogUnify? = null
    private var childViews: View? = null
    private var listener: SomListBulkPrintDialogClickListener? = null

    init {
        childViews = View.inflate(context, com.tokopedia.sellerorder.R.layout.dialog_multi_print_awb, null)
        dialogUnify = DialogUnify(context, DialogUnify.SINGLE_ACTION, DialogUnify.NO_IMAGE).apply {
            dialogPrimaryCTA.gone()
            dialogSecondaryCTA.gone()
            dialogSecondaryLongCTA.gone()
            dialogCTAContainer.gone()
            setUnlockVersion()
            setOverlayClose(false)
            setCancelable(false)
            setChild(childViews)
            setOnShowListener {
                btnSomListBulkPrintDialogPrimaryButton?.setOnClickListener {
                    listener?.onPrintButtonClicked(cbMarkAsPrinted?.isChecked ?: false)
                    dismiss()
                }
                btnSomListBulkPrintDialogSecondaryButton?.setOnClickListener {
                    val userSession = UserSession(context)
                    SomAnalytics.eventClickCancelOnBulkPrintAwb(userSession.userId)
                    dismiss()
                }
            }
        }
    }

    fun setTitle(title: String) {
        childViews?.findViewById<Typography>(com.tokopedia.sellerorder.R.id.tvSomListBulkPrintDialogTitle)?.text = title
    }

    fun setListener(listener: SomListBulkPrintDialogClickListener) {
        this.listener = listener
    }

    fun show() {
        dialogUnify?.show()
    }

    interface SomListBulkPrintDialogClickListener {
        fun onPrintButtonClicked(markAsPrinted: Boolean)
    }
}