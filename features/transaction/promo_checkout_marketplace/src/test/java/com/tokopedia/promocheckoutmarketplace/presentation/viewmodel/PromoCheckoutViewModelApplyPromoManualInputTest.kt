package com.tokopedia.promocheckoutmarketplace.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.localizationchooseaddress.util.ChosenAddressRequestHelper
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideGetPromoListRequest
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideGetPromoListResponseApplyManualFailed
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideGetPromoListResponseEmptyStateCouponListEmpty
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideGetPromoListResponseEmptyStateEmpty
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideGetPromoListResponseSuccessAllExpanded
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideNoCurrentSelectedCollapsedGlobalPromoData
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideNoCurrentSelectedCollapsedMerchantPromoData
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideNoCurrentSelectedExpandedGlobalPromoData
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideNoCurrentSelectedExpandedMerchantPromoData
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.providePromoRequestWithSelectedCollapsedGlobalPromo
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.providePromoRequestWithSelectedCollapsedMerchantPromo
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.providePromoRequestWithSelectedExpandedGlobalPromo
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.providePromoRequestWithSelectedExpandedMerchantPromo
import com.tokopedia.promocheckoutmarketplace.data.response.CouponListRecommendationResponse
import com.tokopedia.promocheckoutmarketplace.presentation.analytics.PromoCheckoutAnalytics
import com.tokopedia.promocheckoutmarketplace.presentation.mapper.PromoCheckoutUiModelMapper
import com.tokopedia.purchase_platform.common.constant.PAGE_CART
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest
import io.mockk.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.reflect.Type

class PromoCheckoutViewModelApplyPromoManualInputTest {

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

        every { analytics.eventViewAvailablePromoListEligiblePromo(any(), any()) } just Runs
        every { analytics.eventViewAvailablePromoListIneligibleProduct(any(), any()) } just Runs

        viewModel.initFragmentUiModel(PAGE_CART)
    }

    @Test
    fun `WHEN apply promo manual input THEN skip apply should be 0`() {
        //given
        val attemptedPromoCode = "PROMO_CODE"
        val result = HashMap<Type, Any>()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)
        val promoRequest = provideGetPromoListRequest()

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.getPromoList("", promoRequest, attemptedPromoCode)

        //then
        assert(promoRequest.skipApply == 0)
    }

    @Test
    fun `WHEN apply promo manual input THEN should be added to request param`() {
        //given
        val attemptedPromoCode = "PROMO_CODE"
        val result = HashMap<Type, Any>()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)
        val promoRequest = provideGetPromoListRequest()

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.getPromoList("", promoRequest, attemptedPromoCode)

        //then
        assert(promoRequest.attemptedCodes[0] == attemptedPromoCode)
    }

    @Test
    fun `WHEN re apply promo manual input THEN attempted code request param should be equal with typed promo code`() {
        //given
        val attemptedPromoCode = "PROMO_CODE"
        val result = HashMap<Type, Any>()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)
        val promoRequest = provideGetPromoListRequest()
        promoRequest.codes.add(attemptedPromoCode)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.getPromoList("", promoRequest, attemptedPromoCode)

        //then
        assert(promoRequest.attemptedCodes[0] == attemptedPromoCode)
    }

    @Test
    fun `WHEN re apply promo manual input THEN global promo request should not contain attempted promo code`() {
        //given
        val attemptedPromoCode = "PROMO_CODE"
        val result = HashMap<Type, Any>()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)
        val promoRequest = provideGetPromoListRequest()
        promoRequest.codes.add(attemptedPromoCode)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.getPromoList("", promoRequest, attemptedPromoCode)

        //then
        assert(!promoRequest.codes.contains(attemptedPromoCode))
    }

    @Test
    fun `WHEN re apply promo manual input THEN merchant promo request should not contain attempted promo code`() {
        //given
        val attemptedPromoCode = "PROMO_CODE"
        val result = HashMap<Type, Any>()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)
        val promoRequest = provideGetPromoListRequest()
        promoRequest.orders[0].codes.add(attemptedPromoCode)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.getPromoList("", promoRequest, attemptedPromoCode)

        //then
        assert(!promoRequest.orders[0].codes.contains(attemptedPromoCode))
    }

    @Test
    fun `WHEN apply manual input promo has param selected expanded global promo but already unselected THEN request param should not contain global promo code`() {
        //given
        val result = HashMap<Type, Any>()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)
        viewModel.setPromoListValue(provideNoCurrentSelectedExpandedGlobalPromoData())
        val promoRequest = providePromoRequestWithSelectedExpandedGlobalPromo()

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.getPromoList("", promoRequest, "GLOBAL PROMO")

        //then
        assert(promoRequest.codes.isEmpty())
    }

    @Test
    fun `WHEN apply manual input promo has param selected expanded merchant promo but already unselected THEN request param should not contain merchant promo code`() {
        //given
        val result = HashMap<Type, Any>()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)
        viewModel.setPromoListValue(provideNoCurrentSelectedExpandedMerchantPromoData())
        val promoRequest = providePromoRequestWithSelectedExpandedMerchantPromo()

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.getPromoList("", promoRequest, "MERCHANT PROMO")

        //then
        assert(promoRequest.orders[0].codes.isEmpty())
    }

    @Test
    fun `WHEN apply manual input promo has param selected collapsed global promo but already unselected THEN request param should not contain global promo code`() {
        //given
        val result = HashMap<Type, Any>()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)
        viewModel.setPromoListValue(provideNoCurrentSelectedCollapsedGlobalPromoData())
        val promoRequest = providePromoRequestWithSelectedCollapsedGlobalPromo()

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.getPromoList("", promoRequest, "GLOBAL PROMO")

        //then
        assert(promoRequest.codes.isEmpty())
    }

    @Test
    fun `WHEN apply manual input promo has param selected collapsed merchant promo but already unselected THEN request param should not contain merchant promo code`() {
        //given
        val result = HashMap<Type, Any>()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)
        viewModel.setPromoListValue(provideNoCurrentSelectedCollapsedMerchantPromoData())
        val promoRequest = providePromoRequestWithSelectedCollapsedMerchantPromo()

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.getPromoList("", promoRequest, "MERCHANT PROMO")

        //then
        assert(promoRequest.orders[0].codes.isEmpty())
    }

    @Test
    fun `WHEN re apply promo manual input THEN get coupon list action state should be clear data`() {
        //given
        val attemptedPromoCode = "PROMO_CODE"
        val result = HashMap<Type, Any>()
        result[CouponListRecommendationResponse::class.java] = provideGetPromoListResponseSuccessAllExpanded()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)
        val promoRequest = provideGetPromoListRequest()
        promoRequest.codes.add(attemptedPromoCode)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.getPromoList("", PromoRequest(), attemptedPromoCode)

        //then
        assert(viewModel.getPromoListResponseAction.value?.state == GetPromoListResponseAction.ACTION_CLEAR_DATA)
    }

    @Test
    fun `WHEN apply manual input promo THEN get error`() {
        //given
        val result = HashMap<Type, Any>()
        result[CouponListRecommendationResponse::class.java] = provideGetPromoListResponseApplyManualFailed()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.getPromoList("", PromoRequest(), "PROMO_CODE")

        //then
        assert(viewModel.promoInputUiModel.value?.uiState?.isError == true)
    }

    @Test
    fun `WHEN apply manual input promo THEN get exception`() {
        //given
        val result = HashMap<Type, Any>()
        result[CouponListRecommendationResponse::class.java] = provideGetPromoListResponseApplyManualFailed()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.getPromoList("", PromoRequest(), "PROMO_CODE")

        //then
        assert(viewModel.promoInputUiModel.value?.uiData?.exception != null)
    }

    @Test
    fun `WHEN apply manual input promo and get empty data THEN promo list state should be show toast error`() {
        //given
        val result = HashMap<Type, Any>()
        result[CouponListRecommendationResponse::class.java] = provideGetPromoListResponseEmptyStateEmpty()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.getPromoList("", PromoRequest(), "PROMO CODE")

        //then
        assert(viewModel.getPromoListResponseAction.value?.state == GetPromoListResponseAction.ACTION_SHOW_TOAST_ERROR)
    }

    @Test
    fun `WHEN apply manual input promo and get empty data THEN promo input state should be error`() {
        //given
        val result = HashMap<Type, Any>()
        result[CouponListRecommendationResponse::class.java] = provideGetPromoListResponseEmptyStateCouponListEmpty()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)
        viewModel.initPromoInput()

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.getPromoList("", PromoRequest(), "PROMO CODE")

        //then
        assert(viewModel.promoInputUiModel.value?.uiState?.isError == true)
    }

}