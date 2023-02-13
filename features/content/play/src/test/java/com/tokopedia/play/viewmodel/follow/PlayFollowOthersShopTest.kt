package com.tokopedia.play.viewmodel.follow

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.play.domain.repository.PlayViewerRepository
import com.tokopedia.play.model.PlayChannelDataModelBuilder
import com.tokopedia.play.model.PlayPartnerInfoModelBuilder
import com.tokopedia.play.robot.andWhen
import com.tokopedia.play.robot.play.*
import com.tokopedia.play.robot.thenVerify
import com.tokopedia.play.util.assertEqualTo
import com.tokopedia.play.util.isEqualToIgnoringFields
import com.tokopedia.play.view.uimodel.action.PlayViewerNewAction
import com.tokopedia.play.view.uimodel.event.OpenPageEvent
import com.tokopedia.play.view.uimodel.recom.PartnerFollowableStatus
import com.tokopedia.play.view.uimodel.recom.PlayPartnerFollowStatus
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by jegul on 15/07/21
 */
class PlayFollowOthersShopTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = CoroutineTestDispatchers

    private val partnerId = 2L

    private val channelDataBuilder = PlayChannelDataModelBuilder()
    private val partnerInfoModelBuilder = PlayPartnerInfoModelBuilder()
    private val mockChannelData = channelDataBuilder.buildChannelData(
            partnerInfo = partnerInfoModelBuilder.buildPlayPartnerInfo(
                    id = partnerId
            )
    )

    private val mockUserSession: UserSessionInterface = mockk(relaxed = true)

    init {
        every { mockUserSession.shopId } returns "1"
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher.coroutineDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given user is logged in and has not followed shop, when click follow, the shop should be followed`() {
        val mockPartnerRepo: PlayViewerRepository = mockk(relaxed = true)
        coEvery { mockPartnerRepo.getIsFollowingPartner(any()) } returns false
        coEvery { mockPartnerRepo.postFollowStatus(any(), any()) } returns true

        givenPlayViewModelRobot(
                repo = mockPartnerRepo,
                dispatchers = testDispatcher,
                userSession = mockUserSession,
        ) {
            setLoggedIn(true)
            createPage(mockChannelData)
            focusPage(mockChannelData)
        } andWhen {
            submitAction(PlayViewerNewAction.Follow)
        } thenVerify {
            withState {
                partner.status.assertEqualTo(
                    PlayPartnerFollowStatus.Followable(followStatus = PartnerFollowableStatus.Followed)
                )
            }
        }
    }

    @Test
    fun `given user is logged in and has followed shop, when click follow, the shop should be unfollowed`() {
        val mockPartnerRepo: PlayViewerRepository = mockk(relaxed = true)
        coEvery { mockPartnerRepo.getIsFollowingPartner(any()) } returns true
        coEvery { mockPartnerRepo.postFollowStatus(any(), any()) } returns false

        givenPlayViewModelRobot(
                repo = mockPartnerRepo,
                dispatchers = testDispatcher,
                userSession = mockUserSession,
        ) {
            setLoggedIn(true)
            createPage(mockChannelData)
            focusPage(mockChannelData)
        } andWhen {
            submitAction(PlayViewerNewAction.Follow)
        } thenVerify {
            withState {
                partner.status.assertEqualTo(
                        PlayPartnerFollowStatus.Followable(followStatus = PartnerFollowableStatus.NotFollowed)
                )
            }
        }
    }

    @Test
    fun `given user is not logged in, when click follow, the shop should stay unfollowed and open login page`() {
        val mockPartnerRepo: PlayViewerRepository = mockk(relaxed = true)
        coEvery { mockPartnerRepo.getIsFollowingPartner(any()) } returns false

        givenPlayViewModelRobot(
                repo = mockPartnerRepo,
                dispatchers = testDispatcher,
                userSession = mockUserSession,
        ) {
            setLoggedIn(false)
            createPage(mockChannelData)
            focusPage(mockChannelData)
        } andWhenExpectEvent {
            submitAction(PlayViewerNewAction.Follow)
        } thenVerify { event ->
            withState {
                partner.status.assertEqualTo(
                        PlayPartnerFollowStatus.Followable(followStatus = PartnerFollowableStatus.NotFollowed)
                )
            }

            event.isEqualToIgnoringFields(
                    OpenPageEvent(applink = ApplinkConst.LOGIN),
                    OpenPageEvent::requestCode
            )
        }
    }

    /**
     * Interactive
     */

    @Test
    fun `interactive - given user is logged in and has not followed shop, when click follow, the shop should be followed`() {
        val mockPartnerRepo: PlayViewerRepository = mockk(relaxed = true)
        coEvery { mockPartnerRepo.getIsFollowingPartner(any()) } returns false
        coEvery { mockPartnerRepo.postFollowStatus(any(), any()) } returns true

        givenPlayViewModelRobot(
            repo = mockPartnerRepo,
            dispatchers = testDispatcher,
            userSession = mockUserSession,
        ) {
            setLoggedIn(true)
            createPage(mockChannelData)
            focusPage(mockChannelData)
        } andWhen {
            submitAction(PlayViewerNewAction.FollowInteractive)
        } thenVerify {
            withState {
                partner.status.assertEqualTo(
                    PlayPartnerFollowStatus.Followable(followStatus = PartnerFollowableStatus.Followed)
                )
            }
        }
    }

    @Test
    fun `interactive - given user is logged in and has followed shop, when click follow, the shop should be unfollowed`() {
        val mockPartnerRepo: PlayViewerRepository = mockk(relaxed = true)
        coEvery { mockPartnerRepo.getIsFollowingPartner(any()) } returns true
        coEvery { mockPartnerRepo.postFollowStatus(any(), any()) } returns false

        givenPlayViewModelRobot(
            repo = mockPartnerRepo,
            dispatchers = testDispatcher,
            userSession = mockUserSession,
        ) {
            setLoggedIn(true)
            createPage(mockChannelData)
            focusPage(mockChannelData)
        } andWhen {
            submitAction(PlayViewerNewAction.FollowInteractive)
        } thenVerify {
            withState {
                partner.status.assertEqualTo(
                    PlayPartnerFollowStatus.Followable(followStatus = PartnerFollowableStatus.NotFollowed)
                )
            }
        }
    }

    @Test
    fun `interactive - given user is not logged in, when click follow, the shop should stay unfollowed and open login page`() {
        val mockPartnerRepo: PlayViewerRepository = mockk(relaxed = true)
        coEvery { mockPartnerRepo.getIsFollowingPartner(any()) } returns false

        givenPlayViewModelRobot(
            repo = mockPartnerRepo,
            dispatchers = testDispatcher,
            userSession = mockUserSession,
        ) {
            setLoggedIn(false)
            createPage(mockChannelData)
            focusPage(mockChannelData)
        } andWhenExpectEvent {
            submitAction(PlayViewerNewAction.FollowInteractive)
        } thenVerify { event ->
            withState {
                partner.status.assertEqualTo(
                    PlayPartnerFollowStatus.Followable(followStatus = PartnerFollowableStatus.NotFollowed)
                )
            }

            event.isEqualToIgnoringFields(
                OpenPageEvent(applink = ApplinkConst.LOGIN),
                OpenPageEvent::requestCode
            )
        }
    }
}