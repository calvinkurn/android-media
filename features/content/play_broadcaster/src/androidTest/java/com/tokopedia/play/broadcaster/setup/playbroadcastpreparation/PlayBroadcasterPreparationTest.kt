package com.tokopedia.play.broadcaster.setup.playbroadcastpreparation

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.cassavatest.CassavaTestRule
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
import com.tokopedia.play.broadcaster.ui.model.ChannelStatus
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by fachrizalmrsln on 28/09/22
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class PlayBroadcasterPreparationTest {

    @get:Rule
    var cassavaTestRule = CassavaTestRule(sendValidationResult = false)

    private val mockDispatcher: CoroutineDispatchers = CoroutineDispatchersProvider
    private val mockDataStore: PlayBroadcastDataStore = mockk(relaxed = true)
    private val mockUserSession: UserSessionInterface = mockk(relaxed = true)
    private val mockConfigStore: HydraConfigStore = mockk(relaxed = true)
    private val mockRepo: PlayBroadcastRepository = mockk(relaxed = true)
    private val mockGetChannelUseCase: GetChannelUseCase = mockk(relaxed = true)
    private val mockGetAddedTagUseCase: GetAddedChannelTagsUseCase = mockk(relaxed = true)

    private val mockAddedTag = GetAddedChannelTagsResponse()

    private fun createRobot() = PlayBroadcasterPreparationRobot(
        dataStore = mockDataStore,
        hydraConfigStore = mockConfigStore,
        userSessionInterface = mockUserSession,
        dispatcher = mockDispatcher,
        repo = mockRepo,
        channelUseCase = mockGetChannelUseCase,
        addedChannelTagsUseCase = mockGetAddedTagUseCase
    )

    init {
        coEvery { mockRepo.getAccountList() } returns accountListResponse()
        coEvery {
            mockRepo.getChannelConfiguration(
                any(),
                any()
            )
        } returns buildConfigurationUiModel()
        coEvery { mockGetChannelUseCase.executeOnBackground() } returns channelResponse
        coEvery { mockGetAddedTagUseCase.executeOnBackground() } returns mockAddedTag
        coEvery { mockRepo.getProductTagSummarySection(any()) } returns emptyList()
        coEvery { mockConfigStore.getAuthorId() } returns accountListResponse()[0].id
        coEvery { mockConfigStore.getAuthorTypeName() } returns ContentCommonUserType.TYPE_NAME_SELLER
        coEvery { mockUserSession.userId } returns accountListResponse()[0].id
    }

    @Test
    fun test_onEnterPreparationWhenBothAccountLive() {
        coEvery {
            mockRepo.getChannelConfiguration(any(), any())
        } returns buildConfigurationUiModel(channelStatus = ChannelStatus.Live)

        createRobot().onEnterPreparationWhenBothAccountLive()
    }

}