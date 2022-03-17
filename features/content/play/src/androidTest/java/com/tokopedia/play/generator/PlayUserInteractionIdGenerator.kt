package com.tokopedia.play.generator

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragment
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.play.R
import com.tokopedia.play.domain.repository.PlayViewerRepository
import com.tokopedia.play.test.espresso.waitFor
import com.tokopedia.play.test.factory.TestFragmentFactory
import com.tokopedia.play.test.factory.TestViewModelFactory
import com.tokopedia.play.view.fragment.PlayBottomSheetFragment
import com.tokopedia.play.view.fragment.PlayFragment
import com.tokopedia.play.view.fragment.PlayUserInteractionFragment
import com.tokopedia.play.view.fragment.PlayVideoFragment
import com.tokopedia.play.view.uimodel.mapper.PlayCartMapper
import com.tokopedia.play.view.uimodel.mapper.PlayChannelStatusMapper
import com.tokopedia.play.view.uimodel.mapper.PlayChatUiMapper
import com.tokopedia.play.view.uimodel.mapper.PlayMerchantVoucherUiMapper
import com.tokopedia.play.view.uimodel.mapper.PlayProductTagUiMapper
import com.tokopedia.play.view.uimodel.mapper.PlayUiModelMapper
import com.tokopedia.play.view.uimodel.mapper.PlayUserReportReasoningMapper
import com.tokopedia.play.view.viewmodel.PlayBottomSheetViewModel
import com.tokopedia.play.view.viewmodel.PlayInteractionViewModel
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.play_common.model.mapper.PlayChannelInteractiveMapper
import com.tokopedia.play_common.model.mapper.PlayInteractiveLeaderboardMapper
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by kenny.hadisaputra on 17/03/22
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class PlayUserInteractionIdGenerator {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val userSession: UserSessionInterface = mockk(relaxed = true)

    private val repo: PlayViewerRepository = mockk(relaxed = true)

    private val mapper = PlayUiModelMapper(
        productTagMapper = PlayProductTagUiMapper(),
        merchantVoucherMapper = PlayMerchantVoucherUiMapper(),
        chatMapper = PlayChatUiMapper(userSession),
        channelStatusMapper = PlayChannelStatusMapper(),
        channelInteractiveMapper = PlayChannelInteractiveMapper(),
        interactiveLeaderboardMapper = PlayInteractiveLeaderboardMapper(),
        cartMapper = PlayCartMapper(),
        playUserReportMapper = PlayUserReportReasoningMapper()
    )

    private val mockViewModelFactory = TestViewModelFactory(
        mapOf(
            PlayInteractionViewModel::class.java to {
                mockk<PlayInteractionViewModel>(relaxed = true)
            },
            PlayBottomSheetViewModel::class.java to {
                PlayBottomSheetViewModel(
                    mockk(relaxed = true),
                    userSession,
                    CoroutineDispatchersProvider,
                    repo
                )
            }
        )
    )

    private val playViewModelFactory = object : PlayViewModel.Factory {
        override fun create(channelId: String): PlayViewModel {
            return PlayViewModel(
                channelId = "12345",
                playVideoBuilder = mockk(relaxed = true),
                videoStateProcessorFactory = mockk(relaxed = true),
                channelStateProcessorFactory = mockk(relaxed = true),
                videoBufferGovernorFactory = mockk(relaxed = true),
                getSocketCredentialUseCase = mockk(relaxed = true),
                getReportSummariesUseCase = mockk(relaxed = true),
                trackVisitChannelBroadcasterUseCase = mockk(relaxed = true),
                playSocketToModelMapper = mockk(relaxed = true),
                playUiModelMapper = mapper,
                userSession = mockk(relaxed = true),
                dispatchers = CoroutineDispatchersProvider,
                remoteConfig = mockk(relaxed = true),
                playPreference = mockk(relaxed = true),
                videoLatencyPerformanceMonitoring = mockk(relaxed = true),
                playChannelWebSocket = mockk(relaxed = true),
                repo = mockk(relaxed = true),
                playAnalytic = mockk(relaxed = true),
                timerFactory = mockk(relaxed = true),
                castPlayerHelper = mockk(relaxed = true),
                playShareExperience = mockk(relaxed = true),
            )
        }
    }

    private val fragmentFactory = TestFragmentFactory(
        mapOf(
            PlayFragment::class.java to {
                PlayFragment(
                    playViewModelFactory,
                    mockk(relaxed = true),
                    mockk(relaxed = true),
                )
            },
            PlayUserInteractionFragment::class.java to {
                PlayUserInteractionFragment(
                    viewModelFactory = mockViewModelFactory,
                    dispatchers = CoroutineDispatchersProvider,
                    pipAnalytic = mockk(relaxed = true),
                    analytic = mockk(relaxed = true),
                    multipleLikesIconCacheStorage = mockk(relaxed = true),
                    castAnalyticHelper = mockk(relaxed = true),
                )
            },
            PlayBottomSheetFragment::class.java to {
                PlayBottomSheetFragment(
                    viewModelFactory = mockViewModelFactory,
                    analytic = mockk(relaxed = true),
                )
            },
            PlayVideoFragment::class.java to {
                PlayVideoFragment(
                    dispatchers = CoroutineDispatchersProvider,
                    pipAnalytic = mockk(relaxed = true),
                    analytic = mockk(relaxed = true),
                    pipSessionStorage = mockk(relaxed = true),
                )
            }
        )
    )

    @Test
    fun userInteraction() {
        val scenario = launchFragment<PlayFragment>(
            factory = fragmentFactory,
            themeResId = R.style.AppTheme,
        )

        waitFor(10000)
    }
}