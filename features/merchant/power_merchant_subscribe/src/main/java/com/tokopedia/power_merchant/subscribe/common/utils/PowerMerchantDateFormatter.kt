package com.tokopedia.power_merchant.subscribe.common.utils

import android.content.Context
import android.text.SpannableString
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.power_merchant.subscribe.common.constant.Constant


object PowerMerchantDateFormatter {

    fun formatCancellationDate(
            context: Context,
            @StringRes warningTextFormat: Int,
            expiredDate: String
    ): SpannableString {
        val cancellationDate = DateFormatUtils.formatDate(Constant.DATE_FORMAT_EXPIRED,
                DateFormatUtils.FORMAT_D_MMMM_YYYY, expiredDate)
        val cancellationHour = DateFormatUtils.formatDate(Constant.DATE_FORMAT_EXPIRED,
                Constant.DATE_FORMAT_HH_MM, expiredDate)
        val warningText = context.getString(warningTextFormat, cancellationDate, cancellationHour)
        val highlightTextColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700)

        return PowerMerchantSpannableUtil.createSpannableString(warningText, cancellationDate, highlightTextColor, true)
    }
}