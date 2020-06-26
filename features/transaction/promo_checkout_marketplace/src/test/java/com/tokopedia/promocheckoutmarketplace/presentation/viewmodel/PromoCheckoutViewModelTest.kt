package com.tokopedia.promocheckoutmarketplace.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.promocheckoutmarketplace.UnitTestFileUtils
import com.tokopedia.promocheckoutmarketplace.data.response.CouponListRecommendationResponse
import com.tokopedia.promocheckoutmarketplace.presentation.analytics.PromoCheckoutAnalytics
import com.tokopedia.promocheckoutmarketplace.presentation.mapper.PromoCheckoutUiModelMapper
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.*
import com.tokopedia.purchase_platform.common.constant.PAGE_CART
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest
import io.mockk.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.reflect.Type

class PromoCheckoutViewModelTest {

    lateinit var baseResponseSuccess: CouponListRecommendationResponse
    lateinit var viewModel: PromoCheckoutViewModel
    private val dispatcher: CoroutineDispatcher = Dispatchers.Unconfined
    private val graphqlRepository: GraphqlRepository = mockk(relaxed = true)
    private val uiModelMapper: PromoCheckoutUiModelMapper = mockk()
    private val analytics: PromoCheckoutAnalytics = mockk()

    @get: Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = PromoCheckoutViewModel(dispatcher, graphqlRepository, uiModelMapper, analytics)
        baseResponseSuccess = Gson().fromJson(UnitTestFileUtils().getJsonFromAsset("assets/base_coupon_recommendation_response_success.json"), CouponListRecommendationResponse::class.java)
    }

    @Test
    fun `WHEN get coupon recommendation and get complete data THEN ui model should not be null`() {
        //given
        val result = HashMap<Type, Any>()
        result[CouponListRecommendationResponse::class.java] = baseResponseSuccess
        val gqlResponse = GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false)

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
        coEvery { graphqlRepository.getReseponse(any(), any()) } returns gqlResponse

        //when
        viewModel.initFragmentUiModel(PAGE_CART)
        viewModel.loadData("", PromoRequest(), "")

        //then
        assertNotNull(viewModel.fragmentUiModel.value)
        assertNotNull(viewModel.promoRecommendationUiModel.value)
        assertNotNull(viewModel.promoInputUiModel.value)
        assertNotNull(viewModel.promoListUiModel.value)
    }

}