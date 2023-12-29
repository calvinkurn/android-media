package com.tokopedia.play.broadcaster.shorts.viewmodel.preparation

import com.tokopedia.play.broadcaster.model.UiModelBuilder
import com.tokopedia.play.broadcaster.shorts.domain.PlayShortsRepository
import com.tokopedia.play.broadcaster.shorts.domain.manager.PlayShortsAccountManager
import com.tokopedia.play.broadcaster.shorts.robot.PlayShortsViewModelRobot
import com.tokopedia.play.broadcaster.shorts.ui.model.action.PlayShortsAction
import com.tokopedia.play.broadcaster.shorts.ui.model.event.PlayShortsUiEvent
import com.tokopedia.play.broadcaster.util.assertEqualTo
import com.tokopedia.play.broadcaster.util.assertFalse
import com.tokopedia.play.broadcaster.util.assertTrue
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

/**
 * Created By : Jonathan Darwin on June 19, 2023
 */
class PlayShortsAffiliateViewModelTest {


    private val mockRepo: PlayShortsRepository = mockk(relaxed = true)
    private val mockAccountManager: PlayShortsAccountManager = mockk(relaxed = true)

    private val uiModelBuilder = UiModelBuilder()

    private val mockAccountList = uiModelBuilder.buildAccountListModel()
    private val mockConfig = uiModelBuilder.buildShortsConfig()
    private val mockAccountUser = mockAccountList.last()
    private val mockSubmitOnboardAffiliateResponseSuccess = uiModelBuilder.buildSubmitOnboardAffiliate()
    private val mockCheckAffiliateTrue = uiModelBuilder.buildBroadcasterCheckAffiliate()
    private val mockCheckAffiliateFalse = uiModelBuilder.buildBroadcasterCheckAffiliate(affiliateName = "", isAffiliate = false)
    private val mockException = uiModelBuilder.buildException()

    @get:Rule
    val coroutineScopeRule = CoroutineTestRule()

    @Test
    fun playShorts_preparation_affiliate_ugc_submitOnboardAffiliate_success() {

        coEvery { mockRepo.getAccountList() } returns mockAccountList
        coEvery { mockRepo.getShortsConfiguration(any(), any()) } returns mockConfig
        coEvery { mockRepo.submitOnboardAffiliateTnc(any()) } returns mockSubmitOnboardAffiliateResponseSuccess
        coEvery { mockRepo.getBroadcasterCheckAffiliate() } returns mockCheckAffiliateTrue
        coEvery { mockAccountManager.getBestEligibleAccount(any(), any()) } returns mockAccountUser

        PlayShortsViewModelRobot(
            repo = mockRepo,
            accountManager = mockAccountManager,
            dispatchers = coroutineScopeRule.dispatchers
        ).use {
            val (state, events) = it.recordStateAndEvent {
                submitAction(PlayShortsAction.PreparePage(preferredAccountType = ""))
                submitAction(PlayShortsAction.SubmitOnboardAffiliateTnc)
            }

            state.isAffiliate.assertTrue()
            events.last().assertEqualTo(PlayShortsUiEvent.SuccessOnboardAffiliate)
            it.isSelectedAccountAffiliate.assertTrue()
        }
    }

    @Test
    fun playShorts_preparation_affiliate_ugc_submitOnboardAffiliate_fail() {
        coEvery { mockRepo.getAccountList() } returns mockAccountList
        coEvery { mockRepo.getShortsConfiguration(any(), any()) } returns mockConfig
        coEvery { mockRepo.submitOnboardAffiliateTnc(any()) } throws mockException
        coEvery { mockRepo.getBroadcasterCheckAffiliate() } returns mockCheckAffiliateFalse
        coEvery { mockAccountManager.getBestEligibleAccount(any(), any()) } returns mockAccountUser

        PlayShortsViewModelRobot(
            repo = mockRepo,
            accountManager = mockAccountManager,
            dispatchers = coroutineScopeRule.dispatchers
        ).use {
            val events = it.recordEvent {
                submitAction(PlayShortsAction.PreparePage(preferredAccountType = ""))
                submitAction(PlayShortsAction.SubmitOnboardAffiliateTnc)
            }

            events.last().assertEqualTo(PlayShortsUiEvent.ErrorOnboardAffiliate(mockException))
            it.isSelectedAccountAffiliate.assertFalse()
        }
    }
}
