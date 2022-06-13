package com.tokopedia.home.beranda.di.module.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.home.beranda.di.module.query.QueryRechargeRecommendation.RECHARGE_RECOMMENDATION_QUERY
import com.tokopedia.home.beranda.di.module.query.QueryRechargeRecommendation.RECHARGE_RECOMMENDATION_QUERY_NAME

@GqlQuery(RECHARGE_RECOMMENDATION_QUERY_NAME, RECHARGE_RECOMMENDATION_QUERY)
internal object QueryRechargeRecommendation {
    const val RECHARGE_RECOMMENDATION_QUERY_NAME = "RechargeRecommendationQuery"
    const val RECHARGE_RECOMMENDATION_QUERY = "query rechargeRecommendation(\$type: Int!) {\n" +
            "              rechargeRecommendation(recommendationType: \$type) {\n" +
// LIST OF RECOMMENDATION
            "                UUID " +
// recharge_watf_userID, used as recommendation hash_key"
            "                recommendations: Recommendations {\n" +
            "                  contentID: ContentID\n" +
            "                  mainText: MainText\n" +
            "                  subText: SubText\n" +
            "                  applink: AppLink\n" +
            "                  link: Link\n" +
            "                  iconURL: IconURL\n" +
            "                  title: Title\n" +
            "                  backgroundColor: BackgroundColor\n" +
            "                  buttonText: ButtonText\n" +
            "                }\n" +
            "              }\n" +
            "            }"
}