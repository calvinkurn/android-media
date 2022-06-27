package com.tokopedia.shopadmin.feature.invitationconfirmation.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.mapper.AdminInvitationConfirmationMapper
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.model.GetShopAdminInfoResponse
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.param.ParamShopInfoByID
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.query.GET_SHOP_ADMIN_INFO_QUERY
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.uimodel.ShopAdminInfoUiModel
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

@GqlQuery("GetShopAdminInfoQuery", GET_SHOP_ADMIN_INFO_QUERY)
class GetShopAdminInfoUseCase @Inject constructor(
    private val useCase: GraphqlUseCase<GetShopAdminInfoResponse>,
    private val adminInvitationConfirmationMapper: AdminInvitationConfirmationMapper
) {
    init {
        useCase.setGraphqlQuery(GetShopAdminInfoQuery())
        useCase.setTypeClass(GetShopAdminInfoResponse::class.java)
    }

    suspend fun execute(shopId: Long): ShopAdminInfoUiModel {
        val paramShopInfoByID = ParamShopInfoByID(shopIDs = listOf(shopId))
        useCase.setRequestParams(createRequestParams(shopId, paramShopInfoByID))
        return adminInvitationConfirmationMapper.mapToShopAdminInfoUiModel(useCase.executeOnBackground())
    }

    private fun createRequestParams(shopId: Long, paramShopInfoByID: ParamShopInfoByID): Map<String, Any> {
        return RequestParams.create().apply {
            putString(SOURCE_KEY, SOURCE)
            putLong(SHOP_ID_KEY, shopId)
            putObject(INPUT_KEY, paramShopInfoByID)
        }.parameters
    }

    companion object {
        private const val SHOP_ID_KEY = "shop_id"
        private const val SOURCE_KEY = "source"
        private const val INPUT_KEY = "input"
        private const val SOURCE = "getShopAdminInfo-android"
    }
}