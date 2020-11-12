package com.tokopedia.flight.orderdetail.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 16/10/2020
 */
@Parcelize
data class FlightOrderDetailPaymentModel(
        val id: Long,
        val status: Int,
        val statusStr: String,
        val gatewayName: String,
        val gatewayIcon: String,
        val paymentDate: String,
        val expireOn: String,
        val transactionCode: String,
        val promoCode: String,
        val adminFeeAmount: Long,
        val adminFeeAmountStr: String,
        val voucherAmount: Long,
        val voucherAmountStr: String,
        val saldoAmount: Long,
        val saldoAmountStr: String,
        val totalAmount: Long,
        val totalAmountStr: String,
        val needToPayAmount: Long,
        val needToPayAmountStr: String,
        val uniqueCode: Long,
        val accountBankName: String,
        val accountBranch: String,
        val accountNo: String,
        val accountName: String,
        val total: String
) : Parcelable