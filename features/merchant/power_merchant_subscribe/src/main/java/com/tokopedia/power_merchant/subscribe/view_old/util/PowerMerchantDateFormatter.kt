package com.tokopedia.power_merchant.subscribe.view_old.util

import android.content.Context
import android.text.SpannableString
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.power_merchant.subscribe.view_old.constant.PowerMerchantDate
import com.tokopedia.power_merchant.subscribe.view_old.util.PowerMerchantSpannableUtil.createSpannableString

object PowerMerchantDateFormatter {

    fun formatCancellationDate(
        context: Context,
        @StringRes warningTextFormat: Int,
        expiredDate: String
    ): SpannableString {
        val cancellationDate = DateFormatUtils.formatDate(PowerMerchantDate.EXPIRED_DATE_FORMAT,
            DateFormatUtils.FORMAT_D_MMMM_YYYY, expiredDate)
        val cancellationHour = DateFormatUtils.formatDate(PowerMerchantDate.EXPIRED_DATE_FORMAT,
            PowerMerchantDate.DATE_FORMAT_HH_MM, expiredDate)
        val warningText = context.getString(warningTextFormat, cancellationDate, cancellationHour)
        val highlightTextColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700)

        return createSpannableString(warningText, cancellationDate, highlightTextColor, true)
    }
}