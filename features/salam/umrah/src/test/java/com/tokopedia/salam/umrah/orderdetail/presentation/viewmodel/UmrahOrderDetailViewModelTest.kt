package com.tokopedia.salam.umrah.orderdetail.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.salam.umrah.common.data.MyUmrahEntity
import com.tokopedia.salam.umrah.common.data.UmrahValueLabelEntity
import com.tokopedia.salam.umrah.common.presentation.model.UmrahSimpleDetailModel
import com.tokopedia.salam.umrah.common.presentation.model.UmrahSimpleModel
import com.tokopedia.salam.umrah.orderdetail.data.UmrahOrderDetailButtonModel
import com.tokopedia.salam.umrah.orderdetail.data.UmrahOrderDetailsEntity
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.lang.reflect.Type

@RunWith(JUnit4::class)
class UmrahOrderDetailViewModelTest{
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val graphqlRepository = mockk<GraphqlRepository>()
    private val umrahDispatchersProvider = CoroutineTestDispatchersProvider

    private lateinit var viewModel : UmrahOrderDetailViewModel

    @Before
    fun setup(){
        viewModel = UmrahOrderDetailViewModel(graphqlRepository, umrahDispatchersProvider)
    }

    @Test
    fun `getOrderDetail_SuccessRetreiveOrderDetail_ShouldSuccess`(){
        val result = HashMap<Type, Any>()
        result[UmrahOrderDetailsEntity.Response::class.java] = UmrahOrderDetailsEntity.Response()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(),any()) } returns gqlResponse

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
        val result = HashMap<Type, Any>()
        result[MyUmrahEntity.Response::class.java] = MyUmrahEntity.Response()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(),any()) } returns gqlResponse

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
    fun `transformToSimpleModel_SuccessTransformToSimpleModel_SuccessDataDetails`(){
        val dataDetails = listOf(
                UmrahOrderDetailsEntity.DataDetail("http://tokopedia.com/image",
                        "http://tokopedia.com/backgroundColor",
                        "#000","1","Order Detail"))
        val simpleDetails = listOf(
                UmrahSimpleModel(
                        "Order Detail","1","#000"
                )
        )


        val actual = viewModel.transformToSimpleModel(dataDetails, listOf(), listOf())
        assert(simpleDetails[0].title.equals(actual[0].title))
        assert(simpleDetails[0].description.equals(actual[0].description))
        assert(simpleDetails[0].textColor.equals(actual[0].textColor))
    }

    @Test
    fun `transformToSimpleModel_SuccessTransformToSimpleModel_SuccessDataPessanger`(){
        val pessangers = listOf(
                UmrahOrderDetailsEntity.Passenger(1,"Firmanda"))

        val simpleDetails = listOf(
                UmrahSimpleModel(
                        "1. Firmanda"
                ))


        val actual = viewModel.transformToSimpleModel(listOf(),pessangers, listOf())
        assert(simpleDetails[0].title.equals(actual[0].title))
    }

    @Test
    fun `transformToSimpleModel_SuccessTransformToSimpleModel_SuccessDataValueLabel`(){

        val valueLabelDatas = listOf(UmrahValueLabelEntity(
                "2000000","Harga Pokok"
        ))

        val simpleDetails = listOf(
                UmrahSimpleModel(
                        "Harga Pokok",  "2000000"
                )
        )


        val actual = viewModel.transformToSimpleModel(listOf(), listOf(),valueLabelDatas)
        assert(simpleDetails[0].title.equals(actual[0].title))
        assert(simpleDetails[0].description.equals(actual[0].description))
    }

    @Test
    fun `transformToSimpleModel_SuccessTransformToSimpleModel_AllNullParameter`(){
        val actual = viewModel.transformToSimpleModel()
        assert(actual.isEmpty())
    }

    @Test
    fun `transformToSimpleDetailModel_SuccessTransformToSimpleDetailModel`(){
        val dataDetails = listOf(
                UmrahOrderDetailsEntity.DataDetail("http://tokopedia.com/image",
                        "http://tokopedia.com/backgroundColor",
                        "#000","1","Order Detail"))
        val simpleDetails = listOf(
                UmrahSimpleDetailModel(
                        "Order Detail","1","http://tokopedia.com/image"
                )
        )

        val actual = viewModel.transformToSimpleDetailModel(dataDetails)
        assert(simpleDetails[0].icon.equals(actual[0].icon))
        assert(simpleDetails[0].subtitle.equals(actual[0].subtitle))
        assert(simpleDetails[0].title.equals(actual[0].title))

    }

    @Test
    fun `transformToButtonModel_SuccessTransformToButtonModel`(){
        val umrahOrderDetailButtons =
                listOf(UmrahOrderDetailButtonModel("1","type","http://tokopedia.com"))

        val actioButtons =
                listOf(UmrahOrderDetailsEntity.ActionButton("1","type",
                UmrahOrderDetailsEntity.ActionButton.Body("http://tokopedia.com","http://tokopedia.com")))

        val actual = viewModel.transformToButtonModel(actioButtons)
        assert(umrahOrderDetailButtons[0].label.equals(actual[0].label))
        assert(umrahOrderDetailButtons[0].buttonLink.equals(actual[0].buttonLink))
        assert(umrahOrderDetailButtons[0].buttonType.equals(actual[0].buttonType))
    }
}