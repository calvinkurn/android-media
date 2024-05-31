package com.tokopedia.product.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.config.GlobalConfig
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.product.detail.data.model.datamodel.ProductRecommendationDataModel
import com.tokopedia.product.detail.usecase.GetProductRecommendationUseCase
import com.tokopedia.product.detail.view.viewmodel.product_detail.IProductRecommSubViewModel
import com.tokopedia.product.detail.view.viewmodel.product_detail.event.ProductRecommendationEvent
import com.tokopedia.product.detail.view.viewmodel.product_detail.event.ViewState
import com.tokopedia.product.detail.view.viewmodel.product_detail.sub_viewmodel.ProductRecommSubViewModel
import com.tokopedia.product.util.getOrAwaitValue
import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.presentation.model.AnnotationChip
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
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

    @RelaxedMockK
    lateinit var remoteConfig: RemoteConfig

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = MainCoroutineRule()

    private lateinit var viewModel: IProductRecommSubViewModel

    companion object {
        private const val LOAD_RECOM_DEBOUNCE = 150L
    }

    private fun TestScope.delayUntilDebounce() {
        advanceTimeBy(LOAD_RECOM_DEBOUNCE + 10L)
    }

    @Before
    fun beforeTest() {
        MockKAnnotations.init(this)
        mockkStatic(GlobalConfig::class)

        viewModel = ProductRecommSubViewModel(
            dispatcher = CoroutineTestDispatchersProvider,
            getRecommendationUseCase = { getRecommendationUseCase },
            remoteConfig = remoteConfig,
            getProductRecommendationUseCase = { getProductRecommendationUseCase }
        ).apply {
            registerScope(viewModelScope = CoroutineScope(UnconfinedTestDispatcher()))
        }

        every {
            remoteConfig.getBoolean(
                RemoteConfigKey.ANDROID_ENABLE_PDP_RECOMMENDATION_FLOW,
                true
            )
        } returns true

        every { GlobalConfig.isSellerApp() } returns false

        every {
            remoteConfig.getLong(
                RemoteConfigKey.ANDROID_PDP_DEBOUNCE_TIME,
                LOAD_RECOM_DEBOUNCE
            )
        } returns LOAD_RECOM_DEBOUNCE
    }

    @After
    fun afterTest() {
        unmockkAll()
    }

    // region view to view recommendation
    @Test
    fun `success recommendation FLOW`() {
        runTest {
            val recomWidget =
                RecommendationWidget(recommendationItemList = listOf(RecommendationItem()))
            val response = listOf(recomWidget)

            val productListData = { viewModel.productListData.value }
            backgroundScope.launch(UnconfinedTestDispatcher()) {
                viewModel.productListData.collect()
            }

            delayUntilDebounce()

            coEvery { getProductRecommendationUseCase.executeOnBackground(any()) } returns response.first()

            viewModel.onRecommendationEvent(
                ProductRecommendationEvent.LoadRecommendation(
                    "view_to_view",
                    "",
                    false,
                    null,
                    "",
                    ""
                )
            )

            delayUntilDebounce()

            coVerify { getProductRecommendationUseCase.executeOnBackground(any()) }
            assertTrue(productListData().size == 1)
            assertTrue(productListData().first().data is ViewState.RenderSuccess)
        }
    }

    @Test
    fun `load recommendation FLOW sellerapp`() = runTest {
        val recomWidget =
            RecommendationWidget(recommendationItemList = listOf(RecommendationItem()))
        val response = listOf(recomWidget)

        every {
            remoteConfig.getBoolean(
                RemoteConfigKey.ANDROID_ENABLE_PDP_RECOMMENDATION_FLOW,
                true
            )
        } returns false

        coEvery { getProductRecommendationUseCase.executeOnBackground(any()) } returns response.first()

        viewModel.onRecommendationEvent(
            ProductRecommendationEvent.LoadRecommendation(
                "view_to_view",
                "",
                false,
                null,
                "",
                ""
            )
        )

        coVerify { getProductRecommendationUseCase.executeOnBackground(any()) }
        assertTrue(viewModel.loadTopAdsProduct.value is Success)
    }

    @Test
    fun `load old recommendation when remote config flow false`() = runTest {
        val recomWidget =
            RecommendationWidget(recommendationItemList = listOf(RecommendationItem()))
        val response = listOf(recomWidget)

        every { GlobalConfig.isSellerApp() } returns true

        coEvery { getProductRecommendationUseCase.executeOnBackground(any()) } returns response.first()

        viewModel.onRecommendationEvent(
            ProductRecommendationEvent.LoadRecommendation(
                "view_to_view",
                "",
                false,
                null,
                "",
                ""
            )
        )

        coVerify(inverse = true) { getProductRecommendationUseCase.executeOnBackground(any()) }
    }

    @Test
    fun `load multiple recommendation FLOW within 150 ms`() = runTest() {
        val recomWidget =
            RecommendationWidget(recommendationItemList = listOf(RecommendationItem()))
        val response = listOf(recomWidget)

        coEvery { getProductRecommendationUseCase.executeOnBackground(any()) } returns response.first()

        val productListData = { viewModel.productListData.value }
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            viewModel.productListData.collect()
        }

        delayUntilDebounce()

        (1..3).forEach {
            viewModel.onRecommendationEvent(
                ProductRecommendationEvent.LoadRecommendation(
                    it.toString(),
                    "",
                    false,
                    null,
                    "",
                    ""
                )
            )
        }

        delayUntilDebounce()

        coVerify(exactly = 3) { getProductRecommendationUseCase.executeOnBackground(any()) }
        assertTrue(productListData().size == 3)
        assertTrue(
            productListData().all {
                it.alreadyCollected
            }
        )

        // After we load 3 recom under 150 ms, then after 150ms passed
        // Load another recom and ensure the data emitted only one
        viewModel.onRecommendationEvent(
            ProductRecommendationEvent.LoadRecommendation(
                "after",
                "",
                false,
                null,
                "",
                ""
            )
        )

        delayUntilDebounce()

        coVerify(exactly = 4) { getProductRecommendationUseCase.executeOnBackground(any()) }
        assertTrue(productListData().size == 1)
        assertTrue(
            productListData().all {
                it.alreadyCollected
            }
        )
    }

    @Test
    fun `fail load recommendation FLOW on exception`() = runTest {
        val productListData = { viewModel.productListData.value }
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            viewModel.productListData.collect()
        }

        coEvery { getProductRecommendationUseCase.executeOnBackground(any()) } throws Exception()

        viewModel.onRecommendationEvent(
            ProductRecommendationEvent.LoadRecommendation(
                "view_to_view",
                "",
                false,
                null,
                "",
                ""
            )
        )

        delayUntilDebounce()

        coVerify { getProductRecommendationUseCase.executeOnBackground(any()) }
        assertTrue(productListData().first().data is ViewState.RenderFailure)
    }
    //endregion

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

        viewModel.loadRecommendation("pdp_1", "", false, mutableMapOf(), "", "")
        Thread.sleep(500)
        // hit again with same page name
        viewModel.loadRecommendation("pdp_1", "", false, mutableMapOf(), "", "")

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

        viewModel.loadRecommendation("pdp_1", "", false, mutableMapOf(), "", "")

        coVerify(inverse = true) {
            getProductRecommendationUseCase.executeOnBackground(any())
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
            mutableMapOf("123" to MiniCartItem.MiniCartItemProduct()),
            "",
            ""
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
            null,
            "",
            ""
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

        viewModel.loadRecommendation(pageName, "", false, mutableMapOf(), "", "")

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

        viewModel.loadRecommendation(pageName, "", false, mutableMapOf(), "", "")

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
            mutableMapOf(),
            queryParam = "",
            thematicId = ""
        )

        coVerify {
            getProductRecommendationUseCase.executeOnBackground(any())
        }

        assertTrue((viewModel.loadTopAdsProduct.value as Success).data.tid == "1")
    }
    // endregion
}

@ExperimentalCoroutinesApi
class MainCoroutineRule(val dispatcher: TestDispatcher = StandardTestDispatcher()) :
    TestWatcher() {

    override fun starting(description: Description) {
        super.starting(description)
        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: Description) {
        super.finished(description)
        Dispatchers.resetMain()
    }
}
