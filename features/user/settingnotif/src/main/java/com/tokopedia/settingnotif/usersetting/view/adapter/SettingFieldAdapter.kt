package com.tokopedia.settingnotif.usersetting.view.adapter

import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.settingnotif.usersetting.domain.pojo.ParentSetting
import com.tokopedia.settingnotif.usersetting.view.adapter.viewholder.SettingViewHolder

class SettingFieldAdapter<T : Visitable<SettingFieldTypeFactory>>(
        private val baseListAdapterTypeFactory: SettingFieldTypeFactory,
        onAdapterInteractionListener: OnAdapterInteractionListener<T>?
) : BaseListAdapter<T, SettingFieldTypeFactory>(baseListAdapterTypeFactory, onAdapterInteractionListener),
        SettingViewHolder.SettingListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<out Visitable<*>> {
        val view = onCreateViewItem(parent, viewType)
        return baseListAdapterTypeFactory.createViewHolder(view, viewType, this)
    }

    override fun updateSettingView(positions: List<Int>) {
        for (position in positions) {
            notifyItemChanged(position, SettingViewHolder.PAYLOAD_SWITCH)
        }
    }

    override fun getParentSetting(childAdapterPosition: Int): Pair<ParentSetting, Int>? {
        for (index in (childAdapterPosition - 1) downTo 0) {
            if (visitables[index] is ParentSetting) {
                val parentSetting = visitables[index] as ParentSetting
                return Pair(parentSetting, index)
            }
        }
        return null
    }

}