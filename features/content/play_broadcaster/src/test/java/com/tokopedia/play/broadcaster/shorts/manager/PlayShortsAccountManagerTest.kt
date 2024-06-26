package com.tokopedia.play.broadcaster.shorts.manager

import com.tokopedia.content.common.types.ContentCommonUserType.TYPE_SHOP
import com.tokopedia.content.common.types.ContentCommonUserType.TYPE_USER
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.play.broadcaster.model.UiModelBuilder
import com.tokopedia.play.broadcaster.shorts.domain.manager.PlayShortsAccountManagerImpl
import com.tokopedia.play.broadcaster.util.assertEqualTo
import com.tokopedia.play.broadcaster.util.preference.HydraSharedPreferences
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created By : Jonathan Darwin on December 06, 2022
 */
class PlayShortsAccountManagerTest {

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers

    private val mockSharedPref: HydraSharedPreferences = mockk(relaxed = true)

    private val accountManager = PlayShortsAccountManagerImpl(
        sharedPref = mockSharedPref,
        dispatchers = testDispatcher,
    )

    private val uiModelBuilder = UiModelBuilder()

    private val mockAccountList = uiModelBuilder.buildAccountListModel()
    private val mockAccountListOnlyShopEligible = uiModelBuilder.buildAccountListModel(usernameBuyer = false, tncBuyer = false, tncShop = true)
    private val mockAccountListOnlyUserEligible = uiModelBuilder.buildAccountListModel(usernameBuyer = true, tncBuyer = true, tncShop = false)

    private val mockAccountShop = uiModelBuilder.buildAccountListModel(onlyShop = true)
    private val mockAccountShopNotEligible = uiModelBuilder.buildAccountListModel(onlyShop = true, tncShop = false)

    private val mockAccountUser = uiModelBuilder.buildAccountListModel(onlyBuyer = true)
    private val mockAccountUserNoUsername = uiModelBuilder.buildAccountListModel(onlyBuyer = true, usernameBuyer = false, tncBuyer = false)
    private val mockAccountUserNoTnc = uiModelBuilder.buildAccountListModel(onlyBuyer = true, usernameBuyer = true, tncBuyer = false)

    @Before
    fun setUp() {
        coEvery { mockSharedPref.getLastSelectedAccountType() } returns ""
    }

    @Test
    fun playShorts_accountManager_switchAccount_shop_to_ugc() {
        runBlockingTest(testDispatcher.coroutineDispatcher) {
            val account = accountManager.switchAccount(mockAccountList, TYPE_SHOP)

            account.assertEqualTo(mockAccountList.first { it.isUser })
        }
    }

    @Test
    fun playShorts_accountManager_switchAccount_ugc_to_shop() {
        runBlockingTest(testDispatcher.coroutineDispatcher) {
            val account = accountManager.switchAccount(mockAccountList, TYPE_USER)

            account.assertEqualTo(mockAccountList.first { it.isShop })
        }
    }

    @Test
    fun playShorts_accountManager_switchAccount_noCurrentSelectedAccount() {
        runBlockingTest(testDispatcher.coroutineDispatcher) {
            val account = accountManager.switchAccount(mockAccountList, "")

            account.assertEqualTo(mockAccountList.first())
        }
    }

    @Test
    fun playShorts_accountManager_switchAccount_accountNotFound() {
        runBlockingTest(testDispatcher.coroutineDispatcher) {
            val account = accountManager.switchAccount(mockAccountList, "random_account_type")

            account.assertEqualTo(mockAccountList.first())
        }
    }

    @Test
    fun playShorts_accountManager_switchAccount_onlyHas1Account() {
        runBlockingTest(testDispatcher.coroutineDispatcher) {
            val account = accountManager.switchAccount(mockAccountUser, TYPE_USER)

            account.assertEqualTo(mockAccountUser.first())
        }
    }

    @Test
    fun playShorts_accountManager_getBestEligibleAccount_noAccountList() {
        runBlockingTest(testDispatcher.coroutineDispatcher) {
            val account = accountManager.getBestEligibleAccount(listOf(), preferredAccountType = "")

            account.assertEqualTo(ContentAccountUiModel.Empty)
        }
    }

    @Test
    fun playShorts_accountManager_getBestEligibleAccount_onlyShop_eligible() {
        runBlockingTest(testDispatcher.coroutineDispatcher) {
            val account = accountManager.getBestEligibleAccount(mockAccountShop, preferredAccountType = "")

            account.assertEqualTo(mockAccountShop.first())
        }
    }

    @Test
    fun playShorts_accountManager_getBestEligibleAccount_onlyShop_notEligible() {
        runBlockingTest(testDispatcher.coroutineDispatcher) {
            val account = accountManager.getBestEligibleAccount(mockAccountShopNotEligible, preferredAccountType = "")

            account.assertEqualTo(mockAccountShopNotEligible.first())
        }
    }

    @Test
    fun playShorts_accountManager_getBestEligibleAccount_onlyUgc_eligible() {
        runBlockingTest(testDispatcher.coroutineDispatcher) {
            val account = accountManager.getBestEligibleAccount(mockAccountUser, preferredAccountType = "")

            account.assertEqualTo(mockAccountUser.first())
        }
    }

    @Test
    fun playShorts_accountManager_getBestEligibleAccount_onlyUgc_noUsername() {
        runBlockingTest(testDispatcher.coroutineDispatcher) {
            val account = accountManager.getBestEligibleAccount(mockAccountUserNoUsername, preferredAccountType = "")

            account.assertEqualTo(mockAccountUserNoUsername.first())
        }
    }

    @Test
    fun playShorts_accountManager_getBestEligibleAccount_onlyUgc_noTnc() {
        runBlockingTest(testDispatcher.coroutineDispatcher) {
            val account = accountManager.getBestEligibleAccount(mockAccountUserNoTnc, preferredAccountType = "")

            account.assertEqualTo(mockAccountUserNoTnc.first())
        }
    }

    @Test
    fun playShorts_accountManager_getBestEligibleAccount_shopAndUgc_bothEligible() {
        runBlockingTest(testDispatcher.coroutineDispatcher) {
            val account = accountManager.getBestEligibleAccount(mockAccountList, preferredAccountType = "")

            account.assertEqualTo(mockAccountList.first())
        }
    }

    @Test
    fun playShorts_accountManager_getBestEligibleAccount_shopAndUgc_bothNotEligible() {
        runBlockingTest(testDispatcher.coroutineDispatcher) {
            val account = accountManager.getBestEligibleAccount(mockAccountList, preferredAccountType = "")

            account.assertEqualTo(mockAccountList.first { it.isShop })
        }
    }

    @Test
    fun playShorts_accountManager_getBestEligibleAccount_shopAndUgc_onlyShopEligible() {
        runBlockingTest(testDispatcher.coroutineDispatcher) {
            val account = accountManager.getBestEligibleAccount(mockAccountListOnlyShopEligible, preferredAccountType = "")

            account.assertEqualTo(mockAccountList.first { it.isShop })
        }
    }

    @Test
    fun playShorts_accountManager_getBestEligibleAccount_shopAndUgc_onlyUgcEligible() {
        runBlockingTest(testDispatcher.coroutineDispatcher) {
            val account = accountManager.getBestEligibleAccount(mockAccountListOnlyUserEligible, preferredAccountType = "")

            account.assertEqualTo(mockAccountList.first { it.isUser })
        }
    }

    @Test
    fun playShorts_accountManager_getBestEligibleAccount_shopAndUgc_preferredShop() {
        runBlockingTest(testDispatcher.coroutineDispatcher) {
            val account = accountManager.getBestEligibleAccount(mockAccountList, preferredAccountType = TYPE_SHOP)

            account.assertEqualTo(mockAccountList.first { it.isShop })
        }
    }

    @Test
    fun playShorts_accountManager_getBestEligibleAccount_shopAndUgc_preferredUgc() {
        runBlockingTest(testDispatcher.coroutineDispatcher) {
            val account = accountManager.getBestEligibleAccount(mockAccountList, preferredAccountType = TYPE_USER)

            account.assertEqualTo(mockAccountList.first { it.isUser })
        }
    }

    @Test
    fun playShorts_accountManager_getBestEligibleAccount_shopAndUgc_preferredShop_butShopNotEligible() {
        runBlockingTest(testDispatcher.coroutineDispatcher) {
            val account = accountManager.getBestEligibleAccount(mockAccountListOnlyUserEligible, preferredAccountType = TYPE_USER)

            account.assertEqualTo(mockAccountListOnlyUserEligible.first { it.isUser })
        }
    }

    @Test
    fun playShorts_accountManager_getBestEligibleAccount_onlyShop_preferredShop_butShopNotEligible() {
        runBlockingTest(testDispatcher.coroutineDispatcher) {
            val account = accountManager.getBestEligibleAccount(mockAccountShopNotEligible, preferredAccountType = TYPE_USER)

            account.assertEqualTo(mockAccountShopNotEligible.first())
        }
    }

    @Test
    fun playShorts_accountManager_getBestEligibleAccount_shopAndUgc_lastSelectedShop() {
        runBlockingTest(testDispatcher.coroutineDispatcher) {
            coEvery { mockSharedPref.getLastSelectedAccountType() } returns TYPE_SHOP

            val account = accountManager.getBestEligibleAccount(mockAccountList, preferredAccountType = "")

            account.assertEqualTo(mockAccountList.first { it.isShop })
        }
    }

    @Test
    fun playShorts_accountManager_getBestEligibleAccount_shopAndUgc_lastSelectedUser() {
        runBlockingTest(testDispatcher.coroutineDispatcher) {
            coEvery { mockSharedPref.getLastSelectedAccountType() } returns TYPE_USER

            val account = accountManager.getBestEligibleAccount(mockAccountList, preferredAccountType = "")

            account.assertEqualTo(mockAccountList.first { it.isUser })
        }
    }

    @Test
    fun playShorts_accountManager_getBestEligibleAccount_shopAndUgc_lastSelectedShop_butShopNotEligible() {
        runBlockingTest(testDispatcher.coroutineDispatcher) {
            coEvery { mockSharedPref.getLastSelectedAccountType() } returns TYPE_SHOP

            val account = accountManager.getBestEligibleAccount(mockAccountListOnlyUserEligible, preferredAccountType = "")

            account.assertEqualTo(mockAccountListOnlyUserEligible.first { it.isUser })
        }
    }

    @Test
    fun playShorts_accountManager_getBestEligibleAccount_onlyShop_lastSelectedShop_butShopNotEligible() {
        runBlockingTest(testDispatcher.coroutineDispatcher) {
            coEvery { mockSharedPref.getLastSelectedAccountType() } returns TYPE_SHOP

            val account = accountManager.getBestEligibleAccount(mockAccountShopNotEligible, preferredAccountType = "")

            account.assertEqualTo(mockAccountShopNotEligible.first())
        }
    }
}
