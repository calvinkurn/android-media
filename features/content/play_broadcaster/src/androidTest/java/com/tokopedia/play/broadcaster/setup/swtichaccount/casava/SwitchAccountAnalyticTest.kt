package com.tokopedia.play.broadcaster.setup.swtichaccount.casava

import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.content.common.types.ContentCommonUserType
import com.tokopedia.play.broadcaster.data.config.HydraConfigStore
import com.tokopedia.play.broadcaster.data.datastore.*
import com.tokopedia.play.broadcaster.domain.model.GetAddedChannelTagsResponse
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.domain.usecase.GetAddedChannelTagsUseCase
import com.tokopedia.play.broadcaster.domain.usecase.GetChannelUseCase
import com.tokopedia.play.broadcaster.helper.containsEventAction
import com.tokopedia.play.broadcaster.setup.*
import com.tokopedia.play.broadcaster.setup.swtichaccount.SwitchAccountRobot
import com.tokopedia.play.broadcaster.ui.model.beautification.BeautificationConfigUiModel
import com.tokopedia.play.broadcaster.util.preference.HydraSharedPreferences
import com.tokopedia.test.application.annotations.CassavaTest
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by fachrizalmrsln on 28/09/22
 */
@CassavaTest
@RunWith(AndroidJUnit4ClassRunner::class)
class SwitchAccountAnalyticTest {

    private val mockDispatcher: CoroutineDispatchers = CoroutineDispatchersProvider
    private val mockDataStore = PlayBroadcastDataStoreImpl(
        mSetupDataStore = PlayBroadcastSetupDataStoreImpl(
            coverDataStore = CoverDataStoreImpl(mockDispatcher, mockk(relaxed = true)),
            titleDataStore = TitleDataStoreImpl(dispatcher = mockDispatcher, mockk(relaxed = true)),
            tagsDataStore = TagsDataStoreImpl(dispatcher = mockDispatcher, mockk(relaxed = true)),
            scheduleDataStore = BroadcastScheduleDataStoreImpl(mockDispatcher, mockk(relaxed = true)),
            interactiveDataStore = InteractiveDataStoreImpl(),
            productTagDataStore = ProductTagDataStoreImpl()
        ),
    )
    private val mockConfigStore: HydraConfigStore = mockk(relaxed = true)
    private val mockRepo: PlayBroadcastRepository = mockk(relaxed = true)
    private val mockGetChannelUseCase: GetChannelUseCase = mockk(relaxed = true)
    private val mockGetAddedTagUseCase: GetAddedChannelTagsUseCase = mockk(relaxed = true)
    private val mockHydraSharedPreferences: HydraSharedPreferences = mockk(relaxed = true)
    private val mockUserSession: UserSessionInterface = mockk(relaxed = true)

    private val mockAddedTag = GetAddedChannelTagsResponse()

    private fun createRobot() = SwitchAccountRobot(
        dataStore = mockDataStore,
        hydraConfigStore = mockConfigStore,
        userSessionInterface = mockUserSession,
        dispatcher = mockDispatcher,
        repo = mockRepo,
        channelUseCase = mockGetChannelUseCase,
        addedChannelTagsUseCase = mockGetAddedTagUseCase,
        playShortsEntryPointRemoteConfig = mockk(relaxed = true),
        sharedPreferences = mockHydraSharedPreferences,
    )

    init {
        coEvery { mockRepo.getBroadcastingConfig(any(), any()) } returns buildBroadcastingConfigUiModel()
        coEvery { mockRepo.getAccountList() } returns accountListResponse()
        coEvery {
            mockRepo.getChannelConfiguration(any(), any())
        } returns buildConfigurationUiModel(
            beautificationConfig = BeautificationConfigUiModel.Empty
        )
        coEvery { mockGetChannelUseCase.executeOnBackground() } returns channelResponse
        coEvery { mockGetAddedTagUseCase.executeOnBackground() } returns mockAddedTag
        coEvery { mockRepo.getProductTagSummarySection(any()) } returns emptyList()
        coEvery { mockConfigStore.getAuthorId() } returns accountListResponse()[0].id
        coEvery { mockConfigStore.getAuthorTypeName() } returns ContentCommonUserType.TYPE_NAME_SELLER
        coEvery { mockUserSession.userId } returns accountListResponse()[0].id
    }

    @Test
    fun testAnalytic_switchAccount() {
        coEvery {
            mockGetChannelUseCase.executeOnBackground()
        } returns channelWithTitleResponse

        createRobot()
            .onClickDropDownAccount()
            .assertEventAction("click - available account")

            .onClickAccountFromBottomSheet()
            .assertEventAction("click - pilih akun")

            .onSwitchAccountConfirmationDialogShown()
            .assertEventAction("click - available account after draft")

            .onClickCancelSwitchWhenHavingDraft()
            .assertEventAction("click - batal switch account")

            .onClickDropDownAccount()
            .onClickAccountFromBottomSheet()
            .onSwitchAccountConfirmationDialogShown()
            .onClickConfirmSwitchWhenHavingDraft()
            .assertEventAction("click - confirm switch account")
    }
}
