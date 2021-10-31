package com.tokopedia.buyerorder.recharge.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 27/10/2021
 */
@Parcelize
data class RechargeOrderDetailActionButtonListModel(
        val actionButtons: List<RechargeOrderDetailActionButtonModel>
) : Parcelable

@Parcelize
data class RechargeOrderDetailActionButtonModel(
        val label: String,
        val buttonType: String,
        val uri: String,
        val mappingUri: String,
        val weight: Int,
        val value: String,
        val name: String,
        val appUrl: String
) : Parcelable