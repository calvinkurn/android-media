package com.tokopedia.salam.umrah.travel.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert.*
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.salam.umrah.UmrahDispatchersProviderTest
import com.tokopedia.salam.umrah.travel.data.UmrahGalleriesEntity
import com.tokopedia.salam.umrah.travel.data.UmrahGallery
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class UmrahTravelGalleryViewModelTest{
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var mGraphqlRepository: GraphqlRepository
    private val dispatcher = UmrahDispatchersProviderTest()
    private lateinit var umrahTravelGalleryViewModel: UmrahTravelGalleryViewModel

    private val umrahGalleries = listOf(UmrahGallery())

    @Before
    fun setUp(){
        MockKAnnotations.init(this)
        umrahTravelGalleryViewModel = UmrahTravelGalleryViewModel(mGraphqlRepository, dispatcher)
    }

    @Test
    fun getListGallery_shouldReturnData(){
        //given
        val gqlResponseSuccess = GraphqlResponse(
                mapOf(UmrahGalleriesEntity::class.java to UmrahGalleriesEntity(umrahGalleries)),
                        mapOf(), false)

        coEvery {
            mGraphqlRepository.getReseponse(any(),any())
        } returns gqlResponseSuccess

        //when
        umrahTravelGalleryViewModel.getDataGallery(0,"","")
        val actual = umrahTravelGalleryViewModel.galleryResult.value

        assert(actual is Success)
    }


    @Test
    fun getListGallery_shouldReturnError(){
        //given
        val gqlResponseFail = GraphqlResponse(
                mapOf(),
                mapOf(UmrahGalleriesEntity::class.java to listOf(GraphqlError())), false)

        coEvery {
            mGraphqlRepository.getReseponse(any(),any())
        } returns gqlResponseFail

        //when
        umrahTravelGalleryViewModel.getDataGallery(0,"","")
        val actual = umrahTravelGalleryViewModel.galleryResult.value

        assert(actual is Fail)
    }


}