package com.tokopedia.kol.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.kol.common.util.ContentDetailResult
import com.tokopedia.kol.feature.postdetail.domain.ContentDetailRepository
import com.tokopedia.kol.feature.postdetail.view.viewmodel.ContentDetailViewModel
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
 * Created by meyta.taliti on 08/08/22.
 */
class ContentDetailViewModelAddToWishlistTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers

    private val productId = "1234"
    private val builder = ContentDetailModelBuilder()


    private val mockRepo: ContentDetailRepository = mockk(relaxed = true)
    private val viewModel = ContentDetailViewModel(
        testDispatcher,
        mockRepo,
    )

    @Test
    fun `when user add product to wishlist, given response success, then it should emit AddToWishlistV2Response`() {
        val rowNumber = 0
        val expectedResult = builder.getWishlistModel(rowNumber, productId)

        coEvery { mockRepo.addToWishlist(rowNumber, productId) } returns expectedResult

        viewModel.addToWishlist(productId, rowNumber)

        val result = viewModel.observableWishlist.value

        Assertions
            .assertThat(result)
            .isEqualTo(ContentDetailResult.Success(expectedResult))
    }

    @Test
    fun `when user add product to wishlist, given response error, then it should emit error`() {
        val rowNumber = 0
        coEvery { mockRepo.addToWishlist(rowNumber, productId) } throws  Throwable()

        viewModel.addToWishlist(productId, rowNumber)

        val result = viewModel.observableWishlist.value

        Assertions
            .assertThat(result)
            .isInstanceOf(ContentDetailResult.Failure::class.java)
    }

}