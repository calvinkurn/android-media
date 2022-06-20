package com.tokopedia.feedcomponent.data.pojo.mention

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by jegul on 2019-08-05.
 */

data class Profile(
        @SerializedName("id")
        @Expose
        val id: String = "",

        @SerializedName("name")
        @Expose
        val name: String = "",

        @SerializedName("avatar")
        @Expose
        val avatar: String = "",

        @SerializedName("username")
        @Expose
        val username: String = "",

        @SerializedName("bio")
        @Expose
        val bio: String = "",

        @SerializedName("thumbnails")
        @Expose
        val thumbnails: List<String> = emptyList(),

        @SerializedName("followed")
        @Expose
        val followed: Boolean = false,

        @SerializedName("following")
        @Expose
        val following: Int = 0,

        @SerializedName("followers")
        @Expose
        val followers: Int = 0,

        @SerializedName("isaffiliate")
        @Expose
        val isAffiliate: Boolean = false,

        @SerializedName("iskol")
        @Expose
        val isKol: Boolean = false
)