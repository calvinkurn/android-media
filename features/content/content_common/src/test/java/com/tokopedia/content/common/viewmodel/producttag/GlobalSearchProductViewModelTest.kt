package com.tokopedia.content.common.viewmodel.producttag

import com.tokopedia.content.common.model.CommonModelBuilder
import com.tokopedia.content.common.model.GlobalSearchModelBuilder
import com.tokopedia.content.common.producttag.domain.repository.ProductTagRepository
import com.tokopedia.content.common.producttag.view.uimodel.NetworkResult
import com.tokopedia.content.common.producttag.view.uimodel.ProductTagSource
import com.tokopedia.content.common.producttag.view.uimodel.action.ProductTagAction
import com.tokopedia.content.common.producttag.view.uimodel.event.ProductTagUiEvent
import com.tokopedia.content.common.robot.ProductTagViewModelRobot
import com.tokopedia.content.common.util.andThen
import com.tokopedia.content.common.util.isSuccess
import com.tokopedia.content.common.util.assertError
import com.tokopedia.content.common.util.assertTrue
import com.tokopedia.content.common.util.assertEmpty
import com.tokopedia.content.common.util.assertEqualTo
import com.tokopedia.content.common.util.assertEvent
import com.tokopedia.content.common.util.assertFalse
import com.tokopedia.content.common.util.assertEventError
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.Assert.fail
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
            submitAction(ProductTagAction.SetDataFromAutoComplete(ProductTagSource.GlobalSearch, query, "", ""))
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
    fun `when user click suggestion, it should reload products & shops with the new suggestion query`() {

        robot.use {
            val suggestedQuery = "pokemon red"

            val mockSuggestion = globalSearchModelBuilder.buildSuggestionModel(suggestion = suggestedQuery)
            val mockResponse = globalSearchModelBuilder.buildResponseModel(
                suggestion = mockSuggestion,
            )
            val mockShopResponse = globalSearchModelBuilder.buildShopResponseModel()

            coEvery { mockRepo.searchAceProducts(any()) } returns mockResponse
            coEvery { mockRepo.searchAceShops(any()) } returns mockShopResponse

            it.setup {
                submitAction(ProductTagAction.LoadGlobalSearchProduct)
                submitAction(ProductTagAction.LoadGlobalSearchShop)
            }

            it.recordState {
                submitAction(ProductTagAction.SuggestionClicked)
            }.andThen {
                globalSearchProduct.state.isSuccess()
                globalSearchProduct.products.assertEqualTo(mockResponse.pagedData.dataList)
                globalSearchProduct.param.prevQuery.assertEqualTo(query)
                globalSearchProduct.param.query.assertEqualTo(suggestedQuery)

                globalSearchShop.param.prevQuery.assertEqualTo(query)
                globalSearchShop.param.query.assertEqualTo(suggestedQuery)
            }
        }
    }

    @Test
    fun `when user click suggestion but suggestion query is empty, it should not reload products & shops`() {

        robot.use {
            val suggestedQuery = ""

            val mockSuggestion = globalSearchModelBuilder.buildSuggestionModel(suggestion = suggestedQuery)
            val mockResponse = globalSearchModelBuilder.buildResponseModel(
                suggestion = mockSuggestion,
            )
            val mockShopResponse = globalSearchModelBuilder.buildShopResponseModel()

            coEvery { mockRepo.searchAceProducts(any()) } returns mockResponse
            coEvery { mockRepo.searchAceShops(any()) } returns mockShopResponse

            it.setup {
                submitAction(ProductTagAction.LoadGlobalSearchProduct)
                submitAction(ProductTagAction.LoadGlobalSearchShop)
            }

            it.recordState {
                submitAction(ProductTagAction.SuggestionClicked)
            }.andThen {
                globalSearchProduct.state.isSuccess()
                globalSearchProduct.products.assertEqualTo(mockResponse.pagedData.dataList)
                globalSearchProduct.param.prevQuery.assertEmpty()
                globalSearchProduct.param.query.assertEqualTo(query)

                globalSearchShop.param.prevQuery.assertEmpty()
                globalSearchShop.param.query.assertEqualTo(query)
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

    @Test
    fun `when user close ticker, it should emit new state with empty ticker`() {
        val mockTicker = globalSearchModelBuilder.buildTickerModel()
        val mockEmptyTicker = globalSearchModelBuilder.buildTickerModel(text = "", query = "")

        val nextCursor = "20"
        val mockResponse = globalSearchModelBuilder.buildResponseModel(
            size = 20,
            nextCursor = nextCursor,
            ticker = mockTicker,
        )

        coEvery { mockRepo.searchAceProducts(any()) } returns mockResponse

        robot.use {
            it.setup {
                submitAction(ProductTagAction.LoadGlobalSearchProduct)
            }

            it.recordState {
                submitAction(ProductTagAction.CloseTicker)
            }.andThen {
                globalSearchProduct.state.isSuccess()
                globalSearchProduct.products.assertEqualTo(mockResponse.pagedData.dataList)
                globalSearchProduct.ticker.assertEqualTo(mockEmptyTicker)
            }
        }
    }

    @Test
    fun `when user select quick filter, it should reload product with selected filter`() {
        val nextCursor = "20"
        val mockResponse = globalSearchModelBuilder.buildResponseModel(
            size = 20,
            nextCursor = nextCursor,
        )
        val selectedQuickFilter = globalSearchModelBuilder.buildQuickFilterModel()

        coEvery { mockRepo.searchAceProducts(any()) } returns mockResponse

        robot.use {
            it.recordState {
                submitAction(ProductTagAction.SelectProductQuickFilter(selectedQuickFilter))
            }.andThen {
                globalSearchProduct.state.isSuccess()
                globalSearchProduct.products.assertEqualTo(mockResponse.pagedData.dataList)
                globalSearchProduct.param.isParamFound(selectedQuickFilter.key, selectedQuickFilter.value).assertTrue()
            }
        }
    }

    @Test
    fun `when user select and unselect quick filter, it should reload product with no filter`() {
        val nextCursor = "20"
        val mockResponse = globalSearchModelBuilder.buildResponseModel(
            size = 20,
            nextCursor = nextCursor,
        )
        val selectedQuickFilter = globalSearchModelBuilder.buildQuickFilterModel()

        coEvery { mockRepo.searchAceProducts(any()) } returns mockResponse

        robot.use {
            it.recordState {
                submitAction(ProductTagAction.SelectProductQuickFilter(selectedQuickFilter))
            }.andThen {
                globalSearchProduct.state.isSuccess()
                globalSearchProduct.products.assertEqualTo(mockResponse.pagedData.dataList)
                globalSearchProduct.param.isParamFound(selectedQuickFilter.key, selectedQuickFilter.value).assertTrue()
            }

            it.recordState {
                submitAction(ProductTagAction.SelectProductQuickFilter(selectedQuickFilter))
            }.andThen {
                globalSearchProduct.state.isSuccess()
                globalSearchProduct.products.assertEqualTo(mockResponse.pagedData.dataList)
                globalSearchProduct.param.isParamFound(selectedQuickFilter.key, selectedQuickFilter.value).assertFalse()
            }
        }
    }

    @Test
    fun `when user wants to open sort filter bottomsheet, it should load sort filter data and emit open bottomsheet event`() {
        val mockSortFilter = globalSearchModelBuilder.buildSortFilterResponseModel()

        coEvery { mockRepo.getSortFilter(any()) } returns mockSortFilter

        robot.use {
            it.recordStateAndEvent {
                submitAction(ProductTagAction.OpenProductSortFilterBottomSheet)
            }.andThen { state, events ->
                state.globalSearchProduct.sortFilters.assertEqualTo(mockSortFilter)
                val lastEvent = events.last()

                if(lastEvent is ProductTagUiEvent.OpenProductSortFilterBottomSheet) {
                    lastEvent.param.assertEqualTo(state.globalSearchProduct.param)
                    lastEvent.data.assertEqualTo(mockSortFilter)
                }
                else {
                    fail("Event should be OpenProductSortFilterBottomSheet")
                }
            }

            /** Open bottom sheet once again and shouldnt hit gql getSortFilter anymore.
             * To prove that, we mock getSortFilter to throw an error
             */
            coEvery { mockRepo.getSortFilter(any()) } throws mockException

            it.recordStateAndEvent {
                submitAction(ProductTagAction.OpenProductSortFilterBottomSheet)
            }.andThen { state, events ->
                state.globalSearchProduct.sortFilters.assertEqualTo(mockSortFilter)
                val lastEvent = events.last()

                if(lastEvent is ProductTagUiEvent.OpenProductSortFilterBottomSheet) {
                    lastEvent.param.assertEqualTo(state.globalSearchProduct.param)
                    lastEvent.data.assertEqualTo(state.globalSearchProduct.sortFilters)
                }
                else {
                    fail("Event should be OpenProductSortFilterBottomSheet")
                }
            }
        }
    }

    @Test
    fun `when user wants to open sort filter bottomsheet but error occurs, it should emit error event`() {

        coEvery { mockRepo.getSortFilter(any()) } throws mockException

        robot.use {
            it.recordStateAndEvent {
                submitAction(ProductTagAction.OpenProductSortFilterBottomSheet)
            }.andThen { state, events ->
                state.globalSearchProduct.sortFilters.isEmpty().assertTrue()

                events.last().assertEventError(mockException)
            }
        }
    }

    @Test
    fun `when user counts filter product number successfully, it should emit success event`() {
        val mockResponse = "12"
        coEvery { mockRepo.getSortFilterProductCount(any()) } returns mockResponse

        robot.use {
            it.recordEvent {
                submitAction(ProductTagAction.RequestProductFilterProductCount(emptyMap()))
            }.andThen {
                last().assertEvent(ProductTagUiEvent.SetProductFilterProductCount(NetworkResult.Success(mockResponse)))
            }
        }
    }

    @Test
    fun `when user counts filter product number unsuccessfully, it should emit error event`() {
        coEvery { mockRepo.getSortFilterProductCount(any()) } throws mockException

        robot.use {
            it.recordEvent {
                submitAction(ProductTagAction.RequestProductFilterProductCount(emptyMap()))
            }.andThen {
                last().assertEvent(ProductTagUiEvent.SetProductFilterProductCount(NetworkResult.Error(mockException)))
            }
        }
    }

    @Test
    fun `when user apply filter sort param, it should reload products with the new applied param`() {
        val mockResponse = globalSearchModelBuilder.buildResponseModel()
        val selectedSortFilter = globalSearchModelBuilder.buildSortFilterModel()

        coEvery { mockRepo.searchAceProducts(any()) } returns mockResponse

        robot.use {
            it.recordState {
                submitAction(ProductTagAction.ApplyProductSortFilter(selectedSortFilter))
            }.andThen {
                globalSearchProduct.products.assertEqualTo(mockResponse.pagedData.dataList)
                selectedSortFilter.forEach { sortFilter ->
                    globalSearchProduct.param.isParamFound(sortFilter.key, sortFilter.value).assertTrue()
                }
            }
        }
    }

    @Test
    fun `when user swipe to refresh, it should reset the pagination, reload products, while keeping the applied param`() {
        val mockResponse = globalSearchModelBuilder.buildResponseModel()
        val selectedSortFilter = globalSearchModelBuilder.buildSortFilterModel()

        coEvery { mockRepo.searchAceProducts(any()) } returns mockResponse

        robot.use {
            it.recordState {
                submitAction(ProductTagAction.ApplyProductSortFilter(selectedSortFilter))
                submitAction(ProductTagAction.LoadGlobalSearchProduct)
            }.andThen {
                globalSearchProduct.products.assertEqualTo(mockResponse.pagedData.dataList + mockResponse.pagedData.dataList)
                selectedSortFilter.forEach { sortFilter ->
                    globalSearchProduct.param.isParamFound(sortFilter.key, sortFilter.value).assertTrue()
                }
            }

            it.recordState {
                submitAction(ProductTagAction.SwipeRefreshGlobalSearchProduct)
            }.andThen {
                globalSearchProduct.products.assertEqualTo(mockResponse.pagedData.dataList)
                selectedSortFilter.forEach { sortFilter ->
                    globalSearchProduct.param.isParamFound(sortFilter.key, sortFilter.value).assertTrue()
                }
            }
        }
    }
}