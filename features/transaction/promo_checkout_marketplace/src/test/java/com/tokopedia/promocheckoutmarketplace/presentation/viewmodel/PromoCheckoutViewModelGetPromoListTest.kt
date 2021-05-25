package com.tokopedia.promocheckoutmarketplace.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.localizationchooseaddress.util.ChosenAddressRequestHelper
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideCurrentSelectedCollapsedGlobalPromoData
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideCurrentSelectedCollapsedMerchantPromoData
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideCurrentSelectedExpandedGlobalPromoData
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideCurrentSelectedExpandedMerchantPromoData
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideGetPromoListRequest
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideGetPromoListResponseEmptyStateBlacklisted
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideGetPromoListResponseEmptyStateCouponListEmpty
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideGetPromoListResponseEmptyStateEmpty
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideGetPromoListResponseEmptyStatePhoneVerification
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideGetPromoListResponseEmptyStateUnknown
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideGetPromoListResponseError
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideGetPromoListResponseSuccessAllCollapsed
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideGetPromoListResponseSuccessAllEligible
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideGetPromoListResponseSuccessAllExpanded
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideGetPromoListResponseSuccessAllIneligible
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideGetPromoListResponseSuccessWithPreSelectedPromo
import com.tokopedia.promocheckoutmarketplace.data.response.CouponListRecommendationResponse
import com.tokopedia.promocheckoutmarketplace.presentation.analytics.PromoCheckoutAnalytics
import com.tokopedia.promocheckoutmarketplace.presentation.mapper.PromoCheckoutUiModelMapper
import com.tokopedia.purchase_platform.common.constant.PAGE_CART
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest
import io.mockk.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.reflect.Type

class PromoCheckoutViewModelGetPromoListTest {

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
    fun `WHEN get promo list and get complete expanded data THEN fragment ui model should not be null and state success`() {
        //given
        val result = HashMap<Type, Any>()
        result[CouponListRecommendationResponse::class.java] = provideGetPromoListResponseSuccessAllExpanded()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.getPromoList("", PromoRequest(), "")

        //then
        assertNotNull(viewModel.fragmentUiModel.value)
        assert(viewModel.fragmentUiModel.value?.uiState?.hasFailedToLoad == false)
    }

    @Test
    fun `WHEN get promo list and get complete expanded data THEN promoRecommendation ui model should not be null`() {
        //given
        val result = HashMap<Type, Any>()
        result[CouponListRecommendationResponse::class.java] = provideGetPromoListResponseSuccessAllExpanded()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.getPromoList("", PromoRequest(), "")

        //then
        assertNotNull(viewModel.promoRecommendationUiModel.value)
    }

    @Test
    fun `WHEN get promo list and get complete expanded data THEN promo input ui model should not be null`() {
        //given
        val result = HashMap<Type, Any>()
        result[CouponListRecommendationResponse::class.java] = provideGetPromoListResponseSuccessAllExpanded()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.getPromoList("", PromoRequest(), "")

        //then
        assertNotNull(viewModel.promoInputUiModel.value)
    }

    @Test
    fun `WHEN get promo list and get complete expanded data THEN promo list ui model should not be null`() {
        //given
        val result = HashMap<Type, Any>()
        result[CouponListRecommendationResponse::class.java] = provideGetPromoListResponseSuccessAllExpanded()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.getPromoList("", PromoRequest(), "")

        //then
        assertNotNull(viewModel.promoListUiModel.value)
    }

    @Test
    fun `WHEN get promo list and get complete collapsed data THEN fragment ui model should not be null and state success`() {
        //given
        val result = HashMap<Type, Any>()
        result[CouponListRecommendationResponse::class.java] = provideGetPromoListResponseSuccessAllCollapsed()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.getPromoList("", PromoRequest(), "")

        //then
        assertNotNull(viewModel.fragmentUiModel.value)
        assert(viewModel.fragmentUiModel.value?.uiState?.hasFailedToLoad == false)
    }

    @Test
    fun `WHEN get promo list and get complete collapsed data THEN promo recommendation ui model should not be null`() {
        //given
        val result = HashMap<Type, Any>()
        result[CouponListRecommendationResponse::class.java] = provideGetPromoListResponseSuccessAllCollapsed()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.getPromoList("", PromoRequest(), "")

        //then
        assertNotNull(viewModel.promoRecommendationUiModel.value)
    }

    @Test
    fun `WHEN get promo list and get complete collapsed data THEN promo input ui model should not be null`() {
        //given
        val result = HashMap<Type, Any>()
        result[CouponListRecommendationResponse::class.java] = provideGetPromoListResponseSuccessAllCollapsed()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.getPromoList("", PromoRequest(), "")

        //then
        assertNotNull(viewModel.promoInputUiModel.value)
    }

    @Test
    fun `WHEN get promo list and get complete collapsed data THEN promo list ui model should not be null`() {
        //given
        val result = HashMap<Type, Any>()
        result[CouponListRecommendationResponse::class.java] = provideGetPromoListResponseSuccessAllCollapsed()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.getPromoList("", PromoRequest(), "")

        //then
        assertNotNull(viewModel.promoListUiModel.value)
    }

    @Test
    fun `WHEN get promo list and get no selected promo THEN fragment ui model state should be has no promo`() {
        //given
        val result = HashMap<Type, Any>()
        result[CouponListRecommendationResponse::class.java] = provideGetPromoListResponseSuccessAllExpanded()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.getPromoList("", PromoRequest(), "")

        //then
        assert(viewModel.fragmentUiModel.value?.uiState?.hasAnyPromoSelected == false)
    }

    @Test
    fun `WHEN get promo list and get selected promo THEN fragment ui model state should be has promo`() {
        //given
        val result = HashMap<Type, Any>()
        result[CouponListRecommendationResponse::class.java] = provideGetPromoListResponseSuccessWithPreSelectedPromo()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.getPromoList("", PromoRequest(), "")

        //then
        assert(viewModel.fragmentUiModel.value?.uiState?.hasAnyPromoSelected == true)
    }

    @Test
    fun `WHEN get promo list and all eligible THEN fragment ui model should not be null and state success`() {
        //given
        val result = HashMap<Type, Any>()
        result[CouponListRecommendationResponse::class.java] = provideGetPromoListResponseSuccessAllEligible()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.getPromoList("", PromoRequest(), "")

        //then
        assertNotNull(viewModel.fragmentUiModel.value)
        assert(viewModel.fragmentUiModel.value?.uiState?.hasFailedToLoad == false)
    }

    @Test
    fun `WHEN get promo list and all eligible THEN promo input ui model should not be null`() {
        //given
        val result = HashMap<Type, Any>()
        result[CouponListRecommendationResponse::class.java] = provideGetPromoListResponseSuccessAllEligible()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.getPromoList("", PromoRequest(), "")

        //then
        assertNotNull(viewModel.promoInputUiModel.value)
    }

    @Test
    fun `WHEN get promo list and all eligible THEN promo list ui model should not be null`() {
        //given
        val result = HashMap<Type, Any>()
        result[CouponListRecommendationResponse::class.java] = provideGetPromoListResponseSuccessAllEligible()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.getPromoList("", PromoRequest(), "")

        //then
        assertNotNull(viewModel.promoListUiModel.value)
    }

    @Test
    fun `WHEN get promo list and all eligible THEN promo recommendation ui model should not be null`() {
        //given
        val result = HashMap<Type, Any>()
        result[CouponListRecommendationResponse::class.java] = provideGetPromoListResponseSuccessAllEligible()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.getPromoList("", PromoRequest(), "")

        //then
        assertNotNull(viewModel.promoRecommendationUiModel.value)
    }

    @Test
    fun `WHEN get promo list and all ineligible THEN fragment ui model should not be null and state success`() {
        //given
        val result = HashMap<Type, Any>()
        result[CouponListRecommendationResponse::class.java] = provideGetPromoListResponseSuccessAllIneligible()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.getPromoList("", PromoRequest(), "")

        //then
        assertNotNull(viewModel.fragmentUiModel.value)
        assert(viewModel.fragmentUiModel.value?.uiState?.hasFailedToLoad == false)
    }

    @Test
    fun `WHEN get promo list and all ineligible THEN promo input ui model should not be null`() {
        //given
        val result = HashMap<Type, Any>()
        result[CouponListRecommendationResponse::class.java] = provideGetPromoListResponseSuccessAllIneligible()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.getPromoList("", PromoRequest(), "")

        //then
        assertNotNull(viewModel.promoInputUiModel.value)
    }

    @Test
    fun `WHEN get promo list and all ineligible THEN promo list ui model should not be null`() {
        //given
        val result = HashMap<Type, Any>()
        result[CouponListRecommendationResponse::class.java] = provideGetPromoListResponseSuccessAllIneligible()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.getPromoList("", PromoRequest(), "")

        //then
        assertNotNull(viewModel.promoListUiModel.value)
    }

    @Test
    fun `WHEN get promo list and all ineligible THEN promo recommendation ui model should be null`() {
        //given
        val result = HashMap<Type, Any>()
        result[CouponListRecommendationResponse::class.java] = provideGetPromoListResponseSuccessAllIneligible()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.getPromoList("", PromoRequest(), "")

        //then
        assertNull(viewModel.promoRecommendationUiModel.value)
    }

    @Test
    fun `WHEN reload action has selected expanded global promo THEN should be added to request param`() {
        //given
        val result = HashMap<Type, Any>()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)
        viewModel.setPromoListValue(provideCurrentSelectedExpandedGlobalPromoData())
        val promoRequest = provideGetPromoListRequest()

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.getPromoList("", promoRequest, "")

        //then
        assert(promoRequest.codes.isNotEmpty())
    }

    @Test
    fun `WHEN reload action has selected expanded merchant promo THEN should be added to request param`() {
        //given
        val result = HashMap<Type, Any>()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)
        viewModel.setPromoListValue(provideCurrentSelectedExpandedMerchantPromoData())
        val promoRequest = provideGetPromoListRequest()

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.getPromoList("", promoRequest, "")

        //then
        assert(promoRequest.orders[0].codes.isNotEmpty() ||
                promoRequest.orders[1].codes.isNotEmpty() ||
                promoRequest.orders[2].codes.isNotEmpty())
    }

    @Test
    fun `WHEN reload action has selected collapsed global promo THEN should be added to request param`() {
        //given
        val result = HashMap<Type, Any>()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)
        viewModel.setPromoListValue(provideCurrentSelectedCollapsedGlobalPromoData())
        val promoRequest = provideGetPromoListRequest()

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.getPromoList("", promoRequest, "")

        //then
        assert(promoRequest.codes.isNotEmpty())
    }

    @Test
    fun `WHEN reload action has selected collapsed merchant promo THEN should be added to request param`() {
        //given
        val result = HashMap<Type, Any>()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)
        viewModel.setPromoListValue(provideCurrentSelectedCollapsedMerchantPromoData())
        val promoRequest = provideGetPromoListRequest()

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.getPromoList("", promoRequest, "")

        //then
        assert(promoRequest.orders[0].codes.isNotEmpty() ||
                promoRequest.orders[1].codes.isNotEmpty() ||
                promoRequest.orders[2].codes.isNotEmpty())
    }

    @Test
    fun `WHEN get promo list and get response error THEN fragment state should be failed to load`() {
        //given
        val result = HashMap<Type, Any>()
        result[CouponListRecommendationResponse::class.java] = provideGetPromoListResponseError()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.getPromoList("", PromoRequest(), "")

        //then
        assert(viewModel.fragmentUiModel.value?.uiState?.hasFailedToLoad == true)
    }

    @Test
    fun `WHEN get promo list empty and empty state also empty THEN fragment state should be failed to load`() {
        //given
        val result = HashMap<Type, Any>()
        result[CouponListRecommendationResponse::class.java] = provideGetPromoListResponseEmptyStateEmpty()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.getPromoList("", PromoRequest(), "")

        //then
        assert(viewModel.fragmentUiModel.value?.uiState?.hasFailedToLoad == true)
    }

    @Test
    fun `WHEN get promo list and status is coupon list is empty THEN should show empty state`() {
        //given
        val result = HashMap<Type, Any>()
        result[CouponListRecommendationResponse::class.java] = provideGetPromoListResponseEmptyStateCouponListEmpty()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        every { analytics.eventViewAvailablePromoListNoPromo(any()) } just Runs
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.getPromoList("", PromoRequest(), "")

        //then
        assertNotNull(viewModel.promoEmptyStateUiModel.value)
    }

    @Test
    fun `WHEN get promo list and status is coupon list is empty THEN should not show button action`() {
        //given
        val result = HashMap<Type, Any>()
        result[CouponListRecommendationResponse::class.java] = provideGetPromoListResponseEmptyStateCouponListEmpty()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        every { analytics.eventViewAvailablePromoListNoPromo(any()) } just Runs
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.getPromoList("", PromoRequest(), "")

        //then
        assert(viewModel.promoEmptyStateUiModel.value?.uiState?.isShowButton == false)
    }

    @Test
    fun `WHEN get promo list and status is coupon list is empty THEN should show promo input`() {
        //given
        val result = HashMap<Type, Any>()
        result[CouponListRecommendationResponse::class.java] = provideGetPromoListResponseEmptyStateCouponListEmpty()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.getPromoList("", PromoRequest(), "")

        //then
        assertNotNull(viewModel.promoInputUiModel.value)
    }

    @Test
    fun `WHEN get promo list and status is phone not verified THEN should show empty state`() {
        //given
        val result = HashMap<Type, Any>()
        result[CouponListRecommendationResponse::class.java] = provideGetPromoListResponseEmptyStatePhoneVerification()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        every { analytics.eventViewPhoneVerificationMessage(any()) } just Runs
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.getPromoList("", PromoRequest(), "")

        //then
        assertNotNull(viewModel.promoEmptyStateUiModel.value)
    }

    @Test
    fun `WHEN get promo list and status is phone not verified THEN should show button action`() {
        //given
        val result = HashMap<Type, Any>()
        result[CouponListRecommendationResponse::class.java] = provideGetPromoListResponseEmptyStatePhoneVerification()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        every { analytics.eventViewPhoneVerificationMessage(any()) } just Runs
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.getPromoList("", PromoRequest(), "")

        //then
        assert(viewModel.promoEmptyStateUiModel.value?.uiState?.isShowButton == true)
    }

    @Test
    fun `WHEN get promo list and status is blacklisted THEN should show empty state`() {
        //given
        val result = HashMap<Type, Any>()
        result[CouponListRecommendationResponse::class.java] = provideGetPromoListResponseEmptyStateBlacklisted()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        every { analytics.eventViewBlacklistErrorAfterApplyPromo(any()) } just Runs
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.getPromoList("", PromoRequest(), "")

        //then
        assertNotNull(viewModel.promoEmptyStateUiModel.value)
    }

    @Test
    fun `WHEN get promo list and status is blacklisted THEN should not show button action`() {
        //given
        val result = HashMap<Type, Any>()
        result[CouponListRecommendationResponse::class.java] = provideGetPromoListResponseEmptyStateBlacklisted()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        every { analytics.eventViewBlacklistErrorAfterApplyPromo(any()) } just Runs
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.getPromoList("", PromoRequest(), "")

        //then
        assert(viewModel.promoEmptyStateUiModel.value?.uiState?.isShowButton == false)
    }

    @Test
    fun `WHEN get promo list and status is unknown THEN should show empty state`() {
        //given
        val result = HashMap<Type, Any>()
        result[CouponListRecommendationResponse::class.java] = provideGetPromoListResponseEmptyStateUnknown()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.getPromoList("", PromoRequest(), "")

        //then
        assertNotNull(viewModel.promoEmptyStateUiModel.value)
    }

    @Test
    fun `WHEN get promo list and status is unknown THEN should show button action`() {
        //given
        val result = HashMap<Type, Any>()
        result[CouponListRecommendationResponse::class.java] = provideGetPromoListResponseEmptyStateUnknown()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.getPromoList("", PromoRequest(), "")

        //then
        assert(viewModel.promoEmptyStateUiModel.value?.uiState?.isShowButton == true)
    }

    @Test
    fun `WHEN get promo list and get response error followed with success THEN fragment state should be success`() {
        //precondition
        val result = HashMap<Type, Any>()
        result[CouponListRecommendationResponse::class.java] = provideGetPromoListResponseError()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        viewModel.getPromoList("", PromoRequest(), "")

        //given
        val newResult = HashMap<Type, Any>()
        newResult[CouponListRecommendationResponse::class.java] = provideGetPromoListResponseSuccessAllEligible()
        val newGqlResponse = GraphqlResponse(newResult, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns newGqlResponse

        //when
        viewModel.getPromoList("", PromoRequest(), "")

        //then
        assert(viewModel.fragmentUiModel.value?.uiState?.hasFailedToLoad == false)
    }
}