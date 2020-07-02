package com.tokopedia.vouchercreation.voucherlist.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchercreation.voucherlist.domain.usecase.UpdateQuotaUseCase
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
import org.mockito.ArgumentMatchers.anyInt

@ExperimentalCoroutinesApi
class EditQuotaViewModelTest {

    @RelaxedMockK
    lateinit var updateQuotaUseCase: UpdateQuotaUseCase

    @RelaxedMockK
    lateinit var editQuotaSuccessObserver: Observer<in Result<Boolean>>

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        mViewModel.editQuotaSuccessLiveData.observeForever(editQuotaSuccessObserver)
    }

    @After
    fun cleanup() {
        testDispatcher.cleanupTestCoroutines()

        mViewModel.editQuotaSuccessLiveData.removeObserver(editQuotaSuccessObserver)
    }

    private val testDispatcher by lazy {
        TestCoroutineDispatcher()
    }

    private val mViewModel by lazy {
        EditQuotaViewModel(testDispatcher, updateQuotaUseCase)
    }

    @Test
    fun `success changing quota value`() = runBlocking {
        val dummyEditQuotaSuccess = true

        coEvery {
            updateQuotaUseCase.executeOnBackground()
        } returns dummyEditQuotaSuccess

        mViewModel.changeQuotaValue(anyInt(), anyInt())

        coVerify {
            updateQuotaUseCase.executeOnBackground()
        }

        assert(mViewModel.editQuotaSuccessLiveData.value == Success(dummyEditQuotaSuccess))
    }

    @Test
    fun `fail changing quota value`() = runBlocking {
        val dummyThrowable = MessageErrorException("")

        coEvery {
            updateQuotaUseCase.executeOnBackground()
        } throws dummyThrowable

        mViewModel.changeQuotaValue(anyInt(), anyInt())

        mViewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            updateQuotaUseCase.executeOnBackground()
        }

        assert(mViewModel.editQuotaSuccessLiveData.value is Fail)
    }

}