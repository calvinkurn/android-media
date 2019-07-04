package com.tokopedia.settingnotif.usersetting.view.adapter.viewholder

import android.view.View
import android.widget.Switch
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.settingnotif.usersetting.domain.pojo.BaseSetting
import com.tokopedia.settingnotif.usersetting.domain.pojo.ParentSetting

abstract class SettingViewHolder<T : BaseSetting>(
        itemView: View?,
        val settingListener: SettingListener
) : AbstractViewHolder<T>(itemView) {

    protected val settingSwitch: Switch? = getSwitchView(itemView)

    private val PARAM_SETTING_KEY = "name"
    private val PARAM_SETTING_VALUE = "value"

    abstract fun getSwitchView(itemView: View?): Switch?

    interface SettingListener {
        fun updateSettingView(positions: List<Int>)
        fun getParentSetting(childAdapterPosition: Int): Pair<ParentSetting, Int>?
        fun getNotificationType(): String
        fun requestUpdateUserSetting(notificationType: String, updatedSettingIds: List<Map<String, Any>>)
    }

    override fun bind(element: T?) {
        if (element == null) return

        settingSwitch?.isChecked = element.status
        settingSwitch?.setOnCheckedChangeListener { _, isChecked ->
            if (element.status != isChecked) {
                element.status = isChecked
                updateSiblingAndChild(element, isChecked)
                updateUserSetting(element, isChecked)
            }
        }
    }

    override fun bind(element: T, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) return

        val payload = payloads[0]
        if (payload == PAYLOAD_SWITCH) {
            settingSwitch?.isChecked = !settingSwitch?.isChecked!!
        }
    }

    abstract fun updateSiblingAndChild(element: T, checked: Boolean)

    private fun updateUserSetting(element: T, checked: Boolean) {
        val notificationType: String = settingListener.getNotificationType()
        val updatedSettingIds: List<Map<String, Any>> = getUpdatedSettingIds(element, checked)
        settingListener.requestUpdateUserSetting(notificationType, updatedSettingIds)
    }

    protected fun getMapSettingToChange(element: BaseSetting, checked: Boolean) : Map<String, Any> {
        return mapOf(
                Pair(PARAM_SETTING_KEY, element.key),
                Pair(PARAM_SETTING_VALUE, checked)
        )
    }

    abstract fun getUpdatedSettingIds(element: T, checked: Boolean): List<Map<String, Any>>

    companion object {
        val PAYLOAD_SWITCH = "payload_switch"
    }

}