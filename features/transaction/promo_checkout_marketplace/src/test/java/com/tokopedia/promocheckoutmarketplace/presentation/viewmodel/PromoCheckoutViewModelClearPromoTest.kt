package com.tokopedia.promocheckoutmarketplace.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.promocheckoutmarketplace.ApplyPromoDataProvider.provideApplyPromoGlobalAndMerchantRequestInvalid
import com.tokopedia.promocheckoutmarketplace.ClearPromoDataProvider.provideClearPromoResponseFailed
import com.tokopedia.promocheckoutmarketplace.ClearPromoDataProvider.provideClearPromoResponseSuccess
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideCurrentUnSelectedCollapsedGlobalAndMerchantPromoData
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideCurrentUnSelectedExpandedGlobalAndMerchantPromoData
import com.tokopedia.promocheckoutmarketplace.data.response.ClearPromoResponse
import com.tokopedia.promocheckoutmarketplace.presentation.analytics.PromoCheckoutAnalytics
import com.tokopedia.promocheckoutmarketplace.presentation.mapper.PromoCheckoutUiModelMapper
import com.tokopedia.purchase_platform.common.feature.localizationchooseaddress.request.ChosenAddressRequestHelper
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.set

class PromoCheckoutViewModelClearPromoTest {

    private lateinit var viewModel: PromoCheckoutViewModel
    private lateinit var dispatcher: CoroutineDispatcher

    private var graphqlRepository: GraphqlRepository = mockk(relaxed = true)
    private var uiModelMapper: PromoCheckoutUiModelMapper = spyk()
    private var analytics: PromoCheckoutAnalytics = mockk()
    private var gson = Gson()
    private var chosenAddressRequestHelper: ChosenAddressRequestHelper = mockk(relaxed = true)

    @get: Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        dispatcher = Dispatchers.Unconfined
        viewModel = PromoCheckoutViewModel(dispatcher, graphqlRepository, uiModelMapper, analytics, gson, chosenAddressRequestHelper)
    }

    @Test
    fun `WHEN clear promo and success THEN clear promo response action state should not be null`() {
        //given
        val result = HashMap<Type, Any>()
        result[ClearPromoResponse::class.java] = provideClearPromoResponseSuccess()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)
        viewModel.setPromoListValue(provideCurrentUnSelectedExpandedGlobalAndMerchantPromoData())

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.clearPromo("", ValidateUsePromoRequest(), ArrayList())

        //then
        assertNotNull(viewModel.clearPromoResponse.value)
    }

    @Test
    fun `WHEN clear promo and success THEN clear promo response action state should be success`() {
        //given
        val validateUseRequest = provideApplyPromoGlobalAndMerchantRequestInvalid()
        val result = HashMap<Type, Any>()
        result[ClearPromoResponse::class.java] = provideClearPromoResponseSuccess()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)
        viewModel.setPromoListValue(provideCurrentUnSelectedExpandedGlobalAndMerchantPromoData())

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.clearPromo("", validateUseRequest, ArrayList())

        //then
        assert(viewModel.clearPromoResponse.value?.state == ClearPromoResponseAction.ACTION_STATE_SUCCESS)
    }

    @Test
    fun `WHEN clear promo and failed THEN clear promo response action state should not be null`() {
        //given
        val result = HashMap<Type, Any>()
        result[ClearPromoResponse::class.java] = provideClearPromoResponseFailed()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)
        viewModel.setPromoListValue(provideCurrentUnSelectedCollapsedGlobalAndMerchantPromoData())

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.clearPromo("", ValidateUsePromoRequest(), ArrayList())

        //then
        assertNotNull(viewModel.clearPromoResponse.value)
    }

    @Test
    fun `WHEN clear promo and failed THEN clear promo response action state should be error`() {
        //given
        val result = HashMap<Type, Any>()
        result[ClearPromoResponse::class.java] = provideClearPromoResponseFailed()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)
        viewModel.setPromoListValue(provideCurrentUnSelectedCollapsedGlobalAndMerchantPromoData())

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.clearPromo("", ValidateUsePromoRequest(), ArrayList())

        //then
        assert(viewModel.clearPromoResponse.value?.state == ClearPromoResponseAction.ACTION_STATE_ERROR)
    }

}