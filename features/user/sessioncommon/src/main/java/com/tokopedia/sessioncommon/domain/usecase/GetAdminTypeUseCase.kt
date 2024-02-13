package com.tokopedia.sessioncommon.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.sessioncommon.data.admin.AdminTypeResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

open class GetAdminTypeUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<String, AdminTypeResponse>(dispatcher.io) {

    companion object {
        private const val DEFAULT_SOURCE = "android"
        private const val PARAM_SOURCE = "source"
        private const val SOURCE = "\$source"

        @JvmStatic
        fun createRequestParams(source: String = DEFAULT_SOURCE): RequestParams =
            RequestParams.create().apply {
                putString(PARAM_SOURCE, source)
            }
    }

    override fun graphqlQuery(): String {
        return """
            query getAdminType($SOURCE: String!) {
              getAdminType(source: $SOURCE) {
                shopID
                isMultiLocation
                admin_data {
                  detail_information {
                    admin_role_type {
                      is_shop_admin
                      is_location_admin
                      is_shop_owner            
                    }
                  }
                  status
                }
              }
            }
        """.trimIndent()
    }

    override suspend fun execute(params: String): AdminTypeResponse {
        return repository.request(graphqlQuery(), mapOf(PARAM_SOURCE to params))
    }
}
