package com.tokopedia.content.product.picker.seller.util

import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.content.product.picker.R
import java.util.*

/**
 * Created By : Jonathan Darwin on September 26, 2023
 */
internal fun GlobalError.productTagSummaryEmpty(onAction: () -> Unit) {
    errorIllustration.setImageResource(R.drawable.ic_empty_product_etalase)
    errorTitle.text = context.getString(R.string.product_summary_empty_title)
    errorDescription.text = context.getString(R.string.product_summary_empty_description)
    errorAction.text = context.getString(R.string.product_summary_empty_action_text)
    errorSecondaryAction.gone()
    setActionClickListener { onAction() }
}

internal fun Long.forceToUTCWithoutTimezone(): Date {
    val calendar = Calendar.getInstance()
    calendar.time = Date(this)
    calendar.add(Calendar.MILLISECOND, TimeZone.getDefault().rawOffset * -1)
    return calendar.time
}
