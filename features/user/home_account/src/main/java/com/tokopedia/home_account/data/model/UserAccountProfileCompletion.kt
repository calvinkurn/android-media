package com.tokopedia.home_account.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Yoris Prayogo on 20/10/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

data class UserAccountProfileCompletion (
        @Expose
        @SerializedName("isActive") var isActive: Boolean = false,
        @Expose
        @SerializedName("fullName") var fullName: String = "",
        @Expose
        @SerializedName("birthDate") var birthDate: String = "",
        @Expose
        @SerializedName("birthDay") var birthDay: String = "",
        @Expose
        @SerializedName("birthMonth") var birthMonth: String = "",
        @Expose
        @SerializedName("birthYear") var birthYear: String = "",
        @Expose
        @SerializedName("gender") var gender: Int = 0,
        @Expose
        @SerializedName("email") var email: String = "",
        @Expose
        @SerializedName("msisdn") var msisdn: String = "",
        @Expose
        @SerializedName("isMsisdnVerified") var isMsisdnVerified: Boolean = false,
        @Expose
        @SerializedName("isCreatedPassword") var isCreatedPassword: Boolean = false,
        @Expose
        @SerializedName("isBiodataDone") var isBiodataDone: Boolean = false,
        @Expose
        @SerializedName("isEmailDone") var isEmailDone: Boolean = false,
        @Expose
        @SerializedName("isPasswordDone") var isPasswordDone: Boolean = false,
        @Expose
        @SerializedName("isMsisdnDone") var isMsisdnDone: Boolean = false,
        @Expose
        @SerializedName("completionDone") var completionDone: Boolean = false,
        @Expose
        @SerializedName("completionScore") var completionScore: Int = 0
)