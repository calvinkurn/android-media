package com.tokopedia.gm.common.domain.interactor

import com.tokopedia.gm.common.data.source.cloud.model.GetIsOfficialResponse
import com.tokopedia.gm.common.data.source.cloud.model.PMPeriodTypeResponse
import com.tokopedia.gm.common.data.source.cloud.model.ShopInfoByIDResponse
import com.tokopedia.gm.common.domain.mapper.ShopScoreMapper
import com.tokopedia.gm.common.presentation.model.OfficialStorePeriodUiModel
import com.tokopedia.gm.common.presentation.model.ShopInfoPeriodUiModel
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetIsOfficialStorePeriodUseCase @Inject constructor(private val graphqlRepository: GraphqlRepository):
        GraphqlUseCase<OfficialStorePeriodUiModel>(graphqlRepository) {

    companion object {
        const val SHOP_ID = "shop_id"
        val IS_OFFICIAL_STORE_QUERY = """
            query getIsOfficial(${'$'}shop_id: Int!) {
              getIsOfficial(shop_id: ${'$'}shop_id){
                message_error
                data{
                  expired_date
                  is_official
                }
              }
            }
        """.trimIndent()

        @JvmStatic
        fun createParams(shopID: Int): RequestParams = RequestParams.create().apply {
            putInt(SHOP_ID, shopID)
        }
    }

    var requestParams: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): OfficialStorePeriodUiModel {
        var officialStorePeriod = OfficialStorePeriodUiModel()
        val shopId = requestParams.getInt(GetShopInfoUseCase.SHOP_ID, 0)

        val officialStoreParam = mapOf(SHOP_ID to shopId)
        val periodTypeParam = mapOf(GetShopInfoUseCase.SHOP_ID to shopId, GetShopInfoUseCase.SOURCE to GetPMPeriodTypeUseCase.GOLD_MERCHANT_SOURCE)

        val officialStoreRequest = GraphqlRequest(GetShopInfoUseCase.SHOP_INFO_ID_QUERY, GetIsOfficialResponse::class.java, officialStoreParam)
        val periodTypeRequest = GraphqlRequest(GetPMPeriodTypeUseCase.PM_SETTING_INFO_QUERY, PMPeriodTypeResponse::class.java, periodTypeParam)

        val requests = mutableListOf(officialStoreRequest, periodTypeRequest)
        try {
            val gqlResponse = graphqlRepository.getReseponse(requests)
            if (!gqlResponse.getError(GetIsOfficialResponse::class.java).isNullOrEmpty() &&
                    !gqlResponse.getError(PMPeriodTypeResponse::class.java).isNullOrEmpty()) {
                val shopInfoData = gqlResponse.getData<GetIsOfficialResponse>(GetIsOfficialResponse::class.java).getIsOfficial.data
                val periodTypeData = gqlResponse.getData<PMPeriodTypeResponse>(PMPeriodTypeResponse::class.java).goldGetPMSettingInfo.data
                officialStorePeriod = ShopScoreMapper.mapToGetIsOfficialStorePeriod(shopInfoData, periodTypeData)
            }
        } catch (e: Throwable) { }

        return officialStorePeriod
    }
}