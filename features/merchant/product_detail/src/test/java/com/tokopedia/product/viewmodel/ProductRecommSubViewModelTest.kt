package com.tokopedia.product.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.config.GlobalConfig
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.product.detail.data.model.datamodel.ProductRecommendationDataModel
import com.tokopedia.product.detail.usecase.GetProductRecommendationUseCase
import com.tokopedia.product.detail.view.viewmodel.product_detail.IProductRecommSubViewModel
import com.tokopedia.product.detail.view.viewmodel.product_detail.sub_viewmodel.ProductRecommSubViewModel
import com.tokopedia.product.util.getOrAwaitValue
import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.presentation.model.AnnotationChip
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.unmockkAll
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeoutException

/**
 * Created by yovi.putra on 28/03/23"
 * Project name: android-tokopedia-core
 **/

@ExperimentalCoroutinesApi
class ProductRecommSubViewModelTest {

    @RelaxedMockK
    lateinit var getProductRecommendationUseCase: GetProductRecommendationUseCase

    @RelaxedMockK
    lateinit var getRecommendationUseCase: GetRecommendationUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: IProductRecommSubViewModel

    @Before
    fun beforeTest() {
        MockKAnnotations.init(this)
        mockkStatic(GlobalConfig::class)

        viewModel = ProductRecommSubViewModel(
            getRecommendationUseCase = { getRecommendationUseCase },
            getProductRecommendationUseCase = { getProductRecommendationUseCase }
        ).apply {
            registerScope(viewModelScope = CoroutineScope(CoroutineTestDispatchersProvider.main))
        }
    }

    @After
    fun afterTest() {
        unmockkAll()
    }

    // region view to view recommendation
    @Test
    fun `success load view to view recommendation`() {
        val recomWidget = RecommendationWidget(recommendationItemList = listOf(RecommendationItem()))
        val response = listOf(recomWidget)

        viewModel.onResetAlreadyRecomHit()
        every { GlobalConfig.isSellerApp() } returns false
        coEvery { getRecommendationUseCase.getData(any()) } returns response

        viewModel.loadViewToView("view_to_view", "", false)

        coVerify { getRecommendationUseCase.getData(any()) }
        assertTrue(viewModel.loadViewToView.value is Success)
    }

    @Test
    fun `load view to view sellerapp`() {
        val recomWidget = RecommendationWidget(recommendationItemList = listOf(RecommendationItem()))
        val response = listOf(recomWidget)

        viewModel.onResetAlreadyRecomHit()
        every { GlobalConfig.isSellerApp() } returns true
        coEvery { getRecommendationUseCase.getData(any()) } returns response

        viewModel.loadViewToView("view_to_view", "", false)

        runCatching {
            viewModel.loadViewToView.getOrAwaitValue()
        }.onFailure {
            assertTrue(it is TimeoutException)
        }
    }

    @Test
    fun `already hit recomm view to view when pdp on reload page`() {
        val recomWidget = RecommendationWidget(recommendationItemList = listOf(RecommendationItem()))
        val response = listOf(recomWidget)

        viewModel.onResetAlreadyRecomHit()
        every { GlobalConfig.isSellerApp() } returns false
        coEvery { getRecommendationUseCase.getData(any()) } returns response

        viewModel.loadViewToView("view_to_view", "", false)

        viewModel.loadViewToView("view_to_view", "", false)

        coVerify(exactly = 1) { getRecommendationUseCase.getData(any()) }
    }
    // endregion

    // region recommendationChipClicked
    @Test
    fun `success load recommendation chip clicked`() {
        val mockResponse = RecommendationWidget(
            tid = "1",
            recommendationItemList = listOf(RecommendationItem())
        )
        every { GlobalConfig.isSellerApp() } returns false
        coEvery {
            getRecommendationUseCase.getData(any())
        } returns arrayListOf(mockResponse)

        val mockSelectedChip = AnnotationChip(
            RecommendationFilterChipsEntity.RecommendationFilterChip(
                name = "katun chip",
                isActivated = true,
                value = "queryparambro"
            )
        )

        val initialAnnotationChip = listOf(
            AnnotationChip(
                RecommendationFilterChipsEntity.RecommendationFilterChip(
                    name = "katun chip",
                    isActivated = false
                )
            ),
            AnnotationChip(
                RecommendationFilterChipsEntity.RecommendationFilterChip(
                    name = "kulit chip",
                    isActivated = false
                )
            )
        )

        val recomDataModel = ProductRecommendationDataModel(
            filterData = initialAnnotationChip,
            recomWidgetData = RecommendationWidget(
                pageName = "pdp_11"
            )
        )

        viewModel.recommendationChipClicked(recomDataModel, mockSelectedChip, "123")

        val slotRequestParams = slot<GetRecommendationRequestParam>()
        coVerify {
            getRecommendationUseCase.getData(capture(slotRequestParams))
        }

        // assert request params
        val reqParams = slotRequestParams.captured
        assertEquals(reqParams.pageNumber, 1)
        assertEquals(reqParams.pageName, "pdp_11")
        assertEquals(reqParams.queryParam, "queryparambro")
        assertEquals(reqParams.productIds, listOf("123"))

        val filterData = viewModel.filterTopAdsProduct.value
        assertNotNull(filterData)

        assertEquals(filterData!!.isRecomenDataEmpty, false)
        assertEquals(filterData.filterData!!.isNotEmpty(), true)

        val selectedChip = filterData.filterData!!.first {
            it.recommendationFilterChip.isActivated
        }.recommendationFilterChip.name

        val otherChipUnselected = filterData.filterData!!.any {
            it.recommendationFilterChip.name == "kulit chip" && !it.recommendationFilterChip.isActivated
        }

        assertEquals(selectedChip, "katun chip")
        assertEquals(otherChipUnselected, true)
        assertNull(viewModel.statusFilterTopAdsProduct.value)
    }

    @Test
    fun `success load recommendation chip clicked return empty list`() {
        every { GlobalConfig.isSellerApp() } returns false
        coEvery {
            getRecommendationUseCase.getData(any())
        } returns emptyList()

        viewModel.recommendationChipClicked(ProductRecommendationDataModel(), AnnotationChip(), "")

        coVerify {
            getRecommendationUseCase.getData(any())
        }

        assertNull(viewModel.filterTopAdsProduct.value?.recomWidgetData)
        assertNull(viewModel.statusFilterTopAdsProduct.value)
    }

    @Test
    fun `seller app load recommendation chip clicked nothing action`() {
        every { GlobalConfig.isSellerApp() } returns true
        coEvery {
            getRecommendationUseCase.getData(any())
        } returns emptyList()

        viewModel.recommendationChipClicked(ProductRecommendationDataModel(), AnnotationChip(), "")

        runCatching {
            viewModel.filterTopAdsProduct.getOrAwaitValue()
        }.onFailure {
            assertTrue(it is TimeoutException)
        }

        runCatching {
            viewModel.statusFilterTopAdsProduct.getOrAwaitValue()
        }.onFailure {
            assertTrue(it is TimeoutException)
        }
    }

    @Test
    fun `error load recommendation chip clicked`() {
        every { GlobalConfig.isSellerApp() } returns false
        coEvery {
            getRecommendationUseCase.getData(any())
        } throws Throwable()

        viewModel.recommendationChipClicked(ProductRecommendationDataModel(), AnnotationChip(), "")

        coVerify {
            getRecommendationUseCase.getData(any())
        }

        assertNull(viewModel.filterTopAdsProduct.value?.recomWidgetData)
        assertTrue(viewModel.statusFilterTopAdsProduct.value is Fail)
    }
    // endregion

    // region load recommendation
    @Test
    fun `load recommendation already hitted`() {
        val recomWidget =
            RecommendationWidget(tid = "1", recommendationItemList = listOf(RecommendationItem()))
        viewModel.onResetAlreadyRecomHit()

        coEvery {
            getProductRecommendationUseCase.executeOnBackground(any())
        } returns recomWidget

        viewModel.loadRecommendation("pdp_1", "", false, mutableMapOf())
        Thread.sleep(500)
        // hit again with same page name
        viewModel.loadRecommendation("pdp_1", "", false, mutableMapOf())

        // make sure it will only called once
        coVerify(exactly = 1) {
            getProductRecommendationUseCase.executeOnBackground(any())
        }
    }

    @Test
    fun `load recommendation sellerapp`() {
        every {
            GlobalConfig.isSellerApp()
        } returns true

        viewModel.loadRecommendation("pdp_1", "", false, mutableMapOf())

        coVerify(inverse = true) {
            getProductRecommendationUseCase.executeOnBackground(any())
        }
    }

    @Test
    fun `fail load view to view recommendation when recommendation widget is empty`() {
        val response = listOf<RecommendationWidget>()

        coEvery {
            getRecommendationUseCase.getData(any())
        } returns response

        viewModel.loadViewToView("view_to_view", "", false)

        coVerify { getRecommendationUseCase.getData(any()) }
        assertTrue(viewModel.loadViewToView.value is Fail)
    }

    @Test
    fun `fail load view to view recommendation on exception`() {
        coEvery {
            getRecommendationUseCase.getData(any())
        } throws Exception()

        viewModel.loadViewToView("view_to_view", "", false)

        coVerify { getRecommendationUseCase.getData(any()) }
        assertTrue(viewModel.loadViewToView.value is Fail)
    }

    @Test
    fun `load view to view recommendation already hitted`() {
        val recomWidget = listOf(RecommendationWidget(tid = "1", recommendationItemList = listOf(RecommendationItem())))

        coEvery {
            getRecommendationUseCase.getData(any())
        } returns recomWidget

        viewModel.loadViewToView("view_to_view", "", false)
        Thread.sleep(500)
        // hit again with same page name
        viewModel.loadViewToView("view_to_view", "", false)

        // make sure it will only called once
        coVerify(exactly = 1) {
            getRecommendationUseCase.getData(any())
        }
    }

    @Test
    fun `assert request params tokonow`() {
        coEvery {
            getProductRecommendationUseCase.executeOnBackground(any())
        } returns RecommendationWidget()

        viewModel.loadRecommendation(
            "pdp_10",
            "123",
            true,
            mutableMapOf("123" to MiniCartItem.MiniCartItemProduct())
        )

        val requestParamsSlot = slot<RequestParams>()
        coVerify {
            getProductRecommendationUseCase.executeOnBackground(capture(requestParamsSlot))
        }

        val requestParams = requestParamsSlot.captured
        assertEquals(requestParams.getString("productID", ""), "123")
        assertEquals(requestParams.getString("pageName", ""), "pdp_10")
        assertEquals(requestParams.getBoolean("tokonow", false), true)
        val miniCart = requestParams.getObject("minicart") as? MutableMap<*, *>
        assertEquals(miniCart!!.isNotEmpty(), true)
    }

    @Test
    fun `assert request params non tokonow`() {
        coEvery {
            getProductRecommendationUseCase.executeOnBackground(any())
        } returns RecommendationWidget()

        viewModel.loadRecommendation(
            "pdp_10",
            "123",
            false,
            null
        )

        val requestParamsSlot = slot<RequestParams>()
        coVerify {
            getProductRecommendationUseCase.executeOnBackground(capture(requestParamsSlot))
        }

        val requestParams = requestParamsSlot.captured
        assertEquals(requestParams.getString("productID", ""), "123")
        assertEquals(requestParams.getString("pageName", ""), "pdp_10")
        assertEquals(requestParams.getBoolean("tokonow", false), false)
        val miniCart = requestParams.getObject("minicart") as? MutableMap<*, *>
        assertNull(miniCart)
    }

    @Test
    fun `error load recommendation`() {
        val pageName = "pdp3"
        coEvery {
            getProductRecommendationUseCase.executeOnBackground(any())
        } throws Throwable()

        viewModel.loadRecommendation(pageName, "", false, mutableMapOf())

        coVerify {
            getProductRecommendationUseCase.executeOnBackground(any())
        }
        assertTrue(viewModel.loadTopAdsProduct.value is Fail)
        assertTrue((viewModel.loadTopAdsProduct.value as Fail).throwable.message == pageName)
    }
    // endregion

    // region load recommendation by filter test case
    @Test
    fun `success load recommendation with filter`() {
        val mockFilter = listOf(RecommendationFilterChipsEntity.RecommendationFilterChip())
        val mockRecomm = RecommendationWidget(
            tid = "1",
            recommendationItemList = listOf(RecommendationItem()),
            recommendationFilterChips = mockFilter
        )
        val pageName = "pdp3"

        coEvery {
            getProductRecommendationUseCase.executeOnBackground(any())
        } returns mockRecomm

        viewModel.loadRecommendation(pageName, "", false, mutableMapOf())

        coVerify {
            getProductRecommendationUseCase.executeOnBackground(any())
        }

        assertEquals((viewModel.loadTopAdsProduct.value as Success).data.tid, "1")
        assertEquals(
            (viewModel.loadTopAdsProduct.value as Success)
                .data
                .recommendationFilterChips
                .isNotEmpty(),
            true
        )
    }

    @Test
    fun `success load recommendation without filter`() {
        val mockRecomm = RecommendationWidget(
            tid = "1",
            recommendationItemList = listOf(RecommendationItem())
        )
        val pageName = "pdp3"

        coEvery {
            getProductRecommendationUseCase.executeOnBackground(any())
        } returns mockRecomm

        viewModel.loadRecommendation(
            pageName,
            "123",
            false,
            mutableMapOf()
        )

        coVerify {
            getProductRecommendationUseCase.executeOnBackground(any())
        }

        assertTrue((viewModel.loadTopAdsProduct.value as Success).data.tid == "1")
    }
    // endregion

    // region vertical recomm
    @Test
    fun `verify vertical recommendation when return empty list, will be fail`() {
        val pageName = "pdp_8_vertical"
        val productId = "1234"

        coEvery {
            getRecommendationUseCase.getData(any())
        } returns emptyList()

        viewModel.getVerticalRecommendationData(
            pageName = pageName,
            productId = productId
        )

        assertTrue(viewModel.verticalRecommendation.value is Fail)
    }

    @Test
    fun `verify vertical recommendation throw error, will be fail`() {
        val pageNumber = 1
        val pageName = "pdp_8_vertical"
        val productId = "1234"

        coEvery {
            getRecommendationUseCase.getData(any())
        } throws Throwable()

        viewModel.getVerticalRecommendationData(pageName, pageNumber, productId)

        assertTrue(viewModel.verticalRecommendation.value is Fail)
    }

    @Test
    fun `verify success get vertical recommendation data`() {
        val mockResponse = RecommendationWidget(
            tid = "1",
            recommendationItemList = listOf(RecommendationItem())
        )

        val pageNumber = 1
        val pageName = "pdp_8_vertical"
        val productId = "1234"

        coEvery {
            getRecommendationUseCase.getData(any())
        } returns arrayListOf(mockResponse)

        viewModel.getVerticalRecommendationData(pageName, pageNumber, productId)

        val slotRequestParams = slot<GetRecommendationRequestParam>()
        coVerify {
            getRecommendationUseCase.getData(capture(slotRequestParams))
        }

        val captured = slotRequestParams.captured
        assertEquals(pageName, captured.pageName)
        assertEquals(pageNumber, captured.pageNumber)
        assertEquals(listOf(productId), captured.productIds)

        assertTrue(viewModel.verticalRecommendation.value is Success)
    }

    @Test
    fun `verify success get vertical recommendation data with null productId and pageNumber`() {
        val mockResponse = RecommendationWidget(
            tid = "1",
            recommendationItemList = listOf(RecommendationItem())
        )

        val pageName = "pdp_8_vertical"

        coEvery {
            getRecommendationUseCase.getData(any())
        } returns arrayListOf(mockResponse)

        viewModel.getVerticalRecommendationData(
            pageName = pageName,
            productId = null,
            page = null
        )

        val slotRequestParams = slot<GetRecommendationRequestParam>()
        coVerify {
            getRecommendationUseCase.getData(capture(slotRequestParams))
        }

        val captured = slotRequestParams.captured
        assertEquals(pageName, captured.pageName)
        assertEquals(1, captured.pageNumber)
        assertEquals(listOf(""), captured.productIds)

        assertTrue(viewModel.verticalRecommendation.value is Success)
    }
    // endregion
}
