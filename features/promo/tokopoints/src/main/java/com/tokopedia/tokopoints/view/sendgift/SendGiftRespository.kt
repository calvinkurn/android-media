package com.tokopedia.tokopoints.view.sendgift

import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.model.RedeemCouponBaseEntity
import com.tokopedia.tokopoints.view.model.ValidateCouponBaseEntity
import com.tokopedia.tokopoints.view.util.CommonConstant
import com.tokopedia.tokopoints.view.util.CommonConstant.GQLQuery.TP_GQL_TOKOPOINT_REDEEM_COUPON
import com.tokopedia.tokopoints.view.util.CommonConstant.GQLQuery.TP_GQL_TOKOPOINT_VALIDATE_REDEEM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rx.Subscriber
import java.util.HashMap
import javax.inject.Inject


class SendGiftRespository @Inject constructor(private val mStartSendGift: MultiRequestGraphqlUseCase,
                                              private val mRedeemCouponUseCase: MultiRequestGraphqlUseCase, private val map : Map<String, String>) {

    suspend fun sendGift(id: Int?, email: String, notes: String) = withContext(Dispatchers.IO){
        val variables: MutableMap<String, Any?> = HashMap()
        variables[CommonConstant.GraphqlVariableKeys.CATALOG_ID] = id
        variables[CommonConstant.GraphqlVariableKeys.IS_GIFT] = 1
        variables[CommonConstant.GraphqlVariableKeys.GIFT_EMAIL] = email
        variables[CommonConstant.GraphqlVariableKeys.NOTES] = notes
        val request = GraphqlRequest(map[TP_GQL_TOKOPOINT_REDEEM_COUPON],
                RedeemCouponBaseEntity::class.java,
                variables, false)
        mRedeemCouponUseCase.clearRequest()
        mRedeemCouponUseCase.addRequest(request)
        mRedeemCouponUseCase.executeOnBackground().getSuccessData<RedeemCouponBaseEntity>()
    }

    suspend fun preValidateGift(id: Int?, email: String) = withContext(Dispatchers.Main){
        val variables: MutableMap<String, Any?> = HashMap()
        variables[CommonConstant.GraphqlVariableKeys.CATALOG_ID] = id
        variables[CommonConstant.GraphqlVariableKeys.IS_GIFT] = 1
        variables[CommonConstant.GraphqlVariableKeys.GIFT_EMAIL] = email
        val graphqlRequest = GraphqlRequest(map[TP_GQL_TOKOPOINT_VALIDATE_REDEEM],
                ValidateCouponBaseEntity::class.java, variables, false)
        mStartSendGift.clearRequest()
        mStartSendGift.addRequest(graphqlRequest)
        mRedeemCouponUseCase.executeOnBackground().getSuccessData<ValidateCouponBaseEntity>()
    }
}