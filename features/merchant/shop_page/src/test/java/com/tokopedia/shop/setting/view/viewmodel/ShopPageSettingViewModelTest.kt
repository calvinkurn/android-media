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

@ExperimentalCoroutinesApi
class ShopPageSettingViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var userSessionInterface: UserSessionInterface

    @RelaxedMockK
    lateinit var getShopInfoUseCase: GQLGetShopInfoUseCase

    @RelaxedMockK
    lateinit var authorizeAddressAccessUseCase: AuthorizeAccessUseCase

    @RelaxedMockK
    lateinit var authorizeEtalaseAccessUseCase: AuthorizeAccessUseCase

    @RelaxedMockK
    lateinit var authorizeNotesAccessUseCase: AuthorizeAccessUseCase

    @RelaxedMockK
    lateinit var authorizeInfoAccessUseCase: AuthorizeAccessUseCase

    @RelaxedMockK
    lateinit var authorizeShipmentAccessUseCase: AuthorizeAccessUseCase

    @RelaxedMockK
    lateinit var authorizeProductManageAccessUseCase: AuthorizeAccessUseCase

    private val dispatcherProvider by lazy {
        CoroutineTestDispatchersProvider
    }

    private val viewModel by lazy {
        ShopPageSettingViewModel(
                userSessionInterface,
                getShopInfoUseCase,
                authorizeAddressAccessUseCase,
                authorizeEtalaseAccessUseCase,
                authorizeNotesAccessUseCase,
                authorizeInfoAccessUseCase,
                authorizeShipmentAccessUseCase,
                authorizeProductManageAccessUseCase,
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
        every { userSessionInterface.isShopOwner } returns false
        coEvery { getShopInfoUseCase.executeOnBackground() } returns ShopInfo()

        viewModel.getShop(mockShopId, mockShopDomain, true)

        everyCheckAdminShouldSuccess()
        verifyAllCheckAdminUseCasesShouldBeCalled()
        assert(viewModel.shopSettingAccessLiveData.value is Success)
    }

    @Test
    fun `check whether shopSettingAccess post Success value if response is success`() {
        val mockShopId = "123"
        val mockShopDomain = "domain"
        val mockIsEligible = true
        every { userSessionInterface.isShopOwner } returns false
        coEvery { getShopInfoUseCase.executeOnBackground() } returns ShopInfo()
        everyCheckAdminShouldSuccess(mockIsEligible, mockIsEligible, mockIsEligible,
                mockIsEligible, mockIsEligible, mockIsEligible)

        viewModel.getShop(mockShopId, mockShopDomain, true)

        everyCheckAdminShouldSuccess()
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
        every { userSessionInterface.isShopOwner } returns false
        coEvery { getShopInfoUseCase.executeOnBackground() } returns ShopInfo()
        everyCheckAdminShouldFail()

        viewModel.getShop(mockShopId, mockShopDomain, true)

        everyCheckAdminShouldFail()
        verifyAllCheckAdminUseCasesShouldBeCalled()
        assert(viewModel.shopSettingAccessLiveData.value is Fail)
        assert(viewModel.shopInfoResp.value is Fail)
    }
    
    private fun everyCheckAdminShouldSuccess(
            addressRole: Boolean = true,
            etalaseRole: Boolean = true,
            notesRole: Boolean = true,
            infoRole: Boolean = true,
            shipmentRole: Boolean = true,
            productManageRole: Boolean = true
    ) {
        coEvery { authorizeAddressAccessUseCase.execute(any()) } returns addressRole
        coEvery { authorizeEtalaseAccessUseCase.execute(any()) } returns etalaseRole
        coEvery { authorizeNotesAccessUseCase.execute(any()) } returns notesRole
        coEvery { authorizeInfoAccessUseCase.execute(any()) } returns infoRole
        coEvery { authorizeShipmentAccessUseCase.execute(any()) } returns shipmentRole
        coEvery { authorizeProductManageAccessUseCase.execute(any()) } returns productManageRole
    }

    private fun everyCheckAdminShouldFail() {
        coEvery { authorizeAddressAccessUseCase.execute(any()) } throws ResponseErrorException()
        coEvery { authorizeEtalaseAccessUseCase.execute(any()) } throws ResponseErrorException()
        coEvery { authorizeNotesAccessUseCase.execute(any()) } throws ResponseErrorException()
        coEvery { authorizeInfoAccessUseCase.execute(any()) } throws ResponseErrorException()
        coEvery { authorizeShipmentAccessUseCase.execute(any()) } throws ResponseErrorException()
        coEvery { authorizeProductManageAccessUseCase.execute(any()) } throws ResponseErrorException()
    }

    private fun verifyAllCheckAdminUseCasesShouldBeCalled() {
        coVerify {
            authorizeAddressAccessUseCase.execute(any())
            authorizeEtalaseAccessUseCase.execute(any())
            authorizeNotesAccessUseCase.execute(any())
            authorizeInfoAccessUseCase.execute(any())
            authorizeShipmentAccessUseCase.execute(any())
            authorizeProductManageAccessUseCase.execute(any())
        }
    }


}