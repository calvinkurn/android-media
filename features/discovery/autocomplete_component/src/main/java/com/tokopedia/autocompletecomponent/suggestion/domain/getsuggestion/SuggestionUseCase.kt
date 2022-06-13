package com.tokopedia.autocompletecomponent.suggestion.domain.getsuggestion

import com.tokopedia.autocompletecomponent.suggestion.domain.SuggestionRequestUtils.SUGGESTION_QUERY
import com.tokopedia.autocompletecomponent.suggestion.domain.SuggestionRequestUtils.prepare
import com.tokopedia.autocompletecomponent.suggestion.domain.model.ShopAdsModel
import com.tokopedia.autocompletecomponent.suggestion.domain.model.SuggestionResponse
import com.tokopedia.autocompletecomponent.suggestion.domain.model.SuggestionUniverse
import com.tokopedia.autocompletecomponent.util.UrlParamHelper
import com.tokopedia.discovery.common.constants.SearchConstant.GQL.KEY_PARAMS
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase

open class SuggestionUseCase(
    protected val suggestionGraphqlUseCase: GraphqlUseCase<SuggestionResponse>,
    protected val getShopAdsSuggestionUseCase: UseCase<ShopAdsModel>? = null
) : UseCase<SuggestionUniverse>() {

    private var shopAdsModel = ShopAdsModel()

    override suspend fun executeOnBackground(): SuggestionUniverse {
        getHeadlineAds()

        return getSuggestion()
    }

    private fun getHeadlineAds() {
        shopAdsModel = ShopAdsModel()

        getShopAdsSuggestionUseCase?.execute(
            ::onSuccessGetShopAds,
            ::onErrorGetShopAds,
            useCaseRequestParams,
        )
    }

    private fun onSuccessGetShopAds(shopAdsModel: ShopAdsModel) {
        this.shopAdsModel = shopAdsModel
    }

    private fun onErrorGetShopAds(throwable: Throwable) { }

    private suspend fun getSuggestion(): SuggestionUniverse {
        val suggestionResponse = getSuggestionResponse()

        val suggestionUniverse = suggestionResponse.suggestionUniverse

        suggestionUniverse.cpmModel = shopAdsModel.cpmModel

        return suggestionUniverse
    }

    private suspend fun getSuggestionResponse(): SuggestionResponse {
        suggestionGraphqlUseCase.prepare(
            getGraphqlQuery(),
            SuggestionResponse::class.java,
            createGraphqlRequestParams(useCaseRequestParams),
        )

        return suggestionGraphqlUseCase.executeOnBackground()
    }

    @GqlQuery("SuggestionUseCaseQuery", SUGGESTION_QUERY)
    protected open fun getGraphqlQuery() = SuggestionUseCaseQuery.GQL_QUERY

    protected open fun createGraphqlRequestParams(requestParams: RequestParams): Map<String, String> {
        val params = UrlParamHelper.generateUrlParamString(requestParams.parameters)

        return mapOf(KEY_PARAMS to params)
    }
}