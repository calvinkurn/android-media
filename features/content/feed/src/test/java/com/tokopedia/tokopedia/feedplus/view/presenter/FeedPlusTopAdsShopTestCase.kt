package com.tokopedia.tokopedia.feedplus.view.presenter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.affiliatecommon.domain.TrackAffiliateClickUseCase
import com.tokopedia.feedcomponent.analytics.topadstracker.SendTopAdsUseCase
import com.tokopedia.feedcomponent.view.viewmodel.responsemodel.TrackAffiliateModel
import com.tokopedia.feedplus.view.presenter.FeedViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Success
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class FeedPlusTopAdsShopTestCase {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var feedViewModel: FeedViewModel
    private var trackUrl: String = "test"
    private lateinit var trackAffiliateClickUseCase: TrackAffiliateClickUseCase
    private lateinit var sendTopAdsUseCase: SendTopAdsUseCase
    private lateinit var baseDispatcher: CoroutineDispatchers

    @Before
    @Throws(Exception::class)
    fun setUpFeedViewModel() {
        baseDispatcher = CoroutineTestDispatchersProvider
        trackAffiliateClickUseCase = mockk(relaxed = true)
        sendTopAdsUseCase = mockk(relaxed = true)
        feedViewModel = spyk(
            FeedViewModel(
                baseDispatcher = baseDispatcher,
                userSession = mockk(),
                likeKolPostUseCase = mockk(),
                addToCartUseCase = mockk(),
                trackAffiliateClickUseCase = trackAffiliateClickUseCase,
                deletePostUseCase = mockk(),
                sendTopAdsUseCase = sendTopAdsUseCase,
                playWidgetTools = mockk(),
                getDynamicFeedNewUseCase = mockk(),
                getWhiteListNewUseCase = mockk(),
                sendReportUseCase = mockk(),
                addToWishlistV2UseCase = mockk(),
                trackVisitChannelBroadcasterUseCase = mockk(),
                feedXTrackViewerUseCase = mockk(),
                checkUpcomingCampaignReminderUseCase = mockk(),
                postUpcomingCampaignReminderUseCase = mockk(),
                shopRecomUseCase = mockk(),
                shopRecomMapper = mockk(),
                shopFollowUseCase = mockk(),
                doFollowUseCase = mockk(),
                doUnfollowUseCase = mockk(),
                profileMutationMapper = mockk(),
                getFollowingUseCase = mockk()
            )
        )
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testDoTrackAffiliateRun() {
        //check if the doTrackAffiliate model run once only
        coEvery { feedViewModel.doTrackAffiliate(trackUrl) } just Runs
        feedViewModel.doTrackAffiliate(trackUrl)
        verify(exactly = 1) { feedViewModel.doTrackAffiliate(trackUrl) }
    }

    @Test
    fun testDoTrackAffiliateSuccessResult() {
        //check if the result returned by private method trackAffiliate(url) returns the same test url when the result is success
        mockkStatic(RequestParams::class)
        mockkStatic(TrackAffiliateClickUseCase::class)
        coEvery { RequestParams.create() } returns RequestParams()
        coEvery { TrackAffiliateClickUseCase.createRequestParams(trackUrl) } returns RequestParams()
        coEvery {
            trackAffiliateClickUseCase.createObservable(any()).toBlocking().first()
        } returns true
        feedViewModel.doTrackAffiliate(trackUrl)
        assertEquals(
            feedViewModel.trackAffiliateResp.value,
            Success(TrackAffiliateModel(true, trackUrl))
        )
    }

    @Test
    fun testDoTrackAffiliateFailResult() {
        //check if the result returned by private method trackAffiliate(url) returns exception
        mockkStatic(RequestParams::class)
        mockkStatic(TrackAffiliateClickUseCase::class)
        coEvery { RequestParams.create() } returns RequestParams()
        coEvery { TrackAffiliateClickUseCase.createRequestParams(trackUrl) } returns RequestParams()
        coEvery {
            trackAffiliateClickUseCase.createObservable(any()).toBlocking().first()
        } throws Exception()
        feedViewModel.doTrackAffiliate(trackUrl)
        assertEquals(feedViewModel.trackAffiliateResp.value, null)
    }

    @Test
    fun testDoTopAdsTracker() {
        every { sendTopAdsUseCase.hitClick(any(), any(), any(), any()) } just Runs
        every { sendTopAdsUseCase.hitImpressions(any(), any(), any(), any()) } just Runs
        feedViewModel.doTopAdsTracker("", "", "", "", true)
        verify(exactly = 1) { sendTopAdsUseCase.hitClick(any(), any(), any(), any()) }

        feedViewModel.doTopAdsTracker("", "", "", "", false)
        verify(exactly = 1) { sendTopAdsUseCase.hitImpressions(any(), any(), any(), any()) }
    }

}
