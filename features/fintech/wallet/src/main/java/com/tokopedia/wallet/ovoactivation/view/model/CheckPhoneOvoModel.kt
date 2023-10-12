package com.tokopedia.wallet.ovoactivation.view.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by nabillasabbaha on 24/09/18.
 */
@Parcelize
class CheckPhoneOvoModel(
    var phoneNumber: String = "",
    var isRegistered: Boolean = false,
    var registeredApplink: String = "",
    var notRegisteredApplink: String = "",
    var changeMsisdnApplink: String = "",
    var isAllow: Boolean = false,
    var phoneActionModel: PhoneActionModel? = null,
    var errorModel: ErrorModel? = null
) :
    Parcelable
