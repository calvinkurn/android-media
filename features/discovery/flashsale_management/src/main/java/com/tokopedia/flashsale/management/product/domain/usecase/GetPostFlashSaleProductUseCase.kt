package com.tokopedia.flashsale.management.product.domain.usecase

import android.text.TextUtils
import com.tokopedia.flashsale.management.data.FlashSaleAdminStatusIdTypeDef
import com.tokopedia.flashsale.management.data.FlashSaleCampaignStatusIdTypeDef
import com.tokopedia.flashsale.management.data.FlashSaleConstant
import com.tokopedia.flashsale.management.data.FlashSaleConstant.SOURCE_SELLERAPP
import com.tokopedia.flashsale.management.product.data.FlashSalePostProductGQL
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject
import javax.inject.Named

class GetPostFlashSaleProductUseCase @Inject
constructor(@Named(FlashSaleConstant.NAMED_REQUEST_POST_PRODUCT_LIST) private val gqlRawString: String,
            graphqlRepository: GraphqlRepository) : GraphqlUseCase<FlashSalePostProductGQL>(graphqlRepository) {

    init {
        setTypeClass(FlashSalePostProductGQL::class.java)
        setGraphqlQuery(gqlRawString)
    }

    fun joinComma(vararg s: String): String {
        return TextUtils.join(",", s)
    }

    fun setParams(campaignId: Int, start: Int, rows: Int, query: String, shopId: String,
                  statusId: Int) {
        val map = mutableMapOf<String, Any?>(FlashSaleConstant.PARAM_CID to campaignId,
                FlashSaleConstant.PARAM_SOURCE to SOURCE_SELLERAPP,
                FlashSaleConstant.PARAM_START to start,
                FlashSaleConstant.PARAM_ROWS to rows,
                FlashSaleConstant.PARAM_SHOP_ID to shopId,
                FlashSaleConstant.PARAM_SS to FlashSaleConstant.SYSTEM_STATUS_ACCEPTED)
        if (statusId > 0) {
            val massAdminStatus = when (statusId) {
                FlashSaleCampaignStatusIdTypeDef.READY_CANCELLED ->
                    FlashSaleAdminStatusIdTypeDef.NAKAMA_ACCEPTED.toString() // 1
                FlashSaleCampaignStatusIdTypeDef.ON_GOING,
                FlashSaleCampaignStatusIdTypeDef.FINISHED,
                FlashSaleCampaignStatusIdTypeDef.ONGOING_CANCELLED ->
                    joinComma(FlashSaleAdminStatusIdTypeDef.NAKAMA_ACCEPTED.toString(),  // 1,4
                            FlashSaleAdminStatusIdTypeDef.NAKAMA_TAKEOUT.toString())
                FlashSaleCampaignStatusIdTypeDef.IN_REVIEW,
                FlashSaleCampaignStatusIdTypeDef.READY,
                FlashSaleCampaignStatusIdTypeDef.REVIEW_CANCELLED,
                FlashSaleCampaignStatusIdTypeDef.READY_LOCKED,
                FlashSaleCampaignStatusIdTypeDef.READY_LOCKED_CANCELLED ->
                    joinComma(FlashSaleAdminStatusIdTypeDef.NOT_REVIEWED.toString(),
                            FlashSaleAdminStatusIdTypeDef.NAKAMA_ACCEPTED.toString(),  // 0,1,2
                            FlashSaleAdminStatusIdTypeDef.NAKAMA_REJECTED.toString())
                else -> ""
            }
            if (massAdminStatus.isNotEmpty()) {
                map.put(FlashSaleConstant.PARAM_MAS, massAdminStatus)
            }
        }
        if (query.isNotEmpty()) {
            map.put(FlashSaleConstant.PARAM_Q, query)
        }
        setRequestParams(map)
    }

}
