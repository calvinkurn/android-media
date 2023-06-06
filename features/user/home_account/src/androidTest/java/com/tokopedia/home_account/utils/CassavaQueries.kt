package com.tokopedia.home_account.utils

import com.tokopedia.home_account.AccountConstants
import com.tokopedia.home_account.AccountConstants.Analytics.Label.LABEL_ACCOUNT_SECURITY
import com.tokopedia.home_account.AccountConstants.Analytics.Label.LABEL_BANK_ACCOUNT
import com.tokopedia.home_account.AccountConstants.Analytics.Label.LABEL_INSTANT_PAYMENT
import com.tokopedia.home_account.AccountConstants.Analytics.Label.LABEL_LIST_ADDRESS
import com.tokopedia.home_account.AccountConstants.Analytics.Label.LABEL_NOTIFICATION

object CassavaQueries {

    fun queryAccountSettings(menu: String) = mapOf(
        "event" to "clickAccount",
        "eventCategory" to "akun saya pembeli",
        "eventAction" to "click account settings section",
        "eventLabel" to menu
    )

    fun queryMoreSettings(section: String) =
        mapOf(
            "event" to "clickAccount",
            "eventCategory" to "akun saya pembeli",
            "eventAction" to "click on more option",
            "eventLabel" to section
        )

    fun queryShakeCampaign(isEnable: Boolean): Map<String, String> {
        val status = if (isEnable) {
            "enable"
        } else {
            "disable"
        }
        return mapOf(
            "event" to "clickAccount",
            "eventCategory" to "akun saya pembeli",
            "eventAction" to "click on application setting section",
            "eventLabel" to "Shake shake - $status"
        )
    }

    fun queryProfile() = mapOf(
        "event" to "clickAccount",
        "eventCategory" to "akun saya pembeli",
        "eventAction" to "click - profile page - click profile",
        "eventLabel" to ""
    )

    fun queryOvo() =
        mapOf(
            "event" to "clickAccount",
            "eventCategory" to "akun saya pembeli",
            "eventAction" to "click on ovo",
            "eventLabel" to "click"
        )

    fun querySaldo() =
        mapOf(
            "event" to "clickAccount",
            "eventCategory" to "akun saya pembeli",
            "eventAction" to "click - tokopedia pay - saldo",
            "eventLabel" to "click"
        )

    val clickTrackerFirstPage = listOf(
        queryProfile(),
        queryOvo(),
        querySaldo(),
        queryMoreSettings("Member")
    )

    val clickTrackerPengaturanAkun = listOf(
        queryAccountSettings(LABEL_LIST_ADDRESS),
        queryAccountSettings(LABEL_BANK_ACCOUNT),
        queryAccountSettings(LABEL_INSTANT_PAYMENT),
        queryAccountSettings(LABEL_ACCOUNT_SECURITY),
        queryAccountSettings(LABEL_NOTIFICATION)
    )

    val clickTrackerPengaturanAplikasi = listOf(
        queryMoreSettings(AccountConstants.Analytics.Label.LABEL_APP_SETTING),
        queryShakeCampaign(true),
        queryShakeCampaign(false)
    )
}
