package com.tokopedia.logger.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ScalyrBody(
        @SerializedName("token")
        @Expose
        val token: String,
        @SerializedName("session")
        @Expose
        val session: String,
        @SerializedName("sessionInfo")
        @Expose
        val sessionInfo: ScalyrSessionInfo,
        @SerializedName("events")
        @Expose
        val events: List<ScalyrEvent>)

data class ScalyrSessionInfo(
        @SerializedName("serverHost")
        @Expose
        val serverHost: String,
        @SerializedName("parser")
        @Expose
        val parser: String,
        @SerializedName("packageName")
        @Expose
        val packageName: String,
        @SerializedName("debug")
        @Expose
        val debug: Boolean,
        @SerializedName("priority")
        @Expose
        val priority: Int)

data class ScalyrEvent(
        @SerializedName("ts")
        @Expose
        val ts: Long,
        @SerializedName("attrs")
        @Expose
        val attrs: ScalyrEventAttrs,
        @SerializedName("sev")
        @Expose
        val sev: Int = 3)

data class ScalyrEventAttrs(
        @SerializedName("message")
        @Expose
        val message: String)