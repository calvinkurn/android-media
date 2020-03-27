package com.tokopedia.settingnotif.usersetting.view.adapter

import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.removeFirst
import com.tokopedia.settingnotif.usersetting.domain.pojo.NotificationActivation
import com.tokopedia.settingnotif.usersetting.domain.pojo.ParentSetting
import com.tokopedia.settingnotif.usersetting.view.adapter.factory.SettingFieldTypeFactory
import com.tokopedia.settingnotif.usersetting.view.adapter.viewholder.SettingViewHolder

typealias ItemAdapter = SettingFieldAdapter<Visitable<SettingFieldTypeFactory>>

class SettingFieldAdapter<T : Visitable<SettingFieldTypeFactory>>(
        private val notificationType: String,
        private val settingFieldAdapterListener: SettingFieldAdapterListener,
        private val baseListAdapterTypeFactory: SettingFieldTypeFactory,
        onAdapterInteractionListener: OnAdapterInteractionListener<T>?
) : BaseListAdapter<T, SettingFieldTypeFactory>(baseListAdapterTypeFactory, onAdapterInteractionListener),
        SettingViewHolder.SettingListener {

    interface SettingFieldAdapterListener {
        fun requestUpdateUserSetting(notificationType: String, updatedSettingIds: List<Map<String, Any>>)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<out Visitable<*>> {
        val view = onCreateViewItem(parent, viewType)
        return baseListAdapterTypeFactory.createViewHolder(view, viewType, this)
    }

    override fun updateSettingView(positions: List<Int>) {
        if (positions.isEmpty()) return
        if (positions.size == 1) {
            notifySinglePosition(positions[0])
        } else if (positions.size > 1) {
            notifyRangedPosition(positions)
        }
    }

    private fun notifySinglePosition(position: Int) {
        notifyItemChanged(position, SettingViewHolder.PAYLOAD_SWITCH)
    }

    private fun notifyRangedPosition(positions: List<Int>) {
        val sortedPosition = positions.sorted()
        val endArrayIndex = sortedPosition.size - 1
        val startPosition = sortedPosition.first()
        val endPosition = sortedPosition[endArrayIndex]
        notifyItemRangeChanged(startPosition, endPosition, SettingViewHolder.PAYLOAD_SWITCH)
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

    override fun getNotificationType(): String {
        return notificationType
    }

    override fun requestUpdateUserSetting(notificationType: String, updatedSettingIds: List<Map<String, Any>>) {
        settingFieldAdapterListener.requestUpdateUserSetting(notificationType, updatedSettingIds)
    }

    fun removeNotificationPermission() {
        visitables.removeFirst { it is NotificationActivation }
        notifyDataSetChanged()
    }

}