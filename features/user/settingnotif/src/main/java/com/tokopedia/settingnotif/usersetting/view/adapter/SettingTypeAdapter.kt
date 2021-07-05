package com.tokopedia.settingnotif.usersetting.view.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import com.tokopedia.settingnotif.usersetting.view.adapter.viewholder.SettingTypeViewHolder
import com.tokopedia.settingnotif.usersetting.view.fragment.SettingTypeFragment
import com.tokopedia.settingnotif.usersetting.view.dataview.SettingTypeDataView

class SettingTypeAdapter(
        private val data: List<SettingTypeDataView>,
        private val settingTypeContract: SettingTypeFragment.SettingTypeContract?
) : RecyclerView.Adapter<SettingTypeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingTypeViewHolder {
        return SettingTypeViewHolder.createViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: SettingTypeViewHolder, position: Int) {
        holder.bind(data[position], settingTypeContract)
    }

}