package com.tokopedia.tokopoints.view.model.rewardtopsection

import com.google.gson.annotations.SerializedName

data class TokopediaRewardTopSection(

        @SerializedName("isShowIntroPage")
        val isShowIntroActivity: Boolean = false,

        @SerializedName("backgroundImageURLMobile")
        val backgroundImageURLMobile: String? = null,

        @SerializedName("profilePicture")
        val profilePicture: String? = null,

        @SerializedName("tier")
        val tier: Tier? = null,

        @SerializedName("dynamicActionList")
        val dynamicActionList: List<DynamicActionListItem?>? = null,

        @SerializedName("introductionText")
        val introductionText: String? = null,

        @SerializedName("backgroundImageURLMobileV2")
        val backgroundImageURLMobileV2: String? = null,

        @SerializedName("title")
        val title: String? = null,

        @SerializedName("target")
        val target: Target? = null,

        @SerializedName("backgroundImageURL")
        val backgroundImageURL: String? = null,

        @SerializedName("isShowSavingPage")
        val isShowSavingPage: Boolean? = false,

        @SerializedName("lastVisitBackgroundImageURL")
        val lastVisitBackgroundImageURL: String? = "",

        @SerializedName("lastVisitBackgroundImageURLMobile")
        val lastVisitBackgroundImageURLMobile: String? = "",

        @SerializedName("lastVisitBackgroundImageURLMobileV2")
        val lastVisitBackgroundImageURLMobileV2: String? = "",

        @SerializedName("targetV2")
        val targetV2: TargetV2? = null,

        @SerializedName("progressInfoList")
        val progressInfoList: List<ProgressInfoList>? = null
)