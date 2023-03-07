package com.tokopedia.kol.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.kol.common.util.ContentDetailResult
import com.tokopedia.kol.feature.postdetail.domain.ContentDetailRepository
import com.tokopedia.kol.feature.postdetail.view.viewmodel.ContentDetailViewModel
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.junit.Rule
import org.junit.Test

/**
 * Created by meyta.taliti on 08/08/22.
 */
class ContentDetailViewModelAddToCartTest {

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

    @Test
    fun `when user add product to cart, given response success, then it should emit success`() {
        val expectedResult = true

        coEvery { mockRepo.addToCart("", "", "", "") } returns expectedResult

        viewModel.addToCart("", "", "", "")

        val result = viewModel.atcRespData.value

        Assertions
            .assertThat(result)
            .isEqualTo(ContentDetailResult.Success(expectedResult))
    }

    @Test
    fun `when user add product to cart, given response data error, then it should emit error`() {
        val expectedResult = false

        coEvery { mockRepo.addToCart("", "", "", "") } returns expectedResult

        viewModel.addToCart("", "", "", "")

        val result = viewModel.atcRespData.value

        Assertions
            .assertThat(result)
            .isEqualTo(ContentDetailResult.Success(expectedResult))
    }

    @Test
    fun `when user add product to cart, given response error, then it should emit error`() {

        coEvery { mockRepo.addToCart("", "", "", "") } throws Throwable()

        viewModel.addToCart("", "", "", "")

        val result = viewModel.atcRespData.value

        Assertions
            .assertThat(result)
            .isInstanceOf(ContentDetailResult.Failure::class.java)
    }

}