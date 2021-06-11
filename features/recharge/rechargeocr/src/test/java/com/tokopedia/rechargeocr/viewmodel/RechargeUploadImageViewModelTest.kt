package com.tokopedia.rechargeocr.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.rechargeocr.RechargeCameraUtil
import com.tokopedia.rechargeocr.data.RechargeOcrResponse
import com.tokopedia.rechargeocr.data.RechargeUploadImageResponse
import com.tokopedia.rechargeocr.data.ResultOcr
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockkObject
import kotlinx.coroutines.Dispatchers
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.reflect.Type

class RechargeUploadImageViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    lateinit var rechargeUploadImageViewModel: RechargeUploadImageViewModel

    @MockK
    lateinit var graphqlRepository: GraphqlRepository

    @MockK
    lateinit var rechargeUploadImageUseCase: RechargeUploadImageUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockkObject(RechargeCameraUtil.Companion)
        rechargeUploadImageViewModel = RechargeUploadImageViewModel(rechargeUploadImageUseCase,
                graphqlRepository, Dispatchers.Unconfined)
    }

    @Test
    fun uploadImage_CropAndUpload_ReturnSuccess() {
        //given
        coEvery { RechargeCameraUtil.trimBitmap("") } returns ""

        val stringUrl = "https://ecs7.tokopedia.net/img/cache/100-square/attachment/2019/12/17/image2.jpg"
        coEvery { rechargeUploadImageUseCase.execute(any()) } returns RechargeUploadImageResponse(
                RechargeUploadImageResponse.RechargeUploadImageData(picSrc = stringUrl))

        val expectedData = "12345678910"
        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = RechargeOcrResponse::class.java
        result[objectType] = RechargeOcrResponse(ResultOcr(expectedData))
        val gqlResponseSuccess = GraphqlResponse(result, errors, false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseSuccess

        //when
        rechargeUploadImageViewModel.uploadImageRecharge("", "")

        //then
        val actualData = rechargeUploadImageViewModel.resultDataOcr.value
        assertNotNull(actualData)
        assertEquals(expectedData, actualData)
    }

    @Test
    fun uploadImage_takePictureFromOCR_ReturnUploadFailed() {
        //given
        val errorThrowable = Throwable("error upload")
        coEvery { RechargeCameraUtil.trimBitmap("") } returns ""
        coEvery { rechargeUploadImageUseCase.execute(any()) } coAnswers { throw errorThrowable }

        //when
        rechargeUploadImageViewModel.uploadImageRecharge("", "")

        //then
        val actualData = rechargeUploadImageViewModel.errorActionOcr.value
        assertNotNull(actualData)
        assertEquals(errorThrowable.message, actualData?.message)
    }

    @Test
    fun getResultOCR_CropAndUpload_ReturnFailedErrorFromServer() {
        // given
        coEvery { RechargeCameraUtil.trimBitmap("") } returns ""

        val stringUrl = "https://ecs7.tokopedia.net/img/cache/100-square/attachment/2019/12/17/image2.jpg"
        coEvery { rechargeUploadImageUseCase.execute(any()) } returns RechargeUploadImageResponse(
                RechargeUploadImageResponse.RechargeUploadImageData(picSrc = stringUrl))

        val errorMessage = "Nomor kartu tidak terdeteksi"
        val errorGql = GraphqlError()
        errorGql.message = errorMessage

        val result = HashMap<Type, Any?>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = RechargeOcrResponse::class.java

        result[objectType] = null
        errors[objectType] = listOf(errorGql)

        val gqlResponseFail = GraphqlResponse(result, errors, false)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseFail

        // when
        rechargeUploadImageViewModel.uploadImageRecharge("", "")

        // then the result should be success
        val actualData = rechargeUploadImageViewModel.errorActionOcr.value
        assertNotNull(actualData)
        assertEquals(errorMessage, actualData?.message)
    }
}