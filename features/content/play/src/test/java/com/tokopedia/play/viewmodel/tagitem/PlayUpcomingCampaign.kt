package com.tokopedia.play.viewmodel.tagitem

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.play.domain.repository.PlayViewerRepository
import com.tokopedia.play.domain.repository.PlayViewerTagItemRepository
import com.tokopedia.play.model.PlayChannelDataModelBuilder
import com.tokopedia.play.model.UiModelBuilder
import com.tokopedia.play.robot.andWhen
import com.tokopedia.play.robot.play.andWhenExpectEvent
import com.tokopedia.play.robot.play.givenPlayViewModelRobot
import com.tokopedia.play.robot.play.withState
import com.tokopedia.play.robot.thenVerify
import com.tokopedia.play.util.*
import com.tokopedia.play.view.type.PlayUpcomingBellStatus
import com.tokopedia.play.view.type.ProductSectionType
import com.tokopedia.play.view.uimodel.action.SendUpcomingReminder
import com.tokopedia.play.view.uimodel.event.LoginEvent
import com.tokopedia.play.view.uimodel.event.OpenPageEvent
import com.tokopedia.play.view.uimodel.recom.PlayPartnerFollowStatus
import com.tokopedia.play.view.uimodel.recom.tagitem.ProductSectionUiModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.date.DateUtil
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test

/**
 * @author by astidhiyaa on 22/03/22
 */
@ExperimentalCoroutinesApi
class PlayUpcomingCampaign {
    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val testDispatcher = coroutineTestRule.dispatchers

    private val channelDataBuilder = PlayChannelDataModelBuilder()
    private val modelBuilder = UiModelBuilder.get()

    private val mockUserSession: UserSessionInterface = mockk(relaxed = true)
    private val mockRepo: PlayViewerRepository = mockk(relaxed = true)

    private val campaignId = 1004L

    init {
        every { mockUserSession.shopId } returns "100"
    }

    private fun generateMockSection(reminder: Boolean) = List(3) {
        modelBuilder.buildProductSection(
            productList = listOf(),
            config = ProductSectionUiModel.Section.ConfigUiModel(
                type = ProductSectionType.Upcoming,
                controlTime = DateUtil.getCurrentDate(),
                startTime = null,
                endTime = null,
                serverTime = null,
                timerInfo = "Dimulai dalam",
                background = ProductSectionUiModel.Section.BackgroundUiModel(gradients = emptyList(),
                    imageUrl = "\"https://images.tokopedia.net/img/cache/700/product-1/2017/4/3/5510248/5510248_1fada4fe-8444-4911-b3e0-b70b54b119b6_1500_946.jpg\""),
                title = "L'oreal New Launch",
                reminder = if(reminder) PlayUpcomingBellStatus.On else PlayUpcomingBellStatus.Off,
            ),
            id = campaignId.toString()
        )
    }

    private fun generateMockProduct(reminder: Boolean) = channelDataBuilder.buildChannelData(
        tagItems = modelBuilder.buildTagItem(
            product = modelBuilder.buildProductModel(productList = generateMockSection(reminder))
        )
    )

    @Test
    fun `given user is logged in check if user has reminded upco campaign, if user has not reminded, the campaign should be not reminded`() {
            every { mockRepo.getChannelData(any()) } returns generateMockProduct(false)
            givenPlayViewModelRobot(
                repo = mockRepo,
                userSession = mockUserSession,
                dispatchers = testDispatcher
            ) {
                setLoggedIn(true)
                createPage(generateMockProduct(false))
                focusPage(generateMockProduct(false))
            } thenVerify {
                withState {
                    tagItems.product.productSectionList.forEach {
                        if (it is ProductSectionUiModel.Section) it.config.reminder.assertEqualTo(PlayUpcomingBellStatus.Off)
                    }
                }
            }
    }

    @Test
    fun `given user is logged in check if user has reminded upco campaign, if user has reminded, the campaign should be reminded`() {
        every { mockRepo.getChannelData(any()) } returns generateMockProduct(true)
        givenPlayViewModelRobot(
            repo = mockRepo,
            userSession = mockUserSession,
            dispatchers = testDispatcher
        ) {
            setLoggedIn(true)
            createPage(generateMockProduct(true))
            focusPage(generateMockProduct(true))
        } thenVerify {
            withState {
                tagItems.product.productSectionList.first().apply {
                    if (this is ProductSectionUiModel.Section) config.reminder.assertEqualTo(PlayUpcomingBellStatus.On)
                }
            }
        }
    }

    @Test
    fun `given user is logged in and has not reminded upco campaign, when click remind, the campaign should be reminded`() {
        every { mockRepo.getChannelData(any()) } returns generateMockProduct(false)
        coEvery { mockRepo.checkUpcomingCampaign(any()) } returns false
        coEvery { mockRepo.subscribeUpcomingCampaign(any(),any()) } returns PlayViewerTagItemRepository.CampaignReminder(true, "")

        coroutineTestRule.runBlockingTest {
            givenPlayViewModelRobot(
                repo = mockRepo,
                userSession = mockUserSession,
                dispatchers = testDispatcher
            ) {
                setLoggedIn(true)
                createPage(generateMockProduct(false))
                focusPage(generateMockProduct(false))
            } andWhenExpectEvent {
                submitAction(SendUpcomingReminder(generateMockSection(false).first()))
            } thenVerify {
                withState {
                    tagItems.product.productSectionList.first().apply {
                        if (this is ProductSectionUiModel.Section) config.reminder.assertEqualTo(PlayUpcomingBellStatus.On)
                    }
                }
            }
        }
    }

    @Test
    fun `given user is logged in and has reminded upco campaign, when click unremind, the campaign should be not reminded`() {
        every { mockRepo.getChannelData(any()) } returns generateMockProduct(true)
        coEvery { mockRepo.checkUpcomingCampaign(any()) } returns false
        coEvery { mockRepo.subscribeUpcomingCampaign(any(),any()) } returns PlayViewerTagItemRepository.CampaignReminder(false, "")

        coroutineTestRule.runBlockingTest {
            givenPlayViewModelRobot(
                repo = mockRepo,
                userSession = mockUserSession,
                dispatchers = testDispatcher
            ) {
                setLoggedIn(true)
                createPage(generateMockProduct(true))
                focusPage(generateMockProduct(true))
            } andWhenExpectEvent {
                submitAction(SendUpcomingReminder(generateMockSection(true).first()))
            } thenVerify {
                withState {
                    tagItems.product.productSectionList.first().apply {
                        if (this is ProductSectionUiModel.Section) config.reminder.assertEqualTo(PlayUpcomingBellStatus.Off)
                    }
                }
            }
        }
    }

    @Test
    fun `given user is not logged in and has not reminded upco campaign, open login page`() {
        coroutineTestRule.runBlockingTest {
            givenPlayViewModelRobot(
                repo = mockRepo,
                userSession = mockUserSession,
                dispatchers = testDispatcher
            ) {
                setLoggedIn(false)
                createPage(generateMockProduct(false))
                focusPage(generateMockProduct(false))
            } andWhenExpectEvent {
                submitAction(SendUpcomingReminder(generateMockSection(false).first()))
            } thenVerify { event ->
                event.isEqualToIgnoringFields(
                    LoginEvent {},
                    LoginEvent::afterSuccess,
                )
            }
        }
    }
}
