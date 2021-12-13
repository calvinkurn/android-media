package com.tokopedia.campaignlist.page.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.campaignlist.common.data.model.response.GetCampaignListV2Response
import com.tokopedia.campaignlist.common.data.model.response.GetMerchantCampaignBannerGeneratorDataResponse
import com.tokopedia.campaignlist.common.data.model.response.GetSellerCampaignSellerAppMetaResponse
import com.tokopedia.campaignlist.common.usecase.GetCampaignListUseCase
import com.tokopedia.campaignlist.common.usecase.GetMerchantBannerUseCase
import com.tokopedia.campaignlist.common.usecase.GetSellerMetaDataUseCase
import com.tokopedia.campaignlist.common.util.FakeResourceProvider
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
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

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val viewModel by lazy {
        CampaignListViewModel(
            FakeResourceProvider(),
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

    @After
    fun tearDown() {
        viewModel.getCampaignListResult.removeObserver(getCampaignListObserver)
    }
}