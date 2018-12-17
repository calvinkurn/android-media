package com.tokopedia.instantloan.ddcollector;

import android.Manifest;

public interface DDConstants {

    String REQUIRE = "dd_yes";
    String NOT_REQUIRE = "dd_no";
    String PERMISSION_ENUM_SEPARATOR = "$-$";
    String RGEX_PERMISSION_ENUM_SEPARATOR = "\\$\\-\\$";
    String NOT_AVAILABLE = "n/a";

    enum DDComponents {
        READ_CONTACTS(REQUIRE + PERMISSION_ENUM_SEPARATOR + Manifest.permission.READ_CONTACTS),
        GET_ACCOUNTS(REQUIRE + PERMISSION_ENUM_SEPARATOR + Manifest.permission.GET_ACCOUNTS),
        WRITE_EXTERNAL_STORAGE(REQUIRE + PERMISSION_ENUM_SEPARATOR + Manifest.permission.WRITE_EXTERNAL_STORAGE),
        ACCESS_COARSE_LOCATION(REQUIRE + PERMISSION_ENUM_SEPARATOR + Manifest.permission.ACCESS_COARSE_LOCATION),
        ACCESS_FINE_LOCATION(REQUIRE + PERMISSION_ENUM_SEPARATOR + Manifest.permission.ACCESS_FINE_LOCATION),
        READ_PHONE_STATE(REQUIRE + PERMISSION_ENUM_SEPARATOR + Manifest.permission.READ_PHONE_STATE),
        APP(NOT_REQUIRE + PERMISSION_ENUM_SEPARATOR + "APP"),
        BASIC_DEVICE_DATA(NOT_REQUIRE + PERMISSION_ENUM_SEPARATOR + "BDD");

        private final String component;

        DDComponents(String component) {
            this.component = component;
        }

        public String val() {
            return this.component;
        }
    }
}
