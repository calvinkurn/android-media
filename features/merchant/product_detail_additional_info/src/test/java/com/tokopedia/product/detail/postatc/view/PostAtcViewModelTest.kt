package com.tokopedia.product.detail.postatc.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.product.detail.postatc.model.PostAtcComponentData
import com.tokopedia.product.detail.postatc.model.PostAtcLayout
import com.tokopedia.product.detail.postatc.usecase.GetPostAtcLayoutUseCase
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PostAtcViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var getPostAtcLayoutUseCase: GetPostAtcLayoutUseCase

    @RelaxedMockK
    lateinit var getRecommendationUseCase: GetRecommendationUseCase

    private val viewModel by lazy { createViewModel() }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    private fun createViewModel(): PostAtcViewModel {
        return PostAtcViewModel(
            getPostAtcLayoutUseCase,
            getRecommendationUseCase,
            CoroutineTestDispatchersProvider
        )
    }

    @Test
    fun `on success fetch layout will return component ui models`() {
        val productId = "123"
        val cartId = ""
        val layoutId = ""
        val pageSource = ""
        val response = PostAtcLayout(
            components = listOf(
                PostAtcLayout.Component(
                    name = "post_atc_product_info",
                    type = "post_atc",
                    data = listOf(
                        PostAtcComponentData()
                    )
                )
            )
        )

        coEvery {
            getPostAtcLayoutUseCase.execute(productId, cartId, layoutId, pageSource)
        } returns response

        viewModel.fetchLayout(productId, cartId, layoutId, pageSource)

        Assert.assertTrue(viewModel.layouts.value is Success)
        val data = (viewModel.layouts.value as Success).data
        Assert.assertEquals(1, data.size)
        Assert.assertEquals(data.first().name, "post_atc_product_info")
    }

    @Test
    fun `on success fetch layout also update post atc info data`() {
        val productId = "111"
        val cartId = "222"
        val layoutId = "333"
        val pageSource = "pdp"
        val layoutName = "post atc layout"
        val shopId = "444"
        val categoryId = "555"
        val categoryName = "elektronik"
        val response = PostAtcLayout(
            name = layoutName,
            basicInfo = PostAtcLayout.BasicInfo(
                shopId = shopId,
                category = PostAtcLayout.Category(
                    id = categoryId,
                    name = categoryName
                )
            )
        )

        coEvery {
            getPostAtcLayoutUseCase.execute(productId, cartId, layoutId, pageSource)
        } returns response

        viewModel.fetchLayout(productId, cartId, layoutId, pageSource)

        val atcInfo = viewModel.postAtcInfo
        Assert.assertEquals(productId, atcInfo.productId)
        Assert.assertEquals(cartId, atcInfo.cartId)
        Assert.assertEquals(layoutId, atcInfo.layoutId)
        Assert.assertEquals(pageSource, atcInfo.pageSource)
        Assert.assertEquals(layoutName, atcInfo.layoutName)
        Assert.assertEquals(categoryId, atcInfo.categoryId)
        Assert.assertEquals(categoryName, atcInfo.categoryName)
        Assert.assertEquals(shopId, atcInfo.shopId)
    }

    @Test
    fun `on fetch layout fail`() {
        val productId = "123"
        val cartId = ""
        val layoutId = ""
        val pageSource = ""

        val errorMessage = "something wrong"

        coEvery {
            getPostAtcLayoutUseCase.execute(productId, cartId, layoutId, pageSource)
        } throws Throwable(errorMessage)

        viewModel.fetchLayout(productId, cartId, layoutId, pageSource)

        Assert.assertTrue(viewModel.layouts.value is Fail)
    }

    @Test
    fun `on success fetch recommendation`() {
        val productId = "111"
        val pageName = "pdp_atc_1"
        val uniqueId = 1234

        val recomWidget = RecommendationWidget()

        val requestParam = GetRecommendationRequestParam(
            pageNumber = 1,
            pageName = pageName,
            productIds = listOf(productId)
        )

        val response = listOf(recomWidget)

        coEvery {
            getRecommendationUseCase.getData(requestParam)
        } returns response

        viewModel.fetchRecommendation(productId, pageName, uniqueId)

        val result = viewModel.recommendations.value
        Assert.assertEquals(uniqueId, result?.first)
        Assert.assertTrue(result?.second is Success)

        val data = (result?.second as Success).data
        Assert.assertEquals(recomWidget, data)
    }

    @Test
    fun `on fetch recommendation fail`() {
        val productId = "111"
        val pageName = "pdp_atc_1"
        val uniqueId = 1234

        val requestParam = GetRecommendationRequestParam(
            pageNumber = 1,
            pageName = pageName,
            productIds = listOf(productId)
        )

        val errorMessage = "something wrong"

        coEvery {
            getRecommendationUseCase.getData(requestParam)
        } throws Throwable(errorMessage)

        viewModel.fetchRecommendation(productId, pageName, uniqueId)

        val result = viewModel.recommendations.value
        Assert.assertEquals(uniqueId, result?.first)
        Assert.assertTrue(result?.second is Fail)
    }

}
