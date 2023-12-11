package com.tokopedia.content.product.picker.testcase.filter

import com.tokopedia.content.product.picker.builder.ProductSetupUiModelBuilder
import com.tokopedia.content.product.picker.robot.ContentProductPickerSellerViewModelRobot
import com.tokopedia.content.product.picker.seller.domain.repository.ContentProductPickerSellerRepository
import com.tokopedia.content.product.picker.seller.model.uimodel.ProductSetupAction
import com.tokopedia.content.product.picker.seller.model.etalase.SelectedEtalaseModel
import com.tokopedia.content.product.picker.seller.model.sort.SortUiModel
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

/**
 * Created By : Jonathan Darwin on February 18, 2022
 */
internal class SetupFilterProductViewModelTest {

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers
    private val mockRepo: ContentProductPickerSellerRepository = mockk(relaxed = true)

    /** Mock Response */
    private val productSetupUiModelBuilder = ProductSetupUiModelBuilder()

    private val mockCampaign = productSetupUiModelBuilder.buildCampaignModel()
    private val mockEtalase = productSetupUiModelBuilder.buildEtalaseModel()

    @Test
    fun `when user set sorting, it will emit uiState with new loadParam`() {
        val mockSort = SortUiModel.supportedSortList.first()

        val robot = ContentProductPickerSellerViewModelRobot(
            dispatchers = testDispatcher,
            repo = mockRepo
        )

        robot.use {
            val state = robot.recordState {
                robot.submitAction(ProductSetupAction.SetSort(mockSort))
            }

            assertEquals(state.loadParam.sort, mockSort)
        }
    }

    @Test
    fun `when user search product with keyword, it should emit uiState with newest keyword`() {
        val keyword = "piring cantik"

        val robot = ContentProductPickerSellerViewModelRobot(
            dispatchers = testDispatcher,
            repo = mockRepo
        )

        robot.use {
            val state = robot.recordState {
                robot.submitAction(ProductSetupAction.SearchProduct(keyword))
            }

            assertEquals(state.loadParam.keyword, keyword)
        }
    }

    @Test
    fun `when user select etalase, it should emit uiState with new selected etalase`() {
        val mockSelectedEtalase = SelectedEtalaseModel.Etalase(mockEtalase)

        val robot = ContentProductPickerSellerViewModelRobot(
            dispatchers = testDispatcher,
            repo = mockRepo
        )

        robot.use {
            val state = robot.recordState {
                robot.submitAction(ProductSetupAction.SelectEtalase(mockEtalase))
            }

            assertEquals(state.loadParam.etalase, mockSelectedEtalase)
        }
    }

    @Test
    fun `when user select campaign, it should emit uiState with new selected campaign`() {
        val mockSelectedCampaign = SelectedEtalaseModel.Campaign(mockCampaign)

        val robot = ContentProductPickerSellerViewModelRobot(
            dispatchers = testDispatcher,
            repo = mockRepo
        )

        robot.use {
            val state = robot.recordState {
                robot.submitAction(ProductSetupAction.SelectCampaign(mockCampaign))
            }

            assertEquals(state.loadParam.etalase, mockSelectedCampaign)
        }
    }
}
