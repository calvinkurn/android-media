package com.tokopedia.play.viewmodel.play

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.play.domain.repository.PlayViewerRepository
import com.tokopedia.play.model.PlayChannelDataModelBuilder
import com.tokopedia.play.model.PlayPartnerInfoModelBuilder
import com.tokopedia.play.robot.play.createPlayViewModelRobot
import com.tokopedia.play.ui.toolbar.model.PartnerType
import com.tokopedia.play.util.assertEqualTo
import com.tokopedia.play.util.assertFalse
import com.tokopedia.play.util.assertTrue
import com.tokopedia.play.view.uimodel.action.ClickPartnerNameAction
import com.tokopedia.play.view.uimodel.action.SendWarehouseId
import com.tokopedia.play.view.uimodel.event.OpenPageEvent
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test

/**
 * @author by astidhiyaa on 28/06/22
 */
@ExperimentalCoroutinesApi
class PlayTokoNowTest {
    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val dispatchers = coroutineTestRule.dispatchers

    private val channelDataBuilder = PlayChannelDataModelBuilder()
    private val partnerInfoModelBuilder = PlayPartnerInfoModelBuilder()
    private val mockChannelTokonow = channelDataBuilder.buildChannelData(
        partnerInfo = partnerInfoModelBuilder.buildPlayPartnerInfo(
            type = PartnerType.TokoNow
        )
    )
    private val mockChannelTkpd = channelDataBuilder.buildChannelData(
        partnerInfo = partnerInfoModelBuilder.buildPlayPartnerInfo(
            type = PartnerType.Tokopedia
        )
    )
    private val mockRepo: PlayViewerRepository = mockk(relaxed = true)

    private val tokonowAppLink = "tokopedia://now"

    @Test
    fun `given user is not logged in, when click partner name in tokonow channel, go to login page`() {
        createPlayViewModelRobot (
            repo = mockRepo,
            dispatchers = dispatchers,
        ).use {
            it.createPage(mockChannelTokonow)
            it.focusPage(mockChannelTokonow)
            it.setLoggedIn(false)

            it.recordEvent {
                it.viewModel.submitAction(ClickPartnerNameAction(tokonowAppLink))
            }.last().assertEqualTo(OpenPageEvent(applink = ApplinkConst.LOGIN))
        }
    }

    @Test
    fun `given user is logged in, when click partner name in tokonow channel, go to tokonow page`() {
        createPlayViewModelRobot (
            repo = mockRepo,
            dispatchers = dispatchers,
        ).use {
            it.createPage(mockChannelTokonow)
            it.focusPage(mockChannelTokonow)
            it.setLoggedIn(true)

            it.recordEvent {
                it.viewModel.submitAction(ClickPartnerNameAction(tokonowAppLink))
            }.last().assertEqualTo(OpenPageEvent(applink = tokonowAppLink, pipMode = true))
        }
    }

    @Test
    fun `given user is not logged in, when click partner name in non - tokonow channel, go to that page`() {
        val appLink = "tokopedia://shop/1"
        createPlayViewModelRobot (
            repo = mockRepo,
            dispatchers = dispatchers,
        ).use {
            it.createPage(mockChannelTkpd)
            it.focusPage(mockChannelTkpd)
            it.setLoggedIn(false)

            it.recordEvent {
                it.viewModel.submitAction(ClickPartnerNameAction(appLink))
            }.last().assertEqualTo(OpenPageEvent(applink = appLink, pipMode = true))
        }
    }

    @Test
    fun `given user is logged in, when click partner name in non - tokonow channel, go to that page`() {
        val appLink = "tokopedia://shop/1"
        createPlayViewModelRobot (
            repo = mockRepo,
            dispatchers = dispatchers,
        ).use {
            it.createPage(mockChannelTkpd)
            it.focusPage(mockChannelTkpd)
            it.setLoggedIn(true)

            it.recordEvent {
                it.viewModel.submitAction(ClickPartnerNameAction(appLink))
            }.last().assertEqualTo(OpenPageEvent(applink = appLink, pipMode = true))
        }
    }

    @Test
    fun `given user is non logged in, entering tokonow channel and ooc show address widget`() {
        createPlayViewModelRobot (
            repo = mockRepo,
            dispatchers = dispatchers,
        ).use {
            it.createPage(mockChannelTokonow)
            it.focusPage(mockChannelTokonow)
            it.setLoggedIn(false)

            val state = it.recordState {
                it.viewModel.submitAction(SendWarehouseId(id = "0", isOOC = true))
            }
            state.address.shouldShow.assertTrue()
        }
    }

    @Test
    fun `given user is logged in, entering tokonow channel and ooc show address widget`() {
        createPlayViewModelRobot (
            repo = mockRepo,
            dispatchers = dispatchers,
        ).use {
            it.createPage(mockChannelTokonow)
            it.focusPage(mockChannelTokonow)
            it.setLoggedIn(true)

            val state = it.recordState {
                it.viewModel.submitAction(SendWarehouseId(id = "0", isOOC = true))
            }
            state.address.shouldShow.assertTrue()
        }
    }

    @Test
    fun `given user is non logged in, entering non-tokonow channel and ooc dont show address widget`() {
        createPlayViewModelRobot (
            repo = mockRepo,
            dispatchers = dispatchers,
        ).use {
            it.createPage(mockChannelTkpd)
            it.focusPage(mockChannelTkpd)
            it.setLoggedIn(false)

            val state = it.recordState {
                it.viewModel.submitAction(SendWarehouseId(id = "0", isOOC = true))
            }
            state.address.shouldShow.assertFalse()
        }
    }

    @Test
    fun `given user is logged in, entering non-tokonow channel and ooc dont show address widget`() {
        createPlayViewModelRobot (
            repo = mockRepo,
            dispatchers = dispatchers,
        ).use {
            it.createPage(mockChannelTkpd)
            it.focusPage(mockChannelTkpd)
            it.setLoggedIn(true)

            val state = it.recordState {
                it.viewModel.submitAction(SendWarehouseId(id = "0", isOOC = true))
            }
            state.address.shouldShow.assertFalse()
        }
    }

    @Test
    fun `given user is non logged in, entering tokonow channel and within coverage, dont show address widget`() {
        createPlayViewModelRobot (
            repo = mockRepo,
            dispatchers = dispatchers,
        ).use {
            it.createPage(mockChannelTokonow)
            it.focusPage(mockChannelTokonow)
            it.setLoggedIn(false)

            val state = it.recordState {
                it.viewModel.submitAction(SendWarehouseId(id = "2216", isOOC = false))
            }
            state.address.shouldShow.assertFalse()
        }
    }

    @Test
    fun `given user is logged in, entering tokonow channel and within coverage, dont show address widget`() {
        createPlayViewModelRobot (
            repo = mockRepo,
            dispatchers = dispatchers,
        ).use {
            it.createPage(mockChannelTokonow)
            it.focusPage(mockChannelTokonow)
            it.setLoggedIn(true)

            val state = it.recordState {
                it.viewModel.submitAction(SendWarehouseId(id = "2216", isOOC = false))
            }
            state.address.shouldShow.assertFalse()
        }
    }

    @Test
    fun `given user is logged in, entering non - tokonow channel and within coverage, dont show address widget`() {
        createPlayViewModelRobot (
            repo = mockRepo,
            dispatchers = dispatchers,
        ).use {
            it.createPage(mockChannelTkpd)
            it.focusPage(mockChannelTkpd)
            it.setLoggedIn(true)

            val state = it.recordState {
                it.viewModel.submitAction(SendWarehouseId(id = "2216", isOOC = false))
            }
            state.address.shouldShow.assertFalse()
        }
    }

    @Test
    fun `given user is non - logged in, entering non - tokonow channel and within coverage, dont show address widget`() {
        createPlayViewModelRobot (
            repo = mockRepo,
            dispatchers = dispatchers,
        ).use {
            it.createPage(mockChannelTokonow)
            it.focusPage(mockChannelTkpd)
            it.setLoggedIn(false)

            val state = it.recordState {
                it.viewModel.submitAction(SendWarehouseId(id = "2216", isOOC = false))
            }
            state.address.shouldShow.assertFalse()
        }
    }

    //product tokonow in tokonow channel
    //product tokonow in non-tokonow channel
}