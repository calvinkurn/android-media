package com.tokopedia.instantloan.ddcollector

import android.Manifest
import com.tokopedia.instantloan.ddcollector.DDConstants.Constant.NOT_REQUIRE
import com.tokopedia.instantloan.ddcollector.DDConstants.Constant.PERMISSION_ENUM_SEPARATOR
import com.tokopedia.instantloan.ddcollector.DDConstants.Constant.REQUIRE

object DDConstants {

    enum class DDComponents private constructor(private val component: String) {
        READ_CONTACTS(REQUIRE + PERMISSION_ENUM_SEPARATOR + Manifest.permission.READ_CONTACTS),
        GET_ACCOUNTS(REQUIRE + PERMISSION_ENUM_SEPARATOR + Manifest.permission.GET_ACCOUNTS),
        WRITE_EXTERNAL_STORAGE(REQUIRE + PERMISSION_ENUM_SEPARATOR + Manifest.permission.WRITE_EXTERNAL_STORAGE),
        ACCESS_COARSE_LOCATION(REQUIRE + PERMISSION_ENUM_SEPARATOR + Manifest.permission.ACCESS_COARSE_LOCATION),
        ACCESS_FINE_LOCATION(REQUIRE + PERMISSION_ENUM_SEPARATOR + Manifest.permission.ACCESS_FINE_LOCATION),
        READ_PHONE_STATE(REQUIRE + PERMISSION_ENUM_SEPARATOR + Manifest.permission.READ_PHONE_STATE),
        APP(NOT_REQUIRE + PERMISSION_ENUM_SEPARATOR + "APP"),
        BASIC_DEVICE_DATA(NOT_REQUIRE + PERMISSION_ENUM_SEPARATOR + "BDD");

        fun `val`(): String {
            return this.component
        }
    }

    object Constant {

        val REQUIRE = "dd_yes"
        val NOT_REQUIRE = "dd_no"
        val PERMISSION_ENUM_SEPARATOR = "$-$"
        val RGEX_PERMISSION_ENUM_SEPARATOR = "\\$\\-\\$"
        val NOT_AVAILABLE = "n/a"
    }
}
