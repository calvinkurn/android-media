package com.tokopedia.settingnotif.usersetting.view.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.domain.pojo.PushNotifierTroubleshooterSetting


class PushNotifCheckerViewHolder(
        val v: View,
        val settingListener: SettingViewHolder.SettingListener
) : AbstractViewHolder<PushNotifierTroubleshooterSetting>(v) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_push_notif_checker
    }

    override fun bind(element: PushNotifierTroubleshooterSetting?) {
        element?.run {
            itemView.setOnClickListener {
                settingListener.goToPushNotificationChecker()
            }
        }
    }
}