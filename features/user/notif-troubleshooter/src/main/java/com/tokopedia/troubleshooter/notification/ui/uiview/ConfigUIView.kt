package com.tokopedia.troubleshooter.notification.ui.uiview

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.troubleshooter.notification.ui.adapter.factory.TroubleshooterTypeFactory
import com.tokopedia.troubleshooter.notification.ui.uiview.ConfigState.PushNotification as PushNotification
import com.tokopedia.troubleshooter.notification.ui.uiview.ConfigState.Notification as Notification
import com.tokopedia.troubleshooter.notification.ui.uiview.ConfigState.Categories as Categories
import com.tokopedia.troubleshooter.notification.ui.uiview.ConfigState.Ringtone as Ringtone
import com.tokopedia.troubleshooter.notification.ui.uiview.StatusState.Loading as Loading

data class ConfigUIView(
        val state: ConfigState = PushNotification,
        val title: String = "",
        var status: StatusState = Loading
): Visitable<TroubleshooterTypeFactory> {

    override fun type(typeFactory: TroubleshooterTypeFactory): Int {
        return typeFactory.type(this)
    }

    companion object {
        fun items(): List<ConfigUIView> {
            return arrayListOf(
                    ConfigUIView(PushNotification, "Push Notification"),
                    ConfigUIView(Notification, "Penganturan Notifikasi"),
                    ConfigUIView(Categories, "Penganturan Kategori"),
                    ConfigUIView(Ringtone, "Ringtone")
            )
        }
    }

}