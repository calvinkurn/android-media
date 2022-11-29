package com.tokopedia.content.common.viewmodel.producttag

import com.tokopedia.content.common.model.CommonModelBuilder
import com.tokopedia.content.common.model.GlobalSearchModelBuilder
import com.tokopedia.content.common.producttag.domain.repository.ProductTagRepository
import com.tokopedia.content.common.producttag.view.uimodel.action.ProductTagAction
import com.tokopedia.content.common.robot.ProductTagViewModelRobot
import com.tokopedia.content.common.util.andThen
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import com.tokopedia.content.common.util.isSuccess
import com.tokopedia.content.common.util.assertError
import com.tokopedia.content.common.util.assertEqualTo
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

/**
 * Created By : Jonathan Darwin on May 30, 2022
 */
class ShopViewModelTest {

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers
    private val mockRepo: ProductTagRepository = mockk(relaxed = true)

    private val commonModelBuilder = CommonModelBuilder()
    private val globalSearchModelBuilder = GlobalSearchModelBuilder()

    private val mockException = commonModelBuilder.buildException()

    @Test
    fun `when user load shop product & success, it should emit success state`() {
        val nextCursor = "2"
        val response = globalSearchModelBuilder.buildResponseModel(nextCursor = nextCursor)

        coEvery { mockRepo.searchAceProducts(any()) } returns response

        val robot = ProductTagViewModelRobot(
            dispatcher = testDispatcher,
            repo = mockRepo,
        )

        robot.use {
            it.recordState {
                submitAction(ProductTagAction.LoadShopProduct)
            }.andThen {
                shopProduct.state.isSuccess()
                shopProduct.products.assertEqualTo(response.pagedData.dataList)
                shopProduct.param.start.assertEqualTo(nextCursor.toInt())
            }
        }
    }

    @Test
    fun `when user load shop product & failed, it should emit failed state`() {
        coEvery { mockRepo.searchAceProducts(any()) } throws mockException

        val robot = ProductTagViewModelRobot(
            dispatcher = testDispatcher,
            repo = mockRepo,
        )

        robot.use {
            it.recordState {
                submitAction(ProductTagAction.LoadShopProduct)
            }.andThen {
                shopProduct.state.assertError(mockException)
            }
        }
    }

    @Test
    fun `when user load shop next page, it should emit success state along with appended products`() {
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
                submitAction(ProductTagAction.LoadShopProduct)
            }.andThen {
                shopProduct.state.isSuccess()
                shopProduct.products.assertEqualTo(response1.pagedData.dataList)
                shopProduct.param.start.assertEqualTo(nextCursor1.toInt())
            }

            /** Test: Load Next Page */
            coEvery { mockRepo.searchAceProducts(any()) } returns response2

            it.recordState {
                submitAction(ProductTagAction.LoadShopProduct)
            }.andThen {
                shopProduct.state.isSuccess()
                shopProduct.products.assertEqualTo(response1.pagedData.dataList + response2.pagedData.dataList)
                shopProduct.param.start.assertEqualTo(nextCursor2.toInt())
            }
        }
    }

    @Test
    fun `when user load shop next page but it has no next page, it shouldnt do anything`() {
        val nextCursor = "2"
        val response = globalSearchModelBuilder.buildResponseModel(nextCursor = nextCursor, hasNextPage = false)

        coEvery { mockRepo.searchAceProducts(any()) } returns response

        val robot = ProductTagViewModelRobot(
            dispatcher = testDispatcher,
            repo = mockRepo,
        )

        robot.use {
            it.recordState {
                submitAction(ProductTagAction.LoadShopProduct)
            }.andThen {
                shopProduct.state.isSuccess()
                shopProduct.products.assertEqualTo(response.pagedData.dataList)
                shopProduct.param.start.assertEqualTo(nextCursor.toInt())
            }

            it.recordState {
                submitAction(ProductTagAction.LoadShopProduct)
            }.andThen {
                shopProduct.state.isSuccess()
                shopProduct.products.assertEqualTo(response.pagedData.dataList)
                shopProduct.param.start.assertEqualTo(nextCursor.toInt())
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
                submitAction(ProductTagAction.LoadShopProduct)
            }.andThen {
                shopProduct.state.isSuccess()
                shopProduct.products.assertEqualTo(response.pagedData.dataList)
                shopProduct.param.start.assertEqualTo(nextCursor.toInt())
                shopProduct.param.query.assertEqualTo("")
            }

            coEvery { mockRepo.searchAceProducts(any()) } returns response2

            it.recordState {
                submitAction(ProductTagAction.SearchShopProduct(query))
            }.andThen {
                shopProduct.state.isSuccess()
                shopProduct.products.assertEqualTo(response2.pagedData.dataList)
                shopProduct.param.start.assertEqualTo(nextCursor.toInt())
                shopProduct.param.query.assertEqualTo(query)
            }

            /** Trying to search with the same query, state wont change.
             *  To prove that, mock searchAceProducts again and return the diff result.
             * */
            coEvery { mockRepo.searchAceProducts(any()) } returns response3

            it.recordState {
                submitAction(ProductTagAction.SearchShopProduct(query))
            }.andThen {
                shopProduct.state.isSuccess()
                shopProduct.products.assertEqualTo(response2.pagedData.dataList)
                shopProduct.param.start.assertEqualTo(nextCursor.toInt())
                shopProduct.param.query.assertEqualTo(query)
            }
        }
    }
}