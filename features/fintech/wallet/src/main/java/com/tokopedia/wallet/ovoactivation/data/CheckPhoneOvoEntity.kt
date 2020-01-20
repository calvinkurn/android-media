package com.tokopedia.wallet.ovoactivation.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by nabillasabbaha on 24/09/18.
 */
class CheckPhoneOvoEntity(
    @SerializedName("phone_number")
    @Expose
    val phoneNumber: String = "",
    @SerializedName("is_registered")
    @Expose
    val isRegistered: Boolean = false,
    @SerializedName("registered_applink")
    @Expose
    val registeredApplink: String = "",
    @SerializedName("not_registered_applink")
    @Expose
    val notRegisteredApplink: String = "",
    @SerializedName("change_msisdn_applink")
    @Expose
    val changeMsisdnApplink: String = "",
    @SerializedName("is_allow")
    @Expose
    val isAllow: Boolean = false,
    @SerializedName("action")
    @Expose
    val phoneActionEntity: PhoneActionEntity? = null,
    @SerializedName("errors")
    @Expose
    val errors: ErrorModelEntity? = null)
