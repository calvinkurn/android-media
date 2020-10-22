package com.tokopedia.travel.passenger.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author by resakemal on 17/05/19
 */
@Parcelize
class TravelContactData(var name: String = "",
                        var email: String = "",
                        var phone: String = "",
                        var phoneCode: Int = 0,
                        var phoneCountry: String = "") : Parcelable {

    fun isEmpty(): Boolean {
        return name.isBlank() && email.isBlank() && phoneCode == 0 && phone.isBlank()
    }

}