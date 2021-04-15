package com.tokopedia.play.viewmodel.play

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.model.PlayChannelDataModelBuilder
import com.tokopedia.play.model.PlayLikeModelBuilder
import com.tokopedia.play.robot.play.andWhen
import com.tokopedia.play.robot.play.givenPlayViewModelRobot
import com.tokopedia.play.robot.play.thenVerify
import com.tokopedia.play.view.uimodel.recom.LikeSource
import org.junit.Rule
import org.junit.Test

/**
 * Created by jegul on 15/02/21
 */
class PlayViewModelLikeTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val likeModelBuilder = PlayLikeModelBuilder()
    private val channelDataBuilder = PlayChannelDataModelBuilder()

    @Test
    fun `given like info is not set, when change like count, then it should not be updated`() {
        givenPlayViewModelRobot(
        ) andWhen {
            doLike()
        } thenVerify {
            likeStatusResult
                    .isUnavailable()
        } andWhen {
            doUnlike()
        } thenVerify {
            likeStatusResult
                    .isUnavailable()
        }
    }

    @Test
    fun `given like info is set, when do like, then like count should be increased`() {
        val initialTotalLike = 1

        val channelData = channelDataBuilder.buildChannelData(
                likeInfo = likeModelBuilder.buildCompleteData(
                        param = likeModelBuilder.buildParam(),
                        status = likeModelBuilder.buildStatus(
                                totalLike = initialTotalLike.toLong(),
                                totalLikeFormatted = initialTotalLike.toString(),
                                isLiked = false
                        )
                )
        )

        givenPlayViewModelRobot {
            createPage(channelData)
        } andWhen {
            doLike()
        } thenVerify {
            val newLikeCount = initialTotalLike + 1

            likeStatusResult
                    .isAvailable()
                    .isEqualTo(
                            likeModelBuilder.buildStatus(
                                    totalLike = newLikeCount.toLong(),
                                    totalLikeFormatted = newLikeCount.toString(),
                                    isLiked = true,
                                    source = LikeSource.UserAction
                            )
                    )
        }
    }

    @Test
    fun `given like info is not set, when do unlike, then like count should be decreased`() {
        val initialTotalLike = 1

        val channelData = channelDataBuilder.buildChannelData(
                likeInfo = likeModelBuilder.buildCompleteData(
                        param = likeModelBuilder.buildParam(),
                        status = likeModelBuilder.buildStatus(
                                totalLike = initialTotalLike.toLong(),
                                totalLikeFormatted = initialTotalLike.toString(),
                                isLiked = false
                        )
                )
        )

        givenPlayViewModelRobot {
            createPage(channelData)
        } andWhen {
            doUnlike()
        } thenVerify {
            val newLikeCount = initialTotalLike - 1

            likeStatusResult
                    .isAvailable()
                    .isEqualTo(
                            likeModelBuilder.buildStatus(
                                    totalLike = newLikeCount.toLong(),
                                    totalLikeFormatted = newLikeCount.toString(),
                                    isLiked = false,
                                    source = LikeSource.UserAction
                            )
                    )
        }
    }
}