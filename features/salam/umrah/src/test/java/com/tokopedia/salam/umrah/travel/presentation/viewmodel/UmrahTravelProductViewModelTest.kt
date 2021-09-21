package com.tokopedia.salam.umrah.travel.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.salam.umrah.travel.data.UmrahTravelAgentProductEntity
import com.tokopedia.salam.umrah.travel.data.UmrahTravelProduct
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.lang.reflect.Type

@RunWith(JUnit4::class)
class UmrahTravelProductViewModelTest{
    @get:Rule
    val rule = InstantTaskExecutorRule()


    @RelaxedMockK
    lateinit var mGraphqlRepository: GraphqlRepository
    private val dispatcher = CoroutineTestDispatchersProvider
    private lateinit var umrahTravelProductViewModel: UmrahTravelProductViewModel

    private val umrahTravelProducts = listOf(UmrahTravelProduct())

    @Before
    fun setUp(){
        MockKAnnotations.init(this)
        umrahTravelProductViewModel = UmrahTravelProductViewModel(mGraphqlRepository, dispatcher)
    }

    @Test
    fun getListProduct_shouldReturnData(){
        //given
        val result = HashMap<Type, Any>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = UmrahTravelAgentProductEntity::class.java
        result[objectType] = UmrahTravelAgentProductEntity(umrahTravelProducts)
        val gqlResponseSuccess = GraphqlResponse(result, errors, false)

        coEvery { mGraphqlRepository.getReseponse(any(),any())} returns gqlResponseSuccess

        //when
        umrahTravelProductViewModel.getDataProductTravel(0,"","")

        val actual = umrahTravelProductViewModel.searchResult.value

        //then
        assert(actual is Success)
    }

    @Test
    fun getListProduct_shouldReturnError(){
        //given
        val result = HashMap<Type, Any?>()
        val errors = HashMap<Type, List<GraphqlError>>()
        val objectType = UmrahTravelAgentProductEntity::class.java

        result[objectType] = null
        errors[objectType] = listOf(GraphqlError())

        val gqlResponseFail = GraphqlResponse(result, errors, false)

        coEvery { mGraphqlRepository.getReseponse(any(),any())} returns gqlResponseFail

        //when
        umrahTravelProductViewModel.getDataProductTravel(0,"","")

        val actual = umrahTravelProductViewModel.searchResult.value

        //then
        assert(actual is Fail)

    }

}