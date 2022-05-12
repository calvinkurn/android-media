package com.tokopedia.shopadmin.feature.invitationconfirmation.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.mapper.AdminInvitationConfirmationMapper
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.model.GetShopAdminInfoResponse
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.param.ParamShopInfoByID
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.query.GetShopAdminInfoQuery
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.uimodel.ShopAdminInfoUiModel
import javax.inject.Inject

class GetShopAdminInfoUseCase @Inject constructor(
    private val useCase: GraphqlUseCase<GetShopAdminInfoResponse>,
    private val adminInvitationConfirmationMapper: AdminInvitationConfirmationMapper
) {
    init {
        useCase.setGraphqlQuery(GetShopAdminInfoQuery)
        useCase.setTypeClass(GetShopAdminInfoResponse::class.java)
    }

    suspend fun execute(shopId: Long): ShopAdminInfoUiModel {
        val paramShopInfoByID = ParamShopInfoByID(shopIDs = listOf(shopId))
        useCase.setRequestParams(GetShopAdminInfoQuery.createRequestParams(shopId, paramShopInfoByID))
        return adminInvitationConfirmationMapper.mapToShopAdminInfoUiModel(useCase.executeOnBackground())
    }
}