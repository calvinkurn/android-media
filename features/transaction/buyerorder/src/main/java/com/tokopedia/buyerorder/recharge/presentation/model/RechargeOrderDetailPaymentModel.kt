package com.tokopedia.buyerorder.recharge.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 27/10/2021
 */
@Parcelize
class RechargeOrderDetailPaymentModel(
        val paymentMethod: RechargeOrderDetailSimpleModel,
        val paymentDetails: List<RechargeOrderDetailSimpleModel>,
        val totalPriceLabel: String,
        val totalPrice: String,
        val additionalTicker: RechargeOrderDetailTickerModel?
) : Parcelable