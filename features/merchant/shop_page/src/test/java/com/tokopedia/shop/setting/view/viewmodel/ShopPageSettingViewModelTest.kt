package com.tokopedia.shop.setting.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ShopPageSettingViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var userSessionInterface: UserSessionInterface

    @RelaxedMockK
    lateinit var getShopInfoUseCase: GQLGetShopInfoUseCase

    private val dispatcherProvider by lazy {
        CoroutineTestDispatchersProvider
    }

    private val viewModel by lazy {
        ShopPageSettingViewModel(
                userSessionInterface,
                getShopInfoUseCase,
                dispatcherProvider
        )
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `check whether shopInfoResp post Success value if getShop is called and response is success`() {
        val mockShopId = "123"
        val mockShopDomain = "domain"
        coEvery { getShopInfoUseCase.executeOnBackground() } returns ShopInfo()
        viewModel.getShop(mockShopId, mockShopDomain, true)
        coVerify { getShopInfoUseCase.executeOnBackground() }
        assert(viewModel.shopInfoResp.value is Success)
    }

    @Test
    fun `check whether shopInfoResp post Fail value if getShop is called and response is error`() {
        val mockShopId = "shopId"
        val mockShopDomain = "domain"
        coEvery { getShopInfoUseCase.executeOnBackground() } throws Exception()
        viewModel.getShop(mockShopId, mockShopDomain, false)
        coVerify { getShopInfoUseCase.executeOnBackground() }
        assert(viewModel.shopInfoResp.value is Fail)
    }

    @Test
    fun `check whether shopInfoResp value is null if getShop is called without passing parameter`() {
        viewModel.getShop()
        assert(viewModel.shopInfoResp.value == null)
    }

    @Test
    fun `check whether isMyShop return true if shopId match`() {
        val mockShopId = "123"
        every { userSessionInterface.shopId } returns mockShopId
        assert(viewModel.isMyShop(mockShopId))
    }

    @Test
    fun `check whether isMyShop return false if shopId doesn't match`() {
        val mockShopId = "123"
        every { userSessionInterface.shopId } returns ""
        assert(!viewModel.isMyShop(mockShopId))
    }

}