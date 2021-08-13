package com.tokopedia.loginregister.external_register.base.constant

/**
 * Created by Yoris Prayogo on 16/11/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

object ExternalRegisterConstants {
    const val ADD_NAME_SCREEN = "baseAddName"
    const val ADD_PHONE_SCREEN = "baseAddPhone"

    const val BASE_WEBVIEW_APPLINK = "tokopedia://webview?url="
    const val BASE_MOBILE = "https://m.tokopedia.com/"

    const val PATH_TERM_CONDITION = "terms.pl?isBack=true"
    const val PATH_PRIVACY_POLICY = "privacy.pl?isBack=true"


    const val PREF_KEY_GOAL_ID = "externalRegisterGoalKey"
    const val PREF_KEY_AUTH_CODE = "externalRegisterAuthCode"
    const val PREF_KEY_NAME = "externalRegisterFullname"
    const val PREF_KEY_PHONE = "externalRegisterPhone"
    const val PREF_KEY_NEED_CONTINUE = "externalRegisterContinue"

    const val CONFIG_EXTERNAL_REGISTER = "user_external_register_enable"
    const val OVO_REGISTER_AB_TEST_VALUE = "ovo_auto_account"

    const val REQUEST_OVO_REGISTER = 1001

    object PARAM {
        val PHONE_NO = "phone_number"
        val CLIENT_ID = "client_id"
        val NAME = "name"
        val DATE = "date"

        val URL = "external_url"

        val GOAL_KEY = "goal_key"
        val AUTH_CODE = "auth_code"
        val MSISDN = "phone"
        val INPUT_PARAM = "input"
        val FULLNAME = "fullname"
        val TYPE = "reg_type"
    }

    object KEY {
        val CLIENT_ID = "AxbZkaBowrkB"
        val SECRET = "F0qWZoLyFOARjesfudHv"
    }
}