package com.tokopedia.content.common.viewmodel.producttag

import com.tokopedia.content.common.producttag.view.uimodel.ProductTagSource
import com.tokopedia.content.common.robot.ProductTagViewModelRobot
import com.tokopedia.content.common.util.andThen
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.content.common.util.assertEqualTo
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
        val robot = ProductTagViewModelRobot(
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
        val robot = ProductTagViewModelRobot(
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
        val robot = ProductTagViewModelRobot(
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