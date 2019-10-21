package com.tokopedia.shop.common.graphql.data.stampprogress


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MembershipData(
        @SerializedName("infoMessage")
        @Expose
        val infoMessage: InfoMessage = InfoMessage(),

        @SerializedName("isShown")
        @Expose
        val isShown: Boolean = false,

        @SerializedName("isUserRegistered")
        @Expose
        val isUserRegistered: Boolean = false,

        @SerializedName("program")
        @Expose
        val membershipProgram: MembershipProgram = MembershipProgram()
)