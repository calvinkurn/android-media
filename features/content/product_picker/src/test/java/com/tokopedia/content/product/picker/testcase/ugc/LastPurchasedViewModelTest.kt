package com.tokopedia.content.product.picker.testcase.ugc

import com.tokopedia.content.product.picker.builder.ugc.CommonModelBuilder
import com.tokopedia.content.product.picker.builder.ugc.LastPurchasedModelBuilder
import com.tokopedia.content.product.picker.ugc.view.uimodel.action.ProductTagAction
import com.tokopedia.content.product.picker.util.assertEqualTo
import com.tokopedia.content.product.picker.util.ugc.andThen
import com.tokopedia.content.product.picker.util.ugc.assertError
import com.tokopedia.content.product.picker.util.ugc.isSuccess
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
    private val mockRepo: com.tokopedia.content.product.picker.ugc.domain.repository.ProductTagRepository = mockk(relaxed = true)

    private val commonModelBuilder = CommonModelBuilder()
    private val modelBuilder = LastPurchasedModelBuilder()

    private val mockException = commonModelBuilder.buildException()

    @Test
    fun `when user load last purchased product & success, it should emit success state`() {
        val coachmark = "This is coachmark"
        val isCoachmarkShown = true
        val pagedData = modelBuilder.buildUiModelList(coachmark = coachmark, isCoachmarkShown = isCoachmarkShown)

        coEvery { mockRepo.getLastPurchasedProducts(any(), any()) } returns pagedData

        val robot = com.tokopedia.content.product.picker.robot.ProductTagViewModelRobot(
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

        val robot = com.tokopedia.content.product.picker.robot.ProductTagViewModelRobot(
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
