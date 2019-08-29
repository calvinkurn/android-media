package com.tokopedia.shop.common.graphql.data.stampprogress


import com.google.gson.annotations.SerializedName

data class MembershipQuests(
        @SerializedName("actionButton")
        val actionButton: ActionButton = ActionButton(),

        @SerializedName("currentProgress")
        val currentProgress: Int = 0,

        @SerializedName("iconURL")
        val iconURL: String = "",

        @SerializedName("id")
        val id: Int = 0,

        @SerializedName("questUserID")
        val questUserID: Int = 0,

        @SerializedName("status")
        val status: Int = 0,

        @SerializedName("targetProgress")
        val targetProgress: Int = 0,

        @SerializedName("taskID")
        val taskID: Int = 0,

        @SerializedName("title")
        val title: String = "",

        //for continuous number inside view holder
        var startCountTxt: Int = 0
)