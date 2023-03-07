package com.tokopedia.home.beranda.data.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class SubscriptionsData(
    @SerializedName("ResultStatus")
    @Expose
    val resultStatus: SubscriptionsResultStatus = SubscriptionsResultStatus(),
    @SerializedName("CoachMarkList")
    @Expose
    val subscriptionsCoachMarkList: List<SubscriptionsCoachMarkList> = listOf(),
    @SerializedName("DrawerList")
    @Expose
    val drawerList: List<SubscriptionsDrawerList> = listOf(),
    @SerializedName("IsShown")
    @Expose
    val isShown: Boolean = false,
    @SerializedName("IsSubscriber")
    @Expose
    val isSubscriber: Boolean = false
)