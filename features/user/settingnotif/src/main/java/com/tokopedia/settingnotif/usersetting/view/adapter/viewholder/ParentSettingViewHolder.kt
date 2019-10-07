package com.tokopedia.settingnotif.usersetting.view.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.domain.pojo.ParentSetting

class ParentSettingViewHolder(
        itemView: View?,
        settingListener: SettingListener
) : SettingViewHolder<ParentSetting>(itemView, settingListener) {

    private val settingIcon: ImageView? = itemView?.findViewById(R.id.iv_icon)
    private val switchTitle: TextView? = itemView?.findViewById(R.id.tv_sw_title)
    private val description: TextView? = itemView?.findViewById(R.id.tv_desc)

    override fun getSwitchView(itemView: View?): Switch? {
        return itemView?.findViewById(R.id.sw_setting)
    }

    override fun bind(element: ParentSetting?) {
        if (element == null) return
        super.bind(element)
        setSettingTitle(element)
        setSettingDesc(element)
        setSettingIcon(element)
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

    private fun setSettingIcon(element: ParentSetting) {
        val iconSettingUrl = element.icon
        ImageHandler.LoadImage(settingIcon, iconSettingUrl)
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
        val childToChangeAdapterIndex = childToChangeIndex.map { index -> currentSettingPosition + 1 + index }
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
        @LayoutRes
        val LAYOUT = R.layout.item_parent_setting
    }
}