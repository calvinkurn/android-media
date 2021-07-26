package com.tokopedia.promocheckoutmarketplace.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.promocheckoutmarketplace.GetPromoLastSeenDataProvider.provideGetPromoLastSeenSuccessEmpty
import com.tokopedia.promocheckoutmarketplace.GetPromoLastSeenDataProvider.provideGetPromoLastSeenSuccessWithData
import com.tokopedia.promocheckoutmarketplace.data.response.GetPromoSuggestionResponse
import com.tokopedia.promocheckoutmarketplace.presentation.analytics.PromoCheckoutAnalytics
import com.tokopedia.promocheckoutmarketplace.presentation.mapper.PromoCheckoutUiModelMapper
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
import kotlin.collections.set

class PromoCheckoutViewModelGetPromoLastSeenTest {

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