package com.tokopedia.scp_rewards.cabinet.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.scp_rewards.cabinet.domain.model.ScpRewardsGetUserMedalisResponse
import com.tokopedia.scp_rewards.common.utils.API_VERSION_PARAM
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetUserMedaliUseCase @Inject constructor() {

    @GqlQuery("ScpRewardsGetUserMedalisByTypeGQL", GET_USER_MEDALI_GQL)
    suspend fun getUserMedalis(medaliParams: RequestParams): ScpRewardsGetUserMedalisResponse? {
        GraphqlUseCase<ScpRewardsGetUserMedalisResponse>().apply {
            setTypeClass(ScpRewardsGetUserMedalisResponse::class.java)
            setRequestParams(putCommonParams(medaliParams).parameters)
            setGraphqlQuery(ScpRewardsGetUserMedalisByTypeGQL())
            return try {
                executeOnBackground()
            } catch (err: Throwable) {
                null
            }
        }
    }

    private fun putCommonParams(params: RequestParams): RequestParams {
        params.putString(API_VERSION_PARAM, "1.0.0")
        return params
    }
}

const val GET_USER_MEDALI_GQL = """
    query scpRewardsGetUserMedalisByType(
    ${'$'}type:String,
    ${'$'}page: Int,
    ${'$'}pageSize: Int,
    ${'$'}apiVersion:String,
    ${'$'}pageName:String,
    ${'$'}sourceName:String 
) {
  scpRewardsGetUserMedalisByType(input:{
    type:${'$'}type,
    page:${'$'}page,
    pageSize:${'$'}pageSize,
    apiVersion:${'$'}apiVersion,
    pageName:${'$'}pageName,
    sourceName:${'$'}sourceName
  }){
  	resultStatus{
      code
      message
      status
    }
    medaliList{
      id
      name
      provider
      extraInfo
      logoImageURL
      celebrationImageURL
      isNewMedali
      progressionCompletement
      isDisabled
      cta {
        text
        url
        appLink
        isShown
      }
    }
    medaliBanner{
       imageList {
          imageURL
          redirectURL
          redirectAppLink
          creativeName
       }
    }
    paging{
      hasNext
      cta{
        text
        url
        appLink
        isShown
      }
    }
  }
}
"""
