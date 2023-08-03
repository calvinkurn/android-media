package com.tokopedia.scp_rewards.cabinet.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.scp_rewards.cabinet.domain.model.ScpRewardsGetMedaliSectionResponse
import com.tokopedia.scp_rewards.common.utils.API_VERSION_PARAM
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class MedaliSectionUseCase @Inject constructor() {

    @GqlQuery("ScpRewardsGetMedaliSectionLayoutGQL", GET_MEDALI_SECTION_GQL)
    suspend fun getMedaliHomePageSection(sectionParams: RequestParams): ScpRewardsGetMedaliSectionResponse {
        GraphqlUseCase<ScpRewardsGetMedaliSectionResponse>().apply {
            setTypeClass(ScpRewardsGetMedaliSectionResponse::class.java)
            setRequestParams(putCommonParams(sectionParams).parameters)
            setGraphqlQuery(ScpRewardsGetMedaliSectionLayoutGQL())
            return executeOnBackground()
        }
    }

    private fun putCommonParams(params: RequestParams): RequestParams {
        params.putString(API_VERSION_PARAM, "2.0.0")
        return params
    }
}

const val GET_MEDALI_SECTION_GQL = """
  query scpRewardsGetMedaliSectionLayout(${'$'}apiVersion: String!,${'$'}sourceName: String!,${'$'}pageName: String!) {
   scpRewardsGetMedaliSectionLayout(input:{apiVersion:${'$'}apiVersion,sourceName:${'$'}sourceName,pageName:${'$'}pageName}){
      resultStatus {
        code
        message
        status
      }
      medaliSectionLayoutList {
        id
        layout
        display
        medaliSectionTitle {
          content
          description
          color
        }
        backgroundImageURL
        backgroundColor
        jsonParameter
      }
  }
}
"""
