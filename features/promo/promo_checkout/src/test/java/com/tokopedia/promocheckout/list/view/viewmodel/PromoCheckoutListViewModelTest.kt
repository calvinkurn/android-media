package com.tokopedia.promocheckout.list.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.promocheckout.list.model.listcoupon.DataPromoCheckoutList
import com.tokopedia.promocheckout.list.model.listlastseen.PromoCheckoutLastSeenModel
import com.tokopedia.promocheckout.mockdata.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.lang.reflect.Type

/**
 * @author: astidhiyaa on 06/08/21.
 */
@RunWith(JUnit4::class)
class PromoCheckoutListViewModelTest{
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = object : CoroutineDispatchers {
        override val main: CoroutineDispatcher
            get() = Dispatchers.Unconfined
        override val io: CoroutineDispatcher
            get() = Dispatchers.Unconfined
        override val default: CoroutineDispatcher
            get() = Dispatchers.Unconfined
        override val immediate: CoroutineDispatcher
            get() = Dispatchers.Unconfined
        override val computation: CoroutineDispatcher
            get() = Dispatchers.Unconfined
    }

    private val graphqlRepository = mockk<GraphqlRepository>()
    private lateinit var viewModel: PromoCheckoutListViewModel

    @Before
    fun setup(){
        viewModel = PromoCheckoutListViewModel(dispatcher, graphqlRepository)
    }

    @Test
    fun getPromoList_isSuccess(){
        //given
        val graphqlSuccessResponse = GraphqlResponse(
            mapOf<Type, Any>(DataPromoCheckoutList::class.java to DataPromoCheckoutList()),
            mapOf<Type, List<GraphqlError>>(),
            false)

        coEvery {
            graphqlRepository.getReseponse(any(), any())
        } returns graphqlSuccessResponse

        //then
        viewModel.getPromoList(SERVICE_ID, CATEGORY_ID, PAGE)

        //when
        assert(viewModel.dataPromoCheckoutList.value is Success)
        assert(viewModel.showLoading.value is Boolean)
        assertEquals(Success(SUCCESS_PROMO_LIST), (viewModel.dataPromoCheckoutList.value) as Success)
        assertEquals(false, viewModel.showLoading.value)
    }

    @Test
    fun getPromoList_isFailed(){
        //given
        val graphqlResponseFailed = GraphqlResponse(
            mapOf<Type, Any>(),
            mapOf<Type, List<GraphqlError>>(DataPromoCheckoutList::class.java to listOf(GraphqlError())),
            false)

        coEvery {
            graphqlRepository.getReseponse(any(), any())
        } returns graphqlResponseFailed

        //then
        viewModel.getPromoList(SERVICE_ID, CATEGORY_ID, PAGE)

        //when
        assert(viewModel.dataPromoCheckoutList.value is Fail)
        assert(viewModel.showLoading.value is Boolean)
        assertEquals(Fail(Throwable()), (viewModel.dataPromoCheckoutList.value) as Fail)
        assertEquals(false, viewModel.showLoading.value)
    }

    @Test
    fun getPromoLastSeen_isSuccess(){
        //given
        val graphqlSuccessResponse = GraphqlResponse(
            mapOf<Type, Any>(PromoCheckoutLastSeenModel.Response::class.java to PromoCheckoutLastSeenModel.Response()),
            mapOf<Type, List<GraphqlError>>(),
            false)

        coEvery {
            graphqlRepository.getReseponse(any(), any())
        } returns graphqlSuccessResponse

        //then
        viewModel.getPromoLastSeen(listOf(CATEGORY_ID))

        //when
        assert(viewModel.dataPromoCheckoutList.value is Success)
        assertEquals(Success(SUCCESS_PROMO_LAST_SEEN), (viewModel.dataPromoCheckoutList.value) as Success)
    }

    @Test
    fun getPromoLastSeen_isFailed(){
        //given
        val graphqlResponseFailed = GraphqlResponse(
            mapOf<Type, Any>(),
            mapOf<Type, List<GraphqlError>>(PromoCheckoutLastSeenModel.Response::class.java to listOf(GraphqlError())),
            false)

        coEvery {
            graphqlRepository.getReseponse(any(), any())
        } returns graphqlResponseFailed

        //then
        viewModel.getPromoLastSeen(listOf(CATEGORY_ID))

        //when
        assert(viewModel.dataPromoCheckoutList.value is Fail)
        assertEquals(Fail(Throwable()), (viewModel.dataPromoCheckoutList.value) as Fail)
    }
}