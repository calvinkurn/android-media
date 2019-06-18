package com.tokopedia.settingnotif.usersetting.domain.pojo

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.settingnotif.usersetting.view.adapter.SettingFieldTypeFactory


class UserSettingPojo(
        var sections: List<SettingSectionsPojo>
)

class SettingSectionsPojo(
        var sectionTitle: String,
        var settings: List<ParentSettingPojo>
) : Visitable<SettingFieldTypeFactory> {

    override fun type(typeFactory: SettingFieldTypeFactory): Int {
        return typeFactory.type(this)
    }

}

abstract class Setting(
        var name: String,
        var icon: String,
        var key: String,
        var status: Boolean
) : Visitable<SettingFieldTypeFactory>

class ParentSettingPojo(
        name: String,
        icon: String,
        key: String,
        status: Boolean,
        val description: String,
        val childSettings: List<ChildSettingPojo>
) : Setting(name, icon, key, status) {

    override fun type(typeFactory: SettingFieldTypeFactory): Int {
        return typeFactory.type(this)
    }

    fun hasDescription(): Boolean = description.isNotEmpty()
    fun hasChild(): Boolean = childSettings.isNotEmpty()

}

class ChildSettingPojo(
        name: String,
        icon: String,
        key: String,
        status: Boolean
) : Setting(name, icon, key, status) {

    override fun type(typeFactory: SettingFieldTypeFactory): Int {
        return typeFactory.type(this)
    }

}


object SettingHelper {

    fun createDummyResponse(): UserSettingPojo {
        val transactionSettings = listOf(
                ParentSettingPojo("Transaksi Penjualan", "", "1", true, "", emptyList()),
                ParentSettingPojo("Transaksi Pembelian", "", "1", true, "", emptyList())
        )

        val chatChildSetting = listOf(
                ChildSettingPojo("Dari Tokopedia", "", "1", true),
                ChildSettingPojo("Chat Promosi dari Penjual", "", "1", true),
                ChildSettingPojo("Chat Penjual", "", "1", true)
        )
        val inboxSettings = listOf(
                ParentSettingPojo("Chat", "", "1", true, "", chatChildSetting),
                ParentSettingPojo("Diskusi", "", "1", true, "", emptyList()),
                ParentSettingPojo("Ulasan", "", "1", true, "", emptyList())
        )

        val promoChildSetting = listOf(
                ChildSettingPojo("Promo untuk Pembeli", "", "1", true),
                ChildSettingPojo("Promo untuk Penjual", "", "1", true)
        )

        val infoChildSetting = listOf(
                ChildSettingPojo("Info untuk Pembeli", "", "1", true),
                ChildSettingPojo("Info untuk Penjual", "", "1", true)
        )

        val updateSettings = listOf(
                ParentSettingPojo("Aktivitas", "", "1", true, "Reminder, Feeds, dan Aktivitas akun kamu", emptyList()),
                ParentSettingPojo("Promo", "", "1", true, "", promoChildSetting),
                ParentSettingPojo("Info", "", "1", true, "Informasi terbaru seputar Tokopedia", infoChildSetting)
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