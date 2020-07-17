package com.tokopedia.vouchercreation.create.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.vouchercreation.create.domain.model.ShopInfo
import com.tokopedia.vouchercreation.create.domain.usecase.BasicShopInfoUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class PromotionBudgetAndTypeViewModelTest {

    @RelaxedMockK
    lateinit var basicShopInfoUseCase: BasicShopInfoUseCase

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @After
    fun cleanup() {
        testDispatcher.cleanupTestCoroutines()
    }

    private val testDispatcher by lazy {
        TestCoroutineDispatcher()
    }

    private val mViewModel by lazy {
        PromotionBudgetAndTypeViewModel(testDispatcher, basicShopInfoUseCase, userSession)
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

        mViewModel.coroutineContext[Job]?.children?.forEach { it.join() }

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

        mViewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            basicShopInfoUseCase.executeOnBackground()
        }

        assert(mViewModel.basicShopInfoLiveData.value is Fail)
    }

}