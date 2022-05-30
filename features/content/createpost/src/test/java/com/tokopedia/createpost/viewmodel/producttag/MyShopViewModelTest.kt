package com.tokopedia.createpost.viewmodel.producttag

import com.tokopedia.createpost.model.CommonModelBuilder
import com.tokopedia.createpost.model.GlobalSearchModelBuilder
import com.tokopedia.createpost.model.ShopModelBuilder
import com.tokopedia.createpost.producttag.domain.repository.ProductTagRepository
import com.tokopedia.createpost.producttag.util.preference.ProductTagPreference
import com.tokopedia.createpost.producttag.view.uimodel.action.ProductTagAction
import com.tokopedia.createpost.producttag.view.uimodel.event.ProductTagUiEvent
import com.tokopedia.createpost.robot.ProductTagViewModelRobot
import com.tokopedia.createpost.util.andThen
import com.tokopedia.createpost.util.assertEqualTo
import com.tokopedia.createpost.util.assertError
import com.tokopedia.createpost.util.isSuccess
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

/**
 * Created By : Jonathan Darwin on May 30, 2022
 */
class MyShopViewModelTest {

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers
    private val mockRepo: ProductTagRepository = mockk(relaxed = true)
    private val mockSharedPref: ProductTagPreference = mockk(relaxed = true)

    private val commonModelBuilder = CommonModelBuilder()
    private val shopModelBuilder = ShopModelBuilder()
    private val globalSearchModelBuilder = GlobalSearchModelBuilder()

    private val mockException = commonModelBuilder.buildException()

    @Test
    fun `when user load myshop product & success, it should emit success state`() {
        val nextCursor = "2"
        val response = globalSearchModelBuilder.buildResponseModel(nextCursor = nextCursor)

        coEvery { mockRepo.searchAceProducts(any()) } returns response

        val robot = ProductTagViewModelRobot(
            dispatcher = testDispatcher,
            repo = mockRepo,
        )

        robot.use {
            it.recordState {
                submitAction(ProductTagAction.LoadMyShopProduct)
            }.andThen {
                myShopProduct.state.isSuccess()
                myShopProduct.products.assertEqualTo(response.pagedData.dataList)
                myShopProduct.param.start.assertEqualTo(nextCursor.toInt())
            }
        }
    }

    @Test
    fun `when user load myshop product & failed, it should emit failed state`() {
        coEvery { mockRepo.searchAceProducts(any()) } throws mockException

        val robot = ProductTagViewModelRobot(
            dispatcher = testDispatcher,
            repo = mockRepo,
        )

        robot.use {
            it.recordState {
                submitAction(ProductTagAction.LoadMyShopProduct)
            }.andThen {
                myShopProduct.state.assertError(mockException)
            }
        }
    }

    @Test
    fun `when user load myshop next page, it should emit success state along with appended products`() {
        val nextCursor1 = "2"
        val response1 = globalSearchModelBuilder.buildResponseModel(nextCursor = nextCursor1)

        val nextCursor2 = "3"
        val response2 = globalSearchModelBuilder.buildResponseModel(nextCursor = nextCursor2)

        val robot = ProductTagViewModelRobot(
            dispatcher = testDispatcher,
            repo = mockRepo,
        )

        robot.use {
            /** Setup: Load First Page */
            coEvery { mockRepo.searchAceProducts(any()) } returns response1

            it.recordState {
                submitAction(ProductTagAction.LoadMyShopProduct)
            }.andThen {
                myShopProduct.state.isSuccess()
                myShopProduct.products.assertEqualTo(response1.pagedData.dataList)
                myShopProduct.param.start.assertEqualTo(nextCursor1.toInt())
            }

            /** Test: Load Next Page */
            coEvery { mockRepo.searchAceProducts(any()) } returns response2

            it.recordState {
                submitAction(ProductTagAction.LoadMyShopProduct)
            }.andThen {
                myShopProduct.state.isSuccess()
                myShopProduct.products.assertEqualTo(response1.pagedData.dataList + response2.pagedData.dataList)
                myShopProduct.param.start.assertEqualTo(nextCursor2.toInt())
            }
        }
    }

    @Test
    fun `when user load next page but it has no next page, it shouldnt do anything`() {
        val nextCursor = ""
        val response = globalSearchModelBuilder.buildResponseModel(nextCursor = nextCursor, hasNextPage = false)

        coEvery { mockRepo.searchAceProducts(any()) } returns response

        val robot = ProductTagViewModelRobot(
            dispatcher = testDispatcher,
            repo = mockRepo,
        )

        robot.use {
            it.recordState {
                submitAction(ProductTagAction.LoadMyShopProduct)
            }.andThen {
                myShopProduct.state.isSuccess()
                myShopProduct.products.assertEqualTo(response.pagedData.dataList)
                myShopProduct.param.start.assertEqualTo(nextCursor.toInt())
            }

            it.recordState {
                submitAction(ProductTagAction.LoadMyShopProduct)
            }.andThen {
                myShopProduct.state.isSuccess()
                myShopProduct.products.assertEqualTo(response.pagedData.dataList)
                myShopProduct.param.start.assertEqualTo(nextCursor.toInt())
            }
        }
    }

    @Test
    fun `when user search query, it should reload new product as well as reset the pagination`() {
        val query = "pokemon"
        val nextCursor = "20"
        val response = globalSearchModelBuilder.buildResponseModel(size = 10, nextCursor = nextCursor)
        val response2 = globalSearchModelBuilder.buildResponseModel(size = 5, nextCursor = nextCursor)
        val response3 = globalSearchModelBuilder.buildResponseModel(size = 3, nextCursor = nextCursor)

        val robot = ProductTagViewModelRobot(
            dispatcher = testDispatcher,
            repo = mockRepo,
        )

        robot.use {
            coEvery { mockRepo.searchAceProducts(any()) } returns response

            it.recordState {
                submitAction(ProductTagAction.LoadMyShopProduct)
            }.andThen {
                myShopProduct.state.isSuccess()
                myShopProduct.products.assertEqualTo(response.pagedData.dataList)
                myShopProduct.param.start.assertEqualTo(nextCursor.toInt())
                myShopProduct.param.query.assertEqualTo("")
            }

            coEvery { mockRepo.searchAceProducts(any()) } returns response2

            it.recordState {
                submitAction(ProductTagAction.SearchMyShopProduct(query))
            }.andThen {
                myShopProduct.state.isSuccess()
                myShopProduct.products.assertEqualTo(response2.pagedData.dataList)
                myShopProduct.param.start.assertEqualTo(nextCursor.toInt())
                myShopProduct.param.query.assertEqualTo(query)
            }

            /** Trying to search with the same query, state wont change.
             *  To prove that, mock searchAceProducts again and return the diff result.
             * */
            coEvery { mockRepo.searchAceProducts(any()) } returns response3

            it.recordState {
                submitAction(ProductTagAction.SearchMyShopProduct(query))
            }.andThen {
                myShopProduct.state.isSuccess()
                myShopProduct.products.assertEqualTo(response2.pagedData.dataList)
                myShopProduct.param.start.assertEqualTo(nextCursor.toInt())
                myShopProduct.param.query.assertEqualTo(query)
            }
        }
    }

    @Test
    fun `when user wants to open sort bottomsheet, it should emit event to open bottomsheet along with the data`() {
        val mockResponse = globalSearchModelBuilder.buildSortFilterResponseModel()
        val mockResponse2 = globalSearchModelBuilder.buildSortFilterResponseModel(sizeFilter = 10, sizeSort = 10)

        val robot = ProductTagViewModelRobot(
            dispatcher = testDispatcher,
            repo = mockRepo,
        )

        robot.use {
            coEvery { mockRepo.getSortFilter(any()) } returns mockResponse

            it.recordStateAndEvent {
                submitAction(ProductTagAction.OpenMyShopSortBottomSheet)
            }.andThen { state, events ->
                state.myShopProduct.sorts.size.assertEqualTo(mockResponse.data.sort.size)
                state.myShopProduct.sorts.forEachIndexed { idx, element ->
                    val curr = mockResponse.data.sort[idx]

                    element.text.assertEqualTo(curr.name)
                    element.key.assertEqualTo(curr.key)
                    element.value.assertEqualTo(curr.value)
                }

                events.last().assertEqualTo(ProductTagUiEvent.OpenMyShopSortBottomSheet)
            }

            /** Trying to open sort bottomsheet again and it should not call GQL to get sortFilter anymore.
             *  To prove that, mock getSortFilter again and return the diff result.
             * */
            coEvery { mockRepo.getSortFilter(any()) } returns mockResponse2

            it.recordStateAndEvent {
                submitAction(ProductTagAction.OpenMyShopSortBottomSheet)
            }.andThen { state, events ->
                state.myShopProduct.sorts.size.assertEqualTo(mockResponse.data.sort.size)
                state.myShopProduct.sorts.forEachIndexed { idx, element ->
                    val curr = mockResponse.data.sort[idx]

                    element.text.assertEqualTo(curr.name)
                    element.key.assertEqualTo(curr.key)
                    element.value.assertEqualTo(curr.value)
                }

                events.last().assertEqualTo(ProductTagUiEvent.OpenMyShopSortBottomSheet)
            }
        }
    }
}