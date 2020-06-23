package com.tokopedia.power_merchant.subscribe.view.util

import android.content.Context
import android.text.SpannableString
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view.constant.PowerMerchantDate
import com.tokopedia.power_merchant.subscribe.view.util.PowerMerchantSpannableUtil.createSpannableString

object PowerMerchantDateFormatter {

    fun formatCancellationDate(
        context: Context,
        warningTextFormat: Int,
        expiredDate: String
    ): SpannableString {
        val cancellationDate = DateFormatUtils.formatDate(PowerMerchantDate.EXPIRED_DATE_FORMAT,
            DateFormatUtils.FORMAT_D_MMMM_YYYY, expiredDate)
        val cancellationHour = DateFormatUtils.formatDate(PowerMerchantDate.EXPIRED_DATE_FORMAT,
            PowerMerchantDate.DATE_FORMAT_HH_MM, expiredDate)
        val warningText = context.getString(warningTextFormat, cancellationDate, cancellationHour)
        val highlightTextColor = ContextCompat.getColor(context, R.color.light_N700)

        return createSpannableString(warningText, cancellationDate, highlightTextColor, true)
    }
}