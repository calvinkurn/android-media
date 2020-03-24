package com.tokopedia.sellerhome.settings.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.sellerhome.settings.domain.usecase.GetAllShopInfoUseCase
import com.tokopedia.sellerhome.settings.view.uimodel.shopinfo.SettingShopInfoUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class OtherMenuViewModelTest {

    @RelaxedMockK
    lateinit var getAllShopInfoUseCase: GetAllShopInfoUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    private val mViewModel: OtherMenuViewModel by lazy {
        OtherMenuViewModel(Dispatchers.Unconfined, getAllShopInfoUseCase)
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
}