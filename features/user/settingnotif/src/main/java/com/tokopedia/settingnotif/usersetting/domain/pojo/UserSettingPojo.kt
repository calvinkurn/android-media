package com.tokopedia.settingnotif.usersetting.domain.pojo


class UserSettingPojo(
        var sections: List<SettingSectionsPojo>
)

class SettingSectionsPojo(
        var sectionTitle: String,
        var settings: List<SettingPojo>
)

class SettingPojo(
        var name: String,
        var icon: String,
        var key: String,
        var status: Boolean,
        var description: String,
        var childSettings: List<SettingPojo>
)


object NotificationSettingHelper {

    fun createDummyResponse(): UserSettingPojo {
        val transactionSettings = listOf(
                SettingPojo("Transaksi Pembelian", "", "1", true, "", emptyList())
        )

        val chatSetting = listOf(
                SettingPojo("Dari Tokopedia", "", "1", true, "", emptyList()),
                SettingPojo("Chat Promosi dari Penjual", "", "1", true, "", emptyList()),
                SettingPojo("Chat Penjual", "", "1", true, "", emptyList())
        )
        val inboxSettings = listOf(
                SettingPojo("Chat", "", "1", true, "", chatSetting),
                SettingPojo("Diskusi", "", "1", true, "", emptyList()),
                SettingPojo("Ulasan", "", "1", true, "", emptyList())
        )

        val updateSettings = listOf(
                SettingPojo("Aktivitas", "", "1", true, "Reminder, Feeds, dan Aktivitas akun kamu", emptyList()),
                SettingPojo("Promo", "", "1", true, "", emptyList()),
                SettingPojo("Info", "", "1", true, "Informasi terbaru seputar Tokopedia", emptyList())
        )

        val emailSectionSettings = listOf(
                SettingSectionsPojo(
                        "Transaksi",
                        transactionSettings
                ),
                SettingSectionsPojo(
                        "Inbox",
                        inboxSettings
                ),
                SettingSectionsPojo(
                        "Update",
                        updateSettings
                )
        )

        return UserSettingPojo(emailSectionSettings)
    }
}