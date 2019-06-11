package com.tokopedia.settingnotif.usersetting.view.adapter.viewholder

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.view.viewmodel.SettingType

class SettingTypeViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

    private val settingTypeName: TextView? = itemView?.findViewById(R.id.tv_setting_type)

    fun bind(settingType: SettingType) {
        settingTypeName?.text = settingType.name
    }

    companion object {
        fun createViewHolder(viewGroup: ViewGroup): SettingTypeViewHolder {
            val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_setting_type, viewGroup, false)
            return SettingTypeViewHolder(view)
        }
    }

}