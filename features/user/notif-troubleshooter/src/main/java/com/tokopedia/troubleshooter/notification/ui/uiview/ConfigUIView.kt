package com.tokopedia.troubleshooter.notification.ui.uiview

import android.app.NotificationManager
import android.net.Uri
import android.os.Build
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.troubleshooter.notification.ui.adapter.factory.TroubleshooterTypeFactory
import com.tokopedia.troubleshooter.notification.ui.uiview.ConfigState.PushNotification as PushNotification
import com.tokopedia.troubleshooter.notification.ui.uiview.ConfigState.Notification as Notification
import com.tokopedia.troubleshooter.notification.ui.uiview.ConfigState.Categories as Categories
import com.tokopedia.troubleshooter.notification.ui.uiview.ConfigState.Ringtone as Ringtone
import com.tokopedia.troubleshooter.notification.ui.uiview.StatusState.Loading as Loading

data class ConfigUIView(
        val state: ConfigState = PushNotification,
        var title: String = "",
        var message: String = "",
        var status: StatusState = Loading,
        var ringtone: Uri? = null,
        var visibility: Boolean = true
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

        fun importantNotification(importance: Int): Boolean {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                importance == NotificationManager.IMPORTANCE_HIGH
                    || importance == NotificationManager.IMPORTANCE_DEFAULT
            } else {
                false
            }
        }
    }

}