package com.tokopedia.play.broadcaster.shorts.viewmodel.preparation

import com.tokopedia.play.broadcaster.model.setup.product.ProductSetupUiModelBuilder
import com.tokopedia.play.broadcaster.shorts.robot.PlayShortsViewModelRobot
import com.tokopedia.play.broadcaster.shorts.ui.model.action.PlayShortsAction
import com.tokopedia.play.broadcaster.util.assertEqualTo
import org.junit.Test

/**
 * Created By : Jonathan Darwin on December 06, 2022
 */
class PlayShortsProductViewModelTest {

    private val productModelBuilder = ProductSetupUiModelBuilder()
    private val mockProducts = productModelBuilder.buildProductTagSectionList()

    @Test
    fun playShorts_preparation_product_setProduct() {
        val robot = PlayShortsViewModelRobot()

        robot.use {
            val state = it.recordState {
                submitAction(PlayShortsAction.SetProduct(mockProducts))
            }

            state.productSectionList.assertEqualTo(mockProducts)
        }
    }
}
