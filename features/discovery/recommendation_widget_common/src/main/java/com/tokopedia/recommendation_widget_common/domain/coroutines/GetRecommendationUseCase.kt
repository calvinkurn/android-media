package com.tokopedia.recommendation_widget_common.domain.coroutines

import android.content.Context
import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.recommendation_widget_common.data.RecommendationEntity
import com.tokopedia.recommendation_widget_common.domain.coroutines.base.UseCase
import com.tokopedia.recommendation_widget_common.domain.query.ListProductRecommendationQuery
import com.tokopedia.recommendation_widget_common.domain.query.ListProductRecommendationQueryV2
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.ext.toQueryParam
import com.tokopedia.recommendation_widget_common.extension.mappingToRecommendationModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.user.session.UserSession
import java.lang.ref.WeakReference
import javax.inject.Inject

/**
 * Created by devara fikry on 16/04/19.
 */

// [GetRecommendationUseCase] already sent choose address location params
open class GetRecommendationUseCase @Inject constructor(
    context: Context,
    graphqlRepository: GraphqlRepository,
) : UseCase<GetRecommendationRequestParam, List<RecommendationWidget>>() {
    private val contextReference: WeakReference<Context> = WeakReference(context)
    private val context: Context?
        get() = contextReference.get()

    private val graphqlUseCase = GraphqlUseCase<RecommendationEntity>(graphqlRepository)
    private val getRecommendationQuery: GqlQueryInterface
        get() {
            val remoteConfig: RemoteConfig = FirebaseRemoteConfigImpl(context)
            val isUseGqlFedQuery = remoteConfig.getBoolean(
                RemoteConfigKey.RECOM_USE_GQL_FED_QUERY,
                true
            )
            return if (isUseGqlFedQuery) {
                ListProductRecommendationQueryV2()
            } else {
                ListProductRecommendationQuery()
            }
        }
    init {
        graphqlUseCase.setTypeClass(RecommendationEntity::class.java)
        graphqlUseCase.setGraphqlQuery(getRecommendationQuery)
    }
    override suspend fun getData(inputParameter: GetRecommendationRequestParam): List<RecommendationWidget> {
        val userSession = UserSession(context)
        inputParameter.userId = userSession.userId.toIntOrNull() ?: 0
        val queryParam = context?.let { ChooseAddressUtils.getLocalizingAddressData(it).toQueryParam(inputParameter.queryParam) } ?: inputParameter.queryParam
        graphqlUseCase.setRequestParams(inputParameter.copy(queryParam = queryParam).toGqlRequest())
        return graphqlUseCase.executeOnBackground().productRecommendationWidget.data.mappingToRecommendationModel()
    }
}
