package com.tokopedia.logisticaddaddress.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by Irfan Khoirul on 17/11/17.
 */
@Parcelize
data class AddressResponse(
    var isNextAvailable: Boolean = false,
    var addresses: List<Address> = emptyList()
) : Parcelable
