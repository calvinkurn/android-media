package com.tokopedia.seller.action.common.utils

import android.content.Context
import android.graphics.Bitmap
import android.text.format.DateUtils
import com.bumptech.glide.Glide
import com.tokopedia.kotlin.extensions.convertToDate
import com.tokopedia.seller.action.R
import com.tokopedia.seller.action.common.const.SellerActionConst

object SellerActionUtils {

    internal fun String.isOrderDateToday(): Boolean {
        return try {
            DateUtils.isToday(this.convertToDate(SellerActionConst.SLICE_DATE_FORMAT).time)
        } catch (ex: Exception) {
            false
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