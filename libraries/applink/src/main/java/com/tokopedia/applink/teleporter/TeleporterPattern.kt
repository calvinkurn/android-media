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
        val target: String = ""
) {
    val pathPatternList by lazy { path.split("/").filter { it.isNotEmpty() } }
    val queryMustHavePatternMap by lazy { UriUtil.stringQueryParamsToMap(query) }
    val queryOptionalPatternMap by lazy { UriUtil.stringQueryParamsToMap(queryOptional) }
}