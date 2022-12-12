package com.tokopedia.tkpd.flashsale.common.dialog

import android.content.Context
import android.view.LayoutInflater
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.loaderdialog.LoaderDialog
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsDialogProductSseSubmissionProgressBinding

class FlashSaleProductSseSubmissionProgressDialog(private val context: Context) {
    private var loaderDialogProductSubmitSse: LoaderDialog? = null
    private var dialogProductSubmissionSseBinding: StfsDialogProductSseSubmissionProgressBinding? = null

    init {
        loaderDialogProductSubmitSse = LoaderDialog(context)
    }

    fun isShowing(): Boolean {
        return loaderDialogProductSubmitSse?.dialog?.isShowing.orFalse()
    }

    fun show(){
        if (!isShowing()) {
            loaderDialogProductSubmitSse?.apply {
                dialogProductSubmissionSseBinding =
                    StfsDialogProductSseSubmissionProgressBinding.inflate(LayoutInflater.from(context))
                customView = dialogProductSubmissionSseBinding?.root
                setLoadingText("")
                dialog.setCancelable(false)
                dialog.setOverlayClose(false)
                show()
            }
        }
    }

    fun hide(){
        loaderDialogProductSubmitSse?.dismiss()
    }

    fun updateData(currentProcessedProduct: Int, totalProduct: Int) {
        dialogProductSubmissionSseBinding?.typographyDescription?.text = context.getString(
            R.string.stfs_product_sse_submission_progress_dialog_description,
            currentProcessedProduct,
            totalProduct
        )
    }

}
