package com.tokopedia.campaignlist.page.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.campaignlist.common.data.model.response.GetCampaignListV2Response
import com.tokopedia.campaignlist.common.data.model.response.GetMerchantCampaignBannerGeneratorData
import com.tokopedia.campaignlist.common.data.model.response.GetMerchantCampaignBannerGeneratorDataResponse
import com.tokopedia.campaignlist.common.data.model.response.GetSellerCampaignSellerAppMetaResponse
import com.tokopedia.campaignlist.common.usecase.GetCampaignListUseCase
import com.tokopedia.campaignlist.common.usecase.GetMerchantBannerUseCase
import com.tokopedia.campaignlist.common.usecase.GetSellerMetaDataUseCase
import com.tokopedia.campaignlist.common.util.ResourceProvider
import com.tokopedia.campaignlist.page.presentation.model.ActiveCampaign
import com.tokopedia.campaignlist.page.presentation.model.CampaignTypeSelection
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt

class CampaignListViewModelTest {

    @RelaxedMockK
    lateinit var getCampaignListUseCase: GetCampaignListUseCase

    @RelaxedMockK
    lateinit var getMerchantBannerUseCase: GetMerchantBannerUseCase

    @RelaxedMockK
    lateinit var getSellerMetaDataUseCase: GetSellerMetaDataUseCase

    @RelaxedMockK
    lateinit var getCampaignListObserver: Observer<in Result<GetCampaignListV2Response>>

    @RelaxedMockK
    lateinit var resourceProvider: ResourceProvider

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val viewModel by lazy {
        CampaignListViewModel(
            resourceProvider,
            CoroutineTestDispatchersProvider,
            getCampaignListUseCase,
            getMerchantBannerUseCase,
            getSellerMetaDataUseCase
        )
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel.getCampaignListResult.observeForever(getCampaignListObserver)
    }

    @Test
    fun `When get campaign list success, should emit success to observer`() = runBlocking {
        coEvery { getCampaignListUseCase.executeOnBackground() } returns GetCampaignListV2Response()

        viewModel.getCampaignList()

        assert(viewModel.getCampaignListResult.value is Success)
    }

    @Test
    fun `When get campaign list error, should emit error to observer`() = runBlocking {
        val error = MessageErrorException("Some error message")

        coEvery { getCampaignListUseCase.executeOnBackground() } throws error

        viewModel.getCampaignList()

        assert(viewModel.getCampaignListResult.value is Fail)
    }

    @Test
    fun `When get seller banner success, should emit success to observer`() = runBlocking {
        coEvery { getMerchantBannerUseCase.executeOnBackground() } returns GetMerchantCampaignBannerGeneratorDataResponse()

        viewModel.getSellerBanner(anyInt())

        assert(viewModel.getMerchantBannerResult.value is Success)
    }

    @Test
    fun `When get seller banner error, should emit error to observer`() = runBlocking {
        val error = MessageErrorException("Some error message")

        coEvery { getMerchantBannerUseCase.executeOnBackground() } throws error

        viewModel.getSellerBanner(anyInt())

        assert(viewModel.getMerchantBannerResult.value is Fail)
    }

    @Test
    fun `When get seller metadata success, should emit success to observer`() = runBlocking {
        coEvery { getSellerMetaDataUseCase.executeOnBackground() } returns GetSellerCampaignSellerAppMetaResponse()

        viewModel.getSellerMetaData()

        assert(viewModel.getSellerMetaDataResult.value is Success)
    }

    @Test
    fun `When get seller metadata error, should emit error to observer`() = runBlocking {
        val error = MessageErrorException("Some error message")

        coEvery { getSellerMetaDataUseCase.executeOnBackground() } throws error

        viewModel.getSellerMetaData()

        assert(viewModel.getSellerMetaDataResult.value is Fail)
    }

    @Test
    fun `When set campaign name, should set correct campaign name`() = runBlocking {
        val campaignName = "Flash Sale 12.12"
        val expected = "Flash Sale 12.12"

        viewModel.setCampaignName(campaignName)

        val actual = viewModel.getCampaignName()

        assertEquals(expected, actual)
    }

    @Test
    fun `When set campaign type id, should set correct campaign type`() = runBlocking {
        val campaignType = 20
        val expected = 20

        viewModel.setCampaignTypeId(campaignType)

        val actual = viewModel.getCampaignTypeId()

        assertEquals(expected, actual)
    }

    @Test
    fun `When set campaign status, should set correct campaign status`() = runBlocking {
        val campaignType = listOf(5,6,7)
        val expected = listOf(5,6,7)

        viewModel.setCampaignStatusId(campaignType)

        val actual = viewModel.getCampaignStatusId()

        assertEquals(expected, actual)
        assertEquals(expected.size, actual.size)
    }


    @Test
    fun `When set active campaign, should set correct active campaign`() = runBlocking {
        val activeCampaign = ActiveCampaign(campaignName = "Flash Sale 12.12")
        val expected = ActiveCampaign(campaignName = "Flash Sale 12.12")

        viewModel.setSelectedActiveCampaign(activeCampaign)

        val actual = viewModel.getSelectedActiveCampaign()

        assertEquals(expected, actual)
    }

    @Test
    fun `When set default campaign type, when we get default campaign type should return only selected campaign type`() = runBlocking {
        val selectedCampaignType = listOf(
            CampaignTypeSelection(campaignTypeName = "Flash Sale 11.11", isSelected = true),
            CampaignTypeSelection(campaignTypeName = "Flash Sale 12.12", isSelected = false)
        )

        val expected = CampaignTypeSelection(campaignTypeName = "Flash Sale 11.11", isSelected = true)

        viewModel.setDefaultCampaignTypeSelection(selectedCampaignType)

        val actual = viewModel.getSelectedCampaignTypeSelection()

        assertEquals(expected, actual)
    }

    @Test
    fun `When set default campaign type and there are no selected campaign type, should return null`() = runBlocking {
        val selectedCampaignType = listOf(
            CampaignTypeSelection(campaignTypeName = "Flash Sale 11.11", isSelected = false),
            CampaignTypeSelection(campaignTypeName = "Flash Sale 12.12", isSelected = false)
        )

        val expected = null

        viewModel.setDefaultCampaignTypeSelection(selectedCampaignType)

        val actual = viewModel.getSelectedCampaignTypeSelection()

        assertEquals(expected, actual)
    }

    @Test
    fun `When set merchant banner, should set correct merchant banner`() = runBlocking {
        val merchantBanner = GetMerchantCampaignBannerGeneratorData()
        val expected = GetMerchantCampaignBannerGeneratorData()

        viewModel.setMerchantBannerData(merchantBanner)

        val actual = viewModel.getMerchantBannerData()

        assertEquals(expected, actual)
    }


    @After
    fun tearDown() {
        viewModel.getCampaignListResult.removeObserver(getCampaignListObserver)
    }
}