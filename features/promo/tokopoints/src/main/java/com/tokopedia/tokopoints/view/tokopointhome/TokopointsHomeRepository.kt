package com.tokopedia.tokopoints.view.tokopointhome

import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.tokopoints.di.TokoPointScope
import com.tokopedia.tokopoints.view.cataloglisting.CatalogPurchaseRedeemptionRepository
import com.tokopedia.tokopoints.view.model.TokenDetailOuter
import com.tokopedia.tokopoints.view.model.TokoPointSumCouponOuter
import com.tokopedia.tokopoints.view.model.rewardintro.IntroResponse
import com.tokopedia.tokopoints.view.model.rewardtopsection.RewardResponse
import com.tokopedia.tokopoints.view.model.section.TokopointsSectionOuter
import com.tokopedia.tokopoints.view.util.CommonConstant.GQLQuery.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named


@TokoPointScope
class TokopointsHomeRepository @Inject constructor(@Named(TP_GQL_TOKOPOINT_TOP_SECTION_NEW) private val tp_gql_topsection_new: String,
                                                   @Named(TP_GQL_LUCKY_EGG_DETAILS) private val tp_gql_lucky_egg_details: String,
                                                   @Named(TP_GQL_HOME_PAGE_SECTION) val tp_gql_homepage_section: String,
                                                   @Named(TP_GQL_SUM_COUPON) val tp_gql_sum_coupon: String,
                                                   @Named(TP_GQL_REWARD_INTRO) val tp_gql_reward_intro: String,
                                                   map: Map<String, String>) : CatalogPurchaseRedeemptionRepository(map) {


    @Inject
    lateinit var mGetTokoPointDetailUseCase: MultiRequestGraphqlUseCase

    @Inject
    lateinit var mGetCouponCountUseCase: MultiRequestGraphqlUseCase

    @Inject
    lateinit var mGetRewardIntoUseCase: MultiRequestGraphqlUseCase

    suspend fun getTokoPointDetailData() = withContext(Dispatchers.IO) {
        mGetTokoPointDetailUseCase.clearRequest()
        //Main details
        val request1 = GraphqlRequest(tp_gql_topsection_new,
                RewardResponse::class.java, false)
        mGetTokoPointDetailUseCase.addRequest(request1)
        //Lucky egg
        val request2 = GraphqlRequest(tp_gql_lucky_egg_details,
                TokenDetailOuter::class.java, false)
        mGetTokoPointDetailUseCase.addRequest(request2)
        //Section
        val request4 = GraphqlRequest(tp_gql_homepage_section,
                TokopointsSectionOuter::class.java, false)
        mGetTokoPointDetailUseCase.addRequest(request4)
        mGetTokoPointDetailUseCase.executeOnBackground()

    }

    suspend fun getCouponCountData() = withContext(Dispatchers.IO) {
        mGetCouponCountUseCase.clearCache()
        val request5 = GraphqlRequest(tp_gql_sum_coupon,
                TokoPointSumCouponOuter::class.java, false)
        mGetCouponCountUseCase.addRequest(request5)
        mGetCouponCountUseCase.executeOnBackground().getSuccessData<TokoPointSumCouponOuter>()

    }

    suspend fun getRewardIntroData() = withContext(Dispatchers.IO) {
        mGetRewardIntoUseCase.clearRequest()
        val requestIntro = GraphqlRequest(tp_gql_reward_intro, IntroResponse::class.java, false)
        mGetRewardIntoUseCase.addRequest(requestIntro)
        mGetRewardIntoUseCase.executeOnBackground()
    }
}