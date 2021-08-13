package com.tokopedia.withdraw.auto_withdrawal.presentation.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.withdraw.auto_withdrawal.domain.model.*
import com.tokopedia.withdraw.auto_withdrawal.domain.usecase.*
import com.tokopedia.withdraw.util.observeOnce
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AutoWDSettingsViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val autoWDStatusUseCase: AutoWDStatusUseCase = mockk(relaxed = true)
    private val autoWDInfoUseCase: AutoWDInfoUseCase = mockk(relaxed = true)
    private val bankAccountListUseCase: GQLBankAccountListUseCase = mockk(relaxed = true)
    private val autoWDTNCUseCase: AutoWDTNCUseCase = mockk(relaxed = true)
    private val autoWDUpsertUseCase: AutoWDUpsertUseCase = mockk(relaxed = true)

    private val dispatcher = TestCoroutineDispatcher()
    private lateinit var viewModel: AutoWDSettingsViewModel

    @Before
    fun setUp() {
        viewModel = AutoWDSettingsViewModel(
                autoWDStatusUseCase,
                autoWDInfoUseCase,
                bankAccountListUseCase,
                autoWDTNCUseCase,
                autoWDUpsertUseCase,
                dispatcher
        )
    }

    @Test
    fun `getAutoWDInfo fail`() {
        val result = mockk<Throwable>()
        coEvery {
            autoWDInfoUseCase.getAutoWDInfo(any(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(result)
        }
        viewModel.getAutoWDInfo()
        viewModel.infoAutoWDResultLiveData.observeOnce {
            assert(it is Fail)
        }
    }

    @Test
    fun `getAutoWDStatus fail`() {
        val result = mockk<Throwable>()
        coEvery {
            autoWDStatusUseCase.getAutoWDStatus(any(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(result)
        }
        viewModel.getAutoWDStatus()
        viewModel.autoWDStatusDataResultLiveData.observeOnce {
            assert(it is Fail)
        }
    }

    @Test
    fun `getAutoWDTNC fail`() {
        val result = mockk<Throwable>()
        coEvery {
            autoWDTNCUseCase.getAutoWDTNC(any(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(result)
        }
        viewModel.getAutoWDTNC()
        viewModel.autoWDTNCResultLiveData.observeOnce {
            assert(it is Fail)
        }
    }

    @Test
    fun `getBankAccount fail`() {
        val result = mockk<Throwable>()
        coEvery {
            bankAccountListUseCase.getBankAccountList(any(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(result)
        }
        viewModel.getBankAccount()
        viewModel.bankListResultLiveData.observeOnce {
            assert(it is Fail)
        }
    }

    @Test
    fun `upsertAutoWithdrawal fail`() {
        val data = mockk<AutoWithdrawalUpsertRequest>()
        val result = mockk<Throwable>()
        coEvery {
            autoWDUpsertUseCase.getAutoWDUpsert(any(), any(), any())
        } answers {
            thirdArg<(Throwable) -> Unit>().invoke(result)
        }
        viewModel.upsertAutoWithdrawal(data)
        viewModel.upsertResponseLiveData.observeOnce {
            assert(it is Fail)
        }
    }

    @Test
    fun `getAutoWDInfo success`() {
        val result = mockk<GetInfoAutoWD>()
        coEvery {
            autoWDInfoUseCase.getAutoWDInfo(any(), any())
        } answers {
            firstArg<(GetInfoAutoWD) -> Unit>().invoke(result)
        }
        viewModel.getAutoWDInfo()
        viewModel.infoAutoWDResultLiveData.observeOnce {
            assert(it is Success)
        }
    }

    @Test
    fun `getAutoWDStatus success`() {
        val resultData = mockk<AutoWDStatusData>()
        val result = AutoWDStatus(0, resultData, "")

        coEvery {
            autoWDStatusUseCase.getAutoWDStatus(any(), any())
        } answers {
            firstArg<(AutoWDStatus) -> Unit>().invoke(result)
        }
        viewModel.getAutoWDStatus()
        viewModel.autoWDStatusDataResultLiveData.observeOnce {
            assert(it is Success)
        }
    }

    @Test
    fun `getBankAccount success`() {
        val result = GqlGetBankDataResponse(GqlBankListResponse(0, "", arrayListOf()))
        coEvery {
            bankAccountListUseCase.getBankAccountList(any(), any())
        } answers {
            firstArg<(GqlGetBankDataResponse) -> Unit>().invoke(result)
        }
        viewModel.getBankAccount()
        viewModel.bankListResultLiveData.observeOnce {
            assert(it is Success)
        }
    }

    @Test
    fun `getAutoWDTNC success`() {
        val data = AWDTemplateData("template")
        val result = GetTNCAutoWD(0, data, "")

        coEvery {
            autoWDTNCUseCase.getAutoWDTNC(any(), any())
        } answers {
            firstArg<(GetTNCAutoWD) -> Unit>().invoke(result)
        }
        viewModel.getAutoWDTNC()
        viewModel.autoWDTNCResultLiveData.observeOnce {
            assert(it is Success)
        }
    }

    @Test
    fun `upsertAutoWithdrawal success`() {
        val data = mockk<AutoWithdrawalUpsertRequest>()
        val result = UpsertResponse(200, "")

        coEvery {
            autoWDUpsertUseCase.getAutoWDUpsert(any(), any(), any())
        } answers {
            secondArg<(UpsertResponse) -> Unit>().invoke(result)
        }
        viewModel.upsertAutoWithdrawal(data)
        viewModel.upsertResponseLiveData.observeOnce {
            assert(it is Success)
        }
    }
}