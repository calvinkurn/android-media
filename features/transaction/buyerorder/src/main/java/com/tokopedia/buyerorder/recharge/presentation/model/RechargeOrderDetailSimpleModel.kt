package com.tokopedia.buyerorder.recharge.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 24/10/2021
 */
@Parcelize
data class RechargeOrderDetailSimpleModel(
        val label: String,
        val detail: String,
        val isTitleBold: Boolean,
        val isDetailBold: Boolean,
        val alignment: RechargeSimpleAlignment,
        val isCopyable: Boolean = false,
        val textColor: String = "",
        val textSize: String = "",
        val backgroundColor: String = "",
        val imageUrl: String = ""
) : Parcelable

enum class RechargeSimpleAlignment {
    LEFT, RIGHT
}