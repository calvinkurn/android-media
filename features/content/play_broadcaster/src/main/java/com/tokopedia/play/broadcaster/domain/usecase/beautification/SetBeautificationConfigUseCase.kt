package com.tokopedia.play.broadcaster.domain.usecase.beautification

import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.content.common.types.ContentCommonUserType
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play.broadcaster.domain.model.beautification.SetBeautificationConfigResponse
import com.tokopedia.play.broadcaster.ui.model.beautification.BeautificationConfigUiModel
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on March 07, 2023
 */
@GqlQuery(SetBeautificationConfigUseCase.QUERY_NAME, SetBeautificationConfigUseCase.QUERY)
class SetBeautificationConfigUseCase @Inject constructor(
    @ApplicationContext val repository: GraphqlRepository,
    private val gson: Gson,
) : GraphqlUseCase<SetBeautificationConfigResponse>(repository) {

    init {
        setGraphqlQuery(SetBeautificationConfigUseCaseQuery())
        setCacheStrategy(
            GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build()
        )
        setTypeClass(SetBeautificationConfigResponse::class.java)
    }

    suspend fun execute(
        authorId: String,
        authorType: String,
        beautificationConfig: BeautificationConfigUiModel,
    ): SetBeautificationConfigResponse {
        setRequestParams(
            mapOf(
                PARAM_REQ to mapOf(
                    PARAM_AUTHOR_ID to authorId,
                    PARAM_AUTHOR_TYPE to when (authorType) {
                        ContentCommonUserType.TYPE_USER -> ContentCommonUserType.VALUE_TYPE_ID_USER
                        ContentCommonUserType.TYPE_SHOP -> ContentCommonUserType.VALUE_TYPE_ID_SHOP
                        else -> 0
                    },
                    PARAM_BEAUTIFICATION_CONFIG to gson.toJson(beautificationConfig.convertToDTO())
                )
            )
        )

        return executeOnBackground()
    }

    companion object {
        private const val PARAM_REQ = "req"
        private const val PARAM_AUTHOR_ID = "authorID"
        private const val PARAM_AUTHOR_TYPE = "authorType"
        private const val PARAM_BEAUTIFICATION_CONFIG = "beautificationConfig"

        const val QUERY_NAME = "SetBeautificationConfigUseCaseQuery"
        const val QUERY = """
            mutation BroadcasterSetBeautificationConfig(
                ${"$${PARAM_REQ}"}: BroadcasterSetBeautificationConfig!
            ) {
                broadcasterSetBeautificationConfig(
                    ${PARAM_REQ}: ${"$${PARAM_REQ}"}
                ) {
                  success
                }
            }
        """
    }
}
