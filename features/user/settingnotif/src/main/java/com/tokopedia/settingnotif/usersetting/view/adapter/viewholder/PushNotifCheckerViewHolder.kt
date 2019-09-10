package com.tokopedia.settingnotif.usersetting.view.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import android.widget.Button
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.domain.pojo.PushNotifierTroubleshooterSetting
import com.tokopedia.settingnotif.usersetting.view.listener.SettingFieldContract
import kotlinx.android.synthetic.main.item_push_notif_checker.view.*

class PushNotifCheckerViewHolder(itemView: View): AbstractViewHolder<PushNotifierTroubleshooterSetting>(itemView) {

    private val btnPushNotifPage: Button = itemView.findViewById(R.id.btn_goto_push_notif)
    private lateinit var settingFieldContract: SettingFieldContract.View

    override fun bind(element: PushNotifierTroubleshooterSetting?) {
        itemView.btn_goto_push_notif.setOnClickListener {
            settingFieldContract.onGoToPushNotifCheckerPage()
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_push_notif_checker
    }
}