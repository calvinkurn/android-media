package com.tokopedia.promocheckoutmarketplace.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.promocheckoutmarketplace.ApplyPromoDataProvider.provideApplyPromoEmptyRequest
import com.tokopedia.promocheckoutmarketplace.ApplyPromoDataProvider.provideApplyPromoGlobalAndMerchantRequest
import com.tokopedia.promocheckoutmarketplace.ApplyPromoDataProvider.provideApplyPromoGlobalAndMerchantResponseSuccess
import com.tokopedia.promocheckoutmarketplace.ApplyPromoDataProvider.provideApplyPromoGlobalRequest
import com.tokopedia.promocheckoutmarketplace.ApplyPromoDataProvider.provideApplyPromoGlobalResponseFailed
import com.tokopedia.promocheckoutmarketplace.ApplyPromoDataProvider.provideApplyPromoGlobalResponseSuccess
import com.tokopedia.promocheckoutmarketplace.ApplyPromoDataProvider.provideApplyPromoMerchantRequest
import com.tokopedia.promocheckoutmarketplace.ApplyPromoDataProvider.provideApplyPromoMerchantResponseFailed
import com.tokopedia.promocheckoutmarketplace.ApplyPromoDataProvider.provideApplyPromoMerchantResponseSuccess
import com.tokopedia.promocheckoutmarketplace.ApplyPromoDataProvider.provideApplyPromoMerchantSuccessButGetRedState
import com.tokopedia.promocheckoutmarketplace.ApplyPromoDataProvider.provideApplyPromoResponseClashing
import com.tokopedia.promocheckoutmarketplace.ApplyPromoDataProvider.provideApplyPromoResponseError
import com.tokopedia.promocheckoutmarketplace.ApplyPromoDataProvider.provideApplyPromoResponseFailed
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideCurrentDisabledCollapsedGlobalPromoData
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideCurrentDisabledCollapsedMerchantPromoData
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideCurrentDisabledExpandedGlobalPromoData
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideCurrentDisabledExpandedMerchantPromoData
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideCurrentSelectedCollapsedGlobalPromoData
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideCurrentSelectedCollapsedMerchantPromoData
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideCurrentSelectedExpandedGlobalPromoData
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideCurrentSelectedExpandedMerchantPromoData
import com.tokopedia.promocheckoutmarketplace.presentation.analytics.PromoCheckoutAnalytics
import com.tokopedia.promocheckoutmarketplace.presentation.mapper.PromoCheckoutUiModelMapper
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.PromoRecommendationUiModel
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.response.validateuse.ValidateUseResponse
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
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

class PromoCheckoutViewModelApplyPromoTest {

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
    fun `WHEN apply promo and get success result THEN apply promo response action is not null`() {
        //given
        val request = provideApplyPromoGlobalAndMerchantRequest()
        val result = HashMap<Type, Any>()
        result[ValidateUseResponse::class.java] = provideApplyPromoGlobalAndMerchantResponseSuccess()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)
        viewModel.setPromoRecommendationValue(PromoRecommendationUiModel(
                uiData = PromoRecommendationUiModel.UiData().apply { promoCodes = listOf("THIRX598GSA7MADK2X7") },
                uiState = PromoRecommendationUiModel.UiState())
        )

        every { analytics.eventClickPakaiPromoSuccess(any(), any(), any()) } just Runs
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.applyPromo("", request, ArrayList())

        //then
        assertNotNull(viewModel.applyPromoResponseAction.value)
    }

    @Test
    fun `WHEN apply promo and get success result THEN apply promo response action state should be navigate to caller page`() {
        //given
        val request = provideApplyPromoGlobalAndMerchantRequest()
        val result = HashMap<Type, Any>()
        result[ValidateUseResponse::class.java] = provideApplyPromoGlobalAndMerchantResponseSuccess()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        every { analytics.eventClickPakaiPromoSuccess(any(), any(), any()) } just Runs
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.applyPromo("", request, ArrayList())

        //then
        assertNotNull(viewModel.applyPromoResponseAction.value?.state == ApplyPromoResponseAction.ACTION_NAVIGATE_TO_CALLER_PAGE)
    }

    @Test
    fun `WHEN apply promo success but have red state on voucher order THEN promo response action state should be error`() {
        //given
        val result = HashMap<Type, Any>()
        result[ValidateUseResponse::class.java] = provideApplyPromoMerchantSuccessButGetRedState()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        every { analytics.eventClickPakaiPromoSuccess(any(), any(), any()) } just Runs
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.applyPromo("", ValidateUsePromoRequest(), ArrayList())

        //then
        assert(viewModel.applyPromoResponseAction.value?.state == ApplyPromoResponseAction.ACTION_SHOW_TOAST_ERROR)
    }

    @Test
    fun `WHEN apply promo and get error result THEN apply promo response action state should be show error`() {
        //given
        val result = HashMap<Type, Any>()
        result[ValidateUseResponse::class.java] = provideApplyPromoResponseError()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        every { analytics.eventClickPakaiPromoSuccess(any(), any(), any()) } just Runs
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.applyPromo("", ValidateUsePromoRequest(), ArrayList())

        //then
        assertNotNull(viewModel.applyPromoResponseAction.value?.state == ApplyPromoResponseAction.ACTION_SHOW_TOAST_ERROR)
    }

    @Test
    fun `WHEN apply promo and get global failed result THEN apply promo response action state should be show error`() {
        //given
        val result = HashMap<Type, Any>()
        result[ValidateUseResponse::class.java] = provideApplyPromoGlobalResponseFailed()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)
        viewModel.setPromoListValue(provideCurrentSelectedExpandedGlobalPromoData())

        every { analytics.eventClickPakaiPromoSuccess(any(), any(), any()) } just Runs
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.applyPromo("", ValidateUsePromoRequest(), ArrayList())

        //then
        assertNotNull(viewModel.applyPromoResponseAction.value?.state == ApplyPromoResponseAction.ACTION_SHOW_TOAST_ERROR)
    }

    @Test
    fun `WHEN apply promo and get merchant failed result THEN apply promo response action state should be show error`() {
        //given
        val result = HashMap<Type, Any>()
        result[ValidateUseResponse::class.java] = provideApplyPromoMerchantResponseFailed()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)
        viewModel.setPromoListValue(provideCurrentSelectedCollapsedGlobalPromoData())

        every { analytics.eventClickPakaiPromoSuccess(any(), any(), any()) } just Runs
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.applyPromo("", ValidateUsePromoRequest(), ArrayList())

        //then
        assertNotNull(viewModel.applyPromoResponseAction.value?.state == ApplyPromoResponseAction.ACTION_SHOW_TOAST_ERROR)
    }

    @Test
    fun `WHEN apply promo and get global and merchant failed result THEN apply promo response action state should be show error`() {
        //given
        val result = HashMap<Type, Any>()
        result[ValidateUseResponse::class.java] = provideApplyPromoResponseFailed()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        every { analytics.eventClickPakaiPromoSuccess(any(), any(), any()) } just Runs
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.applyPromo("", ValidateUsePromoRequest(), ArrayList())

        //then
        assertNotNull(viewModel.applyPromoResponseAction.value?.state == ApplyPromoResponseAction.ACTION_SHOW_TOAST_ERROR)
    }

    @Test
    fun `WHEN apply promo and get clashing result THEN apply promo response action state should be show error and reload promo list`() {
        //given
        val result = HashMap<Type, Any>()
        result[ValidateUseResponse::class.java] = provideApplyPromoResponseClashing()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        every { analytics.eventClickPakaiPromoSuccess(any(), any(), any()) } just Runs
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.applyPromo("", ValidateUsePromoRequest(), ArrayList())

        //then
        assertNotNull(viewModel.applyPromoResponseAction.value?.state == ApplyPromoResponseAction.ACTION_SHOW_TOAST_AND_RELOAD_PROMO)
    }

    @Test
    fun `WHEN apply promo global and get success result THEN apply promo response action is not null`() {
        //given
        val request = provideApplyPromoGlobalRequest()
        val result = HashMap<Type, Any>()
        result[ValidateUseResponse::class.java] = provideApplyPromoGlobalResponseSuccess()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        every { analytics.eventClickPakaiPromoSuccess(any(), any(), any()) } just Runs
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.applyPromo("", request, ArrayList())

        //then
        assertNotNull(viewModel.applyPromoResponseAction.value)
    }

    @Test
    fun `WHEN apply promo global from expanded selected promo THEN promo request should contain promo global`() {
        //given
        val request = provideApplyPromoEmptyRequest()
        val result = HashMap<Type, Any>()
        result[ValidateUseResponse::class.java] = provideApplyPromoGlobalResponseSuccess()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)
        viewModel.setPromoListValue(provideCurrentSelectedExpandedGlobalPromoData())

        every { analytics.eventClickPakaiPromoSuccess(any(), any(), any()) } just Runs
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.applyPromo("", request, ArrayList())

        //then
        assert(request.codes.isNotEmpty())
    }

    @Test
    fun `WHEN apply promo global and promo is disabled and expanded THEN promo request should not contain disabled promo global`() {
        //given
        val request = provideApplyPromoGlobalRequest()
        val result = HashMap<Type, Any>()
        result[ValidateUseResponse::class.java] = provideApplyPromoGlobalResponseSuccess()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)
        viewModel.setPromoListValue(provideCurrentDisabledExpandedGlobalPromoData())

        every { analytics.eventClickPakaiPromoSuccess(any(), any(), any()) } just Runs
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.applyPromo("", request, ArrayList())

        //then
        assert(request.codes.isEmpty())
    }

    @Test
    fun `WHEN apply promo global from collapsed selected promo THEN promo request should contain promo global`() {
        //given
        val request = provideApplyPromoEmptyRequest()
        val result = HashMap<Type, Any>()
        result[ValidateUseResponse::class.java] = provideApplyPromoGlobalResponseSuccess()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)
        viewModel.setPromoListValue(provideCurrentSelectedCollapsedGlobalPromoData())

        every { analytics.eventClickPakaiPromoSuccess(any(), any(), any()) } just Runs
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.applyPromo("", request, ArrayList())

        //then
        assert(request.codes.isNotEmpty())
    }

    @Test
    fun `WHEN apply promo global and promo is disabled and collapsed THEN promo request should not contain disabled promo global`() {
        //given
        val request = provideApplyPromoGlobalRequest()
        val result = HashMap<Type, Any>()
        result[ValidateUseResponse::class.java] = provideApplyPromoGlobalResponseSuccess()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)
        viewModel.setPromoListValue(provideCurrentDisabledCollapsedGlobalPromoData())

        every { analytics.eventClickPakaiPromoSuccess(any(), any(), any()) } just Runs
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.applyPromo("", request, ArrayList())

        //then
        assert(request.codes.isEmpty())
    }

    @Test
    fun `WHEN apply promo merchant and get success result THEN apply promo response action is not null`() {
        //given
        val request = provideApplyPromoMerchantRequest()
        val result = HashMap<Type, Any>()
        result[ValidateUseResponse::class.java] = provideApplyPromoMerchantResponseSuccess()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        every { analytics.eventClickPakaiPromoSuccess(any(), any(), any()) } just Runs
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.applyPromo("", request, ArrayList())

        //then
        assertNotNull(viewModel.applyPromoResponseAction.value)
    }

    @Test
    fun `WHEN apply promo merchant from expanded selected promo THEN promo request should contain promo merchant`() {
        //given
        val request = provideApplyPromoEmptyRequest()
        val result = HashMap<Type, Any>()
        result[ValidateUseResponse::class.java] = provideApplyPromoMerchantResponseSuccess()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)
        viewModel.setPromoListValue(provideCurrentSelectedExpandedMerchantPromoData())

        every { analytics.eventClickPakaiPromoSuccess(any(), any(), any()) } just Runs
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.applyPromo("", request, ArrayList())

        //then
        assert(request.orders[0]?.codes?.isNotEmpty() == true)
    }

    @Test
    fun `WHEN apply promo merchant and promo is disabled and expanded THEN promo request should not contain disabled promo global`() {
        //given
        val request = provideApplyPromoMerchantRequest()
        val result = HashMap<Type, Any>()
        result[ValidateUseResponse::class.java] = provideApplyPromoMerchantResponseSuccess()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)
        viewModel.setPromoListValue(provideCurrentDisabledExpandedMerchantPromoData())

        every { analytics.eventClickPakaiPromoSuccess(any(), any(), any()) } just Runs
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.applyPromo("", request, ArrayList())

        //then
        assert(request.orders[0]?.codes?.isEmpty() == true)
    }

    @Test
    fun `WHEN apply promo merchant from collapsed selected promo THEN promo request should contain promo merchant`() {
        //given
        val request = provideApplyPromoEmptyRequest()
        val result = HashMap<Type, Any>()
        result[ValidateUseResponse::class.java] = provideApplyPromoMerchantResponseSuccess()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)
        viewModel.setPromoListValue(provideCurrentSelectedCollapsedMerchantPromoData())

        every { analytics.eventClickPakaiPromoSuccess(any(), any(), any()) } just Runs
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.applyPromo("", request, ArrayList())

        //then
        assert(request.orders[0]?.codes?.isNotEmpty() == true)
    }

    @Test
    fun `WHEN apply promo merchant and promo is disabled and collapsed THEN promo request should not contain disabled promo global`() {
        //given
        val request = provideApplyPromoMerchantRequest()
        val result = HashMap<Type, Any>()
        result[ValidateUseResponse::class.java] = provideApplyPromoMerchantResponseSuccess()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)
        viewModel.setPromoListValue(provideCurrentDisabledCollapsedMerchantPromoData())

        every { analytics.eventClickPakaiPromoSuccess(any(), any(), any()) } just Runs
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.applyPromo("", request, ArrayList())

        //then
        assert(request.orders[0]?.codes?.isEmpty() == true)
    }

}