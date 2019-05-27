package com.tokopedia.search.result.domain.usecase.searchprofile

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.common.constants.SearchConstant.GQL.KEY_PARAMS
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.search.R
import com.tokopedia.search.result.data.mapper.searchprofile.SearchProfileMapperModule
import com.tokopedia.search.result.domain.model.SearchProfileModel
import com.tokopedia.search.utils.UrlParamUtils
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import dagger.Module
import dagger.Provides
import rx.Observable
import rx.functions.Func1
import java.util.*
import javax.inject.Named

private class SearchProfileUseCase(
    private val graphqlRequest : GraphqlRequest,
    private val graphqlUseCase : GraphqlUseCase,
    private val searchProfileModelMapper : Func1<GraphqlResponse, SearchProfileModel>
) : UseCase<SearchProfileModel>() {

    override fun createObservable(requestParams: RequestParams): Observable<SearchProfileModel>? {
        val variables = createParametersForQuery(requestParams.parameters)

        graphqlRequest.variables = variables

        graphqlUseCase.addRequest(graphqlRequest)

        return graphqlUseCase
                .createObservable(RequestParams.EMPTY)
                .map(searchProfileModelMapper)
    }

    private fun createParametersForQuery(requestParams: Map<String, Any>) : Map<String, Any> {
        val variables = HashMap<String, Any>()

        variables[KEY_PARAMS] = UrlParamUtils.generateUrlParamString(requestParams)

        return variables
    }
}

@SearchScope
@Module(includes = [SearchProfileMapperModule::class])
class SearchProfileUseCaseModule {

    @SearchScope
    @Provides
    @Named(SearchConstant.SearchProfile.SEARCH_PROFILE_USE_CASE)
    fun provideSearchProfileUseCase(@ApplicationContext context: Context,
                                    searchProfileModelMapper: Func1<GraphqlResponse, SearchProfileModel>)
            : UseCase<SearchProfileModel> {

        val graphqlRequest = GraphqlRequest(
            GraphqlHelper.loadRawString(context.resources, R.raw.gql_search_profile),
            SearchProfileModel::class.java
        )

        return SearchProfileUseCase(graphqlRequest, GraphqlUseCase(), searchProfileModelMapper)
    }
}