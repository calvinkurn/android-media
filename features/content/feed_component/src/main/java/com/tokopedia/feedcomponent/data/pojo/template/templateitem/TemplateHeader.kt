package com.tokopedia.feedcomponent.data.pojo.template.templateitem

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by yfsx on 04/12/18.
 */
data class TemplateHeader (

    @SerializedName("avatar")
    @Expose
    val avatar: Boolean = false,
    @SerializedName("avatarTitle")
    @Expose
    val avatarTitle: Boolean = false,
    @SerializedName("avatarDate")
    @Expose
    val avatarDate: Boolean = false,
    @SerializedName("avatarBadge")
    @Expose
    val avatarBadge: Boolean = false,
    @SerializedName("avatarDescription")
    @Expose
    val avatarDescription: Boolean = false,
    @SerializedName("followCta")
    @Expose
    val followCta: Boolean = false,
    @SerializedName("report")
    @Expose
    val report: Boolean = false
)