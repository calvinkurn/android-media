package com.tokopedia.promocheckoutmarketplace.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.promocheckoutmarketplace.*
import com.tokopedia.promocheckoutmarketplace.data.response.CouponListRecommendationResponse
import com.tokopedia.promocheckoutmarketplace.presentation.analytics.PromoCheckoutAnalytics
import com.tokopedia.promocheckoutmarketplace.presentation.mapper.PromoCheckoutUiModelMapper
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.*
import com.tokopedia.purchase_platform.common.constant.PAGE_CART
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.reflect.Type

class PromoCheckoutViewModelTest {

    private lateinit var viewModel: PromoCheckoutViewModel
    private lateinit var gson: Gson
    private lateinit var dispatcher: CoroutineDispatcher

    @MockK(relaxed = true)
    private lateinit var graphqlRepository: GraphqlRepository
    @MockK
    private lateinit var uiModelMapper: PromoCheckoutUiModelMapper
    @MockK
    private lateinit var analytics: PromoCheckoutAnalytics

    @get: Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        dispatcher = Dispatchers.Unconfined
        viewModel = PromoCheckoutViewModel(dispatcher, graphqlRepository, uiModelMapper, analytics)
        gson = Gson()

        val fragmentUiModel = FragmentUiModel(FragmentUiModel.UiData(), FragmentUiModel.UiState())
        val promoRecommendationUiModel = PromoRecommendationUiModel(PromoRecommendationUiModel.UiData(), PromoRecommendationUiModel.UiState())
        val promoInputUiModel = PromoInputUiModel(PromoInputUiModel.UiData(), PromoInputUiModel.UiState())
        val promoEligibilityHeaderUiModel = PromoEligibilityHeaderUiModel(PromoEligibilityHeaderUiModel.UiData(), PromoEligibilityHeaderUiModel.UiState())
        val promoListHeaderUimodel = PromoListHeaderUiModel(PromoListHeaderUiModel.UiData(), PromoListHeaderUiModel.UiState())
        val promoListItemUiModel = PromoListItemUiModel(PromoListItemUiModel.UiData(), PromoListItemUiModel.UiState())

        every { viewModel.initFragmentUiModel(any()) } just Runs
        every { uiModelMapper.mapFragmentUiModel(any()) } returns fragmentUiModel
        every { uiModelMapper.mapPromoRecommendationUiModel(any()) } returns promoRecommendationUiModel
        every { uiModelMapper.mapPromoInputUiModel() } returns promoInputUiModel
        every { uiModelMapper.mapPromoEligibilityHeaderUiModel(any()) } returns promoEligibilityHeaderUiModel
        every { uiModelMapper.mapPromoListHeaderUiModel(any(), any(), any()) } returns promoListHeaderUimodel
        every { uiModelMapper.mapPromoListItemUiModel(any(), any(), any(), any()) } returns promoListItemUiModel
        every { analytics.eventViewAvailablePromoListEligiblePromo(any(), any()) } just Runs
        every { analytics.eventViewAvailablePromoListIneligibleProduct(any(), any()) } just Runs

        viewModel.initFragmentUiModel(PAGE_CART)
    }

    @Test
    fun `WHEN get coupon recommendation and get complete expanded data THEN ui model should not be null`() {
        //given
        val result = HashMap<Type, Any>()
        result[CouponListRecommendationResponse::class.java] = provideBasePromoResponseSuccessDataAllExpanded()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)
        val request = provideBasePromoRequestData()

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.loadData("", request, "")

        //then
        assertNotNull(viewModel.fragmentUiModel.value)
        assertNotNull(viewModel.promoRecommendationUiModel.value)
        assertNotNull(viewModel.promoInputUiModel.value)
        assertNotNull(viewModel.promoListUiModel.value)
    }

    @Test
    fun `WHEN get coupon recommendation and get complete collapsed data THEN ui model should not be null`() {
        //given
        val result = HashMap<Type, Any>()
        result[CouponListRecommendationResponse::class.java] = provideBasePromoResponseSuccessDataAllCollapsed()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)
        val request = provideBasePromoRequestData()

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.loadData("", request, "")

        //then
        assertNotNull(viewModel.fragmentUiModel.value)
        assertNotNull(viewModel.promoRecommendationUiModel.value)
        assertNotNull(viewModel.promoInputUiModel.value)
        assertNotNull(viewModel.promoListUiModel.value)
    }

    @Test
    fun `WHEN apply promo manual input THEN should be added to request param`() {
        //given
        val attemptedPromoCode = "PROMO_CODE"
        val result = HashMap<Type, Any>()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)
        val request = provideBasePromoRequestData()

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.loadData("", request, attemptedPromoCode)

        //then
        assert(request.skipApply == 0)
        assert(request.attemptedCodes.isNotEmpty())
        assert(request.attemptedCodes.size == 1)
        assert(request.attemptedCodes[0] == attemptedPromoCode)
    }

    @Test
    fun `WHEN re apply global promo manual input THEN should clear previous manual input code on request param`() {
        //given
        val attemptedPromoCode = "PROMO_CODE"
        val result = HashMap<Type, Any>()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)
        val request = provideBasePromoRequestData()
        request.codes.add(attemptedPromoCode)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.loadData("", request, attemptedPromoCode)

        //then
        assert(request.attemptedCodes[0] == attemptedPromoCode)
        assert(!request.codes.contains(attemptedPromoCode))
    }

    @Test
    fun `WHEN re apply merchant promo manual input THEN should clear previous manual input code on request param`() {
        //given
        val attemptedPromoCode = "PROMO_CODE"
        val result = HashMap<Type, Any>()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)
        val request = provideBasePromoRequestData()
        request.orders[0].codes.add(attemptedPromoCode)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.loadData("", request, attemptedPromoCode)

        //then
        assert(request.attemptedCodes[0] == attemptedPromoCode)
        assert(!request.orders[0].codes.contains(attemptedPromoCode))
    }

    @Test
    fun `WHEN apply manual input promo has param selected expanded global promo but already unselected THEN request param should not contain global promo code`() {
        //given
        val result = HashMap<Type, Any>()
        result[CouponListRecommendationResponse::class.java] = provideBasePromoResponseSuccessDataAllExpanded()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)
        viewModel._promoListUiModel.value = provideNoCurrentSelectedExpandedGlobalPromoData()
        val promoRequest = providePromoRequestWithSelectedExpandedGlobalPromo()

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.loadData("", promoRequest, "GLOBAL PROMO")

        //then
        assert(promoRequest.codes.isEmpty())
    }

    @Test
    fun `WHEN apply manual input promo has param selected expanded merchant promo but already unselected THEN request param should not contain merchant promo code`() {
        //given
        val result = HashMap<Type, Any>()
        result[CouponListRecommendationResponse::class.java] = provideBasePromoResponseSuccessDataAllExpanded()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)
        viewModel._promoListUiModel.value = provideNoCurrentSelectedExpandedMerchantPromoData()
        val promoRequest = providePromoRequestWithSelectedExpandedMerchantPromo()

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.loadData("", promoRequest, "MERCHANT PROMO")

        //then
        assert(promoRequest.orders[0].codes.isEmpty())
        assert(promoRequest.orders[1].codes.isEmpty())
        assert(promoRequest.orders[2].codes.isEmpty())
    }

    @Test
    fun `WHEN apply manual input promo has param selected collapsed global promo but already unselected THEN request param should not contain global promo code`() {
        //given
        val result = HashMap<Type, Any>()
        result[CouponListRecommendationResponse::class.java] = provideBasePromoResponseSuccessDataAllExpanded()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)
        viewModel._promoListUiModel.value = provideNoCurrentSelectedCollapsedGlobalPromoData()
        val promoRequest = providePromoRequestWithSelectedCollapsedGlobalPromo()

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.loadData("", promoRequest, "GLOBAL PROMO")

        //then
        assert(promoRequest.codes.isEmpty())
    }

    @Test
    fun `WHEN apply manual input promo has param selected collapsed merchant promo but already unselected THEN request param should not contain merchant promo code`() {
        //given
        val result = HashMap<Type, Any>()
        result[CouponListRecommendationResponse::class.java] = provideBasePromoResponseSuccessDataAllExpanded()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)
        viewModel._promoListUiModel.value = provideNoCurrentSelectedCollapsedMerchantPromoData()
        val promoRequest = providePromoRequestWithSelectedCollapsedMerchantPromo()

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.loadData("", promoRequest, "MERCHANT PROMO")

        //then
        assert(promoRequest.orders[0].codes.isEmpty())
        assert(promoRequest.orders[1].codes.isEmpty())
        assert(promoRequest.orders[2].codes.isEmpty())
    }

    @Test
    fun `WHEN reload action has selected expanded global promo THEN should be added to request param`() {
        //given
        val result = HashMap<Type, Any>()
        result[CouponListRecommendationResponse::class.java] = provideBasePromoResponseSuccessDataAllExpanded()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)
        viewModel._promoListUiModel.value = provideCurrentSelectedExpandedGlobalPromoData()
        val promoRequest = provideBasePromoRequestData()

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.loadData("", promoRequest, "")

        //then
        assert(promoRequest.codes.isNotEmpty())
        assert(promoRequest.codes.size == 1)
    }

    @Test
    fun `WHEN reload action has selected expanded merchant promo THEN should be added to request param`() {
        //given
        val result = HashMap<Type, Any>()
        result[CouponListRecommendationResponse::class.java] = provideBasePromoResponseSuccessDataAllExpanded()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)
        viewModel._promoListUiModel.value = provideCurrentSelectedExpandedMerchantPromoData()
        val promoRequest = provideBasePromoRequestData()

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.loadData("", promoRequest, "")

        //then
        assert(promoRequest.orders[0].codes.isNotEmpty() ||
                promoRequest.orders[1].codes.isNotEmpty() ||
                promoRequest.orders[2].codes.isNotEmpty())
        assert(promoRequest.orders[0].codes.size == 1 ||
                promoRequest.orders[1].codes.size == 1 ||
                promoRequest.orders[2].codes.size == 1)
    }

    @Test
    fun `WHEN reload action has selected collapsed global promo THEN should be added to request param`() {
        //given
        val result = HashMap<Type, Any>()
        result[CouponListRecommendationResponse::class.java] = provideBasePromoResponseSuccessDataAllCollapsed()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)
        viewModel._promoListUiModel.value = provideCurrentSelectedCollapsedGlobalPromoData()
        val promoRequest = provideBasePromoRequestData()

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.loadData("", promoRequest, "")

        //then
        assert(promoRequest.codes.isNotEmpty())
        assert(promoRequest.codes.size == 1)
    }

    @Test
    fun `WHEN reload action has selected collapsed merchant promo THEN should be added to request param`() {
        //given
        val result = HashMap<Type, Any>()
        result[CouponListRecommendationResponse::class.java] = provideBasePromoResponseSuccessDataAllCollapsed()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)
        viewModel._promoListUiModel.value = provideCurrentSelectedCollapsedMerchantPromoData()
        val promoRequest = provideBasePromoRequestData()

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.loadData("", promoRequest, "")

        //then
        assert(promoRequest.orders[0].codes.isNotEmpty() ||
                promoRequest.orders[1].codes.isNotEmpty() ||
                promoRequest.orders[2].codes.isNotEmpty())
        assert(promoRequest.orders[0].codes.size == 1 ||
                promoRequest.orders[1].codes.size == 1 ||
                promoRequest.orders[2].codes.size == 1)
    }

}