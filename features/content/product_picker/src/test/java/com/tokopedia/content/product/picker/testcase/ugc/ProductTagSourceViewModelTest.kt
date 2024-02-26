package com.tokopedia.content.product.picker.testcase.ugc

import com.tokopedia.content.product.picker.ugc.view.uimodel.ProductTagSource
import com.tokopedia.content.product.picker.util.assertEqualTo
import com.tokopedia.content.product.picker.util.ugc.andThen
import com.tokopedia.unit.test.rule.CoroutineTestRule
import org.junit.Rule
import org.junit.Test

/**
 * Created By : Jonathan Darwin on May 31, 2022
 */
class ProductTagSourceViewModelTest {

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers

    @Test
    fun `when there is a product tag source raw, the list of source should be not empty`() {
        val sources = listOf(
            ProductTagSource.GlobalSearch,
            ProductTagSource.LastPurchase,
            ProductTagSource.MyShop,
        )
        val robot = com.tokopedia.content.product.picker.robot.ProductTagViewModelRobot(
                productTagSourceRaw = sources.joinToString(separator = ",") { it.tag },
                dispatcher = testDispatcher,
        )

        robot.use {
            robot.recordState {  }.andThen {
                productTagSource.productTagSourceList.forEachIndexed { index, e ->
                    e.assertEqualTo(sources[index])
                }
            }
        }
    }

    @Test
    fun `when there is no product tag source raw, the list of source should be unknown`() {
        val robot = com.tokopedia.content.product.picker.robot.ProductTagViewModelRobot(
                productTagSourceRaw = "",
                dispatcher = testDispatcher,
        )

        robot.use {
            robot.recordState {  }.andThen {
                productTagSource.productTagSourceList[0].assertEqualTo(ProductTagSource.Unknown)
            }
        }
    }

    @Test
    fun `when there is unknown product tag source raw, the list of source should be unknown`() {
        val robot = com.tokopedia.content.product.picker.robot.ProductTagViewModelRobot(
                productTagSourceRaw = "random_source",
                dispatcher = testDispatcher,
        )

        robot.use {
            robot.recordState {  }.andThen {
                productTagSource.productTagSourceList[0].assertEqualTo(ProductTagSource.Unknown)
            }
        }
    }
}
