package com.tokopedia.shop.common.graphql.data.stampprogress


import com.google.gson.annotations.SerializedName

data class MembershipProgram(
        @SerializedName("cardID")
        val cardID: Int = 0,

        @SerializedName("id")
        val id: Int = 0,

        @SerializedName("quests")
        val membershipQuests: List<MembershipQuests> = listOf(),

        @SerializedName("sectionID")
        val sectionID: Int = 0
)