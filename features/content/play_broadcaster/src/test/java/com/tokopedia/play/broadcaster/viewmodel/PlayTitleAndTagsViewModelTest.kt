package com.tokopedia.play.broadcaster.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastSetupDataStore
import com.tokopedia.play.broadcaster.data.datastore.TagsDataStore
import com.tokopedia.play.broadcaster.domain.usecase.GetRecommendedChannelTagsUseCase
import com.tokopedia.play.broadcaster.robot.andThen
import com.tokopedia.play.broadcaster.robot.andWhen
import com.tokopedia.play.broadcaster.robot.givenPlayTitleAndTagsViewModel
import com.tokopedia.play.broadcaster.robot.thenVerify
import com.tokopedia.play.broadcaster.testdouble.MockTitleDataStore
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

    private val responseBuilder = PlayBroadcasterResponseBuilder()
    private val testModelBuilder = TestDoubleModelBuilder()

    private val recommendedTags = setOf("abc", "def", "ghi")

    private val recommendedTagsResponse = responseBuilder.buildRecommendedChannelTagsResponse(recommendedTags.toList())
    private val recommendedTagsUseCase: GetRecommendedChannelTagsUseCase = mockk(relaxed = true)


    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher.coroutineDispatcher)

        coEvery { recommendedTagsUseCase.executeOnBackground() } returns recommendedTagsResponse
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given added tags, when retrieved, then it should return the correct added tags`() {
        val addedTags = recommendedTags.take(2).toSet()
        val setupDataStore: PlayBroadcastSetupDataStore = mockk(relaxed = true)

        every { setupDataStore.getTags() } returns addedTags
        givenPlayTitleAndTagsViewModel(
                setupDataStore = setupDataStore,
                getRecommendedChannelTagsUseCase = recommendedTagsUseCase
        ).andWhen {
            getAddedTags()
        }.thenVerify {
            it.isEqualTo(addedTags)
        }.andThen {
            toggleTag(recommendedTags.last())
        }.andWhen {
            getAddedTags()
        }.thenVerify {
            it.isEqualTo((addedTags + recommendedTags.last()).toSet())
        }
    }

    @Test
    fun `given upload tags is always failed, when finish setup, then title should never be uploaded`() {
        val mockTitleDataStore = MockTitleDataStore(dispatcher)
        val mockTagsDataStore: TagsDataStore = mockk(relaxed = true)

        givenPlayTitleAndTagsViewModel(
                setupDataStore = testModelBuilder.buildSetupDataStore(
                        titleDataStore = mockTitleDataStore,
                        tagsDataStore = mockTagsDataStore
                ),
                getRecommendedChannelTagsUseCase = recommendedTagsUseCase
        ) {
            coEvery { mockTagsDataStore.uploadTags(any()) } returns false
        }.thenVerify {
            mockTitleDataStore.isUploaded.isEqualTo(false)
        }.andThen {
            finishSetup("title")
        }.thenVerify {
            mockTitleDataStore.isUploaded.isEqualTo(false)
        }
    }

    @Test
    fun `given upload tags is always successful, when finish setup, then title should be uploaded`() {
        val mockTitleDataStore = MockTitleDataStore(dispatcher)
        val mockTagsDataStore: TagsDataStore = mockk(relaxed = true)

        givenPlayTitleAndTagsViewModel(
                setupDataStore = testModelBuilder.buildSetupDataStore(
                        titleDataStore = mockTitleDataStore,
                        tagsDataStore = mockTagsDataStore
                ),
                getRecommendedChannelTagsUseCase = recommendedTagsUseCase
        ) {
            coEvery { mockTagsDataStore.uploadTags(any()) } returns true
        }.thenVerify {
            mockTitleDataStore.isUploaded.isEqualTo(false)
        }.andThen {
            finishSetup("title")
        }.thenVerify {
            mockTitleDataStore.isUploaded.isEqualTo(true)
        }
    }

    @Test
    fun `when validating tag, it should only be valid when not blank and more between 2 and 32 chars`() {
        givenPlayTitleAndTagsViewModel(
                getRecommendedChannelTagsUseCase = recommendedTagsUseCase
        ).andWhen {
            isTagValid("")
        }.thenVerify {
            it.isEqualTo(false)
        }.andWhen {
            isTagValid("a")
        }.thenVerify {
            it.isEqualTo(false)
        }.andWhen {
            isTagValid("abc")
        }.thenVerify {
            it.isEqualTo(true)
        }.andWhen {
            isTagValid(List(33){ "a"}.joinToString(""))
        }.thenVerify {
            it.isEqualTo(false)
        }
    }
}