package com.tokopedia.promocheckout.list.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.JsonObject
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.promocheckout.list.domain.DealsCheckVoucherUseCase
import com.tokopedia.promocheckout.list.model.listtravelcollectivebanner.PromoChekoutDealsBannerModel
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
class PromoCheckoutListDealsViewModelTest{
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

    private val dealsCheckVoucherUseCase: DealsCheckVoucherUseCase = mockk(relaxed = true)
    private val graphqlRepository = mockk<GraphqlRepository>()
    private lateinit var viewModel: PromoCheckoutListDealsViewModel
    private val jsonObject: JsonObject = JsonObject()

    @Before
    fun setup(){
        viewModel = PromoCheckoutListDealsViewModel(dispatcher, graphqlRepository,dealsCheckVoucherUseCase)
    }

    @Test
    fun checkPromo_isSuccess(){
        //given
        coEvery {
            dealsCheckVoucherUseCase.execute(any() as HashMap<String, Boolean>, any())
        } returns Success(DUMMY_DATA_UI_MODEL)

        //then
        viewModel.processCheckDealPromoCode(false, jsonObject)

        //when
        assert(viewModel.dealsCheckVoucherResult.value is Success)
        assert(viewModel.showLoadingPromoDeals.value is Boolean)
        assertEquals(Success(DUMMY_DATA_UI_MODEL), (viewModel.dealsCheckVoucherResult.value) as Success)
        assertEquals(false, viewModel.showLoadingPromoDeals.value)
    }

    @Test
    fun checkPromo_isFailed(){
        //given
        coEvery {
            dealsCheckVoucherUseCase.execute(any() as HashMap<String, Boolean>, any())
        } returns Fail(FAILED_CHECK_VOUCHER_MESSAGE_EXCEPTION)

        //then
        viewModel.processCheckDealPromoCode(false, jsonObject)

        //when
        assert(viewModel.dealsCheckVoucherResult.value is Fail)
        assert(viewModel.showLoadingPromoDeals.value is Boolean)
        assertEquals(Fail(FAILED_CHECK_VOUCHER_MESSAGE_EXCEPTION), (viewModel.dealsCheckVoucherResult.value) as Fail)
        assertEquals(false, viewModel.showLoadingPromoDeals.value)
    }

    @Test
    fun checkPromo_isGeneralError(){
        //given
        coEvery {
            dealsCheckVoucherUseCase.execute(any() as HashMap<String, Boolean>, any())
        } returns Fail(FAILED_CHECK_VOUCHER_GENERAL_EXCEPTION)

        //then
        viewModel.processCheckDealPromoCode(false, jsonObject)

        //when
        assert(viewModel.dealsCheckVoucherResult.value is Fail)
        assert(viewModel.showLoadingPromoDeals.value is Boolean)
        assertEquals(Fail(FAILED_CHECK_VOUCHER_GENERAL_EXCEPTION), (viewModel.dealsCheckVoucherResult.value) as Fail)
        assertEquals(false, viewModel.showLoadingPromoDeals.value)
    }

    @Test
    fun getListTravelCollectiveBanner_isSuccess(){
        //given
        val graphqlSuccessResponse = GraphqlResponse(
            mapOf<Type, Any>(PromoChekoutDealsBannerModel.Response::class.java to PromoChekoutDealsBannerModel.Response()),
            mapOf<Type, List<GraphqlError>>(),
            false)

        coEvery {
            graphqlRepository.getReseponse(any(), any())
        } returns graphqlSuccessResponse

        //then
        viewModel.getListTravelCollectiveBanner()

        //when
        assert(viewModel.listTravelCollectiveBanner.value is Success)
        assertEquals(Success(SUCCESS_PROMO_LAST_SEEN), (viewModel.listTravelCollectiveBanner.value) as Success)
    }

    @Test
    fun getListTravelCollectiveBanner_isFailed(){
        //given
        val graphqlResponseFailed = GraphqlResponse(
            mapOf<Type, Any>(),
            mapOf<Type, List<GraphqlError>>(
                PromoChekoutDealsBannerModel.Response::class.java to listOf(
                    GraphqlError()
                )),
            false)

        coEvery {
            graphqlRepository.getReseponse(any(), any())
        } returns graphqlResponseFailed

        //then
        viewModel.getListTravelCollectiveBanner()

        //when
        assert(viewModel.listTravelCollectiveBanner.value is Fail)
        assertEquals(Fail(Throwable()), (viewModel.listTravelCollectiveBanner.value) as Fail)
    }
}