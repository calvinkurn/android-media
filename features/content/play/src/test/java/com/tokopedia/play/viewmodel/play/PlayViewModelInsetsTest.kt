package com.tokopedia.play.viewmodel.play

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.model.*
import com.tokopedia.play.robot.play.*
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
                channelInfo = channelInfoBuilder.buildChannelInfo(
                        channelType = PlayChannelType.Live
                )
        )

        givenPlayViewModelRobot {
            createPage(channelData)
        } andWhen {
            showKeyboard()
        } thenVerify {
            bottomInsetsResult
                    .keyboard
                    .isShown()
        }
    }

    @Test
    fun `given channel is vod, when show keyboard, keyboard insets should still be hidden`() {
        val channelData = channelDataBuilder.buildChannelData(
                channelInfo = channelInfoBuilder.buildChannelInfo(
                        channelType = PlayChannelType.VOD
                )
        )

        givenPlayViewModelRobot {
            createPage(channelData)
        } andWhen {
            showKeyboard()
        } thenVerify {
            bottomInsetsResult
                    .keyboard
                    .isHidden()
        }
    }

    @Test
    fun `when show product bottom sheet, product bottom sheet insets should be shown`() {
        givenPlayViewModelRobot(
        ) andWhen {
            showProductBottomSheet()
        } thenVerify {
            bottomInsetsResult
                    .productBottomSheet
                    .isShown()
        }
    }

    @Test
    fun `when show variant bottom sheet, variant bottom sheet insets should be shown`() {
        givenPlayViewModelRobot(
        ) andWhen {
            showVariantBottomSheet()
        } thenVerify {
            bottomInsetsResult
                    .variantBottomSheet
                    .isShown()
        }
    }

    @Test
    fun `given keyboard is shown, when hide keyboard, then keyboard should be hidden`() {
        val channelData = channelDataBuilder.buildChannelData(
                channelInfo = channelInfoBuilder.buildChannelInfo(
                        channelType = PlayChannelType.Live
                )
        )

        givenPlayViewModelRobot {
            createPage(channelData)
            showKeyboard()
        } thenVerify {
            bottomInsetsResult
                    .keyboard
                    .isShown()
        } andWhen {
            hideKeyboard()
        } thenVerify {
            bottomInsetsResult
                    .keyboard
                    .isHidden()
        }
    }

    @Test
    fun `given product bottom sheet is shown, when hide bottom sheet, then bottom sheet should be hidden`() {
        givenPlayViewModelRobot {
            showProductBottomSheet()
        } thenVerify {
            bottomInsetsResult
                    .productBottomSheet
                    .isShown()
        } andWhen {
            hideProductBottomSheet()
        } thenVerify {
            bottomInsetsResult
                    .productBottomSheet
                    .isHidden()
        }
    }

    @Test
    fun `given variant bottom sheet is shown, when hide bottom sheet, then bottom sheet should be hidden`() {
        givenPlayViewModelRobot {
            showVariantBottomSheet()
        } thenVerify {
            bottomInsetsResult
                    .variantBottomSheet
                    .isShown()
        } andWhen {
            hideVariantBottomSheet()
        } thenVerify {
            bottomInsetsResult
                    .variantBottomSheet
                    .isHidden()
        }
    }

    @Test
    fun `given keyboard is shown, when back button is pressed, then keyboard should be hidden and back will be consumed`() {
        val channelData = channelDataBuilder.buildChannelData(
                channelInfo = channelInfoBuilder.buildChannelInfo(
                        channelType = PlayChannelType.Live
                )
        )

        givenPlayViewModelRobot {
            createPage(channelData)
            showKeyboard()
        } thenVerify {
            bottomInsetsResult
                    .keyboard.isShown()
        } andWhen {
            goBack()
        } thenVerify { result ->
            bottomInsetsResult
                    .keyboard
                    .isHidden()

            result.isTrue()
        }
    }

    @Test
    fun `given product bottom sheet is shown, when back button is pressed, then product bottom sheet should be hidden and back will be consumed`() {
        givenPlayViewModelRobot {
            showProductBottomSheet()
        } thenVerify {
            bottomInsetsResult
                    .productBottomSheet.isShown()
        } andWhen {
            goBack()
        } thenVerify { result ->
            bottomInsetsResult
                    .productBottomSheet
                    .isHidden()

            result.isTrue()
        }
    }

    @Test
    fun `given variant bottom sheet is shown, when back button is pressed, then variant bottom sheet should be hidden and back will be consumed`() {
        givenPlayViewModelRobot {
            showVariantBottomSheet()
        } thenVerify {
            bottomInsetsResult
                    .variantBottomSheet.isShown()
        } andWhen {
            goBack()
        } thenVerify { result ->
            bottomInsetsResult
                    .variantBottomSheet
                    .isHidden()

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