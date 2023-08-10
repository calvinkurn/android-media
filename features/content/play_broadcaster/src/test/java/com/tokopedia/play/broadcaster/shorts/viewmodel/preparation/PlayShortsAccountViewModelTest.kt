package com.tokopedia.play.broadcaster.shorts.viewmodel.preparation

import com.tokopedia.play.broadcaster.model.UiModelBuilder
import com.tokopedia.play.broadcaster.shorts.domain.PlayShortsRepository
import com.tokopedia.play.broadcaster.shorts.domain.manager.PlayShortsAccountManager
import com.tokopedia.play.broadcaster.shorts.robot.PlayShortsViewModelRobot
import com.tokopedia.play.broadcaster.shorts.ui.model.action.PlayShortsAction
import com.tokopedia.play.broadcaster.shorts.ui.model.event.PlayShortsUiEvent
import com.tokopedia.play.broadcaster.ui.model.PlayBroadcastPreparationBannerModel
import com.tokopedia.play.broadcaster.util.assertEqualTo
import com.tokopedia.play.broadcaster.util.assertFalse
import com.tokopedia.play.broadcaster.util.assertTrue
import com.tokopedia.play.broadcaster.util.assertType
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

/**
 * Created By : Jonathan Darwin on December 06, 2022
 */
class PlayShortsAccountViewModelTest {

    /**
     * Note:
     * 1. for cases where switching account is failing due to account ineligibility
     * e.g. shortsNotAllowed, noUsername, noTnc, etc
     * already handled in [PlayShortsConfigViewModelTest]
     *
     * 2. mockAccountManager.getBestEligibleAccount() & mockAccountManager.switchAccount()
     * will be tested on [PlayShortsAccountManagerTest]
     */

    private val mockRepo: PlayShortsRepository = mockk(relaxed = true)
    private val mockAccountManager: PlayShortsAccountManager = mockk(relaxed = true)

    private val uiModelBuilder = UiModelBuilder()

    private val mockAccountList = uiModelBuilder.buildAccountListModel()
    private val mockAccountShop = mockAccountList.first()
    private val mockAccountUser = mockAccountList.last()
    private val mockAccountListBeforeAcceptTnc = uiModelBuilder.buildAccountListModel(tncBuyer = false, usernameBuyer = false)
    private val mockAccountListAfterAcceptTnc = uiModelBuilder.buildAccountListModel(tncBuyer = true, usernameBuyer = true)

    private val mockConfig = uiModelBuilder.buildShortsConfig()
    private val mockConfigBanned = uiModelBuilder.buildShortsConfig(isBanned = true)

    private val mockUserAffiliateFalse = uiModelBuilder.buildBroadcasterCheckAffiliate(affiliateName = "", isAffiliate = false,)

    private val mockException = Exception("Network Error")

    @get:Rule
    val coroutineScopeRule = CoroutineTestRule()

    @Test
    fun playShorts_preparation_account_clickSwitchAccount() {
        PlayShortsViewModelRobot().use {
            val events = it.recordEvent {
                submitAction(PlayShortsAction.ClickSwitchAccount)
            }

            events.last().assertType<PlayShortsUiEvent.SwitchAccount>()
        }
    }

    @Test
    fun playShorts_preparation_account_switchAccount_ugc_to_shop_success() {

        coEvery { mockRepo.getAccountList() } returns mockAccountList
        coEvery { mockRepo.getShortsConfiguration(any(), any()) } returns mockConfig
        coEvery { mockAccountManager.getBestEligibleAccount(any(), any()) } returns mockAccountUser
        coEvery { mockAccountManager.switchAccount(any(), any()) } returns mockAccountShop
        coEvery { mockRepo.getBroadcasterCheckAffiliate() } returns mockUserAffiliateFalse

        PlayShortsViewModelRobot(
            repo = mockRepo,
            accountManager = mockAccountManager
        ).use {
            val state = it.setUp {
                submitAction(PlayShortsAction.PreparePage(preferredAccountType = ""))
            }.recordState {
                submitAction(PlayShortsAction.SwitchAccount(isRefreshAccountList = false))
            }

            state.selectedAccount.assertEqualTo(mockAccountShop)
            it.accountList.assertEqualTo(mockAccountList)
            state.bannerPreparation.assertEqualTo(listOf())
        }
    }

    @Test
    fun playShorts_preparation_account_switchAccount_shop_to_ugc() {
        coEvery { mockRepo.getAccountList() } returns mockAccountList
        coEvery { mockRepo.getShortsConfiguration(any(), any()) } returns mockConfig
        coEvery { mockAccountManager.getBestEligibleAccount(any(), any()) } returns mockAccountShop
        coEvery { mockAccountManager.switchAccount(any(), any()) } returns mockAccountUser
        coEvery { mockRepo.getBroadcasterCheckAffiliate() } returns mockUserAffiliateFalse

        PlayShortsViewModelRobot(
            repo = mockRepo,
            accountManager = mockAccountManager
        ).use {
            val state = it.setUp {
                submitAction(PlayShortsAction.PreparePage(preferredAccountType = ""))
            }.recordState {
                submitAction(PlayShortsAction.SwitchAccount(isRefreshAccountList = false))
            }

            state.selectedAccount.assertEqualTo(mockAccountUser)
            it.accountList.assertEqualTo(mockAccountList)
            state.bannerPreparation.assertEqualTo(listOf(PlayBroadcastPreparationBannerModel(PlayBroadcastPreparationBannerModel.TYPE_SHORTS_AFFILIATE)))
        }
    }

    @Test
    fun playShorts_preparation_account_switchAccount_failWhenGettingConfig() {

        coEvery { mockRepo.getAccountList() } returns mockAccountList
        coEvery { mockRepo.getShortsConfiguration(any(), any()) } returns mockConfig
        coEvery { mockAccountManager.getBestEligibleAccount(any(), any()) } returns mockAccountShop

        PlayShortsViewModelRobot(
            repo = mockRepo,
            accountManager = mockAccountManager
        ).use {
            val (state, events) = it.setUp {
                submitAction(PlayShortsAction.PreparePage(preferredAccountType = ""))
            }.recordStateAndEvent {
                coEvery { mockAccountManager.getBestEligibleAccount(any(), any()) } returns mockAccountUser
                coEvery { mockRepo.getShortsConfiguration(any(), any()) } throws mockException

                submitAction(PlayShortsAction.SwitchAccount(isRefreshAccountList = false))
            }

            state.selectedAccount.assertEqualTo(mockAccountShop)
            it.accountList.assertEqualTo(mockAccountList)
        }
    }

    @Test
    fun playShorts_preparation_account_switchAccount_refreshAccountList() {

        coEvery { mockRepo.getAccountList() } returns mockAccountListBeforeAcceptTnc
        coEvery { mockRepo.getShortsConfiguration(any(), any()) } returns mockConfig
        coEvery { mockAccountManager.getBestEligibleAccount(any(), any()) } returns mockAccountListBeforeAcceptTnc.first()

        PlayShortsViewModelRobot(
            repo = mockRepo,
            accountManager = mockAccountManager
        ).use {
            val (state, events) = it.setUp {
                submitAction(PlayShortsAction.PreparePage(preferredAccountType = ""))
            }.recordStateAndEvent {
                coEvery { mockRepo.getAccountList() } returns mockAccountListAfterAcceptTnc
                coEvery { mockAccountManager.switchAccount(mockAccountListAfterAcceptTnc, mockAccountListAfterAcceptTnc.first().type) } returns mockAccountListAfterAcceptTnc.last()

                submitAction(PlayShortsAction.SwitchAccount(isRefreshAccountList = true))
            }

            state.accountList.assertEqualTo(mockAccountListAfterAcceptTnc)
            state.selectedAccount.assertEqualTo(mockAccountListAfterAcceptTnc.last())
        }
    }

    @Test
    fun playShorts_preparation_account_switchAccount_switchToBannedAccount() {

        coEvery { mockRepo.getAccountList() } returns mockAccountList
        coEvery { mockRepo.getShortsConfiguration(any(), any()) } returns mockConfig
        coEvery { mockAccountManager.getBestEligibleAccount(any(), any()) } returns mockAccountShop

        PlayShortsViewModelRobot(
            repo = mockRepo,
            accountManager = mockAccountManager
        ).use {
            val (state, events) = it.setUp {
                submitAction(PlayShortsAction.PreparePage(preferredAccountType = ""))
            }.recordStateAndEvent {
                coEvery { mockAccountManager.getBestEligibleAccount(any(), any()) } returns mockAccountUser
                coEvery { mockRepo.getShortsConfiguration(any(), any()) } returns mockConfigBanned

                submitAction(PlayShortsAction.SwitchAccount(isRefreshAccountList = false))
            }

            state.selectedAccount.assertEqualTo(mockAccountShop)
            events.last().assertType<PlayShortsUiEvent.AccountBanned>()
            it.accountList.assertEqualTo(mockAccountList)
        }
    }

    @Test
    fun playShorts_preparation_account_switchAccount_isAllowChangeAccount() {

        PlayShortsViewModelRobot(
            repo = mockRepo,
            accountManager = mockAccountManager
        ).use {

            coEvery { mockAccountManager.isAllowChangeAccount(any()) } returns true

            it.isAllowChangeAccount.assertTrue()

            coEvery { mockAccountManager.isAllowChangeAccount(any()) } returns false

            it.isAllowChangeAccount.assertFalse()
        }
    }
}
