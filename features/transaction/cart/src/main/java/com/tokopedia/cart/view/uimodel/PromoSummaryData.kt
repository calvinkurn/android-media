package com.tokopedia.cart.view.uimodel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @author by furqan on 17/02/2021
 */
@Parcelize
data class PromoSummaryData(
    val title: String = "",
    val details: MutableList<PromoSummaryDetailData> = arrayListOf()
) : Parcelable

@Parcelize
data class PromoSummaryDetailData(
    val description: String = "",
    val type: String = "",
    val amountStr: String = "",
    val amount: Double = 0.0,
    val currencyDetailStr: String = ""
) : Parcelable
