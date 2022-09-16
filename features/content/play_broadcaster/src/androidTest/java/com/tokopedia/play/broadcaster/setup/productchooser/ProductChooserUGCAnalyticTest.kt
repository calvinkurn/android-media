package com.tokopedia.play.broadcaster.setup.productchooser

import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.content.common.producttag.domain.repository.ProductTagRepository
import com.tokopedia.content.common.producttag.view.uimodel.LastPurchasedProductUiModel
import com.tokopedia.content.common.producttag.view.uimodel.PagedDataUiModel
import com.tokopedia.content.common.producttag.view.uimodel.PagedState
import com.tokopedia.content.common.producttag.view.uimodel.ProductUiModel
import com.tokopedia.play.broadcaster.helper.containsEventAction
import com.tokopedia.play.broadcaster.setup.productSetupViewModel
import com.tokopedia.play.broadcaster.setup.productUGCViewModel
import com.tokopedia.play.test.espresso.delay
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by kenny.hadisaputra on 16/09/22
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class ProductChooserUGCAnalyticTest {

    private val mockRepo: ProductTagRepository = mockk(relaxed = true)

    private val mockProducts = List(5) {
        ProductUiModel(
            id = it.toString(),
            name = "Product $it",
        )
    }

    @get:Rule
    var cassavaTestRule = CassavaTestRule(sendValidationResult = false)

    private val analyticFile = "tracker/content/playbroadcaster/play_bro_product_picker_ugc.json"

    init {
        coEvery { mockRepo.getLastTaggedProducts(any(), any(), any(), any()) } returns PagedDataUiModel(
            dataList = mockProducts,
            hasNextPage = false,
            nextCursor = "",
        )
    }

    private fun createRobot() = ProductChooserUGCRobot(
        viewModel = {
            productSetupViewModel(
                productSectionList = emptyList(),
                repo = mockk(relaxed = true),
            )
        },
        productTagViewModel = { source, config ->
            productUGCViewModel(
                productTagSourceRaw = source,
                productTagConfig = config,
                authorType = "content-user",
                repo = mockRepo,
            )
        }
    )


    @Test
    fun testAnalytic_clickProductSource() {
        val robot = createRobot()

        robot.selectProductSource()

        ViewMatchers.assertThat(
            cassavaTestRule.validate(analyticFile),
            containsEventAction("click - product tagging source")
        )
    }

    @Test
    fun testAnalytic_clickProductSourceOption_Tokopedia() {
        val robot = createRobot()

        robot.selectProductSource()
            .await(2000)
            .selectProductSourceOptionTokopedia()

        ViewMatchers.assertThat(
            cassavaTestRule.validate(analyticFile),
            containsEventAction("click - product source tokopedia")
        )
    }
}