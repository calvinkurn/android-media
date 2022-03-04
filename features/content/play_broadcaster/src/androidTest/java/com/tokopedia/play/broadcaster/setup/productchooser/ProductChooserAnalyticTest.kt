package com.tokopedia.play.broadcaster.setup.productchooser

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.helper.containsPairOf
import com.tokopedia.play.broadcaster.setup.product.view.bottomsheet.ProductChooserBottomSheet
import com.tokopedia.play.broadcaster.setup.productSetupViewModel
import com.tokopedia.play.broadcaster.ui.model.paged.PagedDataUiModel
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by kenny.hadisaputra on 04/03/22
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class ProductChooserAnalyticTest {

    private val mockRepo: PlayBroadcastRepository = mockk(relaxed = true)

    @get:Rule
    var cassavaTestRule = CassavaTestRule(sendValidationResult = false)

    private val analyticFile = "tracker/content/playbroadcaster/play_bro_product_product_setup_analytic.json"

    init {
        coEvery { mockRepo.getProductsInEtalase(any(), any(), any(), any()) } returns PagedDataUiModel(
            dataList = emptyList(),
            hasNextPage = false,
        )
    }

    @Test
    fun testAnalytic_closeBottomSheet_withoutConfirmation() {
        val robot = ProductChooserRobot {
            productSetupViewModel(
                productSectionList = emptyList(),
                repo = mockRepo,
            )
        }

        robot.close()


        assertThat(
            cassavaTestRule.validate(analyticFile),
            containsPairOf(
                "eventAction" to "click - close button on product bottom sheet"
            )
        )
    }
}