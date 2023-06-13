package com.tokopedia.tokopoints.view.tokopointhome

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.mvcwidget.MVC_REWARD_MULTISHOP_QUERY
import com.tokopedia.tokopoints.di.TokoPointScope
import com.tokopedia.tokopoints.view.model.rewardintro.IntroResponse
import com.tokopedia.tokopoints.view.model.rewardtopsection.RewardResponse
import com.tokopedia.tokopoints.view.model.rewrdsStatusMatching.RewardTickerListResponse
import com.tokopedia.tokopoints.view.model.section.TokopointsSectionOuter
import com.tokopedia.tokopoints.view.model.usersaving.UserSavingResponse
import com.tokopedia.tokopoints.view.util.CommonConstant
import com.tokopedia.tokopoints.view.util.CommonConstant.Companion.APIVERSION
import com.tokopedia.tokopoints.view.util.CommonConstant.Companion.REWARDS_SOURCE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import javax.inject.Named
import kotlin.collections.HashMap

@TokoPointScope
class TokopointsHomeUsecase @Inject constructor(@Named(CommonConstant.GQLQuery.TP_GQL_REWARD_INTRO) val tp_gql_reward_intro: String,
                                                @Named(CommonConstant.GQLQuery.TP_GQL_REWARD_USESAVING) val tp_gql_usersaving: String) {


    @Inject
    lateinit var tpHomeUsecase: MultiRequestGraphqlUseCase

    @GqlQuery("TpHomePageSection", TP_HOMEPAGE_SECTION)
    suspend fun getTokoPointDetailData() = withContext(Dispatchers.IO) {
        tpHomeUsecase.clearRequest()
        //Main details
        tpHomeUsecase.addRequest(getTopSectionGqlreuest())
        //Section
        val variables: MutableMap<String, Any> = HashMap()
        variables[CommonConstant.GraphqlVariableKeys.APIVERSION] = APIVERSION_SECTION
        val request = GraphqlRequest(
            String.format(TpHomePageSection.GQL_QUERY,MVC_REWARD_MULTISHOP_QUERY),
                TokopointsSectionOuter::class.java, variables,false)
        tpHomeUsecase.addRequest(request)
        tpHomeUsecase.executeOnBackground()
    }

    @GqlQuery("TPRewardTopSection", TP_REWARD_TOPSECTION)
    fun getTopSectionGqlreuest() : GraphqlRequest{
        val variables: MutableMap<String, Any> = HashMap()
        variables[CommonConstant.GraphqlVariableKeys.APIVERSION] = APIVERSION_TOPSECTION
        return GraphqlRequest(TPRewardTopSection.GQL_QUERY,
            RewardResponse::class.java, variables, false)
    }

    suspend fun getRewardIntroData() = withContext(Dispatchers.IO) {
        tpHomeUsecase.clearRequest()
        val requestIntro = GraphqlRequest(tp_gql_reward_intro, IntroResponse::class.java, false)
        tpHomeUsecase.addRequest(requestIntro)
        tpHomeUsecase.executeOnBackground()
    }

    suspend fun getUserSavingData() = withContext(Dispatchers.IO){
        val variables: MutableMap<String, Any> = HashMap()
        variables[CommonConstant.GraphqlVariableKeys.SAVING_YEAR] = Calendar.getInstance().get(Calendar.YEAR)
        variables[CommonConstant.GraphqlVariableKeys.SAVING_MONTH] = 0
        variables[CommonConstant.GraphqlVariableKeys.SAVING_TYPE] = 1
        variables[CommonConstant.GraphqlVariableKeys.APIVERSION] = "2.0"
        tpHomeUsecase.clearRequest()
        val requestSaving = GraphqlRequest(tp_gql_usersaving,UserSavingResponse::class.java,variables,false)
        tpHomeUsecase.addRequest(requestSaving)
        tpHomeUsecase.executeOnBackground()
    }

    @GqlQuery("TpStatusMatching", TP_STATUS_MATCHING_QUERY)
    suspend fun getUserStatusMatchingData() = withContext(Dispatchers.IO){
        val variables: MutableMap<String, Any> = HashMap()
        variables[CommonConstant.GraphqlVariableKeys.APIVERSION] = APIVERSION
        variables[CommonConstant.GraphqlVariableKeys.SOURCE] = REWARDS_SOURCE
        tpHomeUsecase.clearRequest()
        val requestSaving = GraphqlRequest(TpStatusMatching.GQL_QUERY,
            RewardTickerListResponse::class.java,variables,false)
        tpHomeUsecase.addRequest(requestSaving)
        tpHomeUsecase.executeOnBackground()
    }

    companion object {
        const val APIVERSION_SECTION = "3.0.0"
        const val APIVERSION_TOPSECTION = "2.0.0"
    }
}
