package com.tokopedia.recommendation_widget_common.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetViewToViewRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.viewtoview.bottomsheet.ViewToViewATCStatus
import com.tokopedia.recommendation_widget_common.widget.viewtoview.bottomsheet.ViewToViewDataModel
import com.tokopedia.recommendation_widget_common.widget.viewtoview.bottomsheet.ViewToViewRecommendationResult
import com.tokopedia.recommendation_widget_common.widget.viewtoview.bottomsheet.ViewToViewViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ViewToViewViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getViewToViewRecommendationUseCase =
        mockk<GetViewToViewRecommendationUseCase>(relaxed = true)
    private val addToCartUseCase = mockk<AddToCartUseCase>(relaxed = true)

    private lateinit var viewToViewViewModel: ViewToViewViewModel
    private val userSession = mockk<UserSessionInterface>(relaxed = true)

    private val recommendationItem = RecommendationItem()

    private val recommendationWidget = RecommendationWidget(
        recommendationItemList = listOf(recommendationItem, recommendationItem)
    )

    private val atcResponseSuccess = AddToCartDataModel(
        data = DataModel(
            success = 1,
            cartId = "12345",
            message = arrayListOf("Success")
        ), status = "OK"
    )

    private val atcResponseFail = AddToCartDataModel(
        data = DataModel(
            success = 0,
            message = arrayListOf("Fail")
        ), status = "Fail"
    )

    @Before
    fun setUp() {
        viewToViewViewModel = ViewToViewViewModel(
            getRecommendationUseCase = { getViewToViewRecommendationUseCase },
            addToCartUseCase = { addToCartUseCase },
            userSession = userSession,
            dispatchers = CoroutineTestDispatchersProvider,
        )
    }

    @Test
    fun `view to view recommendation should return success if recommendation item list is not empty`() {
        `Given getViewToViewRecommendationUseCase will return`(recommendationWidget)

        val queryParam = ""
        `When getViewToViewProductRecommendation`(queryParam, true)

        `Then verify view to view recommendation is Success`()
    }

    @Test
    fun `view to view recommendation should return fail if recommendation item list is empty`() {
        `Given getViewToViewRecommendationUseCase will return`(RecommendationWidget())

        val queryParam = ""
        `When getViewToViewProductRecommendation`(queryParam, true)

        `Then verify view to view recommendation is Fail`()
    }

    @Test
    fun `view to view recommendation should return fail if encounter exception`() {
        `Given getViewToViewRecommendationUseCase will throw`(Exception())

        val queryParam = ""
        `When getViewToViewProductRecommendation`(queryParam, true)

        `Then verify view to view recommendation is Fail`()
    }

    @Test
    fun `view to view retry recommendation should success`() {
        `Given getViewToViewRecommendationUseCase will return`(recommendationWidget)

        val queryParam = ""
        viewToViewViewModel.retryViewToViewProductRecommendation(queryParam, true)

        `Then verify view to view recommendation is Success`()
    }

    private fun `Given getViewToViewRecommendationUseCase will return`(widget: RecommendationWidget) {
        coEvery { getViewToViewRecommendationUseCase.getData(any()) } returns listOf(widget)
    }

    private fun `Given getViewToViewRecommendationUseCase will throw`(exception: Exception) {
        coEvery { getViewToViewRecommendationUseCase.getData(any()) } throws exception
    }

    private fun `When getViewToViewProductRecommendation`(
        queryParam: String,
        hasAtcButton: Boolean
    ) {
        viewToViewViewModel.getViewToViewProductRecommendation(queryParam, hasAtcButton)
    }

    private fun `Then verify view to view recommendation is Success`() {
        val result = viewToViewViewModel.viewToViewRecommendationLiveData.value
        assertTrue(result is Success)
    }

    private fun `Then verify view to view recommendation is Fail`() {
        val result = viewToViewViewModel.viewToViewRecommendationLiveData.value
        assertTrue(result is Fail)
    }

    @Test
    fun `add to cart should return success`() {
        `Given view to view loaded`(recommendationWidget)
        `Given user is logged in`()
        `Given addToCartUseCase will return`(atcResponseSuccess)

        val recommendationResult = (viewToViewViewModel.viewToViewRecommendationLiveData.value as Success).data  as ViewToViewRecommendationResult.Product
        val product = recommendationResult.products.first()
        `When addToCart`(product)

        `Then verify addToCart success`()
    }

    @Test
    fun `add to cart should return fail when atc not success`() {
        `Given view to view loaded`(recommendationWidget)
        `Given user is logged in`()
        `Given addToCartUseCase will return`(atcResponseFail)

        val recommendationResult = (viewToViewViewModel.viewToViewRecommendationLiveData.value as Success).data as ViewToViewRecommendationResult.Product
        val product = recommendationResult.products.first()
        `When addToCart`(product)

        `Then verify addToCart fail`()
    }

    @Test
    fun `add to cart should return fail when encounter exception`() {
        `Given view to view loaded`(recommendationWidget)
        `Given user is logged in`()
        `Given addToCartUseCase will throw`(Exception())

        val recommendationResult = (viewToViewViewModel.viewToViewRecommendationLiveData.value as Success).data  as ViewToViewRecommendationResult.Product
        val product = recommendationResult.products.first()
        `When addToCart`(product)

        `Then verify addToCart fail`()
    }

    @Test
    fun `add to cart should return NonLogin`() {
        `Given view to view loaded`(recommendationWidget)
        `Given user is not logged in`()

        val recommendationResult = (viewToViewViewModel.viewToViewRecommendationLiveData.value as Success).data  as ViewToViewRecommendationResult.Product
        val product = recommendationResult.products.first()
        `When addToCart`(product)

        `Then verify addToCart status is NonLogin`()
    }

    private fun `Given view to view loaded`(recommendationWidget: RecommendationWidget) {
        `Given getViewToViewRecommendationUseCase will return`(recommendationWidget)

        val queryParam = ""
        `When getViewToViewProductRecommendation`(queryParam, true)
    }

    private fun `Given addToCartUseCase will return`(addToCartDataModel: AddToCartDataModel) {
        coEvery { addToCartUseCase.executeOnBackground() } returns addToCartDataModel
    }

    private fun `Given addToCartUseCase will throw`(exception: Exception) {
        coEvery { addToCartUseCase.executeOnBackground() } throws exception
    }

    private fun `Given user is logged in`() {
        every { userSession.isLoggedIn } returns true
    }

    private fun `Given user is not logged in`() {
        every { userSession.isLoggedIn } returns false
    }

    private fun `When addToCart`(product: ViewToViewDataModel) {
        viewToViewViewModel.addToCart(product)
    }

    private fun `Then verify addToCart success`() {
        val result = viewToViewViewModel.viewToViewATCStatusLiveData.value
        assertTrue(result is ViewToViewATCStatus.Success)
    }

    private fun `Then verify addToCart fail`() {
        val result = viewToViewViewModel.viewToViewATCStatusLiveData.value
        assertTrue(result is ViewToViewATCStatus.Failure)
    }

    private fun `Then verify addToCart status is NonLogin`() {
        val result = viewToViewViewModel.viewToViewATCStatusLiveData.value
        assertTrue(result is ViewToViewATCStatus.NonLogin)
    }

    @Test
    fun `getUserId should return same user id`() {
        val userId = "1"
        `Given user is logged in`()
        every { userSession.userId } returns userId

        val actualUserId = viewToViewViewModel.getUserId()

        assertEquals(userId, actualUserId)
    }

}
