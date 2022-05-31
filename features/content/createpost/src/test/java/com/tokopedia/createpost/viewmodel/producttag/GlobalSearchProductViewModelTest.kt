package com.tokopedia.createpost.viewmodel.producttag

import com.tokopedia.createpost.model.CommonModelBuilder
import com.tokopedia.createpost.model.GlobalSearchModelBuilder
import com.tokopedia.createpost.model.LastPurchasedModelBuilder
import com.tokopedia.createpost.producttag.domain.repository.ProductTagRepository
import com.tokopedia.createpost.producttag.view.uimodel.ProductTagSource
import com.tokopedia.createpost.producttag.view.uimodel.TickerUiModel
import com.tokopedia.createpost.producttag.view.uimodel.action.ProductTagAction
import com.tokopedia.createpost.robot.ProductTagViewModelRobot
import com.tokopedia.createpost.util.andThen
import com.tokopedia.createpost.util.assertEqualTo
import com.tokopedia.createpost.util.assertError
import com.tokopedia.createpost.util.isSuccess
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created By : Jonathan Darwin on May 31, 2022
 */
class GlobalSearchProductViewModelTest {

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers
    private val mockRepo: ProductTagRepository = mockk(relaxed = true)

    private val commonModelBuilder = CommonModelBuilder()
    private val globalSearchModelBuilder = GlobalSearchModelBuilder()
    private val modelBuilder = LastPurchasedModelBuilder()

    private val mockQuickFilter = globalSearchModelBuilder.buildQuickFilterList()
    private val mockException = commonModelBuilder.buildException()

    private lateinit var robot: ProductTagViewModelRobot
    private val query = "pokemon"

    @Before
    fun setUp() {
        coEvery { mockRepo.getQuickFilter(any(), any()) } returns mockQuickFilter

        robot = ProductTagViewModelRobot(
            dispatcher = testDispatcher,
            repo = mockRepo,
        )

        robot.getViewModel().apply {
            submitAction(ProductTagAction.SetDataFromAutoComplete(ProductTagSource.GlobalSearch, query, ""))
        }
    }

    @Test
    fun `when user load global search product and success, it should emit success state along with the products`() {
        val mockResponse = globalSearchModelBuilder.buildResponseModel()

        coEvery { mockRepo.searchAceProducts(any()) } returns mockResponse

        robot.use {
            it.recordState {
                submitAction(ProductTagAction.LoadGlobalSearchProduct)
            }.andThen {
                globalSearchProduct.state.isSuccess()
                globalSearchProduct.products.assertEqualTo(mockResponse.pagedData.dataList)
                globalSearchProduct.quickFilters.assertEqualTo(mockQuickFilter)
                globalSearchProduct.param.query.assertEqualTo(query)
            }
        }
    }

    @Test
    fun `when user load global search product and failed, it should emit error state`() {
        coEvery { mockRepo.searchAceProducts(any()) } throws mockException

        robot.use {
            it.recordState {
                submitAction(ProductTagAction.LoadGlobalSearchProduct)
            }.andThen {
                globalSearchProduct.state.assertError(mockException)
            }
        }
    }

    @Test
    fun `when user load next page and success, it should emit success state along with the appended products`() {

        robot.use {
            /** Load First Page */
            val nextCursor = "20"
            val mockResponse = globalSearchModelBuilder.buildResponseModel(size = 20, nextCursor = nextCursor)
            coEvery { mockRepo.searchAceProducts(any()) } returns mockResponse

            it.recordState {
                submitAction(ProductTagAction.LoadGlobalSearchProduct)
            }.andThen {
                globalSearchProduct.state.isSuccess()
                globalSearchProduct.products.assertEqualTo(mockResponse.pagedData.dataList)
                globalSearchProduct.quickFilters.assertEqualTo(mockQuickFilter)
                globalSearchProduct.param.query.assertEqualTo(query)
                globalSearchProduct.param.start.assertEqualTo(nextCursor.toInt())
            }

            /** Load Second Page */
            val nextCursor2 = "40"
            val mockResponse2 = globalSearchModelBuilder.buildResponseModel(size = 20, nextCursor = nextCursor2)
            coEvery { mockRepo.searchAceProducts(any()) } returns mockResponse2

            it.recordState {
                submitAction(ProductTagAction.LoadGlobalSearchProduct)
            }.andThen {
                globalSearchProduct.state.isSuccess()
                globalSearchProduct.products.assertEqualTo(mockResponse.pagedData.dataList + mockResponse2.pagedData.dataList)
                globalSearchProduct.quickFilters.assertEqualTo(mockQuickFilter)
                globalSearchProduct.param.query.assertEqualTo(query)
                globalSearchProduct.param.start.assertEqualTo(nextCursor2.toInt())
            }
        }
    }

    @Test
    fun `when user load next page and there's no next page, the state should not change`() {

        robot.use {
            /** Load First Page */
            val nextCursor = "20"
            val mockResponse = globalSearchModelBuilder.buildResponseModel(size = 20, nextCursor = nextCursor, hasNextPage = false)
            coEvery { mockRepo.searchAceProducts(any()) } returns mockResponse

            it.recordState {
                submitAction(ProductTagAction.LoadGlobalSearchProduct)
            }.andThen {
                globalSearchProduct.state.isSuccess()
                globalSearchProduct.products.assertEqualTo(mockResponse.pagedData.dataList)
                globalSearchProduct.quickFilters.assertEqualTo(mockQuickFilter)
                globalSearchProduct.param.query.assertEqualTo(query)
                globalSearchProduct.param.start.assertEqualTo(nextCursor.toInt())
            }

            /** Load Second Page */
            it.recordState {
                submitAction(ProductTagAction.LoadGlobalSearchProduct)
            }.andThen {
                globalSearchProduct.state.isSuccess()
                globalSearchProduct.products.assertEqualTo(mockResponse.pagedData.dataList)
                globalSearchProduct.quickFilters.assertEqualTo(mockQuickFilter)
                globalSearchProduct.param.query.assertEqualTo(query)
                globalSearchProduct.param.start.assertEqualTo(nextCursor.toInt())
            }
        }
    }

    @Test
    fun `when user click ticker, it should reload products with the new param`() {

        robot.use {
            val nextCursor = "20"

            val key = "skip_rewrite"
            val value = "true"
            val tickerQuery = "$key=$value"

            val mockTicker = globalSearchModelBuilder.buildTickerModel(query = tickerQuery)
            val mockResponse = globalSearchModelBuilder.buildResponseModel(
                size = 20,
                nextCursor = nextCursor,
                hasNextPage = false,
                ticker = mockTicker,
            )

            coEvery { mockRepo.searchAceProducts(any()) } returns mockResponse

            it.setup {
                submitAction(ProductTagAction.LoadGlobalSearchProduct)
            }

            /** Load Second Page */
            it.recordState {
                submitAction(ProductTagAction.TickerClicked)
            }.andThen {
                globalSearchProduct.state.isSuccess()
                globalSearchProduct.products.assertEqualTo(mockResponse.pagedData.dataList)
                globalSearchProduct.param.isParamFound(key, value).assertEqualTo(true)
                globalSearchProduct.param.start.assertEqualTo(nextCursor.toInt())
            }
        }
    }

    @Test
    fun `when user click ticker and query is empty, it should not do anything`() {
        val mockTicker = globalSearchModelBuilder.buildTickerModel(query = "")

        val nextCursor = "20"
        val mockResponse = globalSearchModelBuilder.buildResponseModel(
            size = 20,
            nextCursor = nextCursor,
            ticker = mockTicker,
        )

        val nextCursor2 = "40"
        val mockResponse2 = globalSearchModelBuilder.buildResponseModel(
            size = 20,
            nextCursor = nextCursor2,
            ticker = mockTicker,
        )

        robot.use {
            it.setup {
                /** Load First Page */
                coEvery { mockRepo.searchAceProducts(any()) } returns mockResponse

                submitAction(ProductTagAction.LoadGlobalSearchProduct)

                /** Load Second Page */
                coEvery { mockRepo.searchAceProducts(any()) } returns mockResponse2

                submitAction(ProductTagAction.LoadGlobalSearchProduct)
            }

            /** Test */
            coEvery { mockRepo.searchAceProducts(any()) } returns mockResponse

            it.recordState {
                submitAction(ProductTagAction.TickerClicked)
            }.andThen {
                globalSearchProduct.state.isSuccess()
                globalSearchProduct.products.assertEqualTo(mockResponse.pagedData.dataList + mockResponse2.pagedData.dataList)
                globalSearchProduct.param.start.assertEqualTo(nextCursor2.toInt())
            }
        }
    }
}