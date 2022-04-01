package com.tokopedia.play.viewmodel.play

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.helper.getOrAwaitValue
import com.tokopedia.play.model.*
import com.tokopedia.play.robot.andWhen
import com.tokopedia.play.robot.play.*
import com.tokopedia.play.robot.thenVerify
import com.tokopedia.play.util.assertFalse
import com.tokopedia.play.util.assertTrue
import com.tokopedia.play.view.type.BottomInsetsType
import com.tokopedia.play.view.type.PlayChannelType
import org.junit.Rule
import org.junit.Test

/**
 * Created by jegul on 11/02/21
 */
class PlayViewModelInsetsTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val channelInfoBuilder = PlayChannelInfoModelBuilder()
    private val channelDataBuilder = PlayChannelDataModelBuilder()

    @Test
    fun `given channel is live, when show keyboard, keyboard insets should be shown`() {
        val channelData = channelDataBuilder.buildChannelData(
                channelDetail = channelInfoBuilder.buildChannelDetail(
                        channelInfo = channelInfoBuilder.buildChannelInfo(
                                channelType = PlayChannelType.Live
                        )
                )
        )

        givenPlayViewModelRobot {
            createPage(channelData)
        } andWhen {
            showKeyboard()
        } thenVerify {
            viewModel.observableBottomInsetsState
                    .getOrAwaitValue()[BottomInsetsType.Keyboard]!!
                    .isShown.assertTrue()
        }
    }

    @Test
    fun `given channel is vod, when show keyboard, keyboard insets should still be hidden`() {
        val channelData = channelDataBuilder.buildChannelData(
                channelDetail = channelInfoBuilder.buildChannelDetail(
                        channelInfo = channelInfoBuilder.buildChannelInfo(
                                channelType = PlayChannelType.VOD
                        )
                )
        )

        givenPlayViewModelRobot {
            createPage(channelData)
        } andWhen {
            showKeyboard()
        } thenVerify {
            viewModel.observableBottomInsetsState
                    .getOrAwaitValue()[BottomInsetsType.Keyboard]!!
                    .isHidden.assertTrue()
        }
    }

    @Test
    fun `when show product bottom sheet, product bottom sheet insets should be shown`() {
        givenPlayViewModelRobot(
        ) andWhen {
            showProductBottomSheet()
        } thenVerify {
            viewModel.observableBottomInsetsState
                    .getOrAwaitValue()[BottomInsetsType.ProductSheet]!!
                    .isShown.assertTrue()
        }
    }

    @Test
    fun `when show variant bottom sheet, variant bottom sheet insets should be shown`() {
        givenPlayViewModelRobot(
        ) andWhen {
            showVariantBottomSheet()
        } thenVerify {
            viewModel.observableBottomInsetsState
                    .getOrAwaitValue()[BottomInsetsType.VariantSheet]!!
                    .isShown.assertTrue()
        }
    }

    @Test
    fun `when show leaderboard bottom sheet, leaderboard bottom sheet insets should be shown`() {
        givenPlayViewModelRobot(
        ) andWhen {
            showLeaderboardBottomSheet()
        } thenVerify {
            viewModel.observableBottomInsetsState
                    .getOrAwaitValue()[BottomInsetsType.LeaderboardSheet]!!
                    .isShown.assertTrue()
        }
    }

    @Test
    fun `when show coupon bottom sheet, coupon bottom sheet insets should be shown`() {
        givenPlayViewModelRobot(
        ) andWhen {
            showCouponBottomSheet()
        } thenVerify {
            viewModel.observableBottomInsetsState
                .getOrAwaitValue()[BottomInsetsType.CouponSheet]!!
                .isShown.assertTrue()
        }
    }

    @Test
    fun `when show kebab bottom sheet, kebab bottom sheet insets should be shown`() {
        givenPlayViewModelRobot(
        ) andWhen {
            showKebabBottomSheet()
        } thenVerify {
            viewModel.observableBottomInsetsState
                .getOrAwaitValue()[BottomInsetsType.KebabMenuSheet]!!
                .isShown.assertTrue()
        }
    }

    @Test
    fun `when show user report bottom sheet, user report bottom sheet insets should be shown`() {
        givenPlayViewModelRobot(
        ) andWhen {
            showUserReportBottomSheet()
        } thenVerify {
            viewModel.observableBottomInsetsState
                .getOrAwaitValue()[BottomInsetsType.UserReportSheet]!!
                .isShown.assertTrue()
        }
    }

    @Test
    fun `when show user report submission bottom sheet, user report submission bottom sheet insets should be shown`() {
        givenPlayViewModelRobot(
        ) andWhen {
            showUserReportSubmissionBottomSheet()
        } thenVerify {
            viewModel.observableBottomInsetsState
                .getOrAwaitValue()[BottomInsetsType.UserReportSubmissionSheet]!!
                .isShown.assertTrue()
        }
    }

    @Test
    fun `given keyboard is shown, when hide keyboard, then keyboard should be hidden`() {
        val channelData = channelDataBuilder.buildChannelData(
                channelDetail = channelInfoBuilder.buildChannelDetail(
                        channelInfo = channelInfoBuilder.buildChannelInfo(
                                channelType = PlayChannelType.Live
                        )
                )
        )

        givenPlayViewModelRobot {
            createPage(channelData)
            showKeyboard()
        } thenVerify {
            viewModel.observableBottomInsetsState
                    .getOrAwaitValue()[BottomInsetsType.Keyboard]!!
                    .isShown.assertTrue()
        } andWhen {
            hideKeyboard()
        } thenVerify {
            viewModel.observableBottomInsetsState
                    .getOrAwaitValue()[BottomInsetsType.Keyboard]!!
                    .isHidden.assertTrue()
        }
    }

    @Test
    fun `given product bottom sheet is shown, when hide bottom sheet, then bottom sheet should be hidden`() {
        givenPlayViewModelRobot {
            showProductBottomSheet()
        } thenVerify {
            viewModel.observableBottomInsetsState
                    .getOrAwaitValue()[BottomInsetsType.ProductSheet]!!
                    .isShown.assertTrue()
        } andWhen {
            hideProductBottomSheet()
        } thenVerify {
            viewModel.observableBottomInsetsState
                    .getOrAwaitValue()[BottomInsetsType.ProductSheet]!!
                    .isHidden.assertTrue()
        }
    }

    @Test
    fun `given variant bottom sheet is shown, when hide bottom sheet, then bottom sheet should be hidden`() {
        givenPlayViewModelRobot {
            showVariantBottomSheet()
        } thenVerify {
            viewModel.observableBottomInsetsState
                    .getOrAwaitValue()[BottomInsetsType.VariantSheet]!!
                    .isShown.assertTrue()
        } andWhen {
            hideVariantBottomSheet()
        } thenVerify {
            viewModel.observableBottomInsetsState
                    .getOrAwaitValue()[BottomInsetsType.VariantSheet]!!
                    .isHidden.assertTrue()
        }
    }

    @Test
    fun `given leaderboard bottom sheet is shown, when hide bottom sheet, then bottom sheet should be hidden`() {
        givenPlayViewModelRobot {
            showLeaderboardBottomSheet()
        } thenVerify {
            viewModel.observableBottomInsetsState
                    .getOrAwaitValue()[BottomInsetsType.LeaderboardSheet]!!
                    .isShown.assertTrue()
        } andWhen {
            hideLeaderboardBottomSheet()
        } thenVerify {
            viewModel.observableBottomInsetsState
                    .getOrAwaitValue()[BottomInsetsType.LeaderboardSheet]!!
                    .isHidden.assertTrue()
        }
    }

    @Test
    fun `given coupon bottom sheet is shown, when hide bottom sheet, then bottom sheet should be hidden`() {
        givenPlayViewModelRobot {
            showCouponBottomSheet()
        } thenVerify {
            viewModel.observableBottomInsetsState
                .getOrAwaitValue()[BottomInsetsType.CouponSheet]!!
                .isShown.assertTrue()
        } andWhen {
            hideCouponBottomSheet()
        } thenVerify {
            viewModel.observableBottomInsetsState
                .getOrAwaitValue()[BottomInsetsType.CouponSheet]!!
                .isHidden.assertTrue()
        }
    }

    @Test
    fun `given kebab bottom sheet is shown, when hide bottom sheet, then bottom sheet should be hidden`() {
        givenPlayViewModelRobot {
            showKebabBottomSheet()
        } thenVerify {
            viewModel.observableBottomInsetsState
                .getOrAwaitValue()[BottomInsetsType.KebabMenuSheet]!!
                .isShown.assertTrue()
        } andWhen {
            hideKebabBottomSheet()
        } thenVerify {
            viewModel.observableBottomInsetsState
                .getOrAwaitValue()[BottomInsetsType.KebabMenuSheet]!!
                .isHidden.assertTrue()
        }
    }

    @Test
    fun `given user report bottom sheet is shown, when hide bottom sheet, then bottom sheet should be hidden`() {
        givenPlayViewModelRobot {
            showUserReportBottomSheet()
        } thenVerify {
            viewModel.observableBottomInsetsState
                .getOrAwaitValue()[BottomInsetsType.UserReportSheet]!!
                .isShown.assertTrue()
        } andWhen {
            hideUserReportBottomSheet()
        } thenVerify {
            viewModel.observableBottomInsetsState
                .getOrAwaitValue()[BottomInsetsType.UserReportSheet]!!
                .isHidden.assertTrue()
        }
    }

    @Test
    fun `given user report submission bottom sheet is shown, when hide bottom sheet, then bottom sheet should be hidden`() {
        givenPlayViewModelRobot {
            showUserReportSubmissionBottomSheet()
        } thenVerify {
            viewModel.observableBottomInsetsState
                .getOrAwaitValue()[BottomInsetsType.UserReportSubmissionSheet]!!
                .isShown.assertTrue()
        } andWhen {
            hideUserReportSubmissionBottomSheet()
        } thenVerify {
            viewModel.observableBottomInsetsState
                .getOrAwaitValue()[BottomInsetsType.UserReportSubmissionSheet]!!
                .isHidden.assertTrue()
        }
    }

    @Test
    fun `given keyboard is shown, when back button is pressed, then keyboard should be hidden and back will be consumed`() {
        val channelData = channelDataBuilder.buildChannelData(
                channelDetail = channelInfoBuilder.buildChannelDetail(
                        channelInfo = channelInfoBuilder.buildChannelInfo(
                                channelType = PlayChannelType.Live
                        )
                )
        )

        givenPlayViewModelRobot {
            createPage(channelData)
            showKeyboard()
        } thenVerify {
            viewModel.observableBottomInsetsState
                    .getOrAwaitValue()[BottomInsetsType.Keyboard]!!
                    .isShown.assertTrue()
        } andWhen {
            goBack()
        } thenVerify { result ->
            viewModel.observableBottomInsetsState
                    .getOrAwaitValue()[BottomInsetsType.Keyboard]!!
                    .isHidden.assertTrue()

            result.assertTrue()
        }
    }

    @Test
    fun `given coupon bottom sheet is shown, when back button is pressed, then coupon bottom sheet should be hidden and back will be consumed`() {
        givenPlayViewModelRobot {
            showCouponBottomSheet()
        } thenVerify {
            viewModel.observableBottomInsetsState
                    .getOrAwaitValue()[BottomInsetsType.CouponSheet]!!
                    .isShown.assertTrue()
        } andWhen {
            goBack()
        } thenVerify { result ->
            viewModel.observableBottomInsetsState
                    .getOrAwaitValue()[BottomInsetsType.CouponSheet]!!
                    .isHidden.assertTrue()

            result.assertTrue()
        }
    }

    @Test
    fun `given variant bottom sheet is shown, when back button is pressed, then variant bottom sheet should be hidden and back will be consumed`() {
        givenPlayViewModelRobot {
            showVariantBottomSheet()
        } thenVerify {
            viewModel.observableBottomInsetsState
                    .getOrAwaitValue()[BottomInsetsType.VariantSheet]!!
                    .isShown.assertTrue()
        } andWhen {
            goBack()
        } thenVerify { result ->
            viewModel.observableBottomInsetsState
                    .getOrAwaitValue()[BottomInsetsType.VariantSheet]!!
                    .isHidden.assertTrue()

            result.assertTrue()
        }
    }

    @Test
    fun `given leaderboard bottom sheet is shown, when back button is pressed, then leaderboard bottom sheet should be hidden and back will be consumed`() {
        givenPlayViewModelRobot {
            showLeaderboardBottomSheet()
        } thenVerify {
            viewModel.observableBottomInsetsState
                    .getOrAwaitValue()[BottomInsetsType.LeaderboardSheet]!!
                    .isShown.assertTrue()
        } andWhen {
            goBack()
        } thenVerify { result ->
            viewModel.observableBottomInsetsState
                    .getOrAwaitValue()[BottomInsetsType.LeaderboardSheet]!!
                    .isHidden.assertTrue()

            result.assertTrue()
        }
    }

    @Test
    fun `given user report bottom sheet is shown, when back button is pressed, then user report bottom sheet should be hidden and back will be consumed`() {
        givenPlayViewModelRobot {
            showUserReportBottomSheet()
        } thenVerify {
            viewModel.observableBottomInsetsState
                .getOrAwaitValue()[BottomInsetsType.UserReportSheet]!!
                .isShown.assertTrue()
        } andWhen {
            goBack()
        } thenVerify { result ->
            viewModel.observableBottomInsetsState
                .getOrAwaitValue()[BottomInsetsType.UserReportSheet]!!
                .isHidden.assertTrue()

            result.assertTrue()
        }
    }

    @Test
    fun `given user report submission bottom sheet is shown, when back button is pressed, then user report submission bottom sheet should be hidden and back will be consumed`() {
        givenPlayViewModelRobot {
            showUserReportSubmissionBottomSheet()
        } thenVerify {
            viewModel.observableBottomInsetsState
                .getOrAwaitValue()[BottomInsetsType.UserReportSubmissionSheet]!!
                .isShown.assertTrue()
        } andWhen {
            goBack()
        } thenVerify { result ->
            viewModel.observableBottomInsetsState
                .getOrAwaitValue()[BottomInsetsType.UserReportSubmissionSheet]!!
                .isHidden.assertTrue()

            result.assertTrue()
        }
    }

    @Test
    fun `given no bottom insets are shown, when back button is pressed, then back will not be consumed`() {
        givenPlayViewModelRobot (
        ) andWhen {
            goBack()
        } thenVerify { result ->
            result.assertFalse()
        }
    }
}