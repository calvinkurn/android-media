package com.tokopedia.shopadmin.feature.invitationaccepted.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.shopadmin.feature.invitationaccepted.domain.mapper.AdminManagementInfoMapper
import com.tokopedia.shopadmin.feature.invitationaccepted.domain.model.GetAdminPermissionResponse
import com.tokopedia.shopadmin.feature.invitationaccepted.domain.query.GetAdminManagementInfoListQuery
import com.tokopedia.shopadmin.feature.invitationaccepted.presentation.model.AdminPermissionUiModel
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetAdminPermissionListUseCase @Inject constructor(
    private val useCase: GraphqlUseCase<GetAdminPermissionResponse>,
    private val adminManagementInfoMapper: AdminManagementInfoMapper
) {

    init {
        useCase.setGraphqlQuery(GetAdminManagementInfoListQuery)
        useCase.setTypeClass(GetAdminPermissionResponse::class.java)
    }

    suspend fun execute(shopId: String): List<AdminPermissionUiModel> {
        useCase.setRequestParams(createRequestParams(shopId))
        return adminManagementInfoMapper.mapPermissionListUiModel(useCase.executeOnBackground().getAdminInfo)
    }

    private fun createRequestParams(shopId: String): Map<String, Any> {
        return RequestParams.create().apply {
            putString(SHOP_ID_KEY, shopId)
            putString(LANG_KEY, LANG_ID)
            putString(DEVICE_KEY, DEVICE)
            putString(SOURCE_KEY, SOURCE)
        }.parameters
    }

    companion object {
        const val SHOP_ID_KEY = "shop_id"
        const val DEVICE_KEY = "device"
        const val SOURCE_KEY = "source"
        const val LANG_KEY = "lang"

        const val LANG_ID = "id"
        const val SOURCE = "admin-management-android"
        const val DEVICE = "android"
    }
}