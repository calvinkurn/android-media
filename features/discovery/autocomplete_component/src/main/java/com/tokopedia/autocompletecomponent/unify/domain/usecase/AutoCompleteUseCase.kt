package com.tokopedia.autocompletecomponent.unify.domain.usecase

import com.tokopedia.autocompletecomponent.suggestion.domain.model.ShopAdsModel
import com.tokopedia.autocompletecomponent.unify.domain.AutoCompleteUnifyRequestUtil
import com.tokopedia.autocompletecomponent.unify.domain.model.InitialStateUnifyModel
import com.tokopedia.autocompletecomponent.unify.domain.model.SuggestionUnifyModel
import com.tokopedia.autocompletecomponent.unify.domain.model.UniverseSuggestionUnifyModel
import com.tokopedia.autocompletecomponent.util.UrlParamHelper
import com.tokopedia.discovery.common.constants.SearchConstant.GQL
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase

class InitialStateUseCase(
    private val graphqlUseCase: GraphqlUseCase<InitialStateUnifyModel>
) : UseCase<UniverseSuggestionUnifyModel>() {
    override suspend fun executeOnBackground() = graphqlUseCase.run {
        setRequestParams(createGraphqlRequestParams(useCaseRequestParams))
        setGraphqlQuery(getGraphqlQuery())
        setTypeClass(InitialStateUnifyModel::class.java)
        executeOnBackground().data
    }

    @GqlQuery("InitialStateQuery", AutoCompleteUnifyRequestUtil.INITAL_STATE_QUERY)
    fun getGraphqlQuery(): GqlQueryInterface = InitialStateQuery()

    private fun createGraphqlRequestParams(requestParams: RequestParams) =
        mapOf(GQL.KEY_PARAMS to UrlParamHelper.generateUrlParamString(requestParams.parameters))
}

class SuggestionStateUseCase(
    private val graphqlUseCase: GraphqlUseCase<SuggestionUnifyModel>,
    private val shopAdsUseCase: UseCase<ShopAdsModel>
) : UseCase<UniverseSuggestionUnifyModel>() {
    private var shopAdsModel: ShopAdsModel = ShopAdsModel()
    override suspend fun executeOnBackground(): UniverseSuggestionUnifyModel {
        getHeadlineAds()
        val graphqlResult = graphqlUseCase.run {
            setRequestParams(createGraphqlRequestParams(useCaseRequestParams))
            setGraphqlQuery(getGraphqlQuery())
            setTypeClass(SuggestionUnifyModel::class.java)
            executeOnBackground().data
        }
        graphqlResult.cpmModel = shopAdsModel.cpmModel
        return graphqlResult
    }

    @GqlQuery("SuggestionStateQuery", AutoCompleteUnifyRequestUtil.SUGGESTION_STATE_QUERY)
    private fun getGraphqlQuery(): GqlQueryInterface = SuggestionStateQuery()

    private fun createGraphqlRequestParams(requestParams: RequestParams) =
        mapOf(GQL.KEY_PARAMS to UrlParamHelper.generateUrlParamString(requestParams.parameters))

    private fun getHeadlineAds() {
        shopAdsModel = ShopAdsModel()
        shopAdsUseCase.execute(
            ::onSuccessGetShopAds,
            ::onErrorGetShopAds,
            useCaseRequestParams
        )
    }

    private fun onSuccessGetShopAds(shopAdsModel: ShopAdsModel) {
        this.shopAdsModel = shopAdsModel
    }

    private fun onErrorGetShopAds(throwable: Throwable) { }
}
