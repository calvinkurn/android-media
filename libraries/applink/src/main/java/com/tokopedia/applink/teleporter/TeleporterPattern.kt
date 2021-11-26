package com.tokopedia.applink.teleporter

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.applink.UriUtil

class TeleporterPattern(
        @Expose
        @SerializedName("scheme")
        val scheme: String = "",
        @Expose
        @SerializedName("host")
        val host: String = "",
        @Expose
        @SerializedName("path")
        val path: String = "",
        @Expose
        @SerializedName("query")
        val query: String? = "",
        @Expose
        @SerializedName("query_optional")
        val queryOptional: String? = "",
        @Expose
        @SerializedName("target")
        val target: String = "",
        @Expose
        @SerializedName("environment")
        val environment: String = "",
        @Expose
        @SerializedName("min_app_ver_code")
        val minAppVer: String = "",
        @Expose
        @SerializedName("max_app_ver_code")
        val maxAppVer: String = "",
        @Expose
        @SerializedName("device_manufacturers")
        val manufacturers: String = "",
        @Expose
        @SerializedName("device_models")
        val models: String = "",
        @Expose
        @SerializedName("min_os_ver")
        val minOsVer: String = "",
        @Expose
        @SerializedName("max_os_ver")
        val maxOsVer: String = ""
) {
    val pathPatternList by lazy { path.split("/").filter { it.isNotEmpty() } }
    val queryMustHavePatternMap by lazy { UriUtil.stringQueryParamsToMap(query) }
    val queryOptionalPatternMap by lazy { UriUtil.stringQueryParamsToMap(queryOptional) }
}