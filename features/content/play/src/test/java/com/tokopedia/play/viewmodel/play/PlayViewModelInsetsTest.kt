package com.tokopedia.play.viewmodel.play

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.helper.getOrAwaitValue
import com.tokopedia.play.model.*
import com.tokopedia.play.robot.andWhen
import com.tokopedia.play.robot.play.*
import com.tokopedia.play.robot.thenVerify
import com.tokopedia.play.util.isFalse
import com.tokopedia.play.util.isTrue
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
                    .isShown.isTrue()
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
                    .isHidden.isTrue()
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
                    .isShown.isTrue()
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
                    .isShown.isTrue()
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
                    .isShown.isTrue()
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
                    .isShown.isTrue()
        } andWhen {
            hideKeyboard()
        } thenVerify {
            viewModel.observableBottomInsetsState
                    .getOrAwaitValue()[BottomInsetsType.Keyboard]!!
                    .isHidden.isTrue()
        }
    }

    @Test
    fun `given product bottom sheet is shown, when hide bottom sheet, then bottom sheet should be hidden`() {
        givenPlayViewModelRobot {
            showProductBottomSheet()
        } thenVerify {
            viewModel.observableBottomInsetsState
                    .getOrAwaitValue()[BottomInsetsType.ProductSheet]!!
                    .isShown.isTrue()
        } andWhen {
            hideProductBottomSheet()
        } thenVerify {
            viewModel.observableBottomInsetsState
                    .getOrAwaitValue()[BottomInsetsType.ProductSheet]!!
                    .isHidden.isTrue()
        }
    }

    @Test
    fun `given variant bottom sheet is shown, when hide bottom sheet, then bottom sheet should be hidden`() {
        givenPlayViewModelRobot {
            showVariantBottomSheet()
        } thenVerify {
            viewModel.observableBottomInsetsState
                    .getOrAwaitValue()[BottomInsetsType.VariantSheet]!!
                    .isShown.isTrue()
        } andWhen {
            hideVariantBottomSheet()
        } thenVerify {
            viewModel.observableBottomInsetsState
                    .getOrAwaitValue()[BottomInsetsType.VariantSheet]!!
                    .isHidden.isTrue()
        }
    }

    @Test
    fun `given leaderboard bottom sheet is shown, when hide bottom sheet, then bottom sheet should be hidden`() {
        givenPlayViewModelRobot {
            showLeaderboardBottomSheet()
        } thenVerify {
            viewModel.observableBottomInsetsState
                    .getOrAwaitValue()[BottomInsetsType.LeaderboardSheet]!!
                    .isShown.isTrue()
        } andWhen {
            hideLeaderboardBottomSheet()
        } thenVerify {
            viewModel.observableBottomInsetsState
                    .getOrAwaitValue()[BottomInsetsType.LeaderboardSheet]!!
                    .isHidden.isTrue()
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
                    .isShown.isTrue()
        } andWhen {
            goBack()
        } thenVerify { result ->
            viewModel.observableBottomInsetsState
                    .getOrAwaitValue()[BottomInsetsType.Keyboard]!!
                    .isHidden.isTrue()

            result.isTrue()
        }
    }

    @Test
    fun `given product bottom sheet is shown, when back button is pressed, then product bottom sheet should be hidden and back will be consumed`() {
        givenPlayViewModelRobot {
            showProductBottomSheet()
        } thenVerify {
            viewModel.observableBottomInsetsState
                    .getOrAwaitValue()[BottomInsetsType.ProductSheet]!!
                    .isShown.isTrue()
        } andWhen {
            goBack()
        } thenVerify { result ->
            viewModel.observableBottomInsetsState
                    .getOrAwaitValue()[BottomInsetsType.ProductSheet]!!
                    .isHidden.isTrue()

            result.isTrue()
        }
    }

    @Test
    fun `given variant bottom sheet is shown, when back button is pressed, then variant bottom sheet should be hidden and back will be consumed`() {
        givenPlayViewModelRobot {
            showVariantBottomSheet()
        } thenVerify {
            viewModel.observableBottomInsetsState
                    .getOrAwaitValue()[BottomInsetsType.VariantSheet]!!
                    .isShown.isTrue()
        } andWhen {
            goBack()
        } thenVerify { result ->
            viewModel.observableBottomInsetsState
                    .getOrAwaitValue()[BottomInsetsType.VariantSheet]!!
                    .isHidden.isTrue()

            result.isTrue()
        }
    }

    @Test
    fun `given leaderboard bottom sheet is shown, when back button is pressed, then leaderboard bottom sheet should be hidden and back will be consumed`() {
        givenPlayViewModelRobot {
            showLeaderboardBottomSheet()
        } thenVerify {
            viewModel.observableBottomInsetsState
                    .getOrAwaitValue()[BottomInsetsType.LeaderboardSheet]!!
                    .isShown.isTrue()
        } andWhen {
            goBack()
        } thenVerify { result ->
            viewModel.observableBottomInsetsState
                    .getOrAwaitValue()[BottomInsetsType.LeaderboardSheet]!!
                    .isHidden.isTrue()

            result.isTrue()
        }
    }

    @Test
    fun `given no bottom insets are shown, when back button is pressed, then back will not be consumed`() {
        givenPlayViewModelRobot (
        ) andWhen {
            goBack()
        } thenVerify { result ->
            result.isFalse()
        }
    }
}