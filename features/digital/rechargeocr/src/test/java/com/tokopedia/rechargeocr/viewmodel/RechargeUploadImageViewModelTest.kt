package com.tokopedia.rechargeocr.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.rechargeocr.data.RechargeOcrResponse
import com.tokopedia.rechargeocr.data.RechargeUploadImageResponse
import com.tokopedia.rechargeocr.data.ResultOcr
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

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
        rechargeUploadImageViewModel = RechargeUploadImageViewModel(rechargeUploadImageUseCase,
                graphqlRepository, Dispatchers.Unconfined)
    }

    @Test
    fun uploadImage_takePictureFromOCR_ReturnSuccess() {
        //given
        val stringUrl = "https://ecs7.tokopedia.net/img/cache/100-square/attachment/2019/12/17/image2.jpg"
        val expectedUrlImage = "https://ecs7.tokopedia.net/img/cache/500/attachment/2019/12/17/image2.jpg"
        coEvery { rechargeUploadImageUseCase.execute(any()) } returns RechargeUploadImageResponse(
                RechargeUploadImageResponse.RechargeUploadImageData(picSrc = stringUrl))

        //when
        rechargeUploadImageViewModel.uploadImageRecharge("")

        //then
        val actualUrl = rechargeUploadImageViewModel.urlImage.value
        assertNotNull(actualUrl)
        assertEquals(expectedUrlImage, rechargeUploadImageViewModel.urlImage.value)
    }

    @Test
    fun uploadImage_takePictureFromOCR_ReturnFailedFromServer() {
        //given
        val expectedUrlImage = ""
        coEvery { rechargeUploadImageUseCase.execute(any()) } coAnswers { throw Throwable() }

        //when
        rechargeUploadImageViewModel.uploadImageRecharge("")

        //then
        val actualUrl = rechargeUploadImageViewModel.urlImage.value
        assertNotNull(actualUrl)
        assertEquals(expectedUrlImage, rechargeUploadImageViewModel.urlImage.value)
    }

    @Test
    fun getResultOCR_uploadUrlImage_ReturnSuccessCardNumber() {
        // given
        val expectedData = "12345678910"
        val gqlResponseSuccess = GraphqlResponse(
                mapOf(RechargeOcrResponse::class.java to RechargeOcrResponse(ResultOcr(expectedData))),
                mapOf(), false)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseSuccess

        // when
        rechargeUploadImageViewModel.getResultOcr("", "")

        // then the result should be success
        val actualData = rechargeUploadImageViewModel.resultDataOcr.value
        assertNotNull(actualData)
        assertEquals(expectedData, actualData)
    }

    @Test
    fun getResultOCR_uploadUrlImage_ReturnFailedErrorFromServer() {
        // given
        val errorMessage = "Nomor kartu tidak terdeteksi"
        val errorGql = GraphqlError()
        errorGql.message = errorMessage
        val gqlResponseSuccess = GraphqlResponse(
                mapOf(), mapOf(RechargeOcrResponse::class.java to listOf(errorGql)), false)
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponseSuccess

        // when
        rechargeUploadImageViewModel.getResultOcr("", "")

        // then the result should be success
        val actualData = rechargeUploadImageViewModel.errorActionOcr.value
        assertNotNull(actualData)
        assertEquals(errorMessage, actualData)
    }
}