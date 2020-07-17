package com.tokopedia.promocheckoutmarketplace.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.promocheckoutmarketplace.ApplyPromoDataProvider.provideApplyPromoGlobalRequest
import com.tokopedia.promocheckoutmarketplace.ApplyPromoDataProvider.provideApplyPromoGlobalResponseSuccess
import com.tokopedia.promocheckoutmarketplace.ApplyPromoDataProvider.provideApplyPromoMerchantRequest
import com.tokopedia.promocheckoutmarketplace.ApplyPromoDataProvider.provideApplyPromoMerchantResponseSuccess
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
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideCurrentSelectedCollapsedGlobalPromoData
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideCurrentSelectedCollapsedMerchantPromoData
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideCurrentSelectedExpandedGlobalPromoData
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideCurrentSelectedExpandedMerchantPromoData
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
import java.util.ArrayList

class TmpAllTest {

    private lateinit var viewModel: PromoCheckoutViewModel
    private lateinit var dispatcher: CoroutineDispatcher

    private var graphqlRepository: GraphqlRepository = mockk(relaxed = true)
    private var uiModelMapper: PromoCheckoutUiModelMapper = spyk()
    private var analytics: PromoCheckoutAnalytics = mockk()

    @get: Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        dispatcher = Dispatchers.Unconfined
        viewModel = PromoCheckoutViewModel(dispatcher, graphqlRepository, uiModelMapper, analytics)

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

}