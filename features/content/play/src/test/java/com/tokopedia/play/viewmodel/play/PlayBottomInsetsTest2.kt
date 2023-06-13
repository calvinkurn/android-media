package com.tokopedia.play.viewmodel.play

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.extensions.isLeaderboardSheetShown
import com.tokopedia.play.helper.getOrAwaitValue
import com.tokopedia.play.model.PlayChannelDataModelBuilder
import com.tokopedia.play.model.PlayChannelInfoModelBuilder
import com.tokopedia.play.model.PlayProductTagsModelBuilder
import com.tokopedia.play.robot.play.createPlayViewModelRobot
import com.tokopedia.play.util.assertFalse
import com.tokopedia.play.util.assertTrue
import com.tokopedia.play.view.type.BottomInsetsType
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.uimodel.action.ClickCloseLeaderboardSheetAction
import com.tokopedia.play.view.uimodel.action.InteractiveWinnerBadgeClickedAction
import com.tokopedia.play.view.uimodel.state.KebabMenuType
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * @author by astidhiyaa on 21/01/22
 */
class PlayBottomInsetsTest2 {
    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val channelInfoBuilder = PlayChannelInfoModelBuilder()
    private val channelDataBuilder = PlayChannelDataModelBuilder()

    private val mockVODChannelData = channelDataBuilder.buildChannelData(
        channelDetail = channelInfoBuilder.buildChannelDetail(
            channelInfo = channelInfoBuilder.buildChannelInfo(
                channelType = PlayChannelType.VOD
            )
        )
    )
    private val mockLiveChannelData = channelDataBuilder.buildChannelData(
        channelDetail = channelInfoBuilder.buildChannelDetail(
            channelInfo = channelInfoBuilder.buildChannelInfo(
                channelType = PlayChannelType.Live
            )
        )
    )

    private val productTagBuilder = PlayProductTagsModelBuilder()

    @Before
    fun setUp() {
        Dispatchers.setMain(CoroutineTestDispatchers.coroutineDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given channel is live, when show keyboard, keyboard insets should be shown`() {
        createPlayViewModelRobot {
            //given
            createPage(mockLiveChannelData)

            //when
            viewModel.onKeyboardShown(estimatedKeyboardHeight = 100)

            //then
            viewModel.observableBottomInsetsState
                .getOrAwaitValue()[BottomInsetsType.Keyboard]!!
                .isShown.assertTrue()
        }
    }

    @Test
    fun `given channel is vod, when show keyboard, keyboard insets should still be hidden`() {
        createPlayViewModelRobot {
            //given
            createPage(mockVODChannelData)

            //when
            viewModel.onKeyboardShown(estimatedKeyboardHeight = 100)

            //then
            viewModel.observableBottomInsetsState
                .getOrAwaitValue()[BottomInsetsType.Keyboard]!!
                .isHidden.assertTrue()
        }
    }

    @Test
    fun `when show product bottom sheet, product bottom sheet insets should be shown`() {
        createPlayViewModelRobot {
            //when
            viewModel.onShowProductSheet(estimatedProductSheetHeight = 100)

            //then
            viewModel.observableBottomInsetsState
                .getOrAwaitValue()[BottomInsetsType.ProductSheet]!!
                .isShown.assertTrue()
        }
    }

    @Test
    fun `when show variant bottom sheet, variant bottom sheet insets should be shown`() {
        createPlayViewModelRobot {
            //when
            viewModel.onShowVariantSheet(
                estimatedProductSheetHeight = 100
            )

            //then
            viewModel.observableBottomInsetsState
                .getOrAwaitValue()[BottomInsetsType.VariantSheet]!!
                .isShown.assertTrue()
        }
    }

    @Test
    fun `when show leaderboard bottom sheet, leaderboard bottom sheet insets should be shown`() {
        createPlayViewModelRobot()
            .use {
                val state = it.recordState {
                    createPage(mockLiveChannelData)
                    submitAction(InteractiveWinnerBadgeClickedAction(height = 100))
                }
                state.bottomInsets.isLeaderboardSheetShown.assertTrue()
            }
    }

    @Test
    fun `when show coupon bottom sheet, coupon bottom sheet insets should be shown`() {
        createPlayViewModelRobot {
            //when
            viewModel.showCouponSheet(estimatedHeight = 100)

            //then
            viewModel.observableBottomInsetsState
                .getOrAwaitValue()[BottomInsetsType.CouponSheet]!!
                .isShown.assertTrue()
        }
    }

    @Test
    fun `given keyboard is shown, when hide keyboard, then keyboard should be hidden`() {
        createPlayViewModelRobot {
            //given
            createPage(mockLiveChannelData)

            //when
            viewModel.onKeyboardShown(estimatedKeyboardHeight = 100)

            //then
            viewModel.observableBottomInsetsState
                .getOrAwaitValue()[BottomInsetsType.Keyboard]!!
                .isShown.assertTrue()

            // when
            viewModel.onKeyboardHidden()

            //then
            viewModel.observableBottomInsetsState
                .getOrAwaitValue()[BottomInsetsType.Keyboard]!!
                .isHidden.assertTrue()
        }
    }

    @Test
    fun `given product bottom sheet is shown, when hide bottom sheet, then bottom sheet should be hidden`() {
        createPlayViewModelRobot {
            //when
            viewModel.onShowProductSheet(estimatedProductSheetHeight = 100)

            //then
            viewModel.observableBottomInsetsState
                .getOrAwaitValue()[BottomInsetsType.ProductSheet]!!
                .isShown.assertTrue()

            // when
            viewModel.onHideProductSheet()

            //then
            viewModel.observableBottomInsetsState
                .getOrAwaitValue()[BottomInsetsType.ProductSheet]!!
                .isHidden.assertTrue()
        }
    }

    @Test
    fun `given variant bottom sheet is shown, when hide bottom sheet, then bottom sheet should be hidden`() {
        createPlayViewModelRobot {
            //when
            viewModel.onShowVariantSheet(
                estimatedProductSheetHeight = 100
            )

            //then
            viewModel.observableBottomInsetsState
                .getOrAwaitValue()[BottomInsetsType.VariantSheet]!!
                .isShown.assertTrue()

            // when
            viewModel.onHideVariantSheet()

            //then
            viewModel.observableBottomInsetsState
                .getOrAwaitValue()[BottomInsetsType.VariantSheet]!!
                .isHidden.assertTrue()
        }
    }

    @Test
    fun `given leaderboard bottom sheet is shown, when hide bottom sheet, then bottom sheet should be hidden`() {
        createPlayViewModelRobot()
            .use {
                val state = it.recordState {
                    createPage(mockLiveChannelData)
                    submitAction(ClickCloseLeaderboardSheetAction)
                }
                state.bottomInsets.isLeaderboardSheetShown.assertFalse()
            }
    }

    @Test
    fun `given coupon bottom sheet is shown, when hide bottom sheet, then bottom sheet should be hidden`() {
        createPlayViewModelRobot {
            //when
            viewModel.showCouponSheet(estimatedHeight = 100)

            //then
            viewModel.observableBottomInsetsState
                .getOrAwaitValue()[BottomInsetsType.CouponSheet]!!
                .isShown.assertTrue()

            // when
            viewModel.hideCouponSheet()

            //then
            viewModel.observableBottomInsetsState
                .getOrAwaitValue()[BottomInsetsType.CouponSheet]!!
                .isHidden.assertTrue()
        }
    }

    @Test
    fun `given keyboard is shown, when back button is pressed, then keyboard should be hidden and back will be consumed`() {
        createPlayViewModelRobot {
            createPage(mockLiveChannelData)
            viewModel.onKeyboardShown(estimatedKeyboardHeight = 100)

            viewModel.observableBottomInsetsState
                .getOrAwaitValue()[BottomInsetsType.Keyboard]!!
                .isShown.assertTrue()
            val back = viewModel.goBack()

            viewModel.observableBottomInsetsState
                .getOrAwaitValue()[BottomInsetsType.Keyboard]!!
                .isHidden.assertTrue()

            back.assertTrue()
        }
    }

    @Test
    fun `given coupon bottom sheet is shown, when back button is pressed, then coupon bottom sheet should be hidden and back will be consumed`() {
        createPlayViewModelRobot {
            createPage(mockLiveChannelData)
            viewModel.showCouponSheet(estimatedHeight = 100)

            viewModel.observableBottomInsetsState
                .getOrAwaitValue()[BottomInsetsType.CouponSheet]!!
                .isShown.assertTrue()
            val back = viewModel.goBack()

            viewModel.observableBottomInsetsState
                .getOrAwaitValue()[BottomInsetsType.CouponSheet]!!
                .isHidden.assertTrue()

            back.assertTrue()
        }
    }

    @Test
    fun `given variant bottom sheet is shown, when back button is pressed, then variant bottom sheet should be hidden and back will be consumed`() {
        createPlayViewModelRobot {
            createPage(mockLiveChannelData)
            viewModel.onShowVariantSheet(
                estimatedProductSheetHeight = 100
            )

            viewModel.observableBottomInsetsState
                .getOrAwaitValue()[BottomInsetsType.VariantSheet]!!
                .isShown.assertTrue()
            val back = viewModel.goBack()

            viewModel.observableBottomInsetsState
                .getOrAwaitValue()[BottomInsetsType.VariantSheet]!!
                .isHidden.assertTrue()

            back.assertTrue()
        }
    }

    @Test
    fun `given leaderboard bottom sheet is shown, when back button is pressed, then leaderboard bottom sheet should be hidden and back will be consumed`() {
        createPlayViewModelRobot {
            createPage(mockLiveChannelData)
            viewModel.submitAction(InteractiveWinnerBadgeClickedAction(height = 100))

            viewModel.observableBottomInsetsState
                .getOrAwaitValue()[BottomInsetsType.LeaderboardSheet]!!
                .isShown.assertTrue()
            val back = viewModel.goBack()

            viewModel.observableBottomInsetsState
                .getOrAwaitValue()[BottomInsetsType.LeaderboardSheet]!!
                .isHidden.assertTrue()

            back.assertTrue()
        }
    }

    @Test
    fun `given no bottom insets are shown, when back button is pressed, then back will not be consumed`() {
        createPlayViewModelRobot {
            val back = viewModel.goBack()
            back.assertFalse()
        }
    }

    @Test
    fun `given kebab bottom sheet is shown, then kebab bottom sheet should be shown`() {
        createPlayViewModelRobot {
            createPage(mockLiveChannelData)

            viewModel.onShowKebabMenuSheet()

            viewModel.observableKebabMenuSheet.getOrAwaitValue()[KebabMenuType.ThreeDots]!!.isShown.assertTrue()

            viewModel.observableKebabMenuSheet
                .getOrAwaitValue()[KebabMenuType.ThreeDots]!!
                .isShown.assertTrue()
        }
    }

    @Test
    fun `given kebab bottom sheet is hidden, then kebab bottom sheet should be hidden`() {
        createPlayViewModelRobot {
            createPage(mockLiveChannelData)

            viewModel.hideKebabMenuSheet()

            viewModel.observableKebabMenuSheet.getOrAwaitValue()[KebabMenuType.ThreeDots]!!.isHidden.assertTrue()

            viewModel.observableKebabMenuSheet
                .getOrAwaitValue()[KebabMenuType.ThreeDots]!!
                .isHidden.assertTrue()
        }
    }

    @Test
    fun `given user report bottom sheet is shown, then user report bottom sheet should be shown`() {
        createPlayViewModelRobot {
            createPage(mockLiveChannelData)

            viewModel.onShowUserReportSheet()

            viewModel.observableKebabMenuSheet.getOrAwaitValue()[KebabMenuType.UserReportList]!!.isShown.assertTrue()

            viewModel.observableKebabMenuSheet
                .getOrAwaitValue()[KebabMenuType.UserReportList]!!
                .isShown.assertTrue()
        }
    }

    @Test
    fun `given user report bottom sheet is hidden, then user report bottom sheet should be hidden`() {
        createPlayViewModelRobot {
            createPage(mockLiveChannelData)

            viewModel.hideUserReportSheet()

            viewModel.observableKebabMenuSheet.getOrAwaitValue()[KebabMenuType.UserReportList]!!.isHidden.assertTrue()

            viewModel.observableKebabMenuSheet
                .getOrAwaitValue()[KebabMenuType.UserReportList]!!
                .isHidden.assertTrue()
        }
    }
    @Test
    fun `given user report submission bottom sheet is shown, then user report submission bottom sheet should be shown`() {
        createPlayViewModelRobot {
            createPage(mockLiveChannelData)

            viewModel.onShowUserReportSubmissionSheet()

            viewModel.observableKebabMenuSheet.getOrAwaitValue()[KebabMenuType.UserReportSubmission]!!.isShown.assertTrue()

            viewModel.observableKebabMenuSheet
                .getOrAwaitValue()[KebabMenuType.UserReportSubmission]!!
                .isShown.assertTrue()
        }
    }

    @Test
    fun `given user report submission bottom sheet is hidden, then user report submission bottom sheet should be hidden`() {
        createPlayViewModelRobot {
            createPage(mockLiveChannelData)

            viewModel.hideUserReportSubmissionSheet()

            viewModel.observableKebabMenuSheet.getOrAwaitValue()[KebabMenuType.UserReportSubmission]!!.isHidden.assertTrue()

            viewModel.observableKebabMenuSheet
                .getOrAwaitValue()[KebabMenuType.UserReportSubmission]!!
                .isHidden.assertTrue()
        }
    }
}
