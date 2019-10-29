package com.tokopedia.feedcomponent.data.pojo.whitelist

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.util.ArrayList

/**
 * @author by yfsx on 20/06/18.
 */
data class Whitelist (
    @SerializedName("iswhitelist")
    @Expose
    val isWhitelist: Boolean = false,

    @SerializedName("url")
    @Expose
    val url: String = "",

    @SerializedName("error")
    @Expose
    val error: String = "",

    @SerializedName("title")
    @Expose
    val title: String = "",

    @SerializedName("title_identifier")
    @Expose
    val titleIdentifier: String = "",

    @SerializedName("description")
    @Expose
    val description: String = "",

    @SerializedName("post_success")
    @Expose
    val postSuccessMessage: String = "",

    @SerializedName("image_url")
    @Expose
    val imageUrl: String = "",

    @SerializedName("authors")
    @Expose
    val authors: List<Author> = ArrayList()
)
