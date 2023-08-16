package com.tokopedia.play.broadcaster.shorts.viewmodel.preparation

import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.play.broadcaster.model.UiModelBuilder
import com.tokopedia.play.broadcaster.shorts.domain.PlayShortsRepository
import com.tokopedia.play.broadcaster.shorts.domain.manager.PlayShortsAccountManager
import com.tokopedia.play.broadcaster.shorts.robot.PlayShortsViewModelRobot
import com.tokopedia.play.broadcaster.shorts.ui.model.PlayShortsConfigUiModel
import com.tokopedia.play.broadcaster.shorts.ui.model.action.PlayShortsAction
import com.tokopedia.play.broadcaster.shorts.ui.model.event.PlayShortsUiEvent
import com.tokopedia.play.broadcaster.ui.model.PlayBroadcastPreparationBannerModel
import com.tokopedia.play.broadcaster.util.*
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.Assert.fail
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created By : Jonathan Darwin on December 06, 2022
 */
class PlayShortsConfigViewModelTest {

    private val mockRepo: PlayShortsRepository = mockk(relaxed = true)
    private val mockAccountManager: PlayShortsAccountManager = mockk(relaxed = true)

    private val uiModelBuilder = UiModelBuilder()

    private val mockMaxTitleChar = 38
    private val mockMaxTaggedProduct = 30

    private val mockAccountShop = uiModelBuilder.buildAccountListModel(onlyShop = true).first()
    private val mockAccountShopNotEligible = uiModelBuilder.buildAccountListModel(onlyShop = true, tncShop = false).first()
    private val mockAccountUser = uiModelBuilder.buildAccountListModel(onlyBuyer = true).first()
    private val mockAccountUserNoUsername = uiModelBuilder.buildAccountListModel(onlyBuyer = true, usernameBuyer = false, tncBuyer = false).first()
    private val mockAccountUserNoTnc = uiModelBuilder.buildAccountListModel(onlyBuyer = true, usernameBuyer = true, tncBuyer = false).first()

    private val mockConfigAllowed = uiModelBuilder.buildShortsConfig(shortsAllowed = true, maxTitleCharacter = mockMaxTitleChar, maxTaggedProduct = mockMaxTaggedProduct)
    private val mockConfigAllowedNoDraft = uiModelBuilder.buildShortsConfig(shortsId = "", shortsAllowed = true)
    private val mockConfigNotAllowed = uiModelBuilder.buildShortsConfig(shortsAllowed = false)
    private val mockConfigBanned = uiModelBuilder.buildShortsConfig(isBanned = true)

    private val mockUserAffiliateTrue = uiModelBuilder.buildBroadcasterCheckAffiliate()
    private val mockUserAffiliateFalse = uiModelBuilder.buildBroadcasterCheckAffiliate(affiliateName = "", isAffiliate = false,)

    private val mockException = Exception("Network Error")

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    @Before
    fun setUp() {
        /** Doesn't matter what accountList being returned as long as
         * we mock mockAccountManager.getBestEligibleAccount()
         */

        coEvery { mockRepo.getAccountList() } returns listOf()
    }
    
    @Test
    fun playShorts_preparation_config_noAccount() {

        coEvery { mockAccountManager.getBestEligibleAccount(any(), any()) } returns ContentAccountUiModel.Empty

        PlayShortsViewModelRobot(
            repo = mockRepo,
            accountManager = mockAccountManager
        ).use {
            val (state, events) = it.recordStateAndEvent {
                submitAction(PlayShortsAction.PreparePage(preferredAccountType = ""))
            }

            state.accountList.assertEmpty()
            state.config.assertEqualTo(PlayShortsConfigUiModel.Empty)
            state.selectedAccount.assertEqualTo(ContentAccountUiModel.Empty)
            events.last().assertType<PlayShortsUiEvent.AccountNotEligible>()
        }
    }

    @Test
    fun playShorts_preparation_config_shop_eligible() {

        coEvery { mockAccountManager.getBestEligibleAccount(any(), any()) } returns mockAccountShop
        coEvery { mockRepo.getShortsConfiguration(any(), any()) } returns mockConfigAllowed

        PlayShortsViewModelRobot(
            repo = mockRepo,
            accountManager = mockAccountManager
        ).use {
            val state = it.recordState {
                submitAction(PlayShortsAction.PreparePage(preferredAccountType = ""))
            }

            state.config.assertEqualTo(mockConfigAllowed)
            state.selectedAccount.assertEqualTo(mockAccountShop)
            state.bannerPreparation.assertEqualTo(listOf())
        }
    }

    @Test
    fun playShorts_preparation_config_shop_notEligible() {

        coEvery { mockAccountManager.getBestEligibleAccount(any(), any()) } returns mockAccountShopNotEligible
        coEvery { mockRepo.getShortsConfiguration(any(), any()) } returns mockConfigAllowed

        PlayShortsViewModelRobot(
            repo = mockRepo,
            accountManager = mockAccountManager
        ).use {
            val (state, events) = it.recordStateAndEvent {
                submitAction(PlayShortsAction.PreparePage(preferredAccountType = ""))
            }

            it.tncList.assertEqualTo(mockConfigAllowed.tncList)
            state.config.assertEqualTo(PlayShortsConfigUiModel.Empty.copy(tncList = mockConfigAllowed.tncList))
            state.selectedAccount.assertEqualTo(ContentAccountUiModel.Empty)
            events.last().assertType<PlayShortsUiEvent.SellerNotEligible>()
        }
    }

    @Test
    fun playShorts_preparation_config_shop_shortsNotAllowed() {
        coEvery { mockAccountManager.getBestEligibleAccount(any(), any()) } returns mockAccountShop
        coEvery { mockRepo.getShortsConfiguration(any(), any()) } returns mockConfigNotAllowed

        PlayShortsViewModelRobot(
            repo = mockRepo,
            accountManager = mockAccountManager
        ).use {
            val (state, events) = it.recordStateAndEvent {
                submitAction(PlayShortsAction.PreparePage(preferredAccountType = ""))
            }

            it.tncList.assertEqualTo(mockConfigAllowed.tncList)
            state.config.assertEqualTo(PlayShortsConfigUiModel.Empty.copy(tncList = mockConfigAllowed.tncList))
            state.selectedAccount.assertEqualTo(ContentAccountUiModel.Empty)
            events.last().assertType<PlayShortsUiEvent.SellerNotEligible>()
        }
    }

    @Test
    fun playShorts_preparation_config_ugc_eligible() {

        coEvery { mockAccountManager.getBestEligibleAccount(any(), any()) } returns mockAccountUser
        coEvery { mockRepo.getShortsConfiguration(any(), any()) } returns mockConfigAllowed

        PlayShortsViewModelRobot(
            repo = mockRepo,
            accountManager = mockAccountManager
        ).use {
            val state = it.recordState {
                submitAction(PlayShortsAction.PreparePage(preferredAccountType = ""))
            }

            state.config.assertEqualTo(mockConfigAllowed)
            state.selectedAccount.assertEqualTo(mockAccountUser)
        }
    }

    @Test
    fun playShorts_preparation_config_ugc_noUsername() {
        coEvery { mockAccountManager.getBestEligibleAccount(any(), any()) } returns mockAccountUserNoUsername
        coEvery { mockRepo.getShortsConfiguration(any(), any()) } returns mockConfigAllowed

        PlayShortsViewModelRobot(
            repo = mockRepo,
            accountManager = mockAccountManager
        ).use {
            val (state, events) = it.recordStateAndEvent {
                submitAction(PlayShortsAction.PreparePage(preferredAccountType = ""))
            }

            it.tncList.assertEqualTo(mockConfigAllowed.tncList)
            state.config.assertEqualTo(PlayShortsConfigUiModel.Empty.copy(tncList = mockConfigAllowed.tncList))
            state.selectedAccount.assertEqualTo(ContentAccountUiModel.Empty)
            assertEventOnboarding(events.last()) { hasUsername ->
                hasUsername.assertFalse()
            }
        }
    }

    @Test
    fun playShorts_preparation_config_ugc_hasntAcceptTnc() {
        coEvery { mockAccountManager.getBestEligibleAccount(any(), any()) } returns mockAccountUserNoTnc
        coEvery { mockRepo.getShortsConfiguration(any(), any()) } returns mockConfigAllowed

        PlayShortsViewModelRobot(
            repo = mockRepo,
            accountManager = mockAccountManager
        ).use {
            val (state, events) = it.recordStateAndEvent {
                submitAction(PlayShortsAction.PreparePage(preferredAccountType = ""))
            }

            it.tncList.assertEqualTo(mockConfigAllowed.tncList)
            state.config.assertEqualTo(PlayShortsConfigUiModel.Empty.copy(tncList = mockConfigAllowed.tncList))
            state.selectedAccount.assertEqualTo(ContentAccountUiModel.Empty)
            assertEventOnboarding(events.last()) { hasUsername ->
                hasUsername.assertTrue()
            }
        }
    }

    @Test
    fun playShorts_preparation_config_ugc_shortsNotAllowed() {
        coEvery { mockAccountManager.getBestEligibleAccount(any(), any()) } returns mockAccountUser
        coEvery { mockRepo.getShortsConfiguration(any(), any()) } returns mockConfigNotAllowed

        PlayShortsViewModelRobot(
            repo = mockRepo,
            accountManager = mockAccountManager
        ).use {
            val (state, events) = it.recordStateAndEvent {
                submitAction(PlayShortsAction.PreparePage(preferredAccountType = ""))
            }

            it.tncList.assertEqualTo(mockConfigAllowed.tncList)
            state.config.assertEqualTo(PlayShortsConfigUiModel.Empty.copy(tncList = mockConfigAllowed.tncList))
            state.selectedAccount.assertEqualTo(ContentAccountUiModel.Empty)
            events.last().assertType<PlayShortsUiEvent.AccountNotEligible>()
        }
    }

    @Test
    fun playShorts_preparation_config_ugc_isBanned() {
        coEvery { mockAccountManager.getBestEligibleAccount(any(), any()) } returns mockAccountUser
        coEvery { mockRepo.getShortsConfiguration(any(), any()) } returns mockConfigBanned

        PlayShortsViewModelRobot(
            repo = mockRepo,
            accountManager = mockAccountManager
        ).use {
            val (state, events) = it.recordStateAndEvent {
                submitAction(PlayShortsAction.PreparePage(preferredAccountType = ""))
            }

            events.last().assertType<PlayShortsUiEvent.AccountBanned>()
            state.selectedAccount.assertEqualTo(ContentAccountUiModel.Empty)
        }
    }

    @Test
    fun playShorts_preparation_config_shop_noDraftShorts_success() {
        val mockShortsId = "123"

        coEvery { mockAccountManager.getBestEligibleAccount(any(), any()) } returns mockAccountShop
        coEvery { mockRepo.getShortsConfiguration(any(), any()) } returns mockConfigAllowedNoDraft
        coEvery { mockRepo.createShorts(any(), any()) } returns mockShortsId

        PlayShortsViewModelRobot(
            repo = mockRepo,
            accountManager = mockAccountManager
        ).use {
            val state = it.recordState {
                submitAction(PlayShortsAction.PreparePage(preferredAccountType = ""))
            }

            state.config.assertEqualTo(mockConfigAllowedNoDraft.copy(shortsId = mockShortsId))
            state.selectedAccount.assertEqualTo(mockAccountShop)
        }
    }

    @Test
    fun playShorts_preparation_config_shop_noDraftShorts_fail() {

        coEvery { mockAccountManager.getBestEligibleAccount(any(), any()) } returns mockAccountShop
        coEvery { mockRepo.getShortsConfiguration(any(), any()) } returns mockConfigAllowedNoDraft
        coEvery { mockRepo.createShorts(any(), any()) } throws mockException

        PlayShortsViewModelRobot(
            repo = mockRepo,
            accountManager = mockAccountManager
        ).use {
            val (state, events) = it.recordStateAndEvent {
                submitAction(PlayShortsAction.PreparePage(preferredAccountType = ""))
            }

            events.last().assertType<PlayShortsUiEvent.ErrorPreparingPage>()
            state.selectedAccount.assertEqualTo(ContentAccountUiModel.Empty)
        }
    }

    @Test
    fun playShorts_preparation_config_shop_isBanned() {
        coEvery { mockAccountManager.getBestEligibleAccount(any(), any()) } returns mockAccountShop
        coEvery { mockRepo.getShortsConfiguration(any(), any()) } returns mockConfigBanned

        PlayShortsViewModelRobot(
            repo = mockRepo,
            accountManager = mockAccountManager
        ).use {
            val (state, events) = it.recordStateAndEvent {
                submitAction(PlayShortsAction.PreparePage(preferredAccountType = ""))
            }

            events.last().assertType<PlayShortsUiEvent.AccountBanned>()
            state.selectedAccount.assertEqualTo(ContentAccountUiModel.Empty)
        }
    }

    /** Constant */
    @Test
    fun playShorts_preparation_config_maxTitleCharacter() {
        coEvery { mockAccountManager.getBestEligibleAccount(any(), any()) } returns mockAccountUser
        coEvery { mockRepo.getShortsConfiguration(any(), any()) } returns mockConfigAllowed

        PlayShortsViewModelRobot(
            repo = mockRepo,
            accountManager = mockAccountManager
        ).use {
            val state = it.recordState {
                submitAction(PlayShortsAction.PreparePage(preferredAccountType = ""))
            }

            state.config.maxTitleCharacter.assertEqualTo(mockMaxTitleChar)
            it.maxTitleCharacter.assertEqualTo(mockMaxTitleChar)
        }
    }

    @Test
    fun playShorts_preparation_config_maxTaggedProduct() {
        coEvery { mockAccountManager.getBestEligibleAccount(any(), any()) } returns mockAccountUser
        coEvery { mockRepo.getShortsConfiguration(any(), any()) } returns mockConfigAllowed

        PlayShortsViewModelRobot(
            repo = mockRepo,
            accountManager = mockAccountManager
        ).use {
            val state = it.recordState {
                submitAction(PlayShortsAction.PreparePage(preferredAccountType = ""))
            }

            state.config.maxTaggedProduct.assertEqualTo(mockMaxTaggedProduct)
            it.maxProduct.assertEqualTo(mockMaxTaggedProduct)
        }
    }

    /** Banner */
    @Test
    fun playShorts_preparation_config_ugc_isAffiliate_noBannerOnboardingAffiliate() {

        coEvery { mockAccountManager.getBestEligibleAccount(any(), any()) } returns mockAccountUser
        coEvery { mockRepo.getShortsConfiguration(any(), any()) } returns mockConfigAllowed
        coEvery { mockRepo.getBroadcasterCheckAffiliate() } returns mockUserAffiliateTrue

        PlayShortsViewModelRobot(
            repo = mockRepo,
            accountManager = mockAccountManager
        ).use {
            val state = it.recordState {
                submitAction(PlayShortsAction.PreparePage(preferredAccountType = ""))
            }

            state.config.assertEqualTo(mockConfigAllowed)
            state.selectedAccount.assertEqualTo(mockAccountUser)
            state.bannerPreparation.assertEqualTo(listOf())
        }
    }

    @Test
    fun playShorts_preparation_config_ugc_notAffiliate_bannerOnboardingAffiliateExists() {

        coEvery { mockAccountManager.getBestEligibleAccount(any(), any()) } returns mockAccountUser
        coEvery { mockRepo.getShortsConfiguration(any(), any()) } returns mockConfigAllowed
        coEvery { mockRepo.getBroadcasterCheckAffiliate() } returns mockUserAffiliateFalse

        PlayShortsViewModelRobot(
            repo = mockRepo,
            accountManager = mockAccountManager
        ).use {
            val state = it.recordState {
                submitAction(PlayShortsAction.PreparePage(preferredAccountType = ""))
            }

            state.config.assertEqualTo(mockConfigAllowed)
            state.selectedAccount.assertEqualTo(mockAccountUser)
            state.bannerPreparation.assertEqualTo(listOf(PlayBroadcastPreparationBannerModel(PlayBroadcastPreparationBannerModel.TYPE_SHORTS_AFFILIATE)))
        }
    }

    /**
     * Assertion Helper  Function
     */
    fun assertEventOnboarding(event: PlayShortsUiEvent, fn: (hasUsername: Boolean) -> Unit) {
        if(event is PlayShortsUiEvent.UGCOnboarding) {
            fn(event.hasUsername)
        }
        else {
            fail("event should be PlayShortsUiEvent.UGCOnboarding")
        }
    }
}
