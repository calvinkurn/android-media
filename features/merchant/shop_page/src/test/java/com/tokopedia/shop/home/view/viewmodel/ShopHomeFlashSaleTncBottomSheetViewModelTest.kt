package com.tokopedia.shop.home.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.shop.home.data.model.ShopHomeCampaignNplTncModel
import com.tokopedia.shop.home.domain.GetShopHomeMerchantCampaignTncUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ShopHomeFlashSaleTncBottomSheetViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var getMerchantCampaignTncUseCase: GetShopHomeMerchantCampaignTncUseCase

    private val testCoroutineDispatcherProvider by lazy {
        CoroutineTestDispatchersProvider
    }
    private lateinit var viewModel: ShopHomeFlashSaleTncBottomSheetViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = ShopHomeFlashSaleTncBottomSheetViewModel(
            testCoroutineDispatcherProvider,
            getMerchantCampaignTncUseCase
        )
    }

    @Test
    fun `check whether flashSaleTncLiveData should post Success data`() {
        val mockCampaignId = "12345"
        coEvery { getMerchantCampaignTncUseCase.executeOnBackground() } returns ShopHomeCampaignNplTncModel()
        viewModel.getFlashSaleTermsAndConditions(mockCampaignId)
        assert(viewModel.flashSaleTnc.value is Success)
    }

    @Test
    fun `check whether flashSaleTncLiveData should post Fail data`() {
        val mockCampaignId = "12345"
        coEvery { getMerchantCampaignTncUseCase.executeOnBackground() } throws Throwable()
        viewModel.getFlashSaleTermsAndConditions(mockCampaignId)
        assert(viewModel.flashSaleTnc.value is Fail)
    }
}
