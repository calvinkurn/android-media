package com.tokopedia.seller.action.common.utils

import android.text.format.DateUtils
import com.tokopedia.kotlin.extensions.convertToDate
import com.tokopedia.seller.action.common.const.SellerActionConst

object SellerActionUtils {

    internal fun String.isOrderDateToday(): Boolean {
        return try {
            DateUtils.isToday(this.convertToDate(SellerActionConst.DATE_FORMAT).time)
        } catch (ex: Exception) {
            false
        }
    }

}