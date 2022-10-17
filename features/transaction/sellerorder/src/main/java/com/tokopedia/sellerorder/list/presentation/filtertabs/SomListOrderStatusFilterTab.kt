package com.tokopedia.sellerorder.list.presentation.filtertabs

import com.google.android.material.tabs.TabLayout
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.list.presentation.models.SomListFilterUiModel
import com.tokopedia.unifycomponents.TabsUnify
import com.tokopedia.unifycomponents.getCustomText
import com.tokopedia.unifycomponents.setCustomText

class SomListOrderStatusFilterTab(
    private val tabs: TabsUnify,
    private val listener: Listener
) {

    private val tabSelectedListener: TabSelectedListener = TabSelectedListener()
    private var somListFilterUiModel: SomListFilterUiModel? = null
    private var selectedTab: SomListFilterUiModel.Status? = null
    private var filterTabs: ArrayList<TabLayout.Tab> = arrayListOf()

    private fun updateTabs(statusList: List<SomListFilterUiModel.Status>) {
        tabs.getUnifyTabLayout().removeOnTabSelectedListener(tabSelectedListener)
        if (filterTabsChanged(statusList)) {
            recreateTabs(statusList)
        } else {
            updateTabsCounter(statusList)
        }
        updateSelectedTab(statusList.find { it.isChecked })
        tabs.getUnifyTabLayout().addOnTabSelectedListener(tabSelectedListener)
    }

    private fun filterTabsChanged(statusList: List<SomListFilterUiModel.Status>): Boolean {
        statusList.forEachIndexed { index, status ->
            val filterTabTheSame = filterTabs.getOrNull(index)?.getCustomText()?.contains(status.status).orFalse()
            if (!filterTabTheSame) return true
        }
        return false
    }

    private fun updateTabsCounter(statusList: List<SomListFilterUiModel.Status>) {
        statusList.forEachIndexed { index, status ->
            filterTabs[index].run {
                setCustomText(createNewTabs(status))
                if (status.isChecked && !isSelected) select()
            }
        }
    }

    private fun recreateTabs(statusList: List<SomListFilterUiModel.Status>) {
        tabs.getUnifyTabLayout().removeAllTabs()
        filterTabs.clear()
        statusList.forEach { filterTabs.add(tabs.addNewTab(createNewTabs(it), it.isChecked)) }
    }

    private fun createNewTabs(statusFilter: SomListFilterUiModel.Status): String {
        return composeTabTitle(statusFilter.key, statusFilter.status, statusFilter.amount)
    }

    private fun composeTabTitle(key: String, status: String, amount: Int): String {
        return when (key) {
            SomConsts.STATUS_NEW_ORDER, SomConsts.KEY_CONFIRM_SHIPPING, SomConsts.KEY_STATUS_COMPLAINT -> "$status${" ($amount)".takeIf { amount > 0 } ?: ""}"
            else -> status
        }
    }

    private fun updateSelectedTab(status: SomListFilterUiModel.Status?) {
        selectedTab = status
    }

    fun show(newSomListFilterUiModel: SomListFilterUiModel) {
        somListFilterUiModel = newSomListFilterUiModel
        updateTabs(newSomListFilterUiModel.statusList)
        tabs.show()
    }

    fun shouldShowBulkAction() = (selectedTab?.key == SomConsts.STATUS_NEW_ORDER || selectedTab?.key == SomConsts.KEY_CONFIRM_SHIPPING) && GlobalConfig.isSellerApp()
    fun isNewOrderFilterSelected(): Boolean = selectedTab?.key == SomConsts.STATUS_NEW_ORDER
    fun getSelectedFilterOrderCount(): Int = selectedTab?.amount.orZero()
    fun getSelectedFilterStatus(): String = selectedTab?.key.orEmpty()
    fun getSelectedFilterStatusName(): String = selectedTab?.status.orEmpty()

    interface Listener {
        fun onClickOrderStatusFilterTab(
            status: SomListFilterUiModel.Status,
            shouldScrollToTop: Boolean
        )
    }

    inner class TabSelectedListener: TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            somListFilterUiModel?.statusList?.getOrNull(tab?.position ?: -1)?.let { selected ->
                selected.isChecked = true
                listener.onClickOrderStatusFilterTab(
                    status = selected,
                    shouldScrollToTop = true
                )
            }
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
            somListFilterUiModel?.statusList?.getOrNull(tab?.position ?: -1)?.isChecked = false
        }

        override fun onTabReselected(tab: TabLayout.Tab?) {}
    }
}