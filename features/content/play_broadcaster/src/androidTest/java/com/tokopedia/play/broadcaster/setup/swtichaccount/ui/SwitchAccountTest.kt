package com.tokopedia.play.broadcaster.setup.swtichaccount.ui

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.content.common.types.ContentCommonUserType
import com.tokopedia.play.broadcaster.data.config.HydraConfigStore
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastDataStore
import com.tokopedia.play.broadcaster.domain.model.GetAddedChannelTagsResponse
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.domain.usecase.GetAddedChannelTagsUseCase
import com.tokopedia.play.broadcaster.domain.usecase.GetChannelUseCase
import com.tokopedia.play.broadcaster.setup.accountListResponse
import com.tokopedia.play.broadcaster.setup.buildConfigurationUiModel
import com.tokopedia.play.broadcaster.setup.channelResponse
import com.tokopedia.play.broadcaster.setup.swtichaccount.SwitchAccountRobot
import com.tokopedia.play.broadcaster.ui.model.ChannelStatus
import com.tokopedia.play.broadcaster.ui.model.title.PlayTitleUiModel
import com.tokopedia.play.broadcaster.util.preference.HydraSharedPreferences
import com.tokopedia.test.application.annotations.UiTest
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by fachrizalmrsln on 28/09/22
 */
@UiTest
@RunWith(AndroidJUnit4ClassRunner::class)
class SwitchAccountTest {

    private val mockDispatcher: CoroutineDispatchers = CoroutineDispatchersProvider
    private val mockDataStore: PlayBroadcastDataStore = mockk(relaxed = true)
    private val mockConfigStore: HydraConfigStore = mockk(relaxed = true)
    private val mockRepo: PlayBroadcastRepository = mockk(relaxed = true)
    private val mockGetChannelUseCase: GetChannelUseCase = mockk(relaxed = true)
    private val mockGetAddedTagUseCase: GetAddedChannelTagsUseCase = mockk(relaxed = true)
    private val mockHydraSharedPreferences: HydraSharedPreferences = mockk(relaxed = true)

    private val mockAddedTag = GetAddedChannelTagsResponse()

    private fun createRobot() = SwitchAccountRobot(
        dataStore = mockDataStore,
        hydraConfigStore = mockConfigStore,
        userSessionInterface = mockk(relaxed = true),
        dispatcher = mockDispatcher,
        repo = mockRepo,
        channelUseCase = mockGetChannelUseCase,
        addedChannelTagsUseCase = mockGetAddedTagUseCase,
        sharedPreferences = mockHydraSharedPreferences,
    )

    init {
        coEvery { mockRepo.getAccountList() } returns accountListResponse()
        coEvery {
            mockRepo.getChannelConfiguration(any(), any())
        } returns buildConfigurationUiModel()
        coEvery { mockGetChannelUseCase.executeOnBackground() } returns channelResponse
        coEvery { mockGetAddedTagUseCase.executeOnBackground() } returns mockAddedTag
        coEvery { mockRepo.getProductTagSummarySection(any()) } returns emptyList()
    }

    @Test
    fun test_entryPointWhenBothAccountLive() {
        coEvery {
            mockRepo.getChannelConfiguration(any(), any())
        } returns buildConfigurationUiModel(channelStatus = ChannelStatus.Live)

        createRobot().entryPointWhenBothAccountLive()
    }

    @Test
    fun test_switchAccountSellerToBuyer() {
        createRobot().switchAccountSellerToBuyer()
    }

    @Test
    fun test_switchAccountSellerToBuyerAndNotHaveUsername() {
        coEvery { mockRepo.getAccountList() } returns accountListResponse(buyerHasUsername = false)

        createRobot().switchAccountSellerToBuyerAndNotHaveUsername()
    }

    @Test
    fun test_switchAccountSellerToBuyerAndNotAcceptTnc() {
        coEvery { mockRepo.getAccountList() } returns accountListResponse(buyerHasAcceptTnc = false)

        createRobot().switchAccountSellerToBuyerAndNotAcceptTnc()
    }

    @Test
    fun test_switchAccountBuyerToSellerAndNotAllowed() {
        coEvery {
            mockRepo.getAccountList()
        } returns accountListResponse(shopEligible = false).reversed()

        createRobot().switchAccountBuyerToSellerAndNotAllowed()
    }

    @Test
    fun test_switchAccountSellerHaveDraft() {
        coEvery {
            mockDataStore.getSetupDataStore().getTitle()
        } returns PlayTitleUiModel.HasTitle("Title")

        createRobot().switchAccountSellerHaveDraft()
    }

    @Test
    fun test_switchAccountBuyerHaveDraft() {
        coEvery { mockRepo.getAccountList() } returns accountListResponse().reversed()
        coEvery {
            mockHydraSharedPreferences.getLastSelectedAccountType()
        } returns ContentCommonUserType.TYPE_USER
        coEvery {
            mockDataStore.getSetupDataStore().getTitle()
        } returns PlayTitleUiModel.HasTitle("Title")

        createRobot().switchAccountBuyerHaveDraft()
    }

}
