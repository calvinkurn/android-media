package com.tokopedia.catalog_library.usecase

import com.tokopedia.catalog_library.model.raw.CatalogSpecialResponse
import com.tokopedia.catalog_library.model.raw.gql.GQL_CATALOG_SPECIAL
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

@GqlQuery("GQL_CATALOG_SPECIAL", GQL_CATALOG_SPECIAL)
class CatalogSpecialUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository,
    private val userSession: UserSessionInterface
) :
    GraphqlUseCase<CatalogSpecialResponse>(graphqlRepository) {

    fun getSpecialData(
        onSuccess: (CatalogSpecialResponse) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        try {
            this.setTypeClass(CatalogSpecialResponse::class.java)
            this.setRequestParams(getRequestParams(userSession.userId))
            this.setGraphqlQuery(GQL_CATALOG_SPECIAL)
            this.execute(
                { result ->
                    onSuccess(result)
                }, { error ->
                    onError(error)
                })
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }

    private fun getRequestParams(userId: String?): MutableMap<String, Any?> {
        val requestMap = mutableMapOf<String, Any?>()
        requestMap[PARAM_USER_ID] = userId
        return requestMap
    }

    companion object {
        const val PARAM_USER_ID = "user_id"
    }
}


