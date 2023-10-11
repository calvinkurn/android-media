package com.tokopedia.profilecompletion.profilecompletion.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @author by nisie on 8/2/17.
 */

@Parcelize
data class ProfileCompletionDataView(
    var gender: Int = 0,
    var phone: String = "",
    var bday: String = "",
    var completion: Int = 0,
    var isPhoneVerified: Boolean = false
) : Parcelable
