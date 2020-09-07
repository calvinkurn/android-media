package com.tokopedia.troubleshooter.notification.ui.state

import android.app.NotificationManager
import android.net.Uri
import android.os.Build
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.troubleshooter.notification.R
import com.tokopedia.troubleshooter.notification.ui.adapter.factory.TroubleshooterTypeFactory
import com.tokopedia.troubleshooter.notification.ui.state.ConfigState.Device as Device
import com.tokopedia.troubleshooter.notification.ui.state.ConfigState.PushNotification as PushNotification
import com.tokopedia.troubleshooter.notification.ui.state.ConfigState.Notification as Notification
import com.tokopedia.troubleshooter.notification.ui.state.ConfigState.Ringtone as Ringtone
import com.tokopedia.troubleshooter.notification.ui.state.ConfigState.Undefined as Undefined
import com.tokopedia.troubleshooter.notification.ui.state.StatusState.Loading as Loading
import com.tokopedia.troubleshooter.notification.ui.state.StatusState.Success as Success
import com.tokopedia.troubleshooter.notification.ui.state.StatusState.Warning as Warning

data class ConfigUIView(
        val state: ConfigState = PushNotification,
        var title: Int,
        var status: StatusState = Loading,
        var ringtone: Uri? = null
): Visitable<TroubleshooterTypeFactory> {

    override fun type(typeFactory: TroubleshooterTypeFactory): Int {
        return typeFactory.type(this)
    }

    companion object {
        fun items(): List<ConfigUIView> {
            return arrayListOf(
                    ConfigUIView(Notification, R.string.notif_loading_menu_notification),
                    ConfigUIView(Device, R.string.notif_loading_menu_device),
                    ConfigUIView(Ringtone, R.string.notif_loading_menu_ringtone),
                    ConfigUIView(PushNotification, R.string.notif_loading_menu_push)
            )
        }

        fun itemMessage(view: ConfigUIView): Int {
            return when(view.state) {
                is Notification -> {
                    if (view.status == Success || view.status == Warning) {
                        R.string.notif_menu_notification
                    } else {
                        R.string.notif_failed_menu_notification
                    }
                }
                is Device -> {
                    if (view.status == Success || view.status == Warning) {
                        R.string.notif_menu_device
                    } else {
                        R.string.notif_failed_menu_device
                    }
                }
                is Ringtone -> {
                    if (view.status == Success || view.status == Warning) {
                        R.string.notif_menu_ringtone
                    } else {
                        R.string.notif_failed_menu_ringtone
                    }
                }
                is PushNotification -> {
                    if (view.status == Success || view.status == Warning) {
                        R.string.notif_menu_push
                    } else {
                        R.string.notif_failed_menu_push
                    }
                }
                is Undefined -> 0
            }
        }

        fun importantNotification(importance: Int?): Boolean {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                importance == NotificationManager.IMPORTANCE_HIGH
                    || importance == NotificationManager.IMPORTANCE_DEFAULT
            } else {
                false
            }
        }
    }

}