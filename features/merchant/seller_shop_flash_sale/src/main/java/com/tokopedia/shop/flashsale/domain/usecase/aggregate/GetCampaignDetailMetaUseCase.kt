package com.tokopedia.shop.flashsale.domain.usecase.aggregate

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.shop.flashsale.data.request.GetSellerCampaignProductListRequest
import com.tokopedia.shop.flashsale.domain.entity.CampaignDetailMeta
import com.tokopedia.shop.flashsale.domain.usecase.GetSellerCampaignDetailUseCase
import com.tokopedia.shop.flashsale.domain.usecase.GetSellerCampaignProductListUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class GetCampaignDetailMetaUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val getSellerCampaignDetailUseCase: GetSellerCampaignDetailUseCase,
    private val getSellerCampaignProductListUseCase: GetSellerCampaignProductListUseCase,
) : GraphqlUseCase<CampaignDetailMeta>(repository) {

    companion object {
        private const val PRODUCT_LIST_ROWS = 50
        private const val OFFSET = 0
        private const val LIST_TYPE = 0
    }

    suspend fun execute(campaignId: Long): CampaignDetailMeta {
        return coroutineScope {
            val campaignDetailDeferred = async {
                getSellerCampaignDetailUseCase.execute(campaignId)
            }
            val campaignProductListDeferred = async {
                getSellerCampaignProductListUseCase.execute(
                    campaignId,
                    listType = LIST_TYPE,
                    pagination = GetSellerCampaignProductListRequest.Pagination(
                        PRODUCT_LIST_ROWS,
                        OFFSET
                    )
                )
            }
            val campaignDetail = campaignDetailDeferred.await()
            val campaignProductList = campaignProductListDeferred.await()
            CampaignDetailMeta(
                campaignDetail,
                campaignProductList
            )
        }
    }
}