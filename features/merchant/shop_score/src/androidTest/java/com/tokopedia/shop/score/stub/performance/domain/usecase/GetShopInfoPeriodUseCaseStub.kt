package com.tokopedia.shop.score.stub.performance.domain.usecase

import com.tokopedia.gm.common.data.source.cloud.model.ShopInfoPeriodWrapperResponse
import com.tokopedia.gm.common.domain.interactor.GetShopInfoPeriodUseCase
import com.tokopedia.gm.common.domain.mapper.ShopScoreCommonMapper
import com.tokopedia.gm.common.presentation.model.ShopInfoPeriodUiModel
import com.tokopedia.shop.score.stub.common.graphql.repository.GraphqlRepositoryStub
import com.tokopedia.shop.score.stub.performance.domain.response.ShopInfoPeriodResponseStub
import java.io.IOException
import java.lang.Exception

class GetShopInfoPeriodUseCaseStub(
    private val graphqlRepositoryStub: GraphqlRepositoryStub,
    private val shopScoreCommonMapper: ShopScoreCommonMapper
) : GetShopInfoPeriodUseCase(graphqlRepositoryStub, shopScoreCommonMapper) {

    var responseStub: Any = ShopInfoPeriodResponseStub()
        set(value) {
            graphqlRepositoryStub.createMapResult(responseStub::class.java, value)
            field = value
        }

    override suspend fun executeOnBackground(): ShopInfoPeriodUiModel {
        if (responseStub is IOException) {
            throw IOException((responseStub as IOException).message)
        }
        if (responseStub is Exception) {
            throw Exception((responseStub as Exception).message)
        }
        val shopInfoPeriodResponseStub = responseStub as ShopInfoPeriodResponseStub
        val shopScoreWrapperResponse = ShopInfoPeriodWrapperResponse(
            shopInfoByIDResponse = shopInfoPeriodResponseStub.shopInfoByIDResponse,
            goldGetPMSettingInfo = shopInfoPeriodResponseStub.goldGetPMSettingInfo
        )
        return shopScoreCommonMapper.mapToGetShopInfo(shopScoreWrapperResponse)

    }
}