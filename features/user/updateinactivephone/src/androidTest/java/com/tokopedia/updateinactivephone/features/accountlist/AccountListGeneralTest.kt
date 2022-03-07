package com.tokopedia.updateinactivephone.features.accountlist

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.updateinactivephone.domain.data.AccountListDataModel
import com.tokopedia.updateinactivephone.domain.data.InactivePhoneUserDataModel
import com.tokopedia.updateinactivephone.features.accountlist.AccountListViewAction.assertAccountListItem
import com.tokopedia.updateinactivephone.features.accountlist.AccountListViewAction.checkAccountListIsDisplayed
import com.tokopedia.updateinactivephone.features.accountlist.AccountListViewAction.clickAccountListItemAtPosition
import com.tokopedia.updateinactivephone.features.accountlist.AccountListViewAction.isAccountListTextItemDisplayed
import com.tokopedia.updateinactivephone.features.accountlist.AccountListViewAction.smoothScrollAccountListToPosition
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class AccountListGeneralTest : BaseAccountListTest() {

    private val position = 10
    private var userDetails: MutableList<AccountListDataModel.UserDetailDataModel> = mutableListOf()

    override fun before() {
        super.before()
        inactivePhoneDependency.apply {
            getAccountListUseCaseStub.response = accountListDataModel
        }
        userDetails = inactivePhoneDependency.accountListDataModel.accountList.userDetailDataModels
    }

    @Test
    fun show_account_list_and_check_item_list() {
        runTest {
            checkAccountListIsDisplayed()
            assertAccountListItem(userDetails.size)
            smoothScrollAccountListToPosition(position)
            isAccountListTextItemDisplayed(userDetails[position].fullname, position)
        }
    }

    @Test
    fun scroll_to_down() {
        runTest {
            checkAccountListIsDisplayed()
            smoothScrollAccountListToPosition(userDetails.size - 1)
        }
    }

    @Test
    fun scroll_to_position() {
        runTest {
            checkAccountListIsDisplayed()
            smoothScrollAccountListToPosition(position)
        }
    }

    @Test
    fun click_on_position() {
        runTest {
            checkAccountListIsDisplayed()
            smoothScrollAccountListToPosition(position)
            clickAccountListItemAtPosition(position)
        }
    }
}