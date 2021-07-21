package com.tokopedia.cart.domain.model.updatecart

import android.os.Parcelable
import com.tokopedia.purchase_platform.common.feature.outofservice.OutOfServiceData
import com.tokopedia.purchase_platform.common.feature.toasteraction.ToasterActionData
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UpdateCartData(
        var isSuccess: Boolean = false,
        var message: String = "",
        var outOfServiceData: OutOfServiceData = OutOfServiceData(),
        var toasterActionData: ToasterActionData = ToasterActionData()
) : Parcelable
