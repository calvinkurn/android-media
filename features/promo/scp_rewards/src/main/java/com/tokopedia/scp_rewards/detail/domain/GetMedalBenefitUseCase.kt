package com.tokopedia.scp_rewards.detail.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.scp_rewards.common.utils.PAGESIZE_PARAM
import com.tokopedia.scp_rewards.common.utils.PAGE_NAME_PARAM
import com.tokopedia.scp_rewards.common.utils.PAGE_PARAM
import com.tokopedia.scp_rewards.common.utils.SOURCE_NAME_PARAM
import com.tokopedia.scp_rewards.common.utils.TYPE_PARAM
import com.tokopedia.scp_rewards.detail.domain.model.MedalBenefitResponseModel
import javax.inject.Inject

@GqlQuery("ScpRewardsGetMedaliBenefitGQL", SCP_REWARDS_MEDAL_BENEFIT_QUERY)
class GetMedalBenefitUseCase @Inject constructor() : GraphqlUseCase<MedalBenefitResponseModel>() {
    suspend fun getMedalBenefits(medaliSlug: String, sourceName: String, pageName: String, type: String = ""): MedalBenefitResponseModel {
        setTypeClass(MedalBenefitResponseModel::class.java)
        setGraphqlQuery(ScpRewardsGetMedaliBenefitGQL())
        setRequestParams(getRequestParams(medaliSlug, sourceName, pageName, type))
        return executeOnBackground()
    }

    companion object {
        private const val MEDALI_SLUG_KEY = "medaliSlug"
    }

    private fun getRequestParams(medaliSlug: String, sourceName: String, pageName: String, type: String, pageNumber: Int = 1, pageSize: Int = 1) = mapOf(
            PAGE_NAME_PARAM to pageName,
            MEDALI_SLUG_KEY to medaliSlug,
            SOURCE_NAME_PARAM to sourceName,
            TYPE_PARAM to type,
            PAGE_PARAM to pageNumber,
            PAGESIZE_PARAM to pageSize
    )
}

private const val SCP_REWARDS_MEDAL_BENEFIT_QUERY = """
    query scpRewardsGetMedaliBenefitList(
        ${'$'}pageName:String, 
        ${'$'}sourceName:String,
        ${'$'}medaliSlug:String, 
        ${'$'}type:String, 
        ${'$'}page:Int, 
        ${'$'}pageSize:Int
    ) {
      scpRewardsGetMedaliBenefitList(input:{
        type:${'$'}type,
        page:${'$'}page,
        pageSize:${'$'}pageSize,
        medaliSlug:${'$'}medaliSlug,
        pageName:${'$'}pageName,
        sourceName:${'$'}sourceName
      }){
        resultStatus {
          code
          status
        }
        medaliBenefitList {
          category {
            id
            text
            iconImageURL
          }
          benefitInfo
          benefit {
            categoryID
            backgroundImageURL
            podiumImageURL
            medaliImageURL
            isActive
            status
            title
            tnc {
              text
            }
            type {
              iconImageURL
              backgroundColor
            }
            statusInfo {
              text
              backgroundColor
            }
            statusDescription
            info {
              text
              backgroundColor
            }
            url
            appLink
            benefitCTA {
              unifiedStyle
              text
              url
              appLink
              isAutoApply
              couponCode
            }
          }
          cta {
            unifiedStyle
            text
            url
            appLink
          }
          paging {
            hasNext
          }
        }
      }
    }
"""
