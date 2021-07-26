package com.tokopedia.tokopoints.view.tokopointhome

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.tokopoints.di.TokoPointScope
import com.tokopedia.tokopoints.view.cataloglisting.CatalogPurchaseRedeemptionRepository
import com.tokopedia.tokopoints.view.model.rewardintro.IntroResponse
import com.tokopedia.tokopoints.view.model.rewardtopsection.RewardResponse
import com.tokopedia.tokopoints.view.model.section.TokopointsSectionOuter
import com.tokopedia.tokopoints.view.model.usersaving.UserSavingResponse
import com.tokopedia.tokopoints.view.util.CommonConstant
import com.tokopedia.tokopoints.view.util.CommonConstant.GQLQuery.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import javax.inject.Named
import kotlin.collections.HashMap

@TokoPointScope
class TokopointsHomeUsecase @Inject constructor(@Named(CommonConstant.GQLQuery.TP_GQL_TOKOPOINT_TOP_SECTION_NEW) private val tp_gql_topsection_new: String,
                                                   @Named(CommonConstant.GQLQuery.TP_GQL_HOME_PAGE_SECTION) val tp_gql_homepage_section: String,
                                                   @Named(CommonConstant.GQLQuery.TP_GQL_REWARD_INTRO) val tp_gql_reward_intro: String,
                                                   @Named(CommonConstant.GQLQuery.TP_GQL_REWARD_USESAVING) val tp_gql_usersaving: String) {


    @Inject
    lateinit var mGetTokoPointDetailUseCase: MultiRequestGraphqlUseCase

    @Inject
    lateinit var mGetCouponCountUseCase: MultiRequestGraphqlUseCase

    @Inject
    lateinit var mGetRewardIntoUseCase: MultiRequestGraphqlUseCase

    @Inject
    lateinit var mGetUserSavingUsecase: MultiRequestGraphqlUseCase

    suspend fun getTokoPointDetailData() = withContext(Dispatchers.IO) {
        mGetTokoPointDetailUseCase.clearRequest()
        //Main details
        val request1 = GraphqlRequest(tp_gql_topsection_new,
                RewardResponse::class.java, false)
        mGetTokoPointDetailUseCase.addRequest(request1)
        //Section
        val request4 = GraphqlRequest(tp_gql_homepage_section,
                TokopointsSectionOuter::class.java, false)
        mGetTokoPointDetailUseCase.addRequest(request4)
        mGetTokoPointDetailUseCase.executeOnBackground()

    }

    suspend fun getRewardIntroData() = withContext(Dispatchers.IO) {
        mGetRewardIntoUseCase.clearRequest()
        val requestIntro = GraphqlRequest(tp_gql_reward_intro, IntroResponse::class.java, false)
        mGetRewardIntoUseCase.addRequest(requestIntro)
        mGetRewardIntoUseCase.executeOnBackground()
    }

    suspend fun getUserSavingData() = withContext(Dispatchers.IO){
        val variables: MutableMap<String, Any> = HashMap()
        variables[CommonConstant.GraphqlVariableKeys.SAVING_YEAR] = Calendar.getInstance().get(Calendar.YEAR)
        variables[CommonConstant.GraphqlVariableKeys.SAVING_MONTH] = 0
        variables[CommonConstant.GraphqlVariableKeys.SAVING_TYPE] = 1
        mGetUserSavingUsecase.clearRequest()
        val requestSaving = GraphqlRequest(tp_gql_usersaving,UserSavingResponse::class.java,variables,false)
        mGetUserSavingUsecase.addRequest(requestSaving)
        mGetUserSavingUsecase.executeOnBackground()
    }
}