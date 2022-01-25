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
import com.tokopedia.play.view.type.ProductAction
import com.tokopedia.play.view.uimodel.action.ClickCloseLeaderboardSheetAction
import com.tokopedia.play.view.uimodel.action.InteractiveWinnerBadgeClickedAction
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
                estimatedProductSheetHeight = 100,
                action = ProductAction.Buy,
                product = productTagBuilder.buildProductLine()
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
    fun `when show kebab bottom sheet, kebab bottom sheet insets should be shown`() {
        createPlayViewModelRobot {
            //when
            viewModel.onShowKebabMenuSheet(estimatedSheetHeight = 100)

            //then
            viewModel.observableBottomInsetsState
                .getOrAwaitValue()[BottomInsetsType.KebabMenuSheet]!!
                .isShown.assertTrue()
        }
    }

    @Test
    fun `when show user report bottom sheet, user report bottom sheet insets should be shown`() {
        createPlayViewModelRobot {
            //when
            viewModel.onShowUserReportSheet(estimatedSheetHeight = 100)

            //then
            viewModel.observableBottomInsetsState
                .getOrAwaitValue()[BottomInsetsType.UserReportSheet]!!
                .isShown.assertTrue()
        }
    }

    @Test
    fun `when show user report submission bottom sheet, user report submission bottom sheet insets should be shown`() {
        createPlayViewModelRobot {
            //when
            viewModel.onShowUserReportSubmissionSheet(estimatedSheetHeight = 100)

            //then
            viewModel.observableBottomInsetsState
                .getOrAwaitValue()[BottomInsetsType.UserReportSubmissionSheet]!!
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
                estimatedProductSheetHeight = 100,
                action = ProductAction.Buy,
                product = productTagBuilder.buildProductLine()
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
    fun `given kebab bottom sheet is shown, when hide bottom sheet, then bottom sheet should be hidden`() {
        createPlayViewModelRobot {
            //when
            viewModel.onShowKebabMenuSheet(estimatedSheetHeight = 100)

            //then
            viewModel.observableBottomInsetsState
                .getOrAwaitValue()[BottomInsetsType.KebabMenuSheet]!!
                .isShown.assertTrue()

            // when
            viewModel.hideKebabMenuSheet()

            //then
            viewModel.observableBottomInsetsState
                .getOrAwaitValue()[BottomInsetsType.KebabMenuSheet]!!
                .isHidden.assertTrue()
        }
    }

    @Test
    fun `given user report bottom sheet is shown, when hide bottom sheet, then bottom sheet should be hidden`() {
        createPlayViewModelRobot {
            //when
            viewModel.onShowUserReportSheet(estimatedSheetHeight = 100)

            //then
            viewModel.observableBottomInsetsState
                .getOrAwaitValue()[BottomInsetsType.UserReportSheet]!!
                .isShown.assertTrue()

            // when
            viewModel.hideUserReportSheet()

            //then
            viewModel.observableBottomInsetsState
                .getOrAwaitValue()[BottomInsetsType.UserReportSheet]!!
                .isHidden.assertTrue()
        }
    }

    @Test
    fun `given user report submission bottom sheet is shown, when hide bottom sheet, then bottom sheet should be hidden`() {
        createPlayViewModelRobot {
            //when
            viewModel.onShowUserReportSubmissionSheet(estimatedSheetHeight = 100)

            //then
            viewModel.observableBottomInsetsState
                .getOrAwaitValue()[BottomInsetsType.UserReportSubmissionSheet]!!
                .isShown.assertTrue()

            // when
            viewModel.hideUserReportSubmissionSheet()

            //then
            viewModel.observableBottomInsetsState
                .getOrAwaitValue()[BottomInsetsType.UserReportSubmissionSheet]!!
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
                estimatedProductSheetHeight = 100,
                action = ProductAction.Buy,
                product = productTagBuilder.buildProductLine()
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
    fun `given user report bottom sheet is shown, when back button is pressed, then user report bottom sheet should be hidden and back will be consumed`() {
        createPlayViewModelRobot {
            createPage(mockLiveChannelData)
            viewModel.onShowUserReportSheet(estimatedSheetHeight = 100)

            viewModel.observableBottomInsetsState
                .getOrAwaitValue()[BottomInsetsType.UserReportSheet]!!
                .isShown.assertTrue()
            val back = viewModel.goBack()

            viewModel.observableBottomInsetsState
                .getOrAwaitValue()[BottomInsetsType.UserReportSheet]!!
                .isHidden.assertTrue()

            back.assertTrue()
        }
    }

    @Test
    fun `given user report submission bottom sheet is shown, when back button is pressed, then user report submission bottom sheet should be hidden and back will be consumed`() {
        createPlayViewModelRobot {
            createPage(mockLiveChannelData)
            viewModel.onShowUserReportSubmissionSheet(estimatedSheetHeight = 100)

            viewModel.observableBottomInsetsState
                .getOrAwaitValue()[BottomInsetsType.UserReportSubmissionSheet]!!
                .isShown.assertTrue()
            val back = viewModel.goBack()

            viewModel.observableBottomInsetsState
                .getOrAwaitValue()[BottomInsetsType.UserReportSubmissionSheet]!!
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
}
