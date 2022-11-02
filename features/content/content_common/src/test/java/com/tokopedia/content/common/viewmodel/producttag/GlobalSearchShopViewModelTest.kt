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
import com.tokopedia.content.common.util.assertEqualTo
import com.tokopedia.content.common.util.assertEvent
import com.tokopedia.content.common.util.assertFalse
import com.tokopedia.content.common.util.assertEventError
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created By : Jonathan Darwin on May 31, 2022
 */
class GlobalSearchShopViewModelTest {

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
    fun `when user load global search shop and success, it should emit success state along with the shops`() {
        val mockResponse = globalSearchModelBuilder.buildShopResponseModel()

        coEvery { mockRepo.searchAceShops(any()) } returns mockResponse

        robot.use {
            it.recordState {
                submitAction(ProductTagAction.LoadGlobalSearchShop)
            }.andThen {
                globalSearchShop.state.isSuccess()
                globalSearchShop.shops.assertEqualTo(mockResponse.pagedData.dataList)
                globalSearchShop.quickFilters.assertEqualTo(mockQuickFilter)
                globalSearchShop.param.query.assertEqualTo(query)
            }
        }
    }

    @Test
    fun `when user load global search shop and failed, it should emit error state`() {
        coEvery { mockRepo.searchAceShops(any()) } throws mockException

        robot.use {
            it.recordState {
                submitAction(ProductTagAction.LoadGlobalSearchShop)
            }.andThen {
                globalSearchShop.state.assertError(mockException)
            }
        }
    }

    @Test
    fun `when user load next page and success, it should emit success state along with the appended shops`() {

        robot.use {
            /** Load First Page */
            val nextCursor = "20"
            val mockResponse = globalSearchModelBuilder.buildShopResponseModel(size = 20, nextCursor = nextCursor)
            coEvery { mockRepo.searchAceShops(any()) } returns mockResponse

            it.recordState {
                submitAction(ProductTagAction.LoadGlobalSearchShop)
            }.andThen {
                globalSearchShop.state.isSuccess()
                globalSearchShop.shops.assertEqualTo(mockResponse.pagedData.dataList)
                globalSearchShop.quickFilters.assertEqualTo(mockQuickFilter)
                globalSearchShop.param.query.assertEqualTo(query)
                globalSearchShop.param.start.assertEqualTo(nextCursor.toInt())
            }

            /** Load Second Page */
            val nextCursor2 = "40"
            val mockResponse2 = globalSearchModelBuilder.buildShopResponseModel(size = 20, nextCursor = nextCursor2)
            coEvery { mockRepo.searchAceShops(any()) } returns mockResponse2

            it.recordState {
                submitAction(ProductTagAction.LoadGlobalSearchShop)
            }.andThen {
                globalSearchShop.state.isSuccess()
                globalSearchShop.shops.assertEqualTo(mockResponse.pagedData.dataList + mockResponse2.pagedData.dataList)
                globalSearchShop.quickFilters.assertEqualTo(mockQuickFilter)
                globalSearchShop.param.query.assertEqualTo(query)
                globalSearchShop.param.start.assertEqualTo(nextCursor2.toInt())
            }
        }
    }

    @Test
    fun `when user load next page and there's no next page, the state should not change`() {

        robot.use {
            /** Load First Page */
            val nextCursor = "20"
            val mockResponse = globalSearchModelBuilder.buildShopResponseModel(size = 20, nextCursor = nextCursor, hasNextPage = false)
            coEvery { mockRepo.searchAceShops(any()) } returns mockResponse

            it.recordState {
                submitAction(ProductTagAction.LoadGlobalSearchShop)
            }.andThen {
                globalSearchShop.state.isSuccess()
                globalSearchShop.shops.assertEqualTo(mockResponse.pagedData.dataList)
                globalSearchShop.quickFilters.assertEqualTo(mockQuickFilter)
                globalSearchShop.param.query.assertEqualTo(query)
                globalSearchShop.param.start.assertEqualTo(nextCursor.toInt())
            }

            /** Load Second Page */
            it.recordState {
                submitAction(ProductTagAction.LoadGlobalSearchShop)
            }.andThen {
                globalSearchShop.state.isSuccess()
                globalSearchShop.shops.assertEqualTo(mockResponse.pagedData.dataList)
                globalSearchShop.quickFilters.assertEqualTo(mockQuickFilter)
                globalSearchShop.param.query.assertEqualTo(query)
                globalSearchShop.param.start.assertEqualTo(nextCursor.toInt())
            }
        }
    }

    @Test
    fun `when user select shop and shop is accessible, it should append shop source to stack`() {

        robot.use {
            val selectedShop = commonModelBuilder.buildShopModel(shopStatus = 0)

            it.recordState {
                submitAction(ProductTagAction.ShopSelected(selectedShop))
            }.andThen {
                shopProduct.shop.assertEqualTo(selectedShop)
                productTagSource.productTagSourceStack.last().assertEqualTo(ProductTagSource.Shop)
            }
        }
    }

    @Test
    fun `when user select shop and shop is not accessible, it should not append shop source to stack`() {

        robot.use {
            val selectedShop = commonModelBuilder.buildShopModel(shopStatus = 3)

            it.recordState {
                submitAction(ProductTagAction.ShopSelected(selectedShop))
            }.andThen {
                productTagSource.productTagSourceStack.last().assertEqualTo(ProductTagSource.GlobalSearch)
            }
        }
    }

    @Test
    fun `when user select quick filter, it should reload shop with selected filter`() {
        val mockResponse = globalSearchModelBuilder.buildShopResponseModel()
        val selectedQuickFilter = globalSearchModelBuilder.buildQuickFilterModel()

        coEvery { mockRepo.searchAceShops(any()) } returns mockResponse

        robot.use {
            it.recordState {
                submitAction(ProductTagAction.SelectShopQuickFilter(selectedQuickFilter))
            }.andThen {
                globalSearchShop.state.isSuccess()
                globalSearchShop.shops.assertEqualTo(mockResponse.pagedData.dataList)
                globalSearchShop.param.isParamFound(selectedQuickFilter.key, selectedQuickFilter.value).assertTrue()
            }
        }
    }

    @Test
    fun `when user select and unselect quick filter, it should reload shop with no filter`() {
        val mockResponse = globalSearchModelBuilder.buildShopResponseModel()
        val selectedQuickFilter = globalSearchModelBuilder.buildQuickFilterModel()

        coEvery { mockRepo.searchAceShops(any()) } returns mockResponse

        robot.use {
            it.recordState {
                submitAction(ProductTagAction.SelectShopQuickFilter(selectedQuickFilter))
            }.andThen {
                globalSearchShop.state.isSuccess()
                globalSearchShop.shops.assertEqualTo(mockResponse.pagedData.dataList)
                globalSearchShop.param.isParamFound(selectedQuickFilter.key, selectedQuickFilter.value).assertTrue()
            }

            it.recordState {
                submitAction(ProductTagAction.SelectShopQuickFilter(selectedQuickFilter))
            }.andThen {
                globalSearchShop.state.isSuccess()
                globalSearchShop.shops.assertEqualTo(mockResponse.pagedData.dataList)
                globalSearchShop.param.isParamFound(selectedQuickFilter.key, selectedQuickFilter.value).assertFalse()
            }
        }
    }

    @Test
    fun `when user wants to open sort filter bottomsheet, it should load sort filter data and emit open bottomsheet event`() {
        val mockSortFilter = globalSearchModelBuilder.buildSortFilterResponseModel()

        coEvery { mockRepo.getSortFilter(any()) } returns mockSortFilter

        robot.use {
            it.recordStateAndEvent {
                submitAction(ProductTagAction.OpenShopSortFilterBottomSheet)
            }.andThen { state, events ->
                state.globalSearchShop.sortFilters.assertEqualTo(mockSortFilter)
                val lastEvent = events.last()

                if(lastEvent is ProductTagUiEvent.OpenShopSortFilterBottomSheet) {
                    lastEvent.param.assertEqualTo(state.globalSearchShop.param)
                    lastEvent.data.assertEqualTo(mockSortFilter)
                }
                else {
                    Assert.fail("Event should be OpenShopSortFilterBottomSheet")
                }
            }

            /** Open bottom sheet once again and shouldnt hit gql getSortFilter anymore.
             * To prove that, we mock getSortFilter to throw an error
             */
            coEvery { mockRepo.getSortFilter(any()) } throws mockException

            it.recordStateAndEvent {
                submitAction(ProductTagAction.OpenShopSortFilterBottomSheet)
            }.andThen { state, events ->
                state.globalSearchShop.sortFilters.assertEqualTo(mockSortFilter)
                val lastEvent = events.last()

                if(lastEvent is ProductTagUiEvent.OpenShopSortFilterBottomSheet) {
                    lastEvent.param.assertEqualTo(state.globalSearchShop.param)
                    lastEvent.data.assertEqualTo(state.globalSearchShop.sortFilters)
                }
                else {
                    Assert.fail("Event should be OpenShopSortFilterBottomSheet")
                }
            }
        }
    }

    @Test
    fun `when user wants to open sort filter bottomsheet but error occurs, it should emit error event`() {

        coEvery { mockRepo.getSortFilter(any()) } throws mockException

        robot.use {
            it.recordStateAndEvent {
                submitAction(ProductTagAction.OpenShopSortFilterBottomSheet)
            }.andThen { state, events ->
                state.globalSearchShop.sortFilters.isEmpty().assertTrue()

                events.last().assertEventError(mockException)
            }
        }
    }

    @Test
    fun `when user counts filter shop number successfully, it should emit success event`() {
        val mockResponse = "12"
        coEvery { mockRepo.getSortFilterProductCount(any()) } returns mockResponse

        robot.use {
            it.recordEvent {
                submitAction(ProductTagAction.RequestShopFilterProductCount(emptyMap()))
            }.andThen {
                last().assertEvent(ProductTagUiEvent.SetShopFilterProductCount(NetworkResult.Success(mockResponse)))
            }
        }
    }

    @Test
    fun `when user counts filter shop number unsuccessfully, it should emit error event`() {
        coEvery { mockRepo.getSortFilterProductCount(any()) } throws mockException

        robot.use {
            it.recordEvent {
                submitAction(ProductTagAction.RequestShopFilterProductCount(emptyMap()))
            }.andThen {
                last().assertEvent(ProductTagUiEvent.SetShopFilterProductCount(NetworkResult.Error(mockException)))
            }
        }
    }

    @Test
    fun `when user apply filter sort param, it should reload shops with the new applied param`() {
        val mockResponse = globalSearchModelBuilder.buildShopResponseModel()
        val selectedSortFilter = globalSearchModelBuilder.buildSortFilterModel()

        coEvery { mockRepo.searchAceShops(any()) } returns mockResponse

        robot.use {
            it.recordState {
                submitAction(ProductTagAction.ApplyShopSortFilter(selectedSortFilter))
            }.andThen {
                globalSearchShop.shops.assertEqualTo(mockResponse.pagedData.dataList)
                selectedSortFilter.forEach { sortFilter ->
                    globalSearchShop.param.isParamFound(sortFilter.key, sortFilter.value).assertTrue()
                }
            }
        }
    }

    @Test
    fun `when user swipe to refresh, it should reset the pagination, reload shops, while keeping the applied param`() {
        val mockResponse = globalSearchModelBuilder.buildShopResponseModel()
        val selectedSortFilter = globalSearchModelBuilder.buildSortFilterModel()

        coEvery { mockRepo.searchAceShops(any()) } returns mockResponse

        robot.use {
            it.recordState {
                submitAction(ProductTagAction.ApplyShopSortFilter(selectedSortFilter))
                submitAction(ProductTagAction.LoadGlobalSearchShop)
            }.andThen {
                globalSearchShop.shops.assertEqualTo(mockResponse.pagedData.dataList + mockResponse.pagedData.dataList)
                selectedSortFilter.forEach { sortFilter ->
                    globalSearchShop.param.isParamFound(sortFilter.key, sortFilter.value).assertTrue()
                }
            }

            it.recordState {
                submitAction(ProductTagAction.SwipeRefreshGlobalSearchShop)
            }.andThen {
                globalSearchShop.shops.assertEqualTo(mockResponse.pagedData.dataList)
                selectedSortFilter.forEach { sortFilter ->
                    globalSearchShop.param.isParamFound(sortFilter.key, sortFilter.value).assertTrue()
                }
            }
        }
    }
}