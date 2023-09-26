package com.tokopedia.content.product.picker.sgc.util

import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.content.product.picker.R

/**
 * Created By : Jonathan Darwin on September 26, 2023
 */
fun GlobalError.productTagSummaryEmpty(onAction: () -> Unit) {
    errorIllustration.setImageResource(R.drawable.ic_empty_product_etalase)
    errorTitle.text = context.getString(R.string.play_bro_product_summary_empty_title)
    errorDescription.text = context.getString(R.string.play_bro_product_summary_empty_description)
    errorAction.text = context.getString(R.string.play_bro_product_summary_empty_action_text)
    errorSecondaryAction.gone()
    setActionClickListener { onAction() }
}
