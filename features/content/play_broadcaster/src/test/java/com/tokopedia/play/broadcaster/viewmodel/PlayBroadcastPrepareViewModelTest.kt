package com.tokopedia.play.broadcaster.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.broadcaster.data.config.ChannelConfigStore
import com.tokopedia.play.broadcaster.data.config.ChannelConfigStoreImpl
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastDataStore
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastDataStoreImpl
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastSetupDataStore
import com.tokopedia.play.broadcaster.domain.usecase.CreateLiveStreamChannelUseCase
import com.tokopedia.play.broadcaster.domain.usecase.GetLiveFollowersDataUseCase
import com.tokopedia.play.broadcaster.model.ModelBuilder
import com.tokopedia.play.broadcaster.testdouble.MockCoverDataStore
import com.tokopedia.play.broadcaster.testdouble.MockProductDataStore
import com.tokopedia.play.broadcaster.testdouble.MockSetupDataStore
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastUiMapper
import com.tokopedia.play.broadcaster.ui.model.CoverSource
import com.tokopedia.play.broadcaster.ui.model.PlayCoverUiModel
import com.tokopedia.play.broadcaster.util.TestCoroutineDispatcherProvider
import com.tokopedia.play.broadcaster.util.getOrAwaitValue
import com.tokopedia.play.broadcaster.view.state.CoverSetupState
import com.tokopedia.play.broadcaster.view.state.SetupDataState
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastPrepareViewModel
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by jegul on 28/09/20
 */
class PlayBroadcastPrepareViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()
    private val dispatcherProvider = TestCoroutineDispatcherProvider(testDispatcher)

    private lateinit var channelConfigStore: ChannelConfigStore

    private lateinit var productDataStore: MockProductDataStore
    private lateinit var coverDataStore: MockCoverDataStore
    private lateinit var mockSetupDataStore: MockSetupDataStore
    private lateinit var dataStore: PlayBroadcastDataStore

    private val playBroadcastMapper = PlayBroadcastUiMapper()

    private lateinit var createLiveStreamChannelUseCase: CreateLiveStreamChannelUseCase
    private lateinit var getLiveFollowersDataUseCase: GetLiveFollowersDataUseCase

    private lateinit var userSession: UserSessionInterface

    private lateinit var viewModel: PlayBroadcastPrepareViewModel

    private val modelBuilder = ModelBuilder()

    private val liveFollowerResponse = modelBuilder.buildGetLiveFollowers()

    @Before
    fun setUp() {
        channelConfigStore = ChannelConfigStoreImpl()

        productDataStore = MockProductDataStore(dispatcherProvider)
        coverDataStore = MockCoverDataStore(dispatcherProvider)
        mockSetupDataStore = MockSetupDataStore(productDataStore, coverDataStore)

        dataStore = PlayBroadcastDataStoreImpl(mockSetupDataStore)

        userSession = mockk(relaxed = true)
        every { userSession.shopId } returns "12345"

        channelConfigStore.setChannelId("12345")

        createLiveStreamChannelUseCase = mockk(relaxed = true)
        getLiveFollowersDataUseCase = mockk(relaxed = true)

        coEvery { getLiveFollowersDataUseCase.executeOnBackground() } returns liveFollowerResponse

        viewModel = PlayBroadcastPrepareViewModel(
                dispatcher = dispatcherProvider,
                userSession = userSession,
                channelConfigStore = channelConfigStore,
                createLiveStreamChannelUseCase = createLiveStreamChannelUseCase,
                getLiveFollowersDataUseCase = getLiveFollowersDataUseCase,
                mDataStore = dataStore,
                playBroadcastMapper = playBroadcastMapper
        )
    }

    @Test
    fun `when cover title is set up and then fetched, then it should return the correct title`() {
        val title = "abcde"
        dataStore.getSetupDataStore().getCoverDataStore().setFullCover(
                modelBuilder.buildPlayCoverUiModel(
                        title = title
                )
        )

        Assertions
                .assertThat(viewModel.title)
                .isEqualTo(title)
    }

    @Test
    fun `when cover title is not set up and then fetched, then it should throw error`() {
        Assertions
                .assertThatIllegalStateException()
                .isThrownBy { viewModel.title }
    }

    @Test
    fun `when max duration description is set, then it should return the correct description`() {
        val desc = "30 minutes"
        channelConfigStore.setMaxDurationDesc(desc)

        Assertions
                .assertThat(viewModel.maxDurationDesc)
                .isEqualTo(desc)
    }

    @Test
    fun `when max duration description is not set, then it should return empty string`() {
        Assertions
                .assertThat(viewModel.maxDurationDesc)
                .isEmpty()
    }

    @Test
    fun `when setup data store is overwritten and then fetched, it should return the new setup data store detail`() {
        val newDataStore = mockk<PlayBroadcastSetupDataStore>(relaxed = true)
        viewModel.setDataFromSetupDataStore(newDataStore)

        Assertions
                .assertThat(mockSetupDataStore.isOverwritten)
                .isEqualTo(true)
    }

    @Test
    fun `when create livestream with no product, it should return error`() {
        productDataStore.setSelectedProducts(emptyList())

        viewModel.createLiveStream()

        val result = viewModel.observableCreateLiveStream.value

        Assertions
                .assertThat(result)
                .isInstanceOf(NetworkResult.Fail::class.java)
    }

    @Test
    fun `when create livestream with uncropped cover, it should return error`() {
        coverDataStore.setFullCover(PlayCoverUiModel.empty())

        viewModel.createLiveStream()

        val result = viewModel.observableCreateLiveStream.value

        Assertions
                .assertThat(result)
                .isInstanceOf(NetworkResult.Fail::class.java)
    }

    @Test
    fun `when create livestream with no cover, it should return error`() {
        viewModel.createLiveStream()

        val result = viewModel.observableCreateLiveStream.value

        Assertions
                .assertThat(result)
                .isInstanceOf(NetworkResult.Fail::class.java)
    }

    @Test
    fun `when create livestream with products and valid cover, it should return success`() {
        productDataStore.setSelectedProducts(listOf(modelBuilder.buildProductData()))
        coverDataStore.setFullCover(PlayCoverUiModel(
                title = "abc",
                croppedCover = CoverSetupState.Cropped.Uploaded(null, mockk(relaxed = true), CoverSource.Camera),
                state = SetupDataState.Uploaded
        ))

        coEvery { createLiveStreamChannelUseCase.executeOnBackground() } returns modelBuilder.buildCreateLiveStreamGetMedia()

        viewModel.createLiveStream()

        val result = viewModel.observableCreateLiveStream.value

        Assertions
                .assertThat(result)
                .isInstanceOf(NetworkResult.Success::class.java)
    }

    @Test
    fun `when get live followers failed, then it should return the mock follower`() {
        coEvery { getLiveFollowersDataUseCase.executeOnBackground() } throws IllegalStateException()

        viewModel = PlayBroadcastPrepareViewModel(
                dispatcher = dispatcherProvider,
                userSession = userSession,
                channelConfigStore = channelConfigStore,
                createLiveStreamChannelUseCase = createLiveStreamChannelUseCase,
                getLiveFollowersDataUseCase = getLiveFollowersDataUseCase,
                mDataStore = dataStore,
                playBroadcastMapper = playBroadcastMapper
        )

        val result = viewModel.observableFollowers.getOrAwaitValue()

        Assertions
                .assertThat(result.totalFollowers)
                .isEqualTo(0)
    }

    @Test
    fun `when get live followers success, then it should return the live followers`() {
        coEvery { getLiveFollowersDataUseCase.executeOnBackground() } returns liveFollowerResponse

        viewModel = PlayBroadcastPrepareViewModel(
                dispatcher = dispatcherProvider,
                userSession = userSession,
                channelConfigStore = channelConfigStore,
                createLiveStreamChannelUseCase = createLiveStreamChannelUseCase,
                getLiveFollowersDataUseCase = getLiveFollowersDataUseCase,
                mDataStore = dataStore,
                playBroadcastMapper = playBroadcastMapper
        )

        val result = viewModel.observableFollowers.getOrAwaitValue()

        Assertions
                .assertThat(result)
                .isEqualTo(
                        playBroadcastMapper.mapLiveFollowers(liveFollowerResponse)
                )
    }
}