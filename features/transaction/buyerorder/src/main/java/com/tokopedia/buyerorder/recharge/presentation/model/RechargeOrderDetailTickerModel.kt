package com.tokopedia.buyerorder.recharge.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 27/10/2021
 */
@Parcelize
data class RechargeOrderDetailTickerModel(
        val title: String,
        val text: String,
        val urlDetail: String,
        val type: Int
) : Parcelable