package com.tokopedia.epharmacy

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.common_epharmacy.network.response.EPharmacyPrepareProductsGroupResponse
import com.tokopedia.common_epharmacy.usecase.EPharmacyPrepareProductsGroupUseCase
import com.tokopedia.epharmacy.network.params.InitiateConsultationParam
import com.tokopedia.epharmacy.network.response.EPharmacyConsultationDetails
import com.tokopedia.epharmacy.network.response.EPharmacyConsultationDetailsResponse
import com.tokopedia.epharmacy.network.response.EPharmacyInitiateConsultationResponse
import com.tokopedia.epharmacy.network.response.InitiateConsultation
import com.tokopedia.epharmacy.usecase.EPharmacyGetConsultationDetailsUseCase
import com.tokopedia.epharmacy.usecase.EPharmacyInitiateConsultationUseCase
import com.tokopedia.epharmacy.utils.EPHARMACY_ANDROID_SOURCE
import com.tokopedia.epharmacy.utils.EPharmacyMiniConsultationToaster
import com.tokopedia.epharmacy.viewmodel.EPharmacyPrescriptionAttachmentViewModel
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.every
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
    private val updateCartUseCase = mockk<UpdateCartUseCase>(relaxed = true)

    private val dispatcherBackground = TestCoroutineDispatcher()
    private lateinit var viewModel: EPharmacyPrescriptionAttachmentViewModel

    private val fetchFailedErrorMessage = "Fetch Failed"
    private val mockThrowable = Throwable(message = fetchFailedErrorMessage)

    private val ePharmacyProduct = mockk<EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo.Product>(relaxed = true)
    private val ePharmacyProductsInfo = EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo("", arrayListOf(ePharmacyProduct), "23", "", "", "", "")
    private val ePharmacyGroup = EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup(null, EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ConsultationSource("abc", 1, mockk(), "", "", "", "", "", ""), "1", null, null, null, arrayListOf(ePharmacyProductsInfo), null, null)
    private val responseGroup = EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData(
        EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.CheckoutFlowEPharmacy(""), emptyList(),"Hi ", "test", arrayListOf(ePharmacyGroup), EPharmacyPrepareProductsGroupResponse.EPharmacyToaster("PRESCRIPTION_ATTACH_SUCCESS", "sfa", "1"), null)
    private val responseData = EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData(responseGroup)
    private val remoteConfig = mockk<FirebaseRemoteConfigImpl>(relaxed = true)

    @Before
    fun setUp() {
        viewModel = EPharmacyPrescriptionAttachmentViewModel(
            ePharmacyPrepareProductsGroupUseCase,
            ePharmacyInitiateConsultationUseCase,
            ePharmacyGetConsultationDetailsUseCase,
            updateCartUseCase,
            remoteConfig,
            dispatcherBackground
        )
    }

    @Test
    fun getGroupsSuccessTest() {
        every { remoteConfig.getString(any()) } returns ""
        val response = EPharmacyPrepareProductsGroupResponse(responseData)
        coEvery {
            ePharmacyPrepareProductsGroupUseCase.getEPharmacyPrepareProductsGroup(any(), any(), any(), any())
        } coAnswers {
            firstArg<(EPharmacyPrepareProductsGroupResponse, String?) -> Unit>().invoke(response, "")
        }
        viewModel.getPrepareProductGroup("", mutableMapOf())
        assert(viewModel.productGroupLiveDataResponse.value is Success)
        assert(viewModel.ePharmacyPrepareProductsGroupResponseData != null)
    }

    @Test
    fun `getGroupsSuccessTest button data`() {
        every { remoteConfig.getString(any()) } returns ""
        val response = EPharmacyPrepareProductsGroupResponse(responseData)
        coEvery {
            ePharmacyPrepareProductsGroupUseCase.getEPharmacyPrepareProductsGroup(any(), any(), any(), any())
        } coAnswers {
            firstArg<(EPharmacyPrepareProductsGroupResponse, String?) -> Unit>().invoke(response, "")
        }
        viewModel.getPrepareProductGroup("", mutableMapOf())
        assert(viewModel.productGroupLiveDataResponse.value is Success)
        assert(viewModel.buttonLiveData.value == response.detailData?.groupsData?.papPrimaryCTA)
        assert(viewModel.uploadError.value is EPharmacyMiniConsultationToaster)
    }

    @Test
    fun `getGroupsSuccessTest Data check`() {
        every { remoteConfig.getString(any()) } returns ""
        val response = EPharmacyPrepareProductsGroupResponse(responseData)
        coEvery {
            ePharmacyPrepareProductsGroupUseCase.getEPharmacyPrepareProductsGroup(any(), any(), any(), any())
        } coAnswers {
            firstArg<(EPharmacyPrepareProductsGroupResponse, String?) -> Unit>().invoke(response, "")
        }
        viewModel.getPrepareProductGroup("", mutableMapOf())
        assert(viewModel.productGroupLiveDataResponse.value is Success)
        assert(viewModel.ePharmacyPrepareProductsGroupResponseData != null)
        assert(viewModel.getEnablers().isNotEmpty())
        assert(viewModel.getShopIds().isNotEmpty())
        assert(viewModel.getShopIds("1").isNotEmpty())
        assert(viewModel.getGroupIds().isNotEmpty())
        assert(viewModel.findGroup("1") != null)
    }

    @Test
    fun `getGroupsSuccessTest Data to checkout`() {
        every { remoteConfig.getString(any()) } returns ""
        val response = EPharmacyPrepareProductsGroupResponse(responseData)
        coEvery {
            ePharmacyPrepareProductsGroupUseCase.getEPharmacyPrepareProductsGroup(any(), any(), any(), any())
        } coAnswers {
            firstArg<(EPharmacyPrepareProductsGroupResponse, String?) -> Unit>().invoke(response, "")
        }
        viewModel.getPrepareProductGroup("", mutableMapOf())
        assert(viewModel.productGroupLiveDataResponse.value is Success)
        assert(viewModel.ePharmacyPrepareProductsGroupResponseData != null)
        assert(viewModel.getResultForCheckout().isNotEmpty())
    }

    @Test
    fun `getGroupsSuccessTest Toaster Fail`() {
        every { remoteConfig.getString(any()) } returns ""
        val response = EPharmacyPrepareProductsGroupResponse(responseData)
        responseData.groupsData?.toaster?.type = "PRESCRIPTION_ATTACH_FAIL"
        coEvery {
            ePharmacyPrepareProductsGroupUseCase.getEPharmacyPrepareProductsGroup(any(), any(), any(), any())
        } coAnswers {
            firstArg<(EPharmacyPrepareProductsGroupResponse, String?) -> Unit>().invoke(response, "")
        }
        viewModel.getPrepareProductGroup("", mutableMapOf())
        assert(viewModel.productGroupLiveDataResponse.value is Success)
    }

    @Test
    fun `get groups fetch success but condition fail`() {
        val responseGroup = EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData(EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.CheckoutFlowEPharmacy(""),
            emptyList() ,"test", null, null, null,null)
        val response = EPharmacyPrepareProductsGroupResponse(EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData(responseGroup))
        coEvery {
            ePharmacyPrepareProductsGroupUseCase.getEPharmacyPrepareProductsGroup(any(), any(), any(), any())
        } coAnswers {
            firstArg<(EPharmacyPrepareProductsGroupResponse, String?) -> Unit>().invoke(response, "")
        }
        viewModel.getPrepareProductGroup("", mutableMapOf())
        Assert.assertEquals(
            (viewModel.productGroupLiveDataResponse.value as Fail).throwable.localizedMessage,
            "Data Invalid"
        )
    }

    @Test
    fun `get Groups fetch failed throws exception`() {
        coEvery {
            ePharmacyPrepareProductsGroupUseCase.getEPharmacyPrepareProductsGroup(any(), any(), any(), any())
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.getPrepareProductGroup("", mutableMapOf())
        Assert.assertEquals(
            (viewModel.productGroupLiveDataResponse.value as Fail).throwable,
            mockThrowable
        )
    }

    @Test
    fun initiateConsultationSuccessTest() {
        val responseGroup = InitiateConsultation.InitiateConsultationData(null, null)
        val responseData = InitiateConsultation(responseGroup)
        val response = EPharmacyInitiateConsultationResponse(responseData, "231324")
        coEvery {
            ePharmacyInitiateConsultationUseCase.initiateConsultation(any(), any(), any())
        } coAnswers {
            firstArg<(String, EPharmacyInitiateConsultationResponse) -> Unit>().invoke("", response)
        }
        viewModel.initiateConsultation(InitiateConsultationParam(InitiateConsultationParam.InitiateConsultationParamInput("123", EPHARMACY_ANDROID_SOURCE)))
        assert(viewModel.initiateConsultation.value is Success)
    }

    @Test
    fun `initiation consultation success but condition fail`() {
        val response = EPharmacyInitiateConsultationResponse(null, null)
        coEvery {
            ePharmacyInitiateConsultationUseCase.initiateConsultation(any(), any(), any())
        } coAnswers {
            firstArg<(String, EPharmacyInitiateConsultationResponse) -> Unit>().invoke("", response)
        }
        viewModel.initiateConsultation(InitiateConsultationParam(InitiateConsultationParam.InitiateConsultationParamInput("123", EPHARMACY_ANDROID_SOURCE)))
        Assert.assertEquals(
            (viewModel.initiateConsultation.value as Fail).throwable.localizedMessage,
            "Data Invalid"
        )
    }

    @Test
    fun `initiate consultation fetch failed throws exception`() {
        coEvery {
            ePharmacyInitiateConsultationUseCase.initiateConsultation(any(), any(), any())
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.initiateConsultation(InitiateConsultationParam(InitiateConsultationParam.InitiateConsultationParamInput("123", EPHARMACY_ANDROID_SOURCE)))
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
        viewModel.getConsultationDetails("40")
        assert(viewModel.consultationDetails.value is Success)
    }

    @Test
    fun `get consultation details fetch success but condition fail`() {
        val response = EPharmacyConsultationDetailsResponse(null)
        coEvery {
            ePharmacyGetConsultationDetailsUseCase.getConsultationDetails(any(), any(), any())
        } coAnswers {
            firstArg<(EPharmacyConsultationDetailsResponse) -> Unit>().invoke(response)
        }
        viewModel.getConsultationDetails("40")
        Assert.assertEquals(
            (viewModel.consultationDetails.value as Fail).throwable.localizedMessage,
            "Data Invalid"
        )
    }

    @Test
    fun `get consultation details fetch failed throws exception`() {
        coEvery {
            ePharmacyGetConsultationDetailsUseCase.getConsultationDetails(any(), any(), any())
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.getConsultationDetails("40")
        Assert.assertEquals(
            (viewModel.consultationDetails.value as Fail).throwable,
            mockThrowable
        )
    }

    @Test
    fun testGetShopIds() {
        // Test case 1: ePharmacyGroupId matches and contains shopIds
        val ePharmacyGroupId = "1"
        val expectedShopIds = listOf("23")

        val ePharmacyPrepareProductsGroupResponseData = responseData
        viewModel.ePharmacyPrepareProductsGroupResponseData = EPharmacyPrepareProductsGroupResponse(ePharmacyPrepareProductsGroupResponseData)
        val actualShopIds = viewModel.getShopIds(ePharmacyGroupId)

        Assert.assertEquals(expectedShopIds, actualShopIds)

        // Test case 2: ePharmacyGroupId matches, but does not contain shopIds
        val emptyShopIdsGroupId = "2"
        val expectedEmptyShopIds = emptyList<String>()

        val actualEmptyShopIds = viewModel.getShopIds(emptyShopIdsGroupId)

        Assert.assertEquals(expectedEmptyShopIds, actualEmptyShopIds)

        // Test case 3: ePharmacyGroupId does not match, should return an empty list
        val invalidGroupId = "invalid"
        val expectedEmptyList = emptyList<String>()

        val actualEmptyList = viewModel.getShopIds(invalidGroupId)

        Assert.assertEquals(expectedEmptyList, actualEmptyList)
    }
}
