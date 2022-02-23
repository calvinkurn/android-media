package com.tokopedia.profilecompletion.changebiousername.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class SubmitProfileParam(
    @SerializedName("username")
    val username: String = "",

    @SerializedName("biography")
    val bio: String = "",

    @SerializedName("isUpdateUsername")
    val isUpdateUsername: Boolean = false,

    @SerializedName("isUpdateBiography")
    val isUpdateBioGraphy: Boolean = false
): GqlParam
