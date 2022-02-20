package com.tokopedia.createpost.uprofile.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.library.baseadapter.BaseItem

data class ProfileDoFollowModelBase (
    @SerializedName("feedXProfileFollow")
    val profileFollowers: ProfileDoFollowStatus,
)

data class ProfileDoFollowStatus (
    @SerializedName("status")
    val status: Boolean,
)



