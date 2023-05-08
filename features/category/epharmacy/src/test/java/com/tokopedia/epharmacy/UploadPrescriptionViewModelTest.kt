package com.tokopedia.epharmacy

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.epharmacy.network.response.*
import com.tokopedia.epharmacy.usecase.GetEPharmacyCheckoutDetailUseCase
import com.tokopedia.epharmacy.usecase.GetEPharmacyOrderDetailUseCase
import com.tokopedia.epharmacy.usecase.PostPrescriptionIdUseCase
import com.tokopedia.epharmacy.usecase.UploadPrescriptionUseCase
import com.tokopedia.epharmacy.utils.EPharmacyUploadEmptyImageError
import com.tokopedia.epharmacy.viewmodel.UploadPrescriptionViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.reflect.Type

@ExperimentalCoroutinesApi
class UploadPrescriptionViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getEPharmacyOrderDetailUseCase = mockk<GetEPharmacyOrderDetailUseCase>(relaxed = true)
    private val getEPharmacyCheckoutDetailUseCase = mockk<GetEPharmacyCheckoutDetailUseCase>(relaxed = true)
    private val uploadPrescriptionUseCase = mockk<UploadPrescriptionUseCase>(relaxed = true)
    private val postPrescriptionIdUseCase = mockk<PostPrescriptionIdUseCase>(relaxed = true)

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
            dispatcherBackground
        )
    }

    @Test
    fun orderDetailSuccessTest() {
        val product = mockk<EPharmacyProduct>(relaxed = true)
        val responseDetail = EPharmacyDataResponse(
            EPharmacyDataResponse.EPharmacyOrderDetailData(
                EPharmacyDataResponse.EPharmacyOrderDetailData.EPharmacyDataForm(
                    null, null, null, "",
                    "", "", null,
                    arrayListOf(product), 0L, "", "", "", "",
                    false, "", ""
                )
            )
        )

        coEvery {
            getEPharmacyOrderDetailUseCase.getEPharmacyOrderDetail(any(), any(), 1)
        } coAnswers {
            firstArg<(EPharmacyDataResponse) -> Unit>().invoke(responseDetail)
        }
        viewModel.getEPharmacyOrderDetail(1)
        assert(viewModel.productDetailLiveDataResponse.value is Success)
    }

    @Test
    fun orderDetailSuccessTestReUpload() {
        val product = mockk<EPharmacyProduct>(relaxed = true)
        val responseDetail = EPharmacyDataResponse(
            EPharmacyDataResponse.EPharmacyOrderDetailData(
                EPharmacyDataResponse.EPharmacyOrderDetailData.EPharmacyDataForm(
                    null, null, null, "",
                    "", "",
                    arrayListOf(
                        PrescriptionImage(
                            "", 1, "", "REJECTED",
                            false, false, true, false, "", null
                        )
                    ),
                    arrayListOf(product), 0L, "", "", "", "",
                    true, "", ""
                )
            )
        )

        coEvery {
            getEPharmacyOrderDetailUseCase.getEPharmacyOrderDetail(any(), any(), 1)
        } coAnswers {
            firstArg<(EPharmacyDataResponse) -> Unit>().invoke(responseDetail)
        }
        viewModel.getEPharmacyOrderDetail(1)
        assert(viewModel.productDetailLiveDataResponse.value is Success)
    }

    @Test
    fun `orderDetail fetch success but condition fail`() {
        val responseDetail = EPharmacyDataResponse(
            EPharmacyDataResponse.EPharmacyOrderDetailData(
                EPharmacyDataResponse.EPharmacyOrderDetailData.EPharmacyDataForm(
                    null, null, null, "",
                    "", "", null,
                    arrayListOf(), 0L, "", "", "", "",
                    false, "", ""
                )
            )
        )
        coEvery {
            getEPharmacyOrderDetailUseCase.getEPharmacyOrderDetail(any(), any(), 1)
        } coAnswers {
            firstArg<(EPharmacyDataResponse) -> Unit>().invoke(responseDetail)
        }
        viewModel.getEPharmacyOrderDetail(1)
        Assert.assertEquals(
            (viewModel.productDetailLiveDataResponse.value as Fail).throwable.localizedMessage,
            "Data invalid"
        )
    }

    @Test
    fun `orderDetail fetch failed throws exception`() {
        coEvery {
            getEPharmacyOrderDetailUseCase.getEPharmacyOrderDetail(any(), any(), 1)
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.getEPharmacyOrderDetail(1)
        Assert.assertEquals(
            (viewModel.productDetailLiveDataResponse.value as Fail).throwable,
            mockThrowable
        )
    }

    @Test
    fun checkoutDetailSuccessTest() {
        val product = EPharmacyProduct(
            true, "", "", "", 1, 2.0, 1, "", "", "", "",
            arrayListOf(EPharmacyProduct(true, "", "", "", 1, 2.0, 1, "", "", "", "", null))
        )
        val responseDetail = EPharmacyDataResponse(
            EPharmacyDataResponse.EPharmacyOrderDetailData(
                EPharmacyDataResponse.EPharmacyOrderDetailData.EPharmacyDataForm(
                    null, null, null, "",
                    "", "",
                    arrayListOf(
                        PrescriptionImage(
                            "", 1, "", "REJECTED",
                            false,
                            false, true, false, "", null
                        )
                    ),
                    arrayListOf(product, product), 0L, "", "", "", "",
                    false, "", ""
                )
            )
        )

        coEvery {
            getEPharmacyCheckoutDetailUseCase.getEPharmacyCheckoutDetail(any(), any(), "", "")
        } coAnswers {
            firstArg<(EPharmacyDataResponse) -> Unit>().invoke(responseDetail)
        }
        viewModel.getEPharmacyCheckoutDetail("", "")
        assert(viewModel.productDetailLiveDataResponse.value is Success)
    }

    @Test
    fun checkoutDetailSuccessTestReUpload() {
        val product = mockk<EPharmacyProduct>(relaxed = true)
        val responseDetail = EPharmacyDataResponse(
            EPharmacyDataResponse.EPharmacyOrderDetailData(
                EPharmacyDataResponse.EPharmacyOrderDetailData.EPharmacyDataForm(
                    null, null, null, "",
                    "", "", null,
                    arrayListOf(product), 0L, "", "", "", "",
                    true, "", ""
                )
            )
        )

        coEvery {
            getEPharmacyCheckoutDetailUseCase.getEPharmacyCheckoutDetail(any(), any(), "", "")
        } coAnswers {
            firstArg<(EPharmacyDataResponse) -> Unit>().invoke(responseDetail)
        }
        viewModel.getEPharmacyCheckoutDetail("", "")
        assert(viewModel.productDetailLiveDataResponse.value is Success)
    }

    @Test
    fun `checkoutDetailSuccessTestReUpload Cek Resep`() {
        val product = mockk<EPharmacyProduct>(relaxed = true)
        val responseDetail = EPharmacyDataResponse(
            EPharmacyDataResponse.EPharmacyOrderDetailData(
                EPharmacyDataResponse.EPharmacyOrderDetailData.EPharmacyDataForm(
                    null, null, null, "",
                    "", "", arrayListOf(),
                    arrayListOf(product), 0L, "", "", "", "",
                    true, "", ""
                )
            )
        )

        coEvery {
            getEPharmacyCheckoutDetailUseCase.getEPharmacyCheckoutDetail(any(), any(), "", "")
        } coAnswers {
            firstArg<(EPharmacyDataResponse) -> Unit>().invoke(responseDetail)
        }
        viewModel.getEPharmacyCheckoutDetail("", "")
        assert(viewModel.productDetailLiveDataResponse.value is Success)
    }

    @Test
    fun `checkout fetch success but condition fail`() {
        val responseDetail = EPharmacyDataResponse(
            EPharmacyDataResponse.EPharmacyOrderDetailData(
                EPharmacyDataResponse.EPharmacyOrderDetailData.EPharmacyDataForm(
                    null, null, null, "",
                    "", "", null,
                    arrayListOf(), 0L, "", "", "", "",
                    false, "", ""
                )
            )
        )
        coEvery {
            getEPharmacyCheckoutDetailUseCase.getEPharmacyCheckoutDetail(any(), any(), "", "")
        } coAnswers {
            firstArg<(EPharmacyDataResponse) -> Unit>().invoke(responseDetail)
        }
        viewModel.getEPharmacyCheckoutDetail("", "")
        Assert.assertEquals(
            (viewModel.productDetailLiveDataResponse.value as Fail).throwable.localizedMessage,
            "Data invalid"
        )
    }

    @Test
    fun `checkoutDetail fetch failed throws exception`() {
        coEvery {
            getEPharmacyCheckoutDetailUseCase.getEPharmacyCheckoutDetail(any(), any(), "", "")
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.getEPharmacyCheckoutDetail("", "")
        Assert.assertEquals(
            (viewModel.productDetailLiveDataResponse.value as Fail).throwable,
            mockThrowable
        )
    }

    @Test
    fun uploadIdsSuccessTestOrder() {
        val response = EPharmacyUploadPrescriptionIdsResponse(EPharmacyUploadPrescriptionIdsResponse.EPharmacyUploadPrescriptionData(true))
        coEvery {
            postPrescriptionIdUseCase.postPrescriptionIdsOrder(any(), any(), any(), any())
        } coAnswers {
            firstArg<(EPharmacyUploadPrescriptionIdsResponse) -> Unit>().invoke(response)
        }
        viewModel.uploadPrescriptionIdsInOrder(0L)
        assert(viewModel.uploadPrescriptionIdsData.value is Success)
    }

    @Test
    fun `upload prescription ids Fail`() {
        coEvery {
            postPrescriptionIdUseCase.postPrescriptionIdsOrder(any(), any(), any(), any())
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.uploadPrescriptionIdsInOrder(0L)
        assert(viewModel.uploadPrescriptionIdsData.value is Fail)
    }

    @Test
    fun `upload prescription ids Order success but condition fail`() {
        val response = EPharmacyUploadPrescriptionIdsResponse(EPharmacyUploadPrescriptionIdsResponse.EPharmacyUploadPrescriptionData(false))
        coEvery {
            postPrescriptionIdUseCase.postPrescriptionIdsOrder(any(), any(), any(), any())
        } coAnswers {
            firstArg<(EPharmacyUploadPrescriptionIdsResponse) -> Unit>().invoke(response)
        }
        viewModel.uploadPrescriptionIdsInOrder(0L)
        assert(viewModel.uploadPrescriptionIdsData.value is Fail)
    }

    @Test
    fun uploadIdsSuccessTestCheckout() {
        val response = EPharmacyUploadPrescriptionIdsResponse(EPharmacyUploadPrescriptionIdsResponse.EPharmacyUploadPrescriptionData(true))
        coEvery {
            postPrescriptionIdUseCase.postPrescriptionIdsCheckout(any(), any(), any(), any())
        } coAnswers {
            firstArg<(EPharmacyUploadPrescriptionIdsResponse) -> Unit>().invoke(response)
        }
        viewModel.uploadPrescriptionIdsInCheckout("")
        assert(viewModel.uploadPrescriptionIdsData.value is Success)
    }

    @Test
    fun `upload prescription ids Checkout success but condition fail`() {
        val response = EPharmacyUploadPrescriptionIdsResponse(EPharmacyUploadPrescriptionIdsResponse.EPharmacyUploadPrescriptionData(false))
        coEvery {
            postPrescriptionIdUseCase.postPrescriptionIdsCheckout(any(), any(), any(), any())
        } coAnswers {
            firstArg<(EPharmacyUploadPrescriptionIdsResponse) -> Unit>().invoke(response)
        }
        viewModel.uploadPrescriptionIdsInCheckout("")
        assert(viewModel.uploadPrescriptionIdsData.value is Fail)
    }

    @Test
    fun removePrescriptionImageAtIndex() {
        val prescriptionImage = mockk<PrescriptionImage>(relaxed = true)
        viewModel.onSuccessGetPrescriptionImages(arrayListOf(prescriptionImage))
        viewModel.removePrescriptionImageAt(0)
        assert(viewModel.prescriptionImages.value?.size == 0)
    }

    @Test
    fun addSelectedPrescriptionImages() {
        viewModel.onSuccessGetPrescriptionImages(arrayListOf())
        viewModel.addSelectedPrescriptionImages(arrayListOf("sfaf", "sfa"))
        coEvery {
            viewModel.convertToUploadImageResponse(mockk<HashMap<Type, RestResponse>>())
        } coAnswers {
            EPharmacyPrescriptionUploadResponse(
                arrayListOf(
                    EPharmacyPrescriptionUploadResponse.EPharmacyPrescriptionData(
                        "",
                        1,
                        1
                    )
                ),
                "", mockk()
            )
        }
        assert((viewModel.prescriptionImages.value?.size ?: 0) > 1)
    }

    @Test
    fun reUploadPrescriptionImage() {
        viewModel.onSuccessGetPrescriptionImages(arrayListOf())
        viewModel.addSelectedPrescriptionImages(arrayListOf("sfaf", "sfa"))
        every {
            viewModel.convertToUploadImageResponse(mockk<HashMap<Type, RestResponse>>())
        } coAnswers {
            mockk<EPharmacyPrescriptionUploadResponse>(relaxed = true)
        }
        viewModel.reUploadPrescriptionImage(0, "sfaf")
    }

    @Test
    fun `Adding empty local path image`() {
        viewModel.onSuccessGetPrescriptionImages(arrayListOf())
        viewModel.addSelectedPrescriptionImages(arrayListOf(""))
        assert(viewModel.uploadError.value == EPharmacyUploadEmptyImageError(true))
    }

    @Test
    fun addSelectedPrescriptionImagesEmptyFail() {
        viewModel.addSelectedPrescriptionImages(arrayListOf("afasfsfas", "asfasfasfsa"))
        val arrayResponse = arrayListOf<EPharmacyPrescriptionUploadResponse.EPharmacyPrescriptionData>()
        arrayResponse.add(mockk())
        arrayResponse.add(mockk())
        coEvery {
            uploadPrescriptionUseCase.executeOnBackground(0, "fsfasfasfasf")
        } coAnswers {
            HashMap<Type, RestResponse>().apply {
                put(EPharmacyPrescriptionUploadResponse::class.java, RestResponse(EPharmacyPrescriptionUploadResponse(arrayResponse, "", mockk()), 1, false))
            }
        }
        coVerify(exactly = 0) { uploadPrescriptionUseCase.executeOnBackground(0, "fsfasfasfasf") }
    }
}
