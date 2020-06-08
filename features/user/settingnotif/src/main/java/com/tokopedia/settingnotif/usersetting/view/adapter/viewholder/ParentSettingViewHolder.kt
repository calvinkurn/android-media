package com.tokopedia.settingnotif.usersetting.view.adapter.viewholder

import androidx.annotation.LayoutRes
import android.view.View
import android.widget.Switch
import android.widget.TextView
import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.data.pojo.ParentSetting
import com.tokopedia.settingnotif.usersetting.util.componentTextColor
import androidx.core.content.ContextCompat.getColor as color

class ParentSettingViewHolder(
        itemView: View?,
        settingListener: SettingListener
) : SettingViewHolder<ParentSetting>(itemView, settingListener) {

    private val switchTitle: TextView? = itemView?.findViewById(R.id.tv_sw_title)
    private val description: TextView? = itemView?.findViewById(R.id.tv_desc)

    private val context by lazy { itemView?.context }

    override fun getSwitchView(itemView: View?): Switch? {
        return itemView?.findViewById(R.id.sw_setting)
    }

    override fun bind(element: ParentSetting?) {
        if (element == null) return
        setSettingTitle(element)
        setSettingDesc(element)
        switchComponentHandler(element)
        super.bind(element)
    }

    private fun switchComponentHandler(element: ParentSetting?) {
        element?.let { setting ->
            // change switch state
            getSwitchView(itemView)?.isEnabled = setting.isEnabled

            // change text color
            context?.let {
                val colorId = componentTextColor(setting.isEnabled)
                switchTitle?.setTextColor(color(it, colorId))
            }
        }
    }

    private fun setSettingTitle(element: ParentSetting) {
        switchTitle?.text = element.name
    }

    private fun setSettingDesc(element: ParentSetting) {
        if (element.hasDescription()) {
            description?.visibility = View.VISIBLE
            description?.text = element.description
        } else {
            description?.visibility = View.GONE
        }
    }

    override fun updateSiblingAndChild(element: ParentSetting, checked: Boolean) {
        if (!element.hasChild()) return

        val childSettings = element.childSettings
        val childToChangeIndex = arrayListOf<Int>()
        childSettings.forEachIndexed { index, childSetting ->
            childSetting?.let {
                if (!childSetting.hasSameCheckedStatusWith(checked)) {
                    childSetting.status = checked
                    childToChangeIndex.add(index)
                }
            }
        }

        if (childToChangeIndex.isEmpty()) return

        val currentSettingPosition = adapterPosition
        val childToChangeAdapterIndex = childToChangeIndex.map {
            index -> currentSettingPosition + 1 + index
        }
        settingListener.updateSettingView(childToChangeAdapterIndex)
    }

    override fun getUpdatedSettingIds(element: ParentSetting, checked: Boolean): List<Map<String, Any>> {
        val settingsToChange = arrayListOf<Map<String, Any>>()
        if (element.hasChild()) {
            element.childSettings.forEach { childSetting ->
                if (childSetting != null) {
                    val setting = getMapSettingToChange(childSetting, checked)
                    settingsToChange.add(setting)
                }
            }
        } else {
            val setting = getMapSettingToChange(element, checked)
            settingsToChange.add(setting)
        }
        return settingsToChange
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.item_parent_setting
    }
}