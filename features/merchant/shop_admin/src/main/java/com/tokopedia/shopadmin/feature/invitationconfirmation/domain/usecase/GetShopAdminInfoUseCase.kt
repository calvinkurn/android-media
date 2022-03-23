package com.tokopedia.shopadmin.feature.invitationconfirmation.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.mapper.AdminInvitationConfirmationMapper
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.model.GetShopAdminInfoResponse
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.query.GetShopAdminInfoQuery
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.model.ShopAdminInfoUiModel
import javax.inject.Inject

class GetShopAdminInfoUseCase @Inject constructor(
    private val useCase: GraphqlUseCase<GetShopAdminInfoResponse>,
    private val adminInvitationConfirmationMapper: AdminInvitationConfirmationMapper
) {
    init {
        useCase.setGraphqlQuery(GetShopAdminInfoQuery)
        useCase.setTypeClass(GetShopAdminInfoResponse::class.java)
    }

    suspend fun execute(shopId: String): ShopAdminInfoUiModel {
        useCase.setRequestParams(GetShopAdminInfoQuery.createRequestParams(shopId))
        return adminInvitationConfirmationMapper.mapToShopAdminInfoUiModel(useCase.executeOnBackground())
    }
}