package com.tokopedia.salam.umrah.common.util

import android.content.res.Resources
import android.text.SpannableString
import android.text.style.StrikethroughSpan
import com.tokopedia.salam.umrah.R

object UmrahPriceUtil {
    fun getSlashedPrice(resources: Resources, slashPrice: Int): CharSequence? {
        val formattedSlashPrice = CurrencyFormatter.getRupiahFormat(slashPrice)
        val text = SpannableString(resources.getString(R.string.umrah_common_start_from, formattedSlashPrice))
        text.setSpan(StrikethroughSpan(), 11, formattedSlashPrice.length + 11, 0)

        return if(slashPrice>0) text else resources.getString(R.string.umrah_common_start_from_non_currency)
    }
}