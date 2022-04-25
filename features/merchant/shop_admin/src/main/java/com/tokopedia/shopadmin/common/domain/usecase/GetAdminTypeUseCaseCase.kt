package com.tokopedia.shopadmin.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.shopadmin.common.domain.mapper.AdminTypeMapper
import com.tokopedia.shopadmin.common.domain.model.GetAdminTypeResponse
import com.tokopedia.shopadmin.common.domain.query.GetAdminTypeQuery
import com.tokopedia.shopadmin.common.presentation.uimodel.AdminTypeUiModel
import javax.inject.Inject

class GetAdminTypeUseCaseCase @Inject constructor(
    private val useCase: GraphqlUseCase<GetAdminTypeResponse>,
    private val adminTypeMapper: AdminTypeMapper
) {
    init {
        useCase.setGraphqlQuery(GetAdminTypeQuery)
        useCase.setTypeClass(GetAdminTypeResponse::class.java)
    }

    suspend fun execute(): AdminTypeUiModel {
        useCase.setRequestParams(GetAdminTypeQuery.createRequestParams())
        return adminTypeMapper.mapToAdminTypeUiModel(useCase.executeOnBackground())
    }
}