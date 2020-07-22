package com.tokopedia.promocheckoutmarketplace.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.promocheckoutmarketplace.ApplyPromoDataProvider.provideApplyPromoEmptyRequest
import com.tokopedia.promocheckoutmarketplace.ApplyPromoDataProvider.provideApplyPromoGlobalAndMerchantRequest
import com.tokopedia.promocheckoutmarketplace.ApplyPromoDataProvider.provideApplyPromoGlobalAndMerchantRequestInvalid
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
import com.tokopedia.promocheckoutmarketplace.ClearPromoDataProvider.provideClearPromoResponseFailed
import com.tokopedia.promocheckoutmarketplace.ClearPromoDataProvider.provideClearPromoResponseSuccess
import com.tokopedia.promocheckoutmarketplace.GetPromoLastSeenDataProvider.provideGetPromoLastSeenSuccessEmpty
import com.tokopedia.promocheckoutmarketplace.GetPromoLastSeenDataProvider.provideGetPromoLastSeenSuccessWithData
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideCurrentDisabledCollapsedGlobalPromoData
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideCurrentDisabledCollapsedMerchantPromoData
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideCurrentDisabledExpandedGlobalPromoData
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideCurrentDisabledExpandedMerchantPromoData
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideCurrentUnSelectedCollapsedGlobalAndMerchantPromoData
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideCurrentSelectedCollapsedGlobalPromoData
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideCurrentSelectedCollapsedMerchantPromoData
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideCurrentUnSelectedExpandedGlobalAndMerchantPromoData
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideCurrentSelectedExpandedGlobalPromoData
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideCurrentSelectedExpandedMerchantPromoData
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideGetPromoListRequest
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideGetPromoListResponseApplyManualFailed
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
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideNoCurrentSelectedCollapsedGlobalPromoData
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideNoCurrentSelectedCollapsedMerchantPromoData
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideNoCurrentSelectedExpandedGlobalPromoData
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideNoCurrentSelectedExpandedMerchantPromoData
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.providePromoRequestWithSelectedCollapsedGlobalPromo
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.providePromoRequestWithSelectedCollapsedMerchantPromo
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.providePromoRequestWithSelectedExpandedGlobalPromo
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.providePromoRequestWithSelectedExpandedMerchantPromo
import com.tokopedia.promocheckoutmarketplace.data.response.ClearPromoResponse
import com.tokopedia.promocheckoutmarketplace.data.response.CouponListRecommendationResponse
import com.tokopedia.promocheckoutmarketplace.data.response.GetPromoSuggestionResponse
import com.tokopedia.promocheckoutmarketplace.presentation.analytics.PromoCheckoutAnalytics
import com.tokopedia.promocheckoutmarketplace.presentation.mapper.PromoCheckoutUiModelMapper
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.PromoRecommendationUiModel
import com.tokopedia.purchase_platform.common.constant.PAGE_CART
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.response.validateuse.ValidateUseResponse
import io.mockk.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.List
import kotlin.collections.isNotEmpty
import kotlin.collections.listOf
import kotlin.collections.set

class TmpAllTest {

    private lateinit var viewModel: PromoCheckoutViewModel
    private lateinit var dispatcher: CoroutineDispatcher

    private var graphqlRepository: GraphqlRepository = mockk(relaxed = true)
    private var uiModelMapper: PromoCheckoutUiModelMapper = spyk()
    private var analytics: PromoCheckoutAnalytics = mockk()
    private var gson = Gson()

    @get: Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        dispatcher = Dispatchers.Unconfined
        viewModel = PromoCheckoutViewModel(dispatcher, graphqlRepository, uiModelMapper, analytics, gson)

        every { analytics.eventViewAvailablePromoListEligiblePromo(any(), any()) } just Runs
        every { analytics.eventViewAvailablePromoListIneligibleProduct(any(), any()) } just Runs

        viewModel.initFragmentUiModel(PAGE_CART)
    }


    /* GET PROMO LIST */

    @Test
    fun `WHEN get promo list and get complete expanded data THEN fragment ui model should not be null`() {
        //given
        val result = HashMap<Type, Any>()
        result[CouponListRecommendationResponse::class.java] = provideGetPromoListResponseSuccessAllExpanded()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.getPromoList("", PromoRequest(), "")

        //then
        assertNotNull(viewModel.fragmentUiModel.value)
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
    fun `WHEN get promo list and get complete collapsed data THEN fragment ui model should not be null`() {
        //given
        val result = HashMap<Type, Any>()
        result[CouponListRecommendationResponse::class.java] = provideGetPromoListResponseSuccessAllCollapsed()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.getPromoList("", PromoRequest(), "")

        //then
        assertNotNull(viewModel.fragmentUiModel.value)
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
    fun `WHEN get promo list and all eligible THEN fragment ui model should not be null`() {
        //given
        val result = HashMap<Type, Any>()
        result[CouponListRecommendationResponse::class.java] = provideGetPromoListResponseSuccessAllEligible()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.getPromoList("", PromoRequest(), "")

        //then
        assertNotNull(viewModel.fragmentUiModel.value)
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
    fun `WHEN get promo list and all ineligible THEN fragment ui model should not be null`() {
        //given
        val result = HashMap<Type, Any>()
        result[CouponListRecommendationResponse::class.java] = provideGetPromoListResponseSuccessAllIneligible()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.getPromoList("", PromoRequest(), "")

        //then
        assertNotNull(viewModel.fragmentUiModel.value)
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


    /* PROMO MANUAL INPUT */

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


    /* APPLY PROMO / Validate Use */

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


    /* CLEAR PROMO */

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


    /* GET PROMO LAST SEEN */

    @Test
    fun `WHEN get promo last seen and success THEN get promo last seen response should not be null`() {
        //given
        val result = HashMap<Type, Any>()
        result[GetPromoSuggestionResponse::class.java] = provideGetPromoLastSeenSuccessWithData()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.getPromoLastSeen("")

        //then
        assertNotNull(viewModel.getPromoLastSeenResponse.value)
    }

    @Test
    fun `WHEN get promo last seen and success with not empty data THEN get promo last seen response state should be show promo last seen`() {
        //given
        val result = HashMap<Type, Any>()
        result[GetPromoSuggestionResponse::class.java] = provideGetPromoLastSeenSuccessWithData()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.getPromoLastSeen("")

        //then
        assert(viewModel.getPromoLastSeenResponse.value?.state == GetPromoLastSeenAction.ACTION_SHOW)
    }

    @Test
    fun `WHEN get promo last seen and success with not empty data THEN promo last seen data should not be empty`() {
        //given
        val result = HashMap<Type, Any>()
        result[GetPromoSuggestionResponse::class.java] = provideGetPromoLastSeenSuccessWithData()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.getPromoLastSeen("")

        //then
        assert(viewModel.getPromoLastSeenResponse.value?.data?.uiData?.promoLastSeenItemUiModelList?.isNotEmpty() == true)
    }

    @Test
    fun `WHEN get promo last seen and success with empty data THEN get promo last seen response state should not be show promo last seen`() {
        //given
        val result = HashMap<Type, Any>()
        result[GetPromoSuggestionResponse::class.java] = provideGetPromoLastSeenSuccessEmpty()
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.getPromoLastSeen("")

        //then
        assert(viewModel.getPromoLastSeenResponse.value?.state != GetPromoLastSeenAction.ACTION_SHOW)
    }

}