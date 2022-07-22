package com.tokopedia.tokomember_seller_dashboard.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokomember_seller_dashboard.model.TmDashHomeResponse
import javax.inject.Inject

class TokomemberDashHomeUsecase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<TmDashHomeResponse>(graphqlRepository) {

    @GqlQuery("tm_home", TM_HOME)
    fun getHomeData(
        success: (TmDashHomeResponse) -> Unit,
        onFail: (Throwable) -> Unit,
        shopId: Int
    ){
        this.setTypeClass(TmDashHomeResponse::class.java)
        this.setRequestParams(getRequestParams(shopId))
        this.setGraphqlQuery(TM_HOME)
        execute({
            success(it)
        }, {
            onFail(it)
        })
    }

    private fun getRequestParams(shopId: Int): Map<String, Any> {
        return mapOf(SHOP_ID to shopId)
    }

    companion object {
        const val SHOP_ID = "shopID"

    }
}

const val TM_HOME = """
    query membershipGetSellerAnalyticsTopSection(${'$'}shopID: Int!) {
        membershipGetSellerAnalyticsTopSection(shopID: ${'$'}shopID) {
            ticker {
              id
              title
              description
              iconImageUrl
              isDismissable
              cta {
                text
                url
                urlMobile
                appLink
                type
                icon
                isShown
                isDisabled
                position
              }
            }
            shopProfile{
              card {
                id
                shopID
                name
                intro
                limit
                status
                index
                parentIDs
                tierGroupID
                numberOfLevel
                numberOfLevelStr
              }
              shop {
                id
                name
                url
                appLink
                type
                avatar
                shopStatusIconUrl
              }
              cardTemplate {
                id
                cardID
                backgroundImgUrl
                backgroundColor
                fontColor
              }
            }
            resultStatus {
              code
              message
              reason
            }
        }
    }
"""