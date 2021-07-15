package com.tokopedia.play.viewmodel.follow

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.domain.repository.PlayViewerPartnerRepository
import com.tokopedia.play.model.PlayChannelDataModelBuilder
import com.tokopedia.play.robot.play.andWhen
import com.tokopedia.play.robot.play.givenPlayViewModelRobot
import com.tokopedia.play.robot.play.thenVerify
import com.tokopedia.play.view.uimodel.action.ClickFollowAction
import com.tokopedia.play.view.uimodel.recom.PlayPartnerFollowInfo
import com.tokopedia.play.view.uimodel.recom.PlayPartnerFollowStatus
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import io.mockk.coEvery
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
class PlayFollowOwnShopTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val isOwnShop = true

    private val testDispatcher = CoroutineTestDispatchers

    private val channelDataBuilder = PlayChannelDataModelBuilder()
    private val mockChannelData = channelDataBuilder.buildChannelData()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher.coroutineDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given user is logged in and has not followed shop, when click follow, the shop should be not followable`() {
        val mockPartnerRepo: PlayViewerPartnerRepository = mockk(relaxed = true)
        coEvery { mockPartnerRepo.getPartnerFollowInfo(any()) } returns PlayPartnerFollowInfo(
                isOwnShop = isOwnShop,
                isFollowing = false
        )

        givenPlayViewModelRobot(
                partnerRepo = mockPartnerRepo,
                dispatchers = testDispatcher
        ) {
            setLoggedIn(true)
            createPage(mockChannelData)
            focusPage(mockChannelData)
        } andWhen {
            submitAction(ClickFollowAction)
        } thenVerify {
            withState {
                followStatus.isEqualTo(
                        PlayPartnerFollowStatus.NotFollowable
                )
            }
        }
    }

    @Test
    fun `given user is logged in and has followed shop, when click follow, the shop should be not followable`() {
        val mockPartnerRepo: PlayViewerPartnerRepository = mockk(relaxed = true)
        coEvery { mockPartnerRepo.getPartnerFollowInfo(any()) } returns PlayPartnerFollowInfo(
                isOwnShop = isOwnShop,
                isFollowing = true
        )

        givenPlayViewModelRobot(
                partnerRepo = mockPartnerRepo,
                dispatchers = testDispatcher
        ) {
            setLoggedIn(true)
            createPage(mockChannelData)
            focusPage(mockChannelData)
        } andWhen {
            submitAction(ClickFollowAction)
        } thenVerify {
            withState {
                followStatus.isEqualTo(
                        PlayPartnerFollowStatus.NotFollowable
                )
            }
        }
    }

    @Test
    fun `given user is not logged in, when click follow, the shop should be not followable`() {
        val mockPartnerRepo: PlayViewerPartnerRepository = mockk(relaxed = true)
        coEvery { mockPartnerRepo.getPartnerFollowInfo(any()) } returns PlayPartnerFollowInfo(
                isOwnShop = isOwnShop,
                isFollowing = false
        )

        givenPlayViewModelRobot(
                partnerRepo = mockPartnerRepo,
                dispatchers = testDispatcher
        ) {
            setLoggedIn(false)
            createPage(mockChannelData)
            focusPage(mockChannelData)
        } andWhen {
            submitAction(ClickFollowAction)
        } thenVerify {
            withState {
                followStatus.isEqualTo(
                        PlayPartnerFollowStatus.NotFollowable
                )
            }
        }
    }
}