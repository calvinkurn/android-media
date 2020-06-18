package com.tokopedia.oneclickcheckout.common.domain

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.common.data.model.response.preference.PreferenceListGqlResponse
import com.tokopedia.oneclickcheckout.common.domain.mapper.PreferenceListModelMapper
import com.tokopedia.oneclickcheckout.common.domain.model.preference.PreferenceListResponseModel
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetPreferenceListUseCase @Inject constructor(@ApplicationContext val context: Context, val graphql: GraphqlUseCase<PreferenceListGqlResponse>, val preferenceListModelMapper: PreferenceListModelMapper) : UseCase<PreferenceListResponseModel>() {
    override suspend fun executeOnBackground(): PreferenceListResponseModel {
        val graphqlRequest = GraphqlHelper.loadRawString(context.resources,
                R.raw.query_preference_list)

        graphql.setGraphqlQuery(graphqlRequest)
        graphql.setTypeClass(PreferenceListGqlResponse::class.java)

        val result = graphql.executeOnBackground()

        return preferenceListModelMapper.convertToDomainModel(result)
    }

}