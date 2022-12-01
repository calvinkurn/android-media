package com.tokopedia.epharmacy

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common_epharmacy.network.response.EPharmacyPrepareProductsGroupResponse
import com.tokopedia.common_epharmacy.usecase.EPharmacyPrepareProductsGroupUseCase
import com.tokopedia.epharmacy.network.params.InitiateConsultationParam
import com.tokopedia.epharmacy.network.response.EPharmacyConsultationDetails
import com.tokopedia.epharmacy.network.response.EPharmacyConsultationDetailsResponse
import com.tokopedia.epharmacy.network.response.EPharmacyInitiateConsultationResponse
import com.tokopedia.epharmacy.network.response.InitiateConsultation
import com.tokopedia.epharmacy.usecase.EPharmacyGetConsultationDetailsUseCase
import com.tokopedia.epharmacy.usecase.EPharmacyInitiateConsultationUseCase
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
    private val ePharmacyInitiateConsultationUseCase = mockk<EPharmacyInitiateConsultationUseCase>(relaxed = true)
    private val ePharmacyGetConsultationDetailsUseCase = mockk<EPharmacyGetConsultationDetailsUseCase>(relaxed = true)

    private val dispatcherBackground = TestCoroutineDispatcher()
    private lateinit var viewModel: EPharmacyPrescriptionAttachmentViewModel

    private val fetchFailedErrorMessage = "Fetch Failed"
    private val mockThrowable = Throwable(message = fetchFailedErrorMessage)

    @Before
    fun setUp() {
        viewModel = EPharmacyPrescriptionAttachmentViewModel(
            ePharmacyPrepareProductsGroupUseCase,
            ePharmacyInitiateConsultationUseCase,
            ePharmacyGetConsultationDetailsUseCase,
            dispatcherBackground
        )
    }

    @Test
    fun getGroupsSuccessTest() {
        val epharmacyGroup = mockk<EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup>(relaxed = true)
        val responseGroup = EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData("", "", arrayListOf(epharmacyGroup), null, null)
        val responseData = EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData(responseGroup)
        val response = EPharmacyPrepareProductsGroupResponse(responseData)
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

    @Test
    fun initiateConsultationSuccessTest() {
        val responseGroup = InitiateConsultation.InitiateConsultationData("231324", null)
        val responseData = InitiateConsultation(responseGroup)
        val response = EPharmacyInitiateConsultationResponse(responseData)
        coEvery {
            ePharmacyInitiateConsultationUseCase.initiateConsultation(any(), any(), any())
        } coAnswers {
            firstArg<(EPharmacyInitiateConsultationResponse) -> Unit>().invoke(response)
        }
        viewModel.initiateConsultation(InitiateConsultationParam(InitiateConsultationParam.InitiateConsultationParamInput("123")))
        assert(viewModel.initiateConsultation.value is Success)
    }

    @Test
    fun `initiation consultation success but condition fail`() {
        val response = mockk<EPharmacyInitiateConsultationResponse>(relaxed = true)
        coEvery {
            ePharmacyInitiateConsultationUseCase.initiateConsultation(any(), any(), any())
        } coAnswers {
            firstArg<(EPharmacyInitiateConsultationResponse) -> Unit>().invoke(response)
        }
        viewModel.initiateConsultation(InitiateConsultationParam(InitiateConsultationParam.InitiateConsultationParamInput("123")))
        Assert.assertEquals(
            (viewModel.initiateConsultation.value as Fail).throwable.localizedMessage,
            "Data invalid"
        )
    }

    @Test
    fun `initiate consultation fetch failed throws exception`() {
        coEvery {
            ePharmacyInitiateConsultationUseCase.initiateConsultation(any(), any(), any())
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.initiateConsultation(InitiateConsultationParam(InitiateConsultationParam.InitiateConsultationParamInput("123")))
        Assert.assertEquals(
            (viewModel.initiateConsultation.value as Fail).throwable,
            mockThrowable
        )
    }

    @Test
    fun getConsultationSuccessTest() {
        val responseGroup = EPharmacyConsultationDetails.EPharmacyConsultationDetailsData(mockk())
        val responseData = EPharmacyConsultationDetails(responseGroup)
        val response = EPharmacyConsultationDetailsResponse(responseData)
        coEvery {
            ePharmacyGetConsultationDetailsUseCase.getConsultationDetails(any(), any(), any())
        } coAnswers {
            firstArg<(EPharmacyConsultationDetailsResponse) -> Unit>().invoke(response)
        }
        viewModel.getConsultationDetails("2412")
        assert(viewModel.consultationDetails.value is Success)
    }

//    @Test
//    fun `get consultation details fetch failed throws exception`() {
//        coEvery {
//            ePharmacyGetConsultationDetailsUseCase.getConsultationDetails(any(), any(),"")
//        } coAnswers {
//            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
//        }
//        viewModel.getConsultationDetails("324")
//        Assert.assertEquals(
//            (viewModel.consultationDetails.value as Fail).throwable,
//            mockThrowable
//        )
//    }
}
