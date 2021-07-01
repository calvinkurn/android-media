package com.tokopedia.pms.bankaccount.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.pms.bankaccount.data.model.EditTransfer
import com.tokopedia.pms.bankaccount.domain.SaveAccountDetailUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ChangeBankAccountViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val saveAccountDetailUseCase = mockk<SaveAccountDetailUseCase>(relaxed = true)
    private val dispatcher = TestCoroutineDispatcher()
    private lateinit var viewModel: ChangeBankAccountViewModel


    private val fetchFailedErrorMessage = "Fetch Failed"
    private val nullDataErrorMessage = "NULL DATA"
    private val mockThrowable = Throwable(message = fetchFailedErrorMessage)


    @Before
    fun setUp() {
        viewModel = ChangeBankAccountViewModel(saveAccountDetailUseCase, dispatcher)
    }

    @Test
    fun `saveDetailAccount invoke failed`() {
        coEvery {
            saveAccountDetailUseCase.saveDetailAccount(
                any(), any(),
                "t1", "m1",
                "acc1", "accNo1", "notes1", "dest1"
            )
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.saveDetailAccount(
            "t1", "m1",
            "acc1", "accNo1", "notes1", "dest1"
        )
        assert(viewModel.saveDetailLiveData.value is Fail)

    }

    @Test
    fun `saveDetailAccount invoked successfully`() {
        val editTransfer = EditTransfer().also { it.isSuccess= true }
        coEvery {
            saveAccountDetailUseCase.saveDetailAccount(
                any(), any(),
                "t1", "m1",
                "acc1", "accNo1", "notes1", "dest1"
            )
        } coAnswers {
            firstArg<(EditTransfer) -> Unit>().invoke(editTransfer)
        }
        viewModel.saveDetailAccount(
            "t1", "m1",
            "acc1", "accNo1", "notes1", "dest1"
        )
        assert(viewModel.saveDetailLiveData.value is Success)
        Assertions.assertThat((viewModel.saveDetailLiveData.value as Success).data.isSuccess)
            .isEqualTo(true)

    }
}