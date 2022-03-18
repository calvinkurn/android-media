package com.tokopedia.shopadmin.invitationaccepted.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.shopadmin.invitationaccepted.domain.mapper.AdminManagementInfoMapper
import com.tokopedia.shopadmin.invitationaccepted.domain.model.GetAdminManagementInfoListResponse
import com.tokopedia.shopadmin.invitationaccepted.presentation.model.InvitationAcceptedUiModel
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetAdminManagementInfoListUseCase @Inject constructor(
    private val useCase: GraphqlUseCase<GetAdminManagementInfoListResponse>,
    private val adminManagementInfoMapper: AdminManagementInfoMapper
) {

    init {
        useCase.setGraphqlQuery(GetAdminManagementInfoListQuery)
        useCase.setTypeClass(GetAdminManagementInfoListResponse::class.java)
    }

    suspend fun execute(shopId: String): InvitationAcceptedUiModel {
        useCase.setRequestParams(createRequestParams(shopId))
        return adminManagementInfoMapper.mapInvitationAcceptedUiModel(useCase.executeOnBackground())
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
        const val SOURCE = "admin-management-ui"
        const val DEVICE = "android"
    }
}