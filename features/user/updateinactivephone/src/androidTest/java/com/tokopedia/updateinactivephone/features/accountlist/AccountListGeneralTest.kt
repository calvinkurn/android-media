package com.tokopedia.updateinactivephone.features.accountlist

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.updateinactivephone.domain.data.AccountListDataModel
import com.tokopedia.updateinactivephone.features.accountlist.AccountListViewAction.checkAccountListIsDisplayed
import com.tokopedia.updateinactivephone.features.accountlist.AccountListViewAction.clickAccountListItemAtPosition
import com.tokopedia.updateinactivephone.features.accountlist.AccountListViewAction.isAccountListTextItemDisplayed
import com.tokopedia.updateinactivephone.features.accountlist.AccountListViewAction.smoothScrollAccountListToPosition
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class AccountListGeneralTest : BaseAccountListTest() {

    var userDetails: MutableList<AccountListDataModel.UserDetailDataModel>? = mutableListOf()
    val position = 3

    @Test
    fun show_account_list_and_check_item_list() {
        // Given
        inactivePhoneDependency.apply {
            getAccountListUseCaseStub.response = accountListDataModel
            userDetails = accountListDataModel.accountList.userDetailDataModels
        }

        startAccountListActivity()

        // Then
        val size = userDetails?.size.orZero()
        if (size > 0) {
            checkAccountListIsDisplayed()
            smoothScrollAccountListToPosition(position)
            isAccountListTextItemDisplayed(userDetails?.get(position)?.fullname.toString())
        }
    }

    @Test
    fun scroll_to_down() {
        // Given
        inactivePhoneDependency.apply {
            getAccountListUseCaseStub.response = accountListDataModel
        }

        startAccountListActivity()

        // Then
        val size = userDetails?.size.orZero()
        if (size > 0) {
            checkAccountListIsDisplayed()
            smoothScrollAccountListToPosition(size - 1)
        }
    }

    @Test
    fun scroll_to_position() {
        inactivePhoneDependency.apply {
            getAccountListUseCaseStub.response = accountListDataModel
        }

        startAccountListActivity()

        val size = userDetails?.size.orZero()
        if (size > 0) {
            checkAccountListIsDisplayed()
            smoothScrollAccountListToPosition(position)
        }
    }

    @Test
    fun click_on_position() {
        // Given
        inactivePhoneDependency.apply {
            getAccountListUseCaseStub.response = accountListDataModel
        }

        startAccountListActivity()

        // Then
        val size = userDetails?.size.orZero()
        if (size > 0) {
            checkAccountListIsDisplayed()
            smoothScrollAccountListToPosition(position)
            clickAccountListItemAtPosition(position)
        }
    }
}