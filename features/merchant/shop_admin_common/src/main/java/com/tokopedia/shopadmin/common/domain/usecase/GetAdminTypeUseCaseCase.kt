package com.tokopedia.shopadmin.common.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.shopadmin.common.domain.mapper.AdminTypeMapper
import com.tokopedia.shopadmin.common.domain.model.GetAdminTypeResponse
import com.tokopedia.shopadmin.common.domain.usecase.GetAdminTypeUseCaseCase.Companion.GET_ADMIN_TYPE_QUERY
import com.tokopedia.shopadmin.common.presentation.uimodel.AdminTypeUiModel
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

@GqlQuery("GetAdminTypeQuery", GET_ADMIN_TYPE_QUERY)
class GetAdminTypeUseCaseCase @Inject constructor(
    private val useCase: GraphqlUseCase<GetAdminTypeResponse>,
    private val adminTypeMapper: AdminTypeMapper
) {
    init {
        useCase.setGraphqlQuery(GetAdminTypeQuery())
        useCase.setTypeClass(GetAdminTypeResponse::class.java)
    }

    suspend fun execute(): AdminTypeUiModel {
        useCase.setRequestParams(createRequestParams())
        return adminTypeMapper.mapToAdminTypeUiModel(useCase.executeOnBackground())
    }

    private fun createRequestParams(): Map<String, Any> {
        return RequestParams.create().apply {
            putString(SOURCE_KEY, SOURCE)
        }.parameters
    }

    companion object {
        internal const val GET_ADMIN_TYPE_QUERY = """
            query getAdminType(${'$'}source: String!) {
                getAdminType(source: ${'$'}source) {
                    admin_data {
                        detail_information {
                          admin_role_type {
                            is_shop_admin
                            is_shop_owner
                          }
                        }
                        status
                    }
                    shopID
                }
            }
        """

        private const val SOURCE_KEY = "source"
        private const val SOURCE = "admin-type-android"
    }
}