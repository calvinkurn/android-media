package com.tokopedia.salam.umrah.orderdetail.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.salam.umrah.UmrahDispatchersProviderTest
import com.tokopedia.salam.umrah.common.data.MyUmrahEntity
import com.tokopedia.salam.umrah.orderdetail.data.UmrahOrderDetailsEntity
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class UmrahOrderDetailViewModelTest{
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val graphqlRepository = mockk<GraphqlRepository>()
    private val umrahDispatchersProvider = UmrahDispatchersProviderTest()

    private lateinit var viewModel : UmrahOrderDetailViewModel

    @Before
    fun setup(){
        viewModel = UmrahOrderDetailViewModel(graphqlRepository, umrahDispatchersProvider)
    }


    @Test
    fun `getOrderDetail_SuccessRetreiveOrderDetail_ShouldSuccess`(){
        val graphqlSuccessResponse = GraphqlResponse(
                mapOf(UmrahOrderDetailsEntity.Response::class.java to UmrahOrderDetailsEntity.Response()),
                mapOf(), false)
        coEvery { graphqlRepository.getReseponse(any(),any()) } returns graphqlSuccessResponse

        viewModel.getOrderDetail("","")

        val actual = viewModel.orderDetailData.value
        assert(actual is Success)
    }

    @Test
    fun `getOrderDetail_FailedRetreiveOrderDetail_ShouldFailed`(){
        coEvery { graphqlRepository.getReseponse(any(),any()) } coAnswers {throw Throwable() }

        viewModel.getOrderDetail("","")

        val actual = viewModel.orderDetailData.value
        assert(actual is Fail)
    }

    @Test
    fun `getMyUmrah_SuccessRetreiveMyUmrah_ShouldSuccess`(){
        val graphqlSuccessResponse = GraphqlResponse(
                mapOf(MyUmrahEntity.Response::class.java to MyUmrahEntity.Response()),
                mapOf(), false)
        coEvery { graphqlRepository.getReseponse(any(),any()) } returns graphqlSuccessResponse

        viewModel.getMyUmrahWidget("","")

        val actual = viewModel.myWidgetData.value
        assert(actual is Success)
    }

    @Test
    fun `getMyUmrah_FailedRetreiveMyUmrah_ShouldFailed`(){
        coEvery { graphqlRepository.getReseponse(any(),any()) } coAnswers {throw Throwable() }

        viewModel.getMyUmrahWidget("","")

        val actual = viewModel.myWidgetData.value
        assert(actual is Fail)
    }


    @Test
    fun `transformToSimpleModel_SuccessTransformToSimpleModel`(){

    }


    @Test
    fun `transformToSimpleDetailModel_SuccessTransformToSimpleDetailModel`(){

    }


    @Test
    fun `transformToButtonModel_SuccessTransformToButtonModel`(){

    }
}