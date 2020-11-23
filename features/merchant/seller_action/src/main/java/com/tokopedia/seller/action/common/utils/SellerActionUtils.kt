package com.tokopedia.seller.action.common.utils

import android.content.Context
import android.graphics.Bitmap
import android.text.format.DateUtils
import com.bumptech.glide.Glide
import com.tokopedia.kotlin.extensions.convertToDate
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.seller.action.R
import com.tokopedia.seller.action.common.const.SellerActionConst
import java.util.*

object SellerActionUtils {

    internal fun String.isOrderDateToday(): Boolean {
        return try {
            DateUtils.isToday(this.convertToDate(SellerActionConst.SLICE_DATE_FORMAT).time)
        } catch (ex: Exception) {
            false
        }
    }

    internal fun String?.convertToOrderDateTitle(context: Context): String {
        return if (this == null || this.isOrderDateToday()) {
            context.getString(R.string.seller_action_order_success_title_today)
        } else {
            val formattedDate = convertToDate(SellerActionConst.SLICE_DATE_FORMAT)
                    .toFormattedString(SellerActionConst.SLICE_FULL_DATE_FORMAT, Locale("in", "ID"))
            context.getString(R.string.seller_action_order_success_title_specific_date, formattedDate)
        }
    }

    internal fun String.getBitmap(context: Context): Bitmap? =
        Glide.with(context)
                .asBitmap()
                .placeholder(R.drawable.ic_sellerapp_slice)
                .load(this)
                .submit()
                .get()

}