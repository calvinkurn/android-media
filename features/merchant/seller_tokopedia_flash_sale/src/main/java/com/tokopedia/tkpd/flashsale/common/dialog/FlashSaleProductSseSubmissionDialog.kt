package com.tokopedia.tkpd.flashsale.common.dialog

import android.content.Context
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.seller_tokopedia_flash_sale.R

class FlashSaleProductSseSubmissionDialog(context: Context) {
    private val dialog =
        DialogUnify(context, DialogUnify.SINGLE_ACTION, DialogUnify.WITH_ILLUSTRATION)

    fun show(title: String, onPrimaryCtaClicked: () -> Unit) = with(dialog) {
        setCancelable(false)
        setOverlayClose(false)
        setImageUrl("https://images.tokopedia.net/img/android/campaign/fs-tkpd/ic_ineligible_access_fs_tokopedia.png")
        setTitle(title)
        setDescription(context.getString(R.string.stfs_dialog_error_product_submission_sse_error_description))
        setPrimaryCTAText(context.getString(R.string.stfs_dialog_error_product_submission_sse_error_cta))
        setPrimaryCTAClickListener {
            dismiss()
            onPrimaryCtaClicked()
        }
        show()
    }
}
