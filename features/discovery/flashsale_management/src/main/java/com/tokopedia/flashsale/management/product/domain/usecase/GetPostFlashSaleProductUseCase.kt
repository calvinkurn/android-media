package com.tokopedia.flashsale.management.product.domain.usecase

import android.text.TextUtils
import com.tokopedia.flashsale.management.data.FlashSaleConstant
import com.tokopedia.flashsale.management.data.FlashSaleConstant.ADMIN_STATUS_NAKAMA_ACCEPTED
import com.tokopedia.flashsale.management.data.FlashSaleConstant.ADMIN_STATUS_NAKAMA_REJECTED
import com.tokopedia.flashsale.management.data.FlashSaleConstant.ADMIN_STATUS_NAKAMA_TAKEOUT
import com.tokopedia.flashsale.management.data.FlashSaleConstant.ADMIN_STATUS_NOT_REVIEWED
import com.tokopedia.flashsale.management.data.FlashSaleConstant.SOURCE_SELLERDASHBOARD
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
                FlashSaleConstant.PARAM_SOURCE to SOURCE_SELLERDASHBOARD,
                FlashSaleConstant.PARAM_START to start,
                FlashSaleConstant.PARAM_ROWS to rows,
                FlashSaleConstant.PARAM_SHOP_ID to shopId,
                FlashSaleConstant.PARAM_SS to FlashSaleConstant.SYSTEM_STATUS_ACCEPTED)
        if (statusId > 0) {
            val massAdminStatus = when (statusId) {
                FlashSaleConstant.KEY_STATUS_READY_CANCELLED_EN ->
                    ADMIN_STATUS_NAKAMA_ACCEPTED.toString() // 1
                FlashSaleConstant.KEY_STATUS_ON_GOING_EN,
                FlashSaleConstant.KEY_STATUS_FINISHED_EN,
                FlashSaleConstant.KEY_STATUS_ONGOING_CANCELLED_EN ->
                    joinComma(ADMIN_STATUS_NAKAMA_ACCEPTED.toString(),  // 1,4
                            ADMIN_STATUS_NAKAMA_TAKEOUT.toString())
                FlashSaleConstant.KEY_STATUS_IN_REVIEW_EN,
                FlashSaleConstant.KEY_STATUS_READY_EN,
                FlashSaleConstant.KEY_STATUS_REVIEW_CANCELLED_EN,
                FlashSaleConstant.KEY_STATUS_READY_LOCKED_EN,
                FlashSaleConstant.KEY_STATUS_READY_LOCKED_CANCELLED_EN ->
                    joinComma(ADMIN_STATUS_NOT_REVIEWED.toString(),
                            ADMIN_STATUS_NAKAMA_ACCEPTED.toString(),  // 0,1,2
                            ADMIN_STATUS_NAKAMA_REJECTED.toString())
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
