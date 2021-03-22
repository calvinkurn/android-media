package com.tokopedia.payment.setting.detail.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.payment.setting.detail.domain.GQLDeleteCreditCardQueryUseCase
import com.tokopedia.payment.setting.detail.model.DataResponseDeleteCC
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DetailCreditCardViewModelTest {

    private val usecase = mockk<GQLDeleteCreditCardQueryUseCase>(relaxed = true)
    private val dispatcher = TestCoroutineDispatcher()
    private lateinit var viewModel: DetailCreditCardViewModel
    private var observer = mockk<Observer<Result<DataResponseDeleteCC>>>(relaxed = true)


    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        viewModel = DetailCreditCardViewModel(usecase,
                dispatcher)
    }

    @Test
    fun `deleteCreditCard fail`() {
        val mockThrowable = mockk<Throwable>("fail")
        coEvery { usecase.deleteCreditCard(any(), any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.deleteCreditCard("12345")
        assert(viewModel.creditCardDeleteResultLiveData.value is Fail)
    }


    @Test
    fun `deleteCreditCard success`() {
        val data = mockk<DataResponseDeleteCC>()
        coEvery { usecase.deleteCreditCard(any(), any(), any()) } answers {
            firstArg<(DataResponseDeleteCC) -> Unit>().invoke(data)
        }
        viewModel.deleteCreditCard("12345")
        assert(viewModel.creditCardDeleteResultLiveData.value is Success)
    }
}