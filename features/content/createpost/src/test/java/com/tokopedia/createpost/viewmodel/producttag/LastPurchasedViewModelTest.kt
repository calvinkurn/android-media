package com.tokopedia.createpost.viewmodel.producttag

import com.tokopedia.createpost.model.CommonModelBuilder
import com.tokopedia.createpost.model.LastPurchasedModelBuilder
import com.tokopedia.createpost.producttag.domain.repository.ProductTagRepository
import com.tokopedia.createpost.producttag.view.uimodel.action.ProductTagAction
import com.tokopedia.createpost.robot.ProductTagViewModelRobot
import com.tokopedia.createpost.util.*
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

/**
 * Created By : Jonathan Darwin on May 30, 2022
 */
class LastPurchasedViewModelTest {

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers
    private val mockRepo: ProductTagRepository = mockk(relaxed = true)

    private val commonModelBuilder = CommonModelBuilder()
    private val modelBuilder = LastPurchasedModelBuilder()

    private val mockException = commonModelBuilder.buildException()

    @Test
    fun `when user load last purchased product & success, it should emit success state`() {
        val coachmark = "This is coachmark"
        val isCoachmarkShown = true
        val pagedData = modelBuilder.buildUiModelList(coachmark = coachmark, isCoachmarkShown = isCoachmarkShown)

        coEvery { mockRepo.getLastPurchasedProducts(any(), any()) } returns pagedData

        val robot = ProductTagViewModelRobot(
            dispatcher = testDispatcher,
            repo = mockRepo,
        )

        robot.use {
            it.recordState {
                submitAction(ProductTagAction.LoadLastPurchasedProduct)
            }.andThen {
                lastPurchasedProduct.state.isSuccess()
                lastPurchasedProduct.products.assertEqualTo(pagedData.products)
                lastPurchasedProduct.coachmark.assertEqualTo(coachmark)
                lastPurchasedProduct.isCoachmarkShown.assertEqualTo(isCoachmarkShown)
            }
        }
    }

    @Test
    fun `when user load last purchased product & failed, it should emit failed state`() {
        coEvery { mockRepo.getLastPurchasedProducts(any(), any()) } throws mockException

        val robot = ProductTagViewModelRobot(
            dispatcher = testDispatcher,
            repo = mockRepo,
        )

        robot.use {
            it.recordState {
                submitAction(ProductTagAction.LoadLastPurchasedProduct)
            }.andThen {
                lastPurchasedProduct.state.assertError(mockException)
            }
        }
    }
}