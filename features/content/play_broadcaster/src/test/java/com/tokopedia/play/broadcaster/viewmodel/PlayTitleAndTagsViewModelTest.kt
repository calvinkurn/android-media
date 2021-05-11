package com.tokopedia.play.broadcaster.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastSetupDataStore
import com.tokopedia.play.broadcaster.domain.usecase.GetRecommendedChannelTagsUseCase
import com.tokopedia.play.broadcaster.robot.andThen
import com.tokopedia.play.broadcaster.robot.andWhen
import com.tokopedia.play.broadcaster.robot.givenPlayTitleAndTagsViewModel
import com.tokopedia.play.broadcaster.robot.thenVerify
import com.tokopedia.play.broadcaster.util.PlayBroadcasterResponseBuilder
import com.tokopedia.play.broadcaster.util.TestDoubleModelBuilder
import com.tokopedia.play.broadcaster.util.isEqualTo
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by jegul on 11/05/21
 */
class PlayTitleAndTagsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    val dispatcher = CoroutineTestDispatchers

    val responseBuilder = PlayBroadcasterResponseBuilder()
    val testModelBuilder = TestDoubleModelBuilder()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher.coroutineDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given added tags, when retrieved, it should return the correct added tags`() {
        val recommendedTags = setOf("abc", "def", "ghi")

        val recommendedTagsResponse = responseBuilder.buildRecommendedChannelTagsResponse(recommendedTags.toList())
        val recommendedTagsUseCase: GetRecommendedChannelTagsUseCase = mockk(relaxed = true)
        coEvery { recommendedTagsUseCase.executeOnBackground() } returns recommendedTagsResponse

        val addedTags = recommendedTags.take(2).toSet()
        val setupDataStore: PlayBroadcastSetupDataStore = mockk(relaxed = true)

        every { setupDataStore.getTags() } returns addedTags
        givenPlayTitleAndTagsViewModel(
                setupDataStore = setupDataStore,
                getRecommendedChannelTagsUseCase = recommendedTagsUseCase
        ).andWhen {
            viewModel.addedTags
        }.thenVerify {
            it.isEqualTo(addedTags)
        }.andThen {
            toggleTag(recommendedTags.last())
        }.andWhen {
            viewModel.addedTags
        }.thenVerify {
            it.isEqualTo((addedTags + recommendedTags.last()).toSet())
        }
    }
}