package com.tokopedia.buyerorder.recharge.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 27/10/2021
 */
@Parcelize
data class RechargeOrderDetailTopSectionModel(
        val labelStatusColor: String,
        val textStatusColor: String,
        val textStatus: String,
        val tickerData: RechargeOrderDetailTickerModel,
        val invoiceRefNum: String,
        val invoiceUrl: String,
        val titleData: List<RechargeOrderDetailSimpleModel>
) : Parcelable