package com.tokopedia.settingbank.view.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.settingbank.domain.model.Bank
import com.tokopedia.settingbank.domain.usecase.BankListUseCase
import com.tokopedia.settingbank.domain.usecase.SearchBankListUseCase
import com.tokopedia.settingbank.view.viewState.BankListState
import com.tokopedia.settingbank.view.viewState.OnBankListLoaded
import com.tokopedia.settingbank.view.viewState.OnBankListLoadingError
import com.tokopedia.settingbank.view.viewState.OnBankSearchResult
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SelectBankViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()


    private val bankListUseCase: dagger.Lazy<BankListUseCase> = mockk()
    private val searchBankUseCase: dagger.Lazy<SearchBankListUseCase> = mockk()

    private lateinit var viewModel: SelectBankViewModel

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        viewModel = spyk(SelectBankViewModel(bankListUseCase, searchBankUseCase,
                TestCoroutineDispatcher()))
    }

    @Test
    fun `loadBankList success`() {
        val result = OnBankListLoaded(arrayListOf())
        coEvery { bankListUseCase.get().getBankList(any()) }
                .coAnswers {
                    firstArg<(BankListState) -> Unit>().invoke(result)
                }
        viewModel.loadBankList()
        assert(viewModel.bankListState.value is OnBankListLoaded)
    }

    @Test
    fun `loadBankList Failed`() {
        val result = mockk<OnBankListLoadingError>()
        coEvery { bankListUseCase.get().getBankList(any()) }
                .coAnswers {
                    firstArg<(BankListState) -> Unit>().invoke(result)
                }
        viewModel.loadBankList()
        assert(viewModel.bankListState.value is OnBankListLoadingError)
    }

    @Test
    fun `searchBankByQuery success`() {
        val result = mockk<ArrayList<Bank>>()
        every { searchBankUseCase.get().cancelJobs() } returns Unit
        coEvery { searchBankUseCase.get().searchForBanks(any(), any(), any()) }
                .coAnswers {
                    thirdArg<(ArrayList<Bank>) -> Unit>().invoke(result)
                }
        viewModel.searchBankByQuery("")
        assert(viewModel.bankListState.value is OnBankSearchResult)
    }

    @Test
    fun `resetSearchResult success`() {
        val result = mockk<ArrayList<Bank>>()
        every { searchBankUseCase.get().cancelJobs() } returns Unit
        coEvery { searchBankUseCase.get().searchForBanks(any(), any(), any()) }
                .coAnswers {
                    thirdArg<(ArrayList<Bank>) -> Unit>().invoke(result)
                }
        viewModel.resetSearchResult()
        assert(viewModel.bankListState.value is OnBankSearchResult)
    }

}
