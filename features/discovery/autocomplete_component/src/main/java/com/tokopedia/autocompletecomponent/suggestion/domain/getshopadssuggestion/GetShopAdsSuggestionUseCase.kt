package com.tokopedia.autocompletecomponent.suggestion.domain.getshopadssuggestion

import com.tokopedia.autocompletecomponent.suggestion.domain.SuggestionRequestUtils.HEADLINE_ADS_QUERY
import com.tokopedia.autocompletecomponent.suggestion.domain.SuggestionRequestUtils.createHeadlineParams
import com.tokopedia.autocompletecomponent.suggestion.domain.SuggestionRequestUtils.prepare
import com.tokopedia.autocompletecomponent.suggestion.domain.model.ShopAdsModel
import com.tokopedia.autocompletecomponent.util.UrlParamHelper
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant.GQL.KEY_HEADLINE_PARAMS
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase

class GetShopAdsSuggestionUseCase(
    private val graphqlUseCase: GraphqlUseCase<ShopAdsModel>,
): UseCase<ShopAdsModel>() {

    @GqlQuery("ShopAdsSuggestion", HEADLINE_ADS_QUERY)
    override suspend fun executeOnBackground(): ShopAdsModel {
        if (!isShopAdsEnabled(useCaseRequestParams)) return ShopAdsModel()

        graphqlUseCase.prepare(
            ShopAdsSuggestion(),
            ShopAdsModel::class.java,
            createShopAdsGraphqlRequestParams(useCaseRequestParams)
        )

        return graphqlUseCase.executeOnBackground()
    }

    private fun isShopAdsEnabled(requestParams: RequestParams): Boolean {
        val navSource = requestParams.getString(SearchApiConst.NAVSOURCE, "") ?: ""

        return navSource.isEmpty() || navSource == SearchApiConst.HOME
    }

    private fun createShopAdsGraphqlRequestParams(requestParams: RequestParams): Map<String, String> {
        val params = UrlParamHelper.generateUrlParamString(createHeadlineParams(requestParams))

        return mapOf(KEY_HEADLINE_PARAMS to params)
    }
}