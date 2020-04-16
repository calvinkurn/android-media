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
import com.tokopedia.settingnotif.usersetting.view.dataview.NotificationActivationDataView.activationPushNotif

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
            notifySinglePosition(positions.first())
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

    private fun removeFirstPinnedActivation() {
        visitables.removeFirst {
            it is NotificationActivation
        }
    }

    fun removePinnedActivation() {
        removeFirstPinnedActivation()
        notifyDataSetChanged()
    }

    fun addPinnedActivation() {
        val indexPinned = 0
        removeFirstPinnedActivation()
        addElement(indexPinned, activationPushNotif())
    }

    fun enableSwitchComponent(temporaryList: List<Visitable<*>>) {
        if (temporaryList.isEmpty()) return

        val visitableList = mutableListOf<Visitable<*>>()
        visitables.zip(temporaryList).forEach {
            if (it.first is ParentSetting && it.second is ParentSetting) {
                val currentItem = it.first as ParentSetting
                val lastConfigItem = it.second as ParentSetting
                currentItem.status = lastConfigItem.status
                currentItem.isEnabled = true
            }
            visitableList.add(it.first)
        }

        visitables = visitableList
        notifyDataSetChanged()
    }

    fun disableSwitchComponent() {
        visitables.forEach {
            if (it is ParentSetting) {
                it.isEnabled = false
                it.status = false
            }
        }
        notifyDataSetChanged()
    }

}