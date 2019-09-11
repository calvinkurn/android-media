package com.tokopedia.settingnotif.usersetting.view.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import android.widget.Button
import android.widget.Switch
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.domain.pojo.PushNotifierTroubleshooterSetting
import com.tokopedia.settingnotif.usersetting.view.listener.SettingFieldContract
import kotlinx.android.synthetic.main.item_push_notif_checker.view.*

class PushNotifCheckerViewHolder(
        val v: View,
        val settingListener: SettingViewHolder.SettingListener
) : AbstractViewHolder<PushNotifierTroubleshooterSetting>(v) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_push_notif_checker
    }

//    interface SettingListener {
//        fun goToPushNotifCheckerPage()
//    }

//    private lateinit var settingFieldContract: SettingFieldContract.View

    override fun bind(element: PushNotifierTroubleshooterSetting?) {
        element?.run {
            itemView.setOnClickListener {
                settingListener.goToPushNotificationChecker()
            }
        }

//        if (::settingFieldContract.isInitialized) {
//            itemView.btn_goto_push_notif.setOnClickListener {
//                settingFieldContract?.onGoToPushNotifCheckerPage()
//            }
//        }
    }
}