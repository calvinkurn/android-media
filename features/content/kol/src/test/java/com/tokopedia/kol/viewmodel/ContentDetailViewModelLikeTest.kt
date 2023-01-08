package com.tokopedia.kol.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.kol.common.util.ContentDetailResult
import com.tokopedia.kol.feature.postdetail.domain.ContentDetailRepository
import com.tokopedia.kol.feature.postdetail.view.datamodel.type.ContentLikeAction
import com.tokopedia.kol.feature.postdetail.view.viewmodel.ContentDetailViewModel
import com.tokopedia.kol.model.ContentDetailModelBuilder
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.junit.Rule
import org.junit.Test

/**
 * Created by meyta.taliti on 08/08/22.
 */
class ContentDetailViewModelLikeTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers

    private val mockRepo: ContentDetailRepository = mockk(relaxed = true)
    private val viewModel = ContentDetailViewModel(
        testDispatcher,
        mockRepo,
    )

    private val contentId = "1234"
    private val builder = ContentDetailModelBuilder()

    @Test
    fun `when user like content, given response success, then it should emit LikeContentModel`() {
        val rowNumber = 0
        val likeAction = ContentLikeAction.Like
        val expectedResult = builder.getLikeContentModel(likeAction, rowNumber)

        coEvery { mockRepo.likeContent(contentId, likeAction, rowNumber) } returns expectedResult

        viewModel.likeContent(contentId, likeAction, rowNumber)

        val result = viewModel.getLikeKolResp.value

        Assertions
            .assertThat(result)
            .isEqualTo(ContentDetailResult.Success(expectedResult))
    }

    @Test
    fun `when user unlike content, given response success, then it should emit LikeContentModel`() {
        val rowNumber = 0
        val likeAction = ContentLikeAction.UnLike
        val expectedResult = builder.getLikeContentModel(likeAction, rowNumber)

        coEvery { mockRepo.likeContent(contentId, likeAction, rowNumber) } returns expectedResult

        viewModel.likeContent(contentId, likeAction, rowNumber)

        val result = viewModel.getLikeKolResp.value

        Assertions
            .assertThat(result)
            .isEqualTo(ContentDetailResult.Success(expectedResult))
    }

    @Test
    fun `when user like content, given response error, then it should emit error`() {

        val rowNumber = 0
        val likeAction = ContentLikeAction.Like

        coEvery { mockRepo.likeContent(contentId, likeAction, rowNumber) } throws Throwable()

        viewModel.likeContent(contentId, likeAction, rowNumber)

        val result = viewModel.getLikeKolResp.value

        Assertions
            .assertThat(result)
            .isInstanceOf(ContentDetailResult.Failure::class.java)
    }

    @Test
    fun `when user unlike content, given response error, then it should emit error`() {

        val rowNumber = 0
        val likeAction = ContentLikeAction.UnLike

        coEvery { mockRepo.likeContent(contentId, likeAction, rowNumber) } throws Throwable()

        viewModel.likeContent(contentId, likeAction, rowNumber)

        val result = viewModel.getLikeKolResp.value

        Assertions
            .assertThat(result)
            .isInstanceOf(ContentDetailResult.Failure::class.java)
    }
}