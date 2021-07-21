package com.tokopedia.cart.domain.model.updatecart

import android.os.Parcelable
import com.tokopedia.cartcommon.data.response.common.OutOfService
import com.tokopedia.cartcommon.data.response.updatecart.ToasterAction
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UpdateCartData(
        var isSuccess: Boolean = false,
        var message: String = "",
        var outOfServiceData: OutOfService = OutOfService(),
        var toasterActionData: ToasterAction = ToasterAction()
) : Parcelable
