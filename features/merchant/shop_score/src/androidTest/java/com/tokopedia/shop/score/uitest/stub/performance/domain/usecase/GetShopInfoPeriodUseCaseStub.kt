package com.tokopedia.shop.score.uitest.stub.performance.domain.usecase

import com.tokopedia.gm.common.data.source.cloud.model.ShopInfoPeriodWrapperResponse
import com.tokopedia.gm.common.domain.interactor.GetShopInfoPeriodUseCase
import com.tokopedia.gm.common.domain.mapper.ShopScoreCommonMapper
import com.tokopedia.gm.common.presentation.model.ShopInfoPeriodUiModel
import com.tokopedia.shop.score.uitest.stub.common.graphql.repository.GraphqlRepositoryStub
import com.tokopedia.shop.score.uitest.stub.performance.domain.response.ShopInfoPeriodResponseStub

class GetShopInfoPeriodUseCaseStub(
    private val graphqlRepositoryStub: GraphqlRepositoryStub,
    private val shopScoreCommonMapper: ShopScoreCommonMapper
) : GetShopInfoPeriodUseCase(graphqlRepositoryStub, shopScoreCommonMapper) {

    var responseStub = ShopInfoPeriodResponseStub()
        set(value) {
            graphqlRepositoryStub.createMapResult(responseStub::class.java, value)
            field = value
        }

    override suspend fun executeOnBackground(): ShopInfoPeriodUiModel {
        val shopScoreWrapperResponse = ShopInfoPeriodWrapperResponse(
            shopInfoByIDResponse = responseStub.shopInfoByIDResponse,
            goldGetPMSettingInfo = responseStub.goldGetPMSettingInfo
        )
        return shopScoreCommonMapper.mapToGetShopInfo(shopScoreWrapperResponse)
    }
}