package com.tokopedia.scp_rewards.detail.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.scp_rewards.celebration.domain.ScpRewardsCelebrationPage
import com.tokopedia.scp_rewards.celebration.domain.model.MedalDetailResponseModel
import javax.inject.Inject

@GqlQuery("ScpRewardsCelebrationPage", SCP_REWARDS_MEDAL_DETAIL_QUERY)
class MedalDetailUseCase @Inject constructor() : GraphqlUseCase<MedalDetailResponseModel>() {
    suspend fun getMedalDetail(medaliSlug: String, sourceName: String): MedalDetailResponseModel {
        setTypeClass(MedalDetailResponseModel::class.java)
        setGraphqlQuery(ScpRewardsCelebrationPage())
        setRequestParams(getRequestParams(medaliSlug, sourceName))
        return executeOnBackground()
    }

    private fun getRequestParams(medaliSlug: String, sourceName: String) = mapOf(
        API_VERSION_KEY to "5.0.0",
        MEDALI_SLUG_KEY to medaliSlug,
        SOURCE_NAME_KEY to sourceName
    )

    companion object {
        private const val API_VERSION_KEY = "apiVersion"
        private const val MEDALI_SLUG_KEY = "medaliSlug"
        private const val SOURCE_NAME_KEY = "sourceName"
    }
}

private const val SCP_REWARDS_MEDAL_DETAIL_QUERY = """
    
"""
