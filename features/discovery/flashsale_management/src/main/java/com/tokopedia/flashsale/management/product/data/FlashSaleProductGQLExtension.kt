package com.tokopedia.flashsale.management.product.data

import android.content.Context
import com.tokopedia.flashsale.management.R
import com.tokopedia.flashsale.management.data.FlashSaleAdminStatusIdTypeDef
import com.tokopedia.flashsale.management.data.FlashSaleProductActionTypeDef
import com.tokopedia.flashsale.management.data.FlashSaleProductStatusTypeDef
import com.tokopedia.flashsale.management.data.FlashSaleProductStatusTypeDef.NOTHING

/**
 * Created by hendry on 14/11/18.
 */

fun FlashSaleProductItem.getDepartmentNameString() = getProductDepartmentName().map { it }.joinToString(" > ")

fun Int.getProductStatusString(context: Context): String {
    return when (this) {
        NOTHING -> ""
        FlashSaleProductStatusTypeDef.SUBMITTED -> context.getString(R.string.flash_sale_registered)
        FlashSaleProductStatusTypeDef.REJECTED -> context.getString(R.string.flash_sale_rejected)
        FlashSaleProductStatusTypeDef.RESERVE -> context.getString(R.string.flash_sale_reserve)
        FlashSaleProductStatusTypeDef.SUBMIT_CANCEL -> context.getString(R.string.flash_sale_canceled)
        FlashSaleProductStatusTypeDef.SUBMIT_CANCEL_SUBMIT -> ""
        else -> ""
    }
}

fun Int.getAdminStatusStringAfterReview(context: Context): String {
    return when (this) {
        FlashSaleAdminStatusIdTypeDef.NAKAMA_ACCEPTED -> context.getString(R.string.flash_sale_accepted)
        FlashSaleAdminStatusIdTypeDef.NOT_REVIEWED, // Yes, this status will become rejected, because "inReview" phase has over.
        FlashSaleAdminStatusIdTypeDef.NAKAMA_REJECTED,
        FlashSaleAdminStatusIdTypeDef.SYSTEM_TAKEOUT,
        FlashSaleAdminStatusIdTypeDef.NAKAMA_TAKEOUT -> context.getString(R.string.flash_sale_rejected)
        else -> ""
    }
}

fun FlashSaleProductItemCampaign.getProductStatusActionString(context: Context): String {
    return when (productStatus) {
        FlashSaleProductStatusTypeDef.NOTHING -> context.getString(R.string.flash_sale_reserve_product)
        FlashSaleProductStatusTypeDef.SUBMITTED -> context.getString(R.string.flash_sale_cancel_reserve)
        FlashSaleProductStatusTypeDef.REJECTED -> context.getString(R.string.flash_sale_resubmit_product) // will not happen
        FlashSaleProductStatusTypeDef.RESERVE -> context.getString(R.string.flash_sale_cancel_reserve)
        FlashSaleProductStatusTypeDef.SUBMIT_CANCEL -> context.getString(R.string.flash_sale_undo_cancel)
        FlashSaleProductStatusTypeDef.SUBMIT_CANCEL_SUBMIT -> context.getString(R.string.flash_sale_resubmit_product)
        else -> context.getString(R.string.flash_sale_reserve_product)
    }
}

fun FlashSaleProductItemCampaign.getProductStatusAction(): Int {
    return when (productStatus) {
        FlashSaleProductStatusTypeDef.NOTHING -> FlashSaleProductActionTypeDef.RESERVE
        FlashSaleProductStatusTypeDef.SUBMITTED -> FlashSaleProductActionTypeDef.CANCEL
        FlashSaleProductStatusTypeDef.REJECTED -> FlashSaleProductActionTypeDef.NO_ACTION
        FlashSaleProductStatusTypeDef.RESERVE -> FlashSaleProductActionTypeDef.CANCEL
        FlashSaleProductStatusTypeDef.SUBMIT_CANCEL -> FlashSaleProductActionTypeDef.UNDO_CANCEL
        FlashSaleProductStatusTypeDef.SUBMIT_CANCEL_SUBMIT -> FlashSaleProductActionTypeDef.RESERVE
        else -> FlashSaleProductActionTypeDef.RESERVE
    }
}

fun FlashSaleProductItemCampaign.getProductStatusColor(): StatusColor {
    return when (productStatus) {
        FlashSaleProductStatusTypeDef.NOTHING,
        FlashSaleProductStatusTypeDef.SUBMIT_CANCEL_SUBMIT -> StatusColor(0, 0)
        FlashSaleProductStatusTypeDef.REJECTED,
        FlashSaleProductStatusTypeDef.SUBMIT_CANCEL -> StatusColor(R.color.white, R.drawable.rect_gray_rounded_left)
        FlashSaleProductStatusTypeDef.SUBMITTED,
        FlashSaleProductStatusTypeDef.RESERVE -> StatusColor(R.color.tkpd_main_green, R.drawable.rect_green_rounded_left)
        else -> StatusColor(0, 0)
    }
}

fun Int.getAdminStatusColorAfterReview(): StatusColor {
    return when (this) {
        FlashSaleAdminStatusIdTypeDef.NAKAMA_ACCEPTED -> StatusColor(R.color.tkpd_main_green, R.drawable.rect_green_rounded_left)
        FlashSaleAdminStatusIdTypeDef.NOT_REVIEWED,
        FlashSaleAdminStatusIdTypeDef.NAKAMA_REJECTED,
        FlashSaleAdminStatusIdTypeDef.SYSTEM_TAKEOUT,
        FlashSaleAdminStatusIdTypeDef.NAKAMA_TAKEOUT -> StatusColor(R.color.white, R.drawable.rect_gray_rounded_left)
        else -> StatusColor(0, 0)
    }
}