package com.tokopedia.vouchercreation.create.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.vouchercreation.common.domain.usecase.BasicShopInfoUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.vouchercreation.create.domain.model.ShopInfo
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class PromotionBudgetAndTypeViewModelTest {

    @RelaxedMockK
    lateinit var basicShopInfoUseCase: BasicShopInfoUseCase

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    lateinit var mViewModel: PromotionBudgetAndTypeViewModel

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mViewModel = PromotionBudgetAndTypeViewModel(CoroutineTestDispatchersProvider, basicShopInfoUseCase, userSession)
    }

    @Test
    fun `success get basic shop info`() = runBlocking {
        val dummySuccessShopInfo = ShopInfo()

        coEvery {
            basicShopInfoUseCase.executeOnBackground()
        } returns dummySuccessShopInfo
        coEvery {
            userSession.userId
        } returns "1"

        mViewModel.getBasicShopInfo()

        coVerify {
            basicShopInfoUseCase.executeOnBackground()
        }

        assert(mViewModel.basicShopInfoLiveData.value == Success(dummySuccessShopInfo))
    }

    @Test
    fun `fail get basic shop info`() = runBlocking {
        val dummyThrowable = MessageErrorException("")

        coEvery {
            basicShopInfoUseCase.executeOnBackground()
        } throws dummyThrowable
        coEvery {
            userSession.userId
        } returns "1"

        mViewModel.getBasicShopInfo()

        coVerify {
            basicShopInfoUseCase.executeOnBackground()
        }

        assert(mViewModel.basicShopInfoLiveData.value is Fail)
    }

}