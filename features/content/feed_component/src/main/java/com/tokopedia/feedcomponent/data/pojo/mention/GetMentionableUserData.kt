package com.tokopedia.feedcomponent.data.pojo.mention

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by jegul on 2019-08-05.
 */

data class GetMentionableUserData(
        @SerializedName("aceSearchProfile")
        @Expose
        val aceSearchProfile: SearchProfile = SearchProfile()
)