package com.tokopedia.shop.setting.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.network.exception.ResponseErrorException
import com.tokopedia.shop.common.domain.interactor.AuthorizeAccessUseCase
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.setting.view.model.ShopSettingAccess
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
import javax.inject.Provider

@ExperimentalCoroutinesApi
class ShopPageSettingViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var userSessionInterface: UserSessionInterface

    @RelaxedMockK
    lateinit var getShopInfoUseCase: GQLGetShopInfoUseCase

    @RelaxedMockK
    lateinit var authorizeAccessUseCaseProvider: Provider<AuthorizeAccessUseCase>

    @RelaxedMockK
    lateinit var authorizeAccessUseCase: AuthorizeAccessUseCase

    private val dispatcherProvider by lazy {
        CoroutineTestDispatchersProvider
    }

    private val viewModel by lazy {
        ShopPageSettingViewModel(
                userSessionInterface,
                getShopInfoUseCase,
                authorizeAccessUseCaseProvider,
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
        everyGetProviderUseCase()
        every { userSessionInterface.isShopOwner } returns true
        coEvery { getShopInfoUseCase.executeOnBackground() } returns ShopInfo()
        everyCheckAdminShouldSuccess()
        viewModel.getShop(mockShopId, mockShopDomain, true)
        coVerify { getShopInfoUseCase.executeOnBackground() }
        assert(viewModel.shopInfoResp.value is Success)
    }

    @Test
    fun `check whether shopInfoResp post Fail value if getShop is called and response is error`() {
        val mockShopId = "shopId"
        val mockShopDomain = "domain"
        everyGetProviderUseCase()
        every { userSessionInterface.isShopOwner } returns true
        coEvery { getShopInfoUseCase.executeOnBackground() } throws Exception()
        everyCheckAdminShouldSuccess()
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

    @Test
    fun `check whether shop setting access post success value if user is shop owner`() {
        val mockShopId = "123"
        val mockShopDomain = "domain"
        every { userSessionInterface.isShopOwner } returns true
        coEvery { getShopInfoUseCase.executeOnBackground() } returns ShopInfo()
        viewModel.getShop(mockShopId, mockShopDomain, true)
        assert(viewModel.shopSettingAccessLiveData.value is Success)
    }

    @Test
    fun `check whether access related use cases is called if user is not shop owner`() {
        val mockShopId = "123"
        val mockShopDomain = "domain"
        everyGetProviderUseCase()
        every { userSessionInterface.isShopOwner } returns false
        coEvery { getShopInfoUseCase.executeOnBackground() } returns ShopInfo()
        everyCheckAdminShouldSuccess()

        viewModel.getShop(mockShopId, mockShopDomain, true)

        verifyAllCheckAdminUseCasesShouldBeCalled()
        assert(viewModel.shopSettingAccessLiveData.value is Success)
    }

    @Test
    fun `check whether shopSettingAccess post Success value if response is success`() {
        val mockShopId = "123"
        val mockShopDomain = "domain"
        val mockIsEligible = true
        everyGetProviderUseCase()
        every { userSessionInterface.isShopOwner } returns false
        coEvery { getShopInfoUseCase.executeOnBackground() } returns ShopInfo()
        everyCheckAdminShouldSuccess(mockIsEligible)

        viewModel.getShop(mockShopId, mockShopDomain, true)

        verifyAllCheckAdminUseCasesShouldBeCalled()
        assert(viewModel.shopSettingAccessLiveData.value == Success(
                ShopSettingAccess(
                        mockIsEligible, mockIsEligible, mockIsEligible, mockIsEligible, mockIsEligible, mockIsEligible
                )
        ))
    }

    @Test
    fun `check whether shopSettingAccess and shopInfoResp post Fail value if response is failed`() {
        val mockShopId = "123"
        val mockShopDomain = "domain"
        everyGetProviderUseCase()
        every { userSessionInterface.isShopOwner } returns false
        coEvery { getShopInfoUseCase.executeOnBackground() } returns ShopInfo()
        everyCheckAdminShouldFail()

        viewModel.getShop(mockShopId, mockShopDomain, true)

        verifyAllCheckAdminUseCasesShouldBeCalled()
        assert(viewModel.shopSettingAccessLiveData.value is Fail)
        assert(viewModel.shopInfoResp.value is Fail)
    }

    private fun everyGetProviderUseCase() {
        every {
            authorizeAccessUseCaseProvider.get()
        } returns authorizeAccessUseCase
    }
    
    private fun everyCheckAdminShouldSuccess(isEligible: Boolean = true) {
        coEvery { authorizeAccessUseCase.execute(any()) } returns isEligible
    }

    private fun everyCheckAdminShouldFail() {
        coEvery { authorizeAccessUseCase.execute(any()) } throws ResponseErrorException()
    }

    private fun verifyAllCheckAdminUseCasesShouldBeCalled() {
        coVerify {
            authorizeAccessUseCase.execute(any())
        }
    }


}