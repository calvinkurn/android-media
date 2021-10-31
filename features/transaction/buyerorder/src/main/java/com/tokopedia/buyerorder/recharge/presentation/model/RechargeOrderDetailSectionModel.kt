package com.tokopedia.buyerorder.recharge.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 28/10/2021
 */
@Parcelize
data class RechargeOrderDetailSectionModel(
        val detailList: List<RechargeOrderDetailSimpleModel>
) : Parcelable