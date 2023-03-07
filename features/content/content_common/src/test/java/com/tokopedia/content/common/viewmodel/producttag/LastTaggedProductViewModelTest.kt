package com.tokopedia.content.common.viewmodel.producttag

import com.tokopedia.content.common.model.CommonModelBuilder
import com.tokopedia.content.common.model.LastTaggedModelBuilder
import com.tokopedia.content.common.producttag.domain.repository.ProductTagRepository
import com.tokopedia.content.common.producttag.view.uimodel.action.ProductTagAction
import com.tokopedia.content.common.robot.ProductTagViewModelRobot
import com.tokopedia.content.common.util.andThen
import com.tokopedia.content.common.util.assertEqualTo
import com.tokopedia.content.common.util.assertError
import com.tokopedia.content.common.util.isSuccess
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

/**
 * Created By : Jonathan Darwin on May 30, 2022
 */
class LastTaggedProductViewModelTest {

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers
    private val mockRepo: ProductTagRepository = mockk(relaxed = true)

    private val commonModelBuilder = CommonModelBuilder()
    private val modelBuilder = LastTaggedModelBuilder()

    private val mockException = commonModelBuilder.buildException()

    @Test
    fun `when user load last tagged product & success, it should emit success state`() {
        val nextCursor = "2"
        val pagedData = modelBuilder.buildPagedDataModel(nextCursor = nextCursor)

        coEvery { mockRepo.getLastTaggedProducts(any(), any(), any(), any()) } returns pagedData

        val robot = ProductTagViewModelRobot(
            dispatcher = testDispatcher,
            repo = mockRepo,
        )

        robot.use {
            it.recordState {
                submitAction(ProductTagAction.LoadLastTaggedProduct)
            }.andThen {
                lastTaggedProduct.state.isSuccess()
                lastTaggedProduct.products.assertEqualTo(pagedData.dataList)
                lastTaggedProduct.nextCursor.assertEqualTo(nextCursor)
            }
        }
    }

    @Test
    fun `when user load last tagged product & failed, it should emit failed state`() {
        coEvery { mockRepo.getLastTaggedProducts(any(), any(), any(), any()) } throws mockException

        val robot = ProductTagViewModelRobot(
            dispatcher = testDispatcher,
            repo = mockRepo,
        )

        robot.use {
            it.recordState {
                submitAction(ProductTagAction.LoadLastTaggedProduct)
            }.andThen {
                lastTaggedProduct.state.assertError(mockException)
            }
        }
    }

    @Test
    fun `when user load last tagged next page, it should emit success state along with appended products`() {
        val nextCursor1 = "2"
        val pagedData1 = modelBuilder.buildPagedDataModel(nextCursor = nextCursor1)

        val nextCursor2 = "3"
        val pagedData2 = modelBuilder.buildPagedDataModel(nextCursor = nextCursor2)

        val robot = ProductTagViewModelRobot(
            dispatcher = testDispatcher,
            repo = mockRepo,
        )

        robot.use {
            /** Setup: Load First Page */
            coEvery { mockRepo.getLastTaggedProducts(any(), any(), any(), any()) } returns pagedData1

            it.recordState {
                submitAction(ProductTagAction.LoadLastTaggedProduct)
            }.andThen {
                lastTaggedProduct.state.isSuccess()
                lastTaggedProduct.products.assertEqualTo(pagedData1.dataList)
                lastTaggedProduct.nextCursor.assertEqualTo(nextCursor1)
            }

            /** Test: Load Next Page */
            coEvery { mockRepo.getLastTaggedProducts(any(), any(), any(), any()) } returns pagedData2

            it.recordState {
                submitAction(ProductTagAction.LoadLastTaggedProduct)
            }.andThen {
                lastTaggedProduct.state.isSuccess()
                lastTaggedProduct.products.assertEqualTo(pagedData1.dataList + pagedData2.dataList)
                lastTaggedProduct.nextCursor.assertEqualTo(nextCursor2)
            }
        }
    }

    @Test
    fun `when user load last tagged next page but it has no next page, it shouldnt do anything`() {
        val nextCursor = "1"
        val pagedData = modelBuilder.buildPagedDataModel(nextCursor = nextCursor, hasNextPage = false)

        coEvery { mockRepo.getLastTaggedProducts(any(), any(), any(), any()) } returns pagedData

        val robot = ProductTagViewModelRobot(
            dispatcher = testDispatcher,
            repo = mockRepo,
        )

        robot.use {
            it.recordState {
                submitAction(ProductTagAction.LoadLastTaggedProduct)
            }.andThen {
                lastTaggedProduct.state.isSuccess()
                lastTaggedProduct.products.assertEqualTo(pagedData.dataList)
                lastTaggedProduct.nextCursor.assertEqualTo(nextCursor)
            }

            it.recordState {
                submitAction(ProductTagAction.LoadLastTaggedProduct)
            }.andThen {
                lastTaggedProduct.state.isSuccess()
                lastTaggedProduct.products.assertEqualTo(pagedData.dataList)
                lastTaggedProduct.nextCursor.assertEqualTo(nextCursor)
            }
        }
    }
}