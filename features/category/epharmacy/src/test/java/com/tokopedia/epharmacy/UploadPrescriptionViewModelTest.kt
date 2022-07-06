package com.tokopedia.epharmacy

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.epharmacy.network.response.EPharmacyDataResponse
import com.tokopedia.epharmacy.network.response.EPharmacyProduct
import com.tokopedia.epharmacy.network.response.EPharmacyUploadPrescriptionIdsResponse
import com.tokopedia.epharmacy.network.response.PrescriptionImage
import com.tokopedia.epharmacy.usecase.GetEPharmacyCheckoutDetailUseCase
import com.tokopedia.epharmacy.usecase.GetEPharmacyOrderDetailUseCase
import com.tokopedia.epharmacy.usecase.PostPrescriptionIdUseCase
import com.tokopedia.epharmacy.usecase.UploadPrescriptionUseCase
import com.tokopedia.epharmacy.utils.UPLOAD_CHECKOUT_ID_KEY
import com.tokopedia.epharmacy.viewmodel.UploadPrescriptionViewModel
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
class UploadPrescriptionViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getEPharmacyOrderDetailUseCase = mockk<GetEPharmacyOrderDetailUseCase>(relaxed = true)
    private val getEPharmacyCheckoutDetailUseCase = mockk<GetEPharmacyCheckoutDetailUseCase>(relaxed = true)
    private val uploadPrescriptionUseCase = mockk<UploadPrescriptionUseCase>(relaxed = true)
    private val postPrescriptionIdUseCase = mockk<PostPrescriptionIdUseCase>(relaxed = true)

    private val dispatcherMain = TestCoroutineDispatcher()
    private val dispatcherBackground = TestCoroutineDispatcher()
    private lateinit var viewModel: UploadPrescriptionViewModel

    private val fetchFailedErrorMessage = "Fetch Failed"
    private val mockThrowable = Throwable(message = fetchFailedErrorMessage)


    @Before
    fun setUp() {
        viewModel = UploadPrescriptionViewModel(
            getEPharmacyOrderDetailUseCase,
            getEPharmacyCheckoutDetailUseCase,
            uploadPrescriptionUseCase,
            postPrescriptionIdUseCase,
            dispatcherBackground,
            dispatcherMain
        )
    }

    @Test
    fun orderDetailSuccessTest()
    {
        val product = mockk<EPharmacyProduct>(relaxed = true)
        val responseDetail = EPharmacyDataResponse(
            null, null,null,"",
            "","",null,
            arrayListOf(product),0L,"","","",
            false,"",""
        )

        coEvery {
            getEPharmacyOrderDetailUseCase.getEPharmacyOrderDetail(any(), any(), "")
        } coAnswers {
            firstArg<(EPharmacyDataResponse) -> Unit>().invoke(responseDetail)
        }
        viewModel.getEPharmacyOrderDetail("")
        assert(viewModel.productDetailLiveDataResponse.value is Success)
    }

    @Test
    fun `orderDetail fetch success but condition fail`()
    {
        val responseDetail = EPharmacyDataResponse(
            null, null,null,"",
            "","",null,
            arrayListOf(),0L,"","","",
            false,"",""
        )
        coEvery {
            getEPharmacyOrderDetailUseCase.getEPharmacyOrderDetail(any(), any(), "")
        } coAnswers {
            firstArg<(EPharmacyDataResponse) -> Unit>().invoke(responseDetail)
        }
        viewModel.getEPharmacyOrderDetail("")
        Assert.assertEquals(
            (viewModel.productDetailLiveDataResponse.value as Fail).throwable.localizedMessage,
            "Data invalid"
        )
    }

    @Test
    fun `orderDetail fetch failed throws exception`()
    {
        coEvery {
            getEPharmacyOrderDetailUseCase.getEPharmacyOrderDetail(any(), any(), "")
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.getEPharmacyOrderDetail("")
        Assert.assertEquals(
            (viewModel.productDetailLiveDataResponse.value as Fail).throwable,
            mockThrowable
        )
    }


    @Test
    fun checkoutDetailSuccessTest()
    {
        val product = mockk<EPharmacyProduct>(relaxed = true)
        val responseDetail = EPharmacyDataResponse(
            null, null,null,"",
            "","",null,
            arrayListOf(product),0L,"","","",
            false,"",""
        )

        coEvery {
            getEPharmacyCheckoutDetailUseCase.getEPharmacyCheckoutDetail(any(), any(), "")
        } coAnswers {
            firstArg<(EPharmacyDataResponse) -> Unit>().invoke(responseDetail)
        }
        viewModel.getEPharmacyCheckoutDetail("")
        assert(viewModel.productDetailLiveDataResponse.value is Success)
    }

    @Test
    fun `checkout fetch success but condition fail`()
    {
        val responseDetail = EPharmacyDataResponse(
            null, null,null,"",
            "","",null,
            arrayListOf(),0L,"","","",
            false,"",""
        )
        coEvery {
            getEPharmacyCheckoutDetailUseCase.getEPharmacyCheckoutDetail(any(), any(), "")
        } coAnswers {
            firstArg<(EPharmacyDataResponse) -> Unit>().invoke(responseDetail)
        }
        viewModel.getEPharmacyCheckoutDetail("")
        Assert.assertEquals(
            (viewModel.productDetailLiveDataResponse.value as Fail).throwable.localizedMessage,
            "Data invalid"
        )
    }

    @Test
    fun `checkoutDetail fetch failed throws exception`()
    {
        coEvery {
            getEPharmacyCheckoutDetailUseCase.getEPharmacyCheckoutDetail(any(), any(), "")
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.getEPharmacyCheckoutDetail("")
        Assert.assertEquals(
            (viewModel.productDetailLiveDataResponse.value as Fail).throwable,
            mockThrowable
        )
    }

    @Test
    fun uploadIdsSuccessTest()
    {
        val response = EPharmacyUploadPrescriptionIdsResponse(EPharmacyUploadPrescriptionIdsResponse.Data(true),"")
        coEvery {
            postPrescriptionIdUseCase.postPrescriptionIds(any(), any(), any(),any(),any())
        } coAnswers {
            firstArg<(EPharmacyUploadPrescriptionIdsResponse) -> Unit>().invoke(response)
        }
        viewModel.uploadPrescriptionIds(UPLOAD_CHECKOUT_ID_KEY,"")
        assert(viewModel.uploadPrescriptionIdsData.value is Success)
    }

    @Test
    fun `upload prescription ids success but condition fail`()
    {
        val response = EPharmacyUploadPrescriptionIdsResponse(EPharmacyUploadPrescriptionIdsResponse.Data(false),"")
        coEvery {
            postPrescriptionIdUseCase.postPrescriptionIds(any(), any(), any(),any(),any())
        } coAnswers {
            firstArg<(EPharmacyUploadPrescriptionIdsResponse) -> Unit>().invoke(response)
        }
        viewModel.uploadPrescriptionIds(UPLOAD_CHECKOUT_ID_KEY,"")
        assert(viewModel.uploadPrescriptionIdsData.value is Fail)
    }

    @Test
    fun removePrescriptionImageAtIndex() {
        val prescriptionImage = mockk<PrescriptionImage>(relaxed = true)
        viewModel.onSuccessGetPrescriptionImages(arrayListOf(prescriptionImage))
        viewModel.removePrescriptionImageAt(0)
        assert(viewModel.prescriptionImages.value?.size == 0)
    }

}