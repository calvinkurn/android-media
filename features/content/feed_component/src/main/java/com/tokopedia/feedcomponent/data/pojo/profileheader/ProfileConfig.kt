package com.tokopedia.feedcomponent.data.pojo.profileheader

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by jegul on 19/08/20
 */
data class ProfileConfig(
        @SerializedName("totalFollower")
        @Expose
        val showPostButton: Boolean = false
)