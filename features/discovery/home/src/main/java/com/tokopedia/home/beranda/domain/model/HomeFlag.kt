package com.tokopedia.home.beranda.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class HomeFlag(
        @SerializedName("prompt_server_time")
        @Expose
        var promptServerTime: Long = 0,
        @SerializedName("prompt_refresh_time")
        @Expose
        var promptRefreshTime: String = "",
        @SerializedName("prompt_refresh_message")
        @Expose
        var promptRefreshMessage: String = ""
) {
    companion object {
        val DYNAMIC_ICON_WRAP_STRING = "dynamic_icon_wrap"
        val HAS_RECOM_NAV_BUTTON_STRING = "has_recom_nav_button"
        val HAS_TOKOPOINTS_STRING = "has_tokopoints"
        val PROMPT_REFRESH_STRING = "prompt_refresh"
    }
    enum class TYPE {
        DYNAMIC_ICON_WRAP, HAS_TOKOPOINTS, HAS_RECOM_NAV_BUTTON, PROMPT_REFRESH;

        override fun toString(): String {
            return when(this){
                DYNAMIC_ICON_WRAP -> DYNAMIC_ICON_WRAP_STRING
                HAS_RECOM_NAV_BUTTON -> HAS_RECOM_NAV_BUTTON_STRING
                HAS_TOKOPOINTS -> HAS_TOKOPOINTS_STRING
                PROMPT_REFRESH -> PROMPT_REFRESH_STRING
            }
        }
    }

    @SerializedName("flags")
    @Expose
    var flags: List<Flags> = listOf()

    fun getFlag(type: TYPE): Boolean{
        return flags.find { it.name == type.toString() }?.isActive ?: false
    }
}

data class Flags(
    @SerializedName("name")
    var name: String = "",
    @SerializedName("is_active")
    var isActive: Boolean
)