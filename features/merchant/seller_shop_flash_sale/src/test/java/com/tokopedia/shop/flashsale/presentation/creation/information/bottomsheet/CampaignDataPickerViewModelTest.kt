package com.tokopedia.shop.flashsale.presentation.creation.information.bottomsheet

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.flashsale.common.extension.advanceDayBy
import com.tokopedia.shop.flashsale.common.extension.dateOnly
import com.tokopedia.shop.flashsale.domain.entity.CampaignAttribute
import com.tokopedia.shop.flashsale.domain.entity.CampaignMeta
import com.tokopedia.shop.flashsale.domain.entity.CampaignUiModel
import com.tokopedia.shop.flashsale.domain.entity.Gradient
import com.tokopedia.shop.flashsale.domain.entity.GroupedCampaign
import com.tokopedia.shop.flashsale.domain.entity.enums.CampaignStatus
import com.tokopedia.shop.flashsale.domain.entity.enums.PaymentType
import com.tokopedia.shop.flashsale.domain.entity.enums.activeCampaignStatusIds
import com.tokopedia.shop.flashsale.domain.usecase.GetSellerCampaignAttributeUseCase
import com.tokopedia.shop.flashsale.domain.usecase.GetSellerCampaignListUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import java.util.Date

class CampaignDataPickerViewModelTest {

    @RelaxedMockK
    lateinit var getSellerCampaignListUseCase: GetSellerCampaignListUseCase

    @RelaxedMockK
    lateinit var getSellerCampaignAttributeUseCase: GetSellerCampaignAttributeUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val viewModel by lazy {
        CampaignDataPickerViewModel(
            CoroutineTestDispatchersProvider,
            getSellerCampaignListUseCase,
            getSellerCampaignAttributeUseCase
        )
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }


    //region getUpcomingCampaigns

    @Test
    fun `When get upcoming campaign success, observer should successfully receive the data`() =
        runBlocking {
            //Given
            val today = Date()
            val tomorrow = Date().advanceDayBy(days = 1)

            val firstCampaign = buildCampaignUiModel(campaignId = 100).copy(startDate = tomorrow)
            val secondCampaign = buildCampaignUiModel(campaignId = 200).copy(startDate = today)
            val thirdCampaign = buildCampaignUiModel(campaignId = 300).copy(startDate = tomorrow)
            val campaigns = listOf(firstCampaign, secondCampaign, thirdCampaign)

            val campaignMetadata = CampaignMeta(
                totalCampaign = 10,
                totalCampaignActive = 5,
                totalCampaignFinished = 5,
                campaigns = campaigns
            )

            val expected = Success(
                listOf(
                    GroupedCampaign(tomorrow.dateOnly(), count = 2),
                    GroupedCampaign(today.dateOnly(), count = 1)
                )
            )

            coEvery {
                getSellerCampaignListUseCase.execute(rows = 100, offset = 0, statusId = activeCampaignStatusIds)
            } returns campaignMetadata


            //When
            viewModel.getUpcomingCampaigns()

            //Then
            val actual = viewModel.campaigns.getOrAwaitValue()
            assertEquals(expected, actual)
        }



    @Test
    fun `When get upcoming campaign error, observer should receive error result`() =
        runBlocking {
            //Given
            val error = MessageErrorException("Server error")
            val expected = Fail(error)

            coEvery { getSellerCampaignListUseCase.execute(rows = 100, offset = 0, statusId = activeCampaignStatusIds) } throws error

            //When
            viewModel.getUpcomingCampaigns()

            //Then
            val actual = viewModel.campaigns.getOrAwaitValue()
            assertEquals(expected, actual)
        }

    //endregion

    //region getRemainingQuota
    @Test
    fun `When get remaining quota success, observer should successfully receive the data`() =
        runBlocking {
            //Given
            val remainingQuota = 5
            val campaignAttribute = CampaignAttribute(
                success = true,
                errorMessage = "",
                listOf(),
                remainingCampaignQuota = remainingQuota
            )
            val expected = Success(remainingQuota)

            coEvery {
                getSellerCampaignAttributeUseCase.execute(
                    month = anyInt(),
                    year = anyInt(),
                    vpsPackageId = 1
                )
            } returns campaignAttribute

            //When
            viewModel.getCampaignQuota(month = anyInt(), year = anyInt(), vpsPackageId = 1)

            //Then
            val actual = viewModel.campaignQuota.getOrAwaitValue()
            assertEquals(expected, actual)
        }

    @Test
    fun `When get remaining quota error, observer should receive error result`() =
        runBlocking {
            //Given
            val error = MessageErrorException("Server error")
            val expected = Fail(error)

            coEvery {
                getSellerCampaignAttributeUseCase.execute(
                    month = anyInt(),
                    year = anyInt(),
                    vpsPackageId = 1
                )
            } throws error

            //When
            viewModel.getCampaignQuota(month = anyInt(), year = anyInt(), vpsPackageId = 1)

            //Then
            val actual = viewModel.campaignQuota.getOrAwaitValue()
            assertEquals(expected, actual)
        }
    //endregion

    private fun buildCampaignUiModel(campaignId: Long): CampaignUiModel {
        return CampaignUiModel(
            campaignId,
            "",
            "",
            "",
            isCancellable = false,
            isShareable = false,
            0,
            "",
            "",
            CampaignStatus.ONGOING,
            false,
            CampaignUiModel.ProductSummary(0, 0, 0, 0, 0, 0),
            Date(),
            Date(),
            Gradient("", "", false),
            false,
            Date(),
            PaymentType.INSTANT,
            isUniqueBuyer = false,
            isCampaignRelation = false,
            listOf(),
            isCampaignRuleSubmit = false, 0,
            CampaignUiModel.ThematicInfo(0, 0, "", 0, ""),
            Date(),
            Date(),
            CampaignUiModel.PackageInfo(packageId = 1, packageName = "VPS Package Elite")
        )
    }
}
