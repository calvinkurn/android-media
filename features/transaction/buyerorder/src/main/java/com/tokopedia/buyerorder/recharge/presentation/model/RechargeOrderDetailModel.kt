package com.tokopedia.buyerorder.recharge.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 27/10/2021
 */
@Parcelize
data class RechargeOrderDetailModel(
        val topSectionModel: RechargeOrderDetailTopSectionModel,
        val detailsSection: RechargeOrderDetailSectionModel,
        val paymentSectionModel: RechargeOrderDetailPaymentModel,
        val helpUrl: String,
        val actionButtonList: RechargeOrderDetailActionButtonListModel
) : Parcelable