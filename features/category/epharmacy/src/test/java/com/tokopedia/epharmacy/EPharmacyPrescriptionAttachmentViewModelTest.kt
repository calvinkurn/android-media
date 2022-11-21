package com.tokopedia.epharmacy

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common_epharmacy.network.response.EPharmacyPrepareProductsGroupResponse
import com.tokopedia.common_epharmacy.usecase.EPharmacyPrepareProductsGroupUseCase
import com.tokopedia.epharmacy.viewmodel.EPharmacyPrescriptionAttachmentViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class EPharmacyPrescriptionAttachmentViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val ePharmacyPrepareProductsGroupUseCase = mockk<EPharmacyPrepareProductsGroupUseCase>(relaxed = true)

    private val dispatcherBackground = TestCoroutineDispatcher()
    private lateinit var viewModel: EPharmacyPrescriptionAttachmentViewModel

    private val fetchFailedErrorMessage = "Fetch Failed"
    private val mockThrowable = Throwable(message = fetchFailedErrorMessage)

    @Before
    fun setUp() {
        viewModel = EPharmacyPrescriptionAttachmentViewModel(
            ePharmacyPrepareProductsGroupUseCase,
            dispatcherBackground
        )
    }

    @Test
    fun getGroupsSuccessTest() {
        val response = mockk<EPharmacyPrepareProductsGroupResponse>(relaxed = true)

        coEvery {
            ePharmacyPrepareProductsGroupUseCase.getEPharmacyPrepareProductsGroup(any(), any())
        } coAnswers {
            firstArg<(EPharmacyPrepareProductsGroupResponse) -> Unit>().invoke(response)
        }
        viewModel.getPrepareProductGroup()
        assert(viewModel.productGroupLiveDataResponse.value is Success)
    }

    @Test
    fun `get groups fetch success but condition fail`() {
        val response = mockk<EPharmacyPrepareProductsGroupResponse>(relaxed = true)
        coEvery {
            ePharmacyPrepareProductsGroupUseCase.getEPharmacyPrepareProductsGroup(any(), any())
        } coAnswers {
            firstArg<(EPharmacyPrepareProductsGroupResponse) -> Unit>().invoke(response)
        }
        viewModel.getPrepareProductGroup()
        Assert.assertEquals(
            (viewModel.productGroupLiveDataResponse.value as Fail).throwable.localizedMessage,
            "Data invalid"
        )
    }

    @Test
    fun `get Groups fetch failed throws exception`() {
        coEvery {
            ePharmacyPrepareProductsGroupUseCase.getEPharmacyPrepareProductsGroup(any(), any())
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.getPrepareProductGroup()
        Assert.assertEquals(
            (viewModel.productGroupLiveDataResponse.value as Fail).throwable,
            mockThrowable
        )
    }
}
