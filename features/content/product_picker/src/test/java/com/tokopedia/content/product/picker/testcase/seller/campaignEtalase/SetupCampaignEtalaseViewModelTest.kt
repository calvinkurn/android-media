package com.tokopedia.content.product.picker.testcase.seller.campaignEtalase

import com.tokopedia.content.product.picker.builder.seller.CommonUiModelBuilder
import com.tokopedia.content.product.picker.builder.seller.ProductSetupUiModelBuilder
import com.tokopedia.content.product.picker.robot.ContentProductPickerSellerViewModelRobot
import com.tokopedia.content.product.picker.seller.domain.repository.ContentProductPickerSellerRepository
import com.tokopedia.content.product.picker.seller.domain.repository.ProductPickerSellerCommonRepository
import com.tokopedia.content.product.picker.util.assertEqualTo
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

/**
 * Created By : Jonathan Darwin on February 17, 2022
 */
internal class SetupCampaignEtalaseViewModelTest {

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers
    private val mockRepo: ContentProductPickerSellerRepository = mockk(relaxed = true)
    private val mockCommonRepo: ProductPickerSellerCommonRepository = mockk(relaxed = true)

    /** Mock Response */
    private val productSetupUiModelBuilder = ProductSetupUiModelBuilder()
    private val commonUiModelBuilder = CommonUiModelBuilder()

    private val mockCampaignList = productSetupUiModelBuilder.buildCampaignList()
    private val mockEtalaseList = productSetupUiModelBuilder.buildEtalaseList()

    private val mockException = commonUiModelBuilder.buildException()

    /** Campaign & Etalase */
    @Test
    fun `when user firstly create viewmodel, it will emit uiState with campaign and etalase list`() {

        coEvery { mockCommonRepo.getEtalaseList() } returns mockEtalaseList
        coEvery { mockCommonRepo.getCampaignList() } returns mockCampaignList

        val robot = ContentProductPickerSellerViewModelRobot(
            dispatchers = testDispatcher,
            repo = mockRepo,
            commonRepo = mockCommonRepo,
        )

        robot.use {
            val state = it.recordState {  }
            val campaignAndEtalase = state.campaignAndEtalase
            campaignAndEtalase.etalaseList.assertEqualTo(mockEtalaseList)
            campaignAndEtalase.campaignList.assertEqualTo(mockCampaignList)
        }
    }

    @Test
    fun `when user firstly create viewmodel and theres an error when loading campaign and etalase list, it will emit uiState with empty campaign and etalase`() {

        coEvery { mockCommonRepo.getEtalaseList() } throws mockException
        coEvery { mockCommonRepo.getCampaignList() } throws mockException

        val robot = ContentProductPickerSellerViewModelRobot(
            dispatchers = testDispatcher,
            repo = mockRepo,
            commonRepo = mockCommonRepo,
        )

        robot.use {
            val state = it.recordState {  }
            val campaignAndEtalase = state.campaignAndEtalase
            campaignAndEtalase.etalaseList.assertEqualTo(emptyList())
            campaignAndEtalase.campaignList.assertEqualTo(emptyList())
        }
    }
}
