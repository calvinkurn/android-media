package com.tokopedia.home_account.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.home_account.AccountConstants.Query.QUERY_GET_USER_ASSET_CONFIG
import com.tokopedia.home_account.AccountErrorHandler
import com.tokopedia.home_account.Utils
import com.tokopedia.home_account.data.model.UserPageAssetConfigDataModel
import com.tokopedia.network.exception.MessageErrorException
import javax.inject.Inject

/**
 * Created by Ade Fulki on 22/02/21.
 */
class GetUserPageAssetConfigUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        private val rawQueries: Map<String, String>
) : GraphqlUseCase<UserPageAssetConfigDataModel>(graphqlRepository) {

    override suspend fun executeOnBackground(): UserPageAssetConfigDataModel {
        val query = rawQueries[QUERY_GET_USER_ASSET_CONFIG]
        val gqlRequest = GraphqlRequest(
                query,
                UserPageAssetConfigDataModel::class.java,
                mapOf<String, Any>()
        )
        val gqlStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        val gqlResponse = graphqlRepository.getReseponse(listOf(gqlRequest), gqlStrategy)
        val errors = gqlResponse.getError(UserPageAssetConfigDataModel::class.java)
        if (!errors.isNullOrEmpty()) {
            throw MessageErrorException(errors[0].message)
        } else {
            var data: UserPageAssetConfigDataModel? = gqlResponse.getData(UserPageAssetConfigDataModel::class.java)
            if (data == null) {
                val mapResponse = Utils.convertResponseToJson(gqlResponse)
                data = UserPageAssetConfigDataModel()
                AccountErrorHandler.logDataNull("Account_GetUserPageAssetConfig",
                        Throwable("Results : ${mapResponse[Utils.M_RESULT]} - Errors : ${mapResponse[Utils.M_ERRORS]}"))
            }
            return data
        }
    }
}