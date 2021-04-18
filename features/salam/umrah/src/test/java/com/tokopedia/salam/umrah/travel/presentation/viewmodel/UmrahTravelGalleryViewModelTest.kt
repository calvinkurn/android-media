package com.tokopedia.salam.umrah.travel.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert.*
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
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
import java.lang.reflect.Type

@RunWith(JUnit4::class)
class UmrahTravelGalleryViewModelTest{
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var mGraphqlRepository: GraphqlRepository
    private val dispatcher = CoroutineTestDispatchersProvider
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
        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = UmrahGalleriesEntity::class.java
        result[objectType] = UmrahGalleriesEntity(umrahGalleries)
        val gqlResponseSuccess = GraphqlResponse(result, errors, false)

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
        val result = HashMap<Type, Any?>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = UmrahGalleriesEntity::class.java

        result[objectType] = null
        errors[objectType] = listOf(GraphqlError())

        val gqlResponseFail = GraphqlResponse(result, errors, false)

        coEvery {
            mGraphqlRepository.getReseponse(any(),any())
        } returns gqlResponseFail

        //when
        umrahTravelGalleryViewModel.getDataGallery(0,"","")
        val actual = umrahTravelGalleryViewModel.galleryResult.value

        assert(actual is Fail)
    }


}