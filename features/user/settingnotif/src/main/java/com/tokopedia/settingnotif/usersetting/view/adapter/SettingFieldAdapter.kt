package com.tokopedia.settingnotif.usersetting.view.adapter

import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.removeFirst
import com.tokopedia.settingnotif.usersetting.data.pojo.NotificationActivation
import com.tokopedia.settingnotif.usersetting.data.pojo.ParentSetting
import com.tokopedia.settingnotif.usersetting.data.pojo.SellerSection
import com.tokopedia.settingnotif.usersetting.data.pojo.SettingSections
import com.tokopedia.settingnotif.usersetting.view.adapter.factory.SettingFieldTypeFactory
import com.tokopedia.settingnotif.usersetting.view.adapter.viewholder.SettingViewHolder

class SettingFieldAdapter<T : Visitable<SettingFieldTypeFactory>>(
        private val notificationType: String,
        private val settingFieldAdapterListener: SettingFieldAdapterListener,
        private val baseListAdapterTypeFactory: SettingFieldTypeFactory,
        onAdapterInteractionListener: OnAdapterInteractionListener<T>?
) : BaseListAdapter<T, SettingFieldTypeFactory>(baseListAdapterTypeFactory, onAdapterInteractionListener),
        SettingViewHolder.SettingListener {

    interface SettingFieldAdapterListener {
        fun requestUpdateUserSetting(notificationType: String, updatedSettingIds: List<Map<String, Any>>)
        fun updateSettingState(setting: ParentSetting?)
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

    private inline fun getParentSettingByIndex(index: Int, get: (setting: ParentSetting?) -> Unit) {
        if (visitables[index] is ParentSetting) {
            get(visitables[index] as ParentSetting)
        }
    }

    override fun getNotificationType(): String {
        return notificationType
    }

    override fun requestUpdateUserSetting(notificationType: String, updatedSettingIds: List<Map<String, Any>>) {
        settingFieldAdapterListener.requestUpdateUserSetting(notificationType, updatedSettingIds)
    }

    override fun updateParentSettingLastState(position: Int) {
        // this is will used for update temporary state
        getParentSettingByIndex(position) {
            settingFieldAdapterListener.updateSettingState(it)
        }
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

    fun addPinnedActivation(pinned: NotificationActivation) {
        val indexPinned = 0
        removeFirstPinnedActivation()
        addElement(indexPinned, pinned)
    }

    fun enableSwitchComponent(temporaryList: List<ParentSetting>) {
        if (temporaryList.isEmpty()) return
        if (visitables == temporaryList) return

        // setting section
        visitableFilter<SettingSections>().map {
            it.isEnabled = true
        }

        // seller section
        visitableFilter<SellerSection>().map {
            it.isEnabled = true
        }

        // notification setting handler
        visitableFilter<ParentSetting>()
                .zip(temporaryList)
                .forEach {
                    val currentItem = it.first
                    val lastConfigItem = it.second
                    currentItem.status = lastConfigItem.status
                    currentItem.isEnabled = true
                }

        notifyDataSetChanged()
    }

    private inline fun <reified T> visitableFilter(): List<T> {
        return visitables.filterIsInstance<T>()
    }

    fun disableSwitchComponent() {
        visitables.forEach {
            when (it) {
                is SettingSections -> it.isEnabled = false
                is SellerSection -> it.isEnabled = false
                is ParentSetting -> {
                    it.isEnabled = false
                    it.status = false
                }
            }
        }
        notifyDataSetChanged()
    }

}