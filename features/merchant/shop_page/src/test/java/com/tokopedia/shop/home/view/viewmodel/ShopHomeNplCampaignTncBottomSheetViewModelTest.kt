package com.tokopedia.shop.home.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.shop.home.data.model.ShopHomeCampaignNplTncModel
import com.tokopedia.shop.home.domain.GetShopHomeCampaignNplTncUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.util.TestCoroutineDispatcherProviderImpl
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ShopHomeNplCampaignTncBottomSheetViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface
    @RelaxedMockK
    lateinit var getCampaignNplTncUseCase: GetShopHomeCampaignNplTncUseCase

    private val dispatcherProvider = TestCoroutineDispatcherProviderImpl

    private lateinit var viewModel: ShopHomeNplCampaignTncBottomSheetViewModel


    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = ShopHomeNplCampaignTncBottomSheetViewModel(
                dispatcherProvider,
                userSession,
                getCampaignNplTncUseCase
        )
    }

    @Test
    fun `check whether userSessionShopId return same value as mocked value`() {
        val sampleShopId = "1234"
        every { userSession.shopId } returns sampleShopId
        assert(viewModel.userSessionShopId == sampleShopId)
    }

    @Test
    fun `check whether userSessionShopId return empty string when userSession shopId return null`() {
        every { userSession.shopId } returns null
        assert(viewModel.userSessionShopId == "")
    }

    @Test
    fun `check whether _campaignTncLiveData post success data`() = runBlocking {
        val sampleCampaignId = "1234"
        coEvery { getCampaignNplTncUseCase.executeOnBackground() } returns ShopHomeCampaignNplTncModel()
        viewModel.getTnc(sampleCampaignId)
        coVerify { getCampaignNplTncUseCase.executeOnBackground() }
        assert(viewModel.campaignTncLiveData.value is Success)
    }

    @Test
    fun `check whether _campaignTncLiveData post fail data`() = runBlocking {
        val sampleCampaignId = "1234"
        coEvery { getCampaignNplTncUseCase.executeOnBackground() } throws Throwable()
        viewModel.getTnc(sampleCampaignId)
        coVerify { getCampaignNplTncUseCase.executeOnBackground() }
        assert(viewModel.campaignTncLiveData.value is Fail)
    }


}