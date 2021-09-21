package com.tokopedia.settingnotif.usersetting.view.adapter.viewholder

import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.ADD_EMAIL
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.ADD_PHONE
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.data.pojo.NotificationActivation
import com.tokopedia.settingnotif.usersetting.state.Email
import com.tokopedia.settingnotif.usersetting.state.Phone
import com.tokopedia.settingnotif.usersetting.state.PushNotif
import com.tokopedia.settingnotif.usersetting.util.CacheManager.KEY_PREF_DATE
import com.tokopedia.settingnotif.usersetting.util.CacheManager.getCacheLong
import com.tokopedia.settingnotif.usersetting.util.intent
import com.tokopedia.settingnotif.usersetting.util.notificationSetting
import com.tokopedia.settingnotif.usersetting.util.toLastCheckFormat
import com.tokopedia.settingnotif.usersetting.view.listener.ActivationItemListener
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.settingnotif.usersetting.state.NotificationItemState.Troubleshooter as Troubleshooter
import java.lang.System.currentTimeMillis as currentTimeMillis
import java.util.concurrent.TimeUnit.DAYS as DAYS
import java.util.concurrent.TimeUnit.MILLISECONDS as MILLISECONDS

class ActivationItemViewHolder(
        private val listener: ActivationItemListener,
        itemView: View?
): AbstractViewHolder<NotificationActivation>(itemView) {

    private val txtTitle = itemView?.findViewById<Typography>(R.id.txtTitle)
    private val txtDescription = itemView?.findViewById<Typography>(R.id.txtDescription)
    private val txtLastChecked = itemView?.findViewById<Typography>(R.id.txtLastChecked)
    private val btnActivation = itemView?.findViewById<UnifyButton>(R.id.btnActivation)
    private val btnArrow = itemView?.findViewById<ImageView>(R.id.btnArrow)

    private val context by lazy { itemView?.context }

    override fun bind(element: NotificationActivation?) {
        element?.let { data ->
            txtTitle?.text = context?.getString(data.title)
            txtDescription?.text = context?.getString(data.description)

            if (data.type == Troubleshooter) {
                showTroubleshooter()
                itemView.setOnClickListener {
                    listener.onTroubleshooterClicked()
                }
            } else {
                hideTroubleshooter()
                btnActivation?.text = context?.getString(data.action)
                btnActivation?.setOnClickListener {
                    context?.let {
                        when(data.type) {
                            is PushNotif -> it.startActivity(it.notificationSetting())
                            is Email -> it.startActivity(it.intent(ADD_EMAIL))
                            is Phone -> it.startActivity(it.intent(ADD_PHONE))
                        }
                    }
                }
            }
        }
    }

    private fun showTroubleshooter() {
        btnActivation?.hide()
        btnArrow?.show()
        setLastCheckedText()
    }

    private fun hideTroubleshooter() {
        btnArrow?.hide()
        btnActivation?.show()
        txtLastChecked?.text = ""
        txtLastChecked?.hide()
    }

    private fun setLastCheckedText() {
        val lastChecked = getCacheLong(context, KEY_PREF_DATE)
        if(lastChecked != 0L) {
            txtLastChecked?.show()
            val formattedDate = lastChecked.toLastCheckFormat()
            val amountDays = DAYS.convert((currentTimeMillis() - lastChecked), MILLISECONDS)
            txtLastChecked?.text = if (amountDays == 0L) {
                context?.getString(R.string.settingnotif_timestamp_today_ts, formattedDate)
            } else {
                context?.getString(R.string.settingnotif_timestamp_ts, amountDays, formattedDate)
            }
        } else {
            txtLastChecked?.hide()
            txtTitle?.text = context?.getString(R.string.settingnotif_title_troubleshooter)
            txtDescription?.text = context?.getString(R.string.settingnotif_desc_troubleshooter)
        }
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.item_push_notif_activation
    }

}