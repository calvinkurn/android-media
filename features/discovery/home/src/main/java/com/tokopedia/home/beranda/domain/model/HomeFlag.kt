package com.tokopedia.home.beranda.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class HomeFlag {
    enum class TYPE {
        DYNAMIC_ICON_WRAP, HAS_TOKOPOINTS, HAS_RECOM_NAV_BUTTON;

        override fun toString(): String {
            return when(this){
                DYNAMIC_ICON_WRAP -> "dynamic_icon_wrap"
                HAS_RECOM_NAV_BUTTON -> "has_recom_nav_button"
                HAS_TOKOPOINTS -> "has_tokopoints"
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