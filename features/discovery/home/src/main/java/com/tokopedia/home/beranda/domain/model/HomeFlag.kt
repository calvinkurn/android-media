package com.tokopedia.home.beranda.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class HomeFlag(
        @SerializedName("server_time")
        @Expose
        var serverTime: Long = 0L,
        @SerializedName("event_time")
        @Expose
        var eventTime: String = ""
) {
    companion object {
        val DYNAMIC_ICON_WRAP_STRING = "dynamic_icon_wrap"
        val HAS_RECOM_NAV_BUTTON_STRING = "has_recom_nav_button"
        val HAS_TOKOPOINTS_STRING = "has_tokopoints"
        val IS_AUTO_REFRESH_STRING = "is_autorefresh"
    }
    enum class TYPE {
        DYNAMIC_ICON_WRAP, HAS_TOKOPOINTS, HAS_RECOM_NAV_BUTTON, IS_AUTO_REFRESH;

        override fun toString(): String {
            return when(this){
                DYNAMIC_ICON_WRAP -> DYNAMIC_ICON_WRAP_STRING
                HAS_RECOM_NAV_BUTTON -> HAS_RECOM_NAV_BUTTON_STRING
                HAS_TOKOPOINTS -> HAS_TOKOPOINTS_STRING
                IS_AUTO_REFRESH -> IS_AUTO_REFRESH_STRING
            }
        }
    }

    @SerializedName("flags")
    @Expose
    var flags: MutableList<Flags> = mutableListOf()

    fun addFlag(name: String, isActive: Boolean, integerValue: Int = 0) {
        flags.add(Flags(name, isActive, integerValue))
    }

    fun getFlag(type: TYPE): Boolean{
        return flags.find { it.name == type.toString() }?.isActive ?: false
    }
    fun getFlagValue(type: TYPE): Int{
        return flags.find { it.name == type.toString() }?.integerValue ?: 0
    }
}

//TODO: add integer value -> BE not ready yet
data class Flags(
    @SerializedName("name")
    var name: String = "",
    @SerializedName("is_active")
    var isActive: Boolean,
    @SerializedName("integer_value")
    var integerValue: Int = 0
)