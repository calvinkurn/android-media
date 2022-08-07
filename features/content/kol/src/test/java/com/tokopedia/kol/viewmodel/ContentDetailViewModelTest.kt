package com.tokopedia.kol.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.kol.feature.postdetail.domain.ContentDetailRepository
import com.tokopedia.kol.feature.postdetail.domain.mapper.ContentDetailMapper
import com.tokopedia.kol.feature.postdetail.view.viewmodel.ContentDetailRevampViewModel
import com.tokopedia.kol.model.ContentDetailModelBuilder
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.junit.Rule
import org.junit.Test

/**
 * Created by meyta.taliti on 07/08/22.
 */
class ContentDetailViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers

    private val mockRepo: ContentDetailRepository = mockk(relaxed = true)
    private val mockMapper: ContentDetailMapper = mockk(relaxed = true)
    private val viewModel = ContentDetailRevampViewModel(
        testDispatcher,
        mockRepo,
        mockMapper,
    )

    private val contentId = "1234"
    private val builder = ContentDetailModelBuilder()

    @Test
    fun `given there is content detail, then content detail can be retrieved`() {
        val expectedResult = builder.getContentDetail()

        coEvery { mockRepo.getContentDetail(contentId) } returns expectedResult

        viewModel.getContentDetail(contentId)

        val result = viewModel.getCDPPostFirstPostData.value

        Assertions
            .assertThat(result)
            .isEqualTo(Success(expectedResult))
    }

    @Test
    fun `given error when get content detail, then return error`() {

        coEvery { mockRepo.getContentDetail(contentId) } throws Throwable()

        viewModel.getContentDetail(contentId)

        val result = viewModel.getCDPPostFirstPostData.value

        Assertions
            .assertThat(result)
            .isInstanceOf(Fail::class.java)
    }

    @Test
    fun `given content recommendation, then content recommendation can be retrieved`() {
        val expectedCursorResult = "123"
        val expectedResult = builder.getContentRecommendation().copy(
            nextCursor = expectedCursorResult
        )
        val response = builder.getContentRecommendationData(
            expectedResult
        )
        coEvery { mockRepo.getContentRecommendation(contentId, "") } returns response

        viewModel.getContentDetailRecommendation(contentId)

        val result = viewModel.cDPPostRecomData.value
        val cursorResult = viewModel.currentCursor

        Assertions
            .assertThat(result)
            .isEqualTo(Success(expectedResult))

        Assertions
            .assertThat(cursorResult)
            .isEqualTo(expectedCursorResult)
    }

    @Test
    fun `given error when get content recommendation, then return error`() {
        coEvery { mockRepo.getContentRecommendation(contentId, "") } throws Throwable()

        viewModel.getContentDetailRecommendation(contentId)

        val result = viewModel.cDPPostRecomData.value

        Assertions
            .assertThat(result)
            .isInstanceOf(Fail::class.java)
    }
}