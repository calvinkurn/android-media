package com.tokopedia.updateinactivephone.features.accountlist

import com.tokopedia.test.application.matcher.hasTotalItemOf
import com.tokopedia.updateinactivephone.common.viewaction.*
import com.tokopedia.updateinactivephone.test.R

object AccountListViewAction {

    fun checkAccountListIsDisplayed() {
        isDisplayed(R.id.recyclerViewAccountList)
    }

    fun clickAccountListItemAtPosition(position: Int) {
        clickOnPosition(R.id.recyclerViewAccountList, R.id.mainView, position)
    }

    fun isAccountListTextItemDisplayed(text: String, position: Int) {
        isChildTextDisplayed(
            R.id.recyclerViewAccountList,
            R.id.txtName,
            position,
            text
        )
    }

    fun scrollAccountListToPosition(position: Int) {
        scrollToPosition(R.id.recyclerViewAccountList, position)
    }

    fun smoothScrollAccountListToPosition(position: Int) {
        smoothScrollToPosition(R.id.recyclerViewAccountList, position)
    }

    fun assertAccountListItem(size: Int) {
        assertRecyclerViewItem(R.id.recyclerViewAccountList, hasTotalItemOf(size))
    }
}