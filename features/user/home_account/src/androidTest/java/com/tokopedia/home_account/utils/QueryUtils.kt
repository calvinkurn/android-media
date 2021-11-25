package com.tokopedia.home_account.utils

object QueryUtils {

    fun queryAccountSettings(menu: String) = listOf(
            mapOf("event" to "clickAccount", "eventCategory" to "akun saya pembeli",
                    "eventAction" to "click account settings section", "eventLabel" to menu))

    fun queryMoreSettings(section: String) = listOf(
            mapOf("event" to "clickAccount", "eventCategory" to "akun saya pembeli",
                    "eventAction" to "click on more option", "eventLabel" to section))

    fun queryShakeCampaign(isEnable: Boolean): Map<String, String> {
        val status = if (isEnable) "enable"
        else "disable"
        return mapOf("event" to "clickAccount", "eventCategory" to "akun saya pembeli",
                "eventAction" to "click on application setting section", "eventLabel" to "Shake shake - $status")
    }

    fun queryProfile() = listOf(mapOf("event" to "clickAccount", "eventCategory" to "akun saya pembeli",
            "eventAction" to "click - profile page - click profile", "eventLabel" to ""))

    fun queryOvo() = listOf(
            mapOf("event" to "clickAccount", "eventCategory" to "akun saya pembeli",
                    "eventAction" to "click on ovo", "eventLabel" to "click"))

    fun querySaldo() = listOf(
            mapOf("event" to "clickAccount", "eventCategory" to "akun saya pembeli",
                    "eventAction" to "click - tokopedia pay - saldo", "eventLabel" to "click"))
}