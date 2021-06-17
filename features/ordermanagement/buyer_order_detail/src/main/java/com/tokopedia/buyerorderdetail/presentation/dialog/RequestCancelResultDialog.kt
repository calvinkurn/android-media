package com.tokopedia.buyerorderdetail.presentation.dialog

import android.content.Context
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailMiscConstant
import com.tokopedia.buyerorderdetail.common.utils.BuyerOrderDetailNavigator
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.url.TokopediaUrl

class RequestCancelResultDialog(
        private val navigator: BuyerOrderDetailNavigator
) {
    private var dialog: DialogUnify? = null
    private var title: String = ""
    private var body: String = ""

    private fun initDialog(context: Context): DialogUnify {
        return DialogUnify(context, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE).setup()
    }

    private fun DialogUnify.setup(): DialogUnify {
        setTitle(title)
        setDescription(body)
        setPrimaryCTAText(context.getString(R.string.label_understand))
        setPrimaryCTAClickListener {
            dismiss()
        }
        setSecondaryCTAText(context.getString(R.string.label_contact_help_center))
        setSecondaryCTAClickListener {
            dismiss()
            goToTokopediaCareWebview()
        }
        return this
    }

    private fun goToTokopediaCareWebview() {
        navigator.openWebView(TokopediaUrl.getInstance().MOBILEWEB + BuyerOrderDetailMiscConstant.PATH_TOKOPEDIA_CARE)
    }

    fun show(context: Context) {
        dialog = dialog?.setup() ?: initDialog(context)
        dialog?.show()
    }

    fun setTitle(title: String) {
        this.title = title
    }

    fun setBody(body: String) {
        this.body = body
    }
}