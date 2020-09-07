package com.tokopedia.cart.domain.model.updatecart

import android.os.Parcelable
import com.tokopedia.cart.domain.model.cartlist.OutOfServiceData
import kotlinx.android.parcel.Parcelize

/**
 * @author anggaprasetiyo on 20/02/18.
 */
@Parcelize
data class UpdateCartData(
        var isSuccess: Boolean = false,
        var message: String = "",
        var outOfServiceData: OutOfServiceData = OutOfServiceData(),
        var toasterActionData: ToasterActionData = ToasterActionData()
) : Parcelable
