package com.tokopedia.settingnotif.usersetting.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.kotlin.extensions.view.loadImageDrawable
import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.view.fragment.SettingTypeFragment
import com.tokopedia.settingnotif.usersetting.view.viewmodel.SettingType

class SettingTypeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val settingTypeIcon: ImageView? = itemView.findViewById(R.id.img_main_setting)
    private val settingTypeName: TextView? = itemView.findViewById(R.id.tv_setting_type)

    fun bind(settingType: SettingType, settingTypeContract: SettingTypeFragment.SettingTypeContract) {
        settingTypeIcon?.loadImageDrawable(settingType.icon)
        settingTypeName?.text = itemView.context.getString(settingType.name)

        itemView.setOnClickListener {
            settingTypeContract.openSettingField(settingType)
        }
    }

    companion object {
        fun createViewHolder(viewGroup: ViewGroup): SettingTypeViewHolder {
            val view = LayoutInflater
                    .from(viewGroup.context)
                    .inflate(R.layout.item_setting_type, viewGroup, false)
            return SettingTypeViewHolder(view)
        }
    }

}