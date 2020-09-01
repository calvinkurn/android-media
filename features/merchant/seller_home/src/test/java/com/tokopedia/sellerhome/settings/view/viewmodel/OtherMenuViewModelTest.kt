package com.tokopedia.sellerhome.settings.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.sellerhome.common.viewmodel.NonNullLiveData
import com.tokopedia.sellerhome.settings.domain.usecase.GetAllShopInfoUseCase
import com.tokopedia.seller.menu.common.view.uimodel.shopinfo.SettingShopInfoUiModel
import com.tokopedia.sellerhome.utils.observeOnce
import com.tokopedia.shop.common.domain.interactor.GetShopFreeShippingInfoUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.spyk
import junit.framework.Assert.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.internal.util.reflection.Whitebox

@ExperimentalCoroutinesApi
class OtherMenuViewModelTest {

    @RelaxedMockK
    lateinit var getAllShopInfoUseCase: GetAllShopInfoUseCase

    @RelaxedMockK
    lateinit var getShopFreeShippingInfoUseCase: GetShopFreeShippingInfoUseCase

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    lateinit var remoteConfig: FirebaseRemoteConfigImpl

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    private val testDispatcher = TestCoroutineDispatcher()

    private val mViewModel: OtherMenuViewModel by lazy {
        OtherMenuViewModel(
            testDispatcher,
            getAllShopInfoUseCase,
            getShopFreeShippingInfoUseCase,
            userSession,
            remoteConfig
        )
    }

    @Test
    fun `success get all setting shop info data`() {
        val settingShopInfoSuccess = SettingShopInfoUiModel()

        coEvery {
            getAllShopInfoUseCase.executeOnBackground()
        } returns settingShopInfoSuccess

        mViewModel.getAllSettingShopInfo()

        coVerify {
            getAllShopInfoUseCase.executeOnBackground()
        }

        assert(mViewModel.settingShopInfoLiveData.value == Success(settingShopInfoSuccess))
    }

    @Test
    fun `error get setting shop info data`() {
        val throwable = ResponseErrorException()

        coEvery {
            getAllShopInfoUseCase.executeOnBackground()
        } throws throwable

        mViewModel.getAllSettingShopInfo()

        coVerify {
            getAllShopInfoUseCase.executeOnBackground()
        }

        assert(mViewModel.settingShopInfoLiveData.value == Fail(throwable))

    }

    @Test
    fun `Check delay will alter isToasterAlreadyShown between true and false`() = testDispatcher.runBlockingTest {

        val mockViewModel = spyk(mViewModel, recordPrivateCalls = true)

        mockViewModel.getAllSettingShopInfo(true)

        coVerify {
            mockViewModel["checkDelayErrorResponseTrigger"]()
        }

        testDispatcher.pauseDispatcher()

        mockViewModel.isToasterAlreadyShown.observeOnce {
            assertTrue(it)
        }

        testDispatcher.resumeDispatcher()

        mockViewModel.isToasterAlreadyShown.observeOnce {
            assertFalse(it)
        }
    }

    @Test
    fun `Toaster already shown will not alter values`() {
        val isToasterAlreadyShown = mViewModel.isToasterAlreadyShown.value
        mViewModel.getAllSettingShopInfo(false)
        assertEquals(isToasterAlreadyShown, mViewModel.isToasterAlreadyShown.value)
    }

    @Test
    fun `will not change live data value if toaster is already shown`() {
        Whitebox.setInternalState(mViewModel, "_isToasterAlreadyShown", NonNullLiveData(true))
        mViewModel.getAllSettingShopInfo(true)
        mViewModel.isToasterAlreadyShown.value?.let {
            assert(it)
        }
    }

    @Test
    fun `Setting status bar initial state should change the live data value`() {

        mViewModel.setIsStatusBarInitialState(isInitialState = true)

        val isStatusBarInitialState = mViewModel.isStatusBarInitialState.value

        assertNotNull(isStatusBarInitialState)
        isStatusBarInitialState?.let {
            assert(it)
        }

    }
}