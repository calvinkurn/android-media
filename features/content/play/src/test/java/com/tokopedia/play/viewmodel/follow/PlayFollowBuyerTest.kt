package com.tokopedia.play.viewmodel.follow

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.play.domain.repository.PlayViewerRepository
import com.tokopedia.play.model.PlayChannelDataModelBuilder
import com.tokopedia.play.model.PlayPartnerInfoModelBuilder
import com.tokopedia.play.robot.andWhen
import com.tokopedia.play.robot.play.andWhenExpectEvent
import com.tokopedia.play.robot.play.givenPlayViewModelRobot
import com.tokopedia.play.robot.play.withState
import com.tokopedia.play.robot.thenVerify
import com.tokopedia.play.ui.toolbar.model.PartnerFollowAction
import com.tokopedia.play.ui.toolbar.model.PartnerType
import com.tokopedia.play.util.assertEqualTo
import com.tokopedia.play.util.isEqualToIgnoringFields
import com.tokopedia.play.view.uimodel.action.PlayViewerNewAction
import com.tokopedia.play.view.uimodel.event.OpenPageEvent
import com.tokopedia.play.view.uimodel.recom.PartnerFollowableStatus
import com.tokopedia.play.view.uimodel.recom.PlayPartnerFollowStatus
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test

/**
 * @author by astidhiyaa on 07/03/22
 */
@ExperimentalCoroutinesApi
class PlayFollowBuyerTest {
    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = CoroutineTestDispatchers

    private val encUserId = "ahshdb&822"

    private val channelDataBuilder = PlayChannelDataModelBuilder()
    private val partnerInfoModelBuilder = PlayPartnerInfoModelBuilder()
    private val mockChannelData = channelDataBuilder.buildChannelData(
        partnerInfo = partnerInfoModelBuilder.buildPlayPartnerInfo(
            type = PartnerType.Buyer
        )
    )

    private val mockUserSession: UserSessionInterface = mockk(relaxed = true)

    private val mockNotFollowed = Pair(false, encUserId)
    private val mockFollowed = Pair(true, encUserId)

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    init {
        every { mockUserSession.shopId } returns "2000"
    }

    @Test
    fun `given user is logged in and has not followed buyer, when click follow, the buyer should be followed`() {
        val mockPartnerRepo: PlayViewerRepository = mockk(relaxed = true)
        coEvery { mockPartnerRepo.getFollowingKOL(any()) } returns mockNotFollowed
        coEvery { mockPartnerRepo.postFollowKol(any(), PartnerFollowAction.Follow) } returns true

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
    fun `given user is logged in and has followed buyer, when click follow, the buyer should be unfollowed`() {
        val mockPartnerRepo: PlayViewerRepository = mockk(relaxed = true)
        coEvery { mockPartnerRepo.getFollowingKOL(any()) } returns mockFollowed
        coEvery { mockPartnerRepo.postFollowKol(any(), PartnerFollowAction.UnFollow) } returns true

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
    fun `given user is not logged in, when click follow, the buyer should stay unfollowed and open login page`() {
        val mockPartnerRepo: PlayViewerRepository = mockk(relaxed = true)
        coEvery { mockPartnerRepo.getFollowingKOL(any()) } returns mockNotFollowed

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

    @Test
    fun `given user is logged in and has not followed buyer, when click follow, the buyer should be followable but not followed`() {
        val mockRepo: PlayViewerRepository = mockk(relaxed = true)
        coEvery { mockRepo.getFollowingKOL(any()) } returns mockNotFollowed

        givenPlayViewModelRobot(
            repo = mockRepo,
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
                    PlayPartnerFollowStatus.Followable(PartnerFollowableStatus.NotFollowed)
                )
            }
        }
    }

    @Test
    fun `given user is logged in and has followed buyer, when click follow, the buyer should be followable and not followed`() {
        val mockRepo: PlayViewerRepository = mockk(relaxed = true)
        coEvery { mockRepo.getFollowingKOL(any()) } returns mockFollowed
        coEvery { mockRepo.postFollowKol(any(), any()) } returns true

        givenPlayViewModelRobot(
            repo = mockRepo,
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
                    PlayPartnerFollowStatus.Followable(PartnerFollowableStatus.NotFollowed)
                )
            }
        }
    }

    @Test
    fun `given user is not logged in, when click follow, the buyer should be not unknown`() {
        val mockRepo: PlayViewerRepository = mockk(relaxed = true)
        coEvery { mockRepo.getFollowingKOL(any()) } returns mockNotFollowed

        givenPlayViewModelRobot(
            repo = mockRepo,
            dispatchers = testDispatcher,
            userSession = mockUserSession,
        ) {
            setLoggedIn(false)
            createPage(mockChannelData)
            focusPage(mockChannelData)
        } andWhen {
            submitAction(PlayViewerNewAction.Follow)
        } thenVerify {
            withState {
                partner.status.assertEqualTo(
                    PlayPartnerFollowStatus.Followable(PartnerFollowableStatus.NotFollowed)
                )
            }
        }
    }

    @Test
    fun `when user visit their own channel hide follow button, set as NotFollowble (hide from user)` (){
        val mockChannelData = channelDataBuilder.buildChannelData(
            partnerInfo = partnerInfoModelBuilder.buildPlayPartnerInfo(
                id = 2000,
            ),
        )

        val mockRepo: PlayViewerRepository = mockk(relaxed = true)
        coEvery { mockRepo.getIsFollowingPartner(any()) } returns false

        every { mockRepo.getChannelData(any()) } returns mockChannelData

        givenPlayViewModelRobot(
            repo = mockRepo,
            dispatchers = testDispatcher,
            userSession = mockUserSession,
        ) {
            setLoggedIn(true)
            createPage(mockChannelData)
            focusPage(mockChannelData)
        } thenVerify {
            withState {
                partner.status.assertEqualTo(
                    PlayPartnerFollowStatus.NotFollowable
                )
            }
        }
    }

    /**
     * Interactive
     */

    @Test
    fun `interactive - given user is logged in and has not followed buyer, when click follow, the buyer should be followed`() {
        val mockPartnerRepo: PlayViewerRepository = mockk(relaxed = true)
        coEvery { mockPartnerRepo.getFollowingKOL(any()) } returns mockNotFollowed
        coEvery { mockPartnerRepo.postFollowKol(any(), PartnerFollowAction.Follow) } returns true

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
    fun `interactive - given user is not logged in, when click follow, the buyer should stay unfollowed and open login page`() {
        val mockPartnerRepo: PlayViewerRepository = mockk(relaxed = true)
        coEvery { mockPartnerRepo.getFollowingKOL(any()) } returns mockNotFollowed

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

    @Test
    fun `interactive - given user is logged in and has not followed buyer, when click follow, the buyer should be followable but not followed`() {
        val mockRepo: PlayViewerRepository = mockk(relaxed = true)
        coEvery { mockRepo.getFollowingKOL(any()) } returns mockNotFollowed

        givenPlayViewModelRobot(
            repo = mockRepo,
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
                    PlayPartnerFollowStatus.Followable(PartnerFollowableStatus.NotFollowed)
                )
            }
        }
    }

    @Test
    fun `interactive - given user is not logged in, when click follow, the buyer should be not unknown`() {
        val mockRepo: PlayViewerRepository = mockk(relaxed = true)
        coEvery { mockRepo.getFollowingKOL(any()) } returns mockNotFollowed

        givenPlayViewModelRobot(
            repo = mockRepo,
            dispatchers = testDispatcher,
            userSession = mockUserSession,
        ) {
            setLoggedIn(false)
            createPage(mockChannelData)
            focusPage(mockChannelData)
        } andWhen {
            submitAction(PlayViewerNewAction.FollowInteractive)
        } thenVerify {
            withState {
                partner.status.assertEqualTo(
                    PlayPartnerFollowStatus.Followable(PartnerFollowableStatus.NotFollowed)
                )
            }
        }
    }

    @Test
    fun `interactive - when user visit their own channel hide follow button, set as NotFollowble (hide from user)` (){
        val mockChannelData = channelDataBuilder.buildChannelData(
            partnerInfo = partnerInfoModelBuilder.buildPlayPartnerInfo(
                id = 2000,
            ),
        )

        val mockRepo: PlayViewerRepository = mockk(relaxed = true)
        coEvery { mockRepo.getIsFollowingPartner(any()) } returns false

        every { mockRepo.getChannelData(any()) } returns mockChannelData

        givenPlayViewModelRobot(
            repo = mockRepo,
            dispatchers = testDispatcher,
            userSession = mockUserSession,
        ) {
            setLoggedIn(true)
            createPage(mockChannelData)
            focusPage(mockChannelData)
        } thenVerify {
            withState {
                partner.status.assertEqualTo(
                    PlayPartnerFollowStatus.NotFollowable
                )
            }
        }
    }
}