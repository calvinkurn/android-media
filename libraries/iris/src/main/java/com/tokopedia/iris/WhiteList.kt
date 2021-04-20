package com.tokopedia.iris

/**
 * Author errysuprayogi on 14,July,2020
 */
object WhiteList {

    val REALTIME_EVENT_LIST = mutableListOf(
            "clickLogin",
            "loginSuccess",
            "clickRegister",
            "openScreen",
            "view_item",
            "clickSearch",
            "pushClicked",
            "pushReceived",
            "appInstall",
            "appSiteOpen"
    )

    val CM_REALTIME_EVENT_LIST = mutableListOf(
            "pushClicked",
            "pushReceived",
            "appInstall",
            "appSiteOpen",
            "pushDismissed",
            "inappReceived",
            "inappClicked",
            "inappDismissed"
    )

}