package com.tokopedia.rechargeocr.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.imageuploader.domain.UploadImageUseCase
import com.tokopedia.rechargeocr.data.RechargeUploadImageData
import com.tokopedia.rechargeocr.data.RechargeUploadImageResponse
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RechargeUploadImageViewModelTest2 {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    lateinit var rechargeUploadImageViewModel: RechargeUploadImageViewModel

    @MockK
    lateinit var uploadImageUseCase: UploadImageUseCase<RechargeUploadImageResponse>

    @MockK
    lateinit var userSessionInterface: UserSessionInterface

    @MockK
    lateinit var graphqlRepository: GraphqlRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        rechargeUploadImageViewModel = RechargeUploadImageViewModel(uploadImageUseCase,
                graphqlRepository, userSessionInterface, Dispatchers.Unconfined)
    }

    @Test
    fun uploadImage_shouldReturnStringUrl() {
        //given
        val stringUrl = "https://ecs7.tokopedia.net/img/cache/100-square/attachment/2019/12/17/190168710f6fc075-43c1-4ed7-a1f0-8070d919d8091576571111454/190168710f6fc075-43c1-4ed7-a1f0-8070d919d8091576571111454_d3595c52-f7ed-414a-aecd-7d5a71ea4407.jpg"
        val mockRechargeUploadData = RechargeUploadImageData(picSrc = stringUrl)
        val expectedUrlImage = "https://ecs7.tokopedia.net/img/cache/500/attachment/2019/12/17/190168710f6fc075-43c1-4ed7-a1f0-8070d919d8091576571111454/190168710f6fc075-43c1-4ed7-a1f0-8070d919d8091576571111454_d3595c52-f7ed-414a-aecd-7d5a71ea4407.jpg"
        coEvery {
            rechargeUploadImageViewModel.getUrlUploadImage(any())
        } returns RechargeUploadImageResponse(mockRechargeUploadData)

        //when
        rechargeUploadImageViewModel.uploadImageRecharge("")
        //then
        val actualUrl= rechargeUploadImageViewModel.urlImage.value
//        Assert.assertNotNull(actualUrl)
        Assert.assertEquals(expectedUrlImage, actualUrl)
    }

//    @Test
//    fun uploadImage_shouldReturnStringUrl() {
//        // given
//        val gqlResponseSuccess = GraphqlResponse(
//                mapOf(UmrahSearchProductEntity::class.java to UmrahSearchProductEntity(umrahSearchProducts)),
//                mapOf(), false)
//        coEvery { mGraphqlRepository.getReseponse(any(), any()) } returns gqlResponseSuccess
//
//        // when
//        umrahSearchViewModel.searchUmrahProducts(1, "")
//
//        // then the result should be success
//        val actual = umrahSearchViewModel.searchResult.value
//        assertTrue(actual is Success)
//        val listActualData = (actual as Success).data
//        assertEquals(umrahSearchProducts.size, listActualData.size)
//        assertEquals(umrahSearchProducts[0].id, listActualData[0].id)
//    }
}