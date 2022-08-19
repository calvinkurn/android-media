package com.tokopedia.buyerorder.detail.domain.queries

import com.tokopedia.buyerorder.detail.domain.queries.DigiPersoQuery.QUERY
import com.tokopedia.buyerorder.detail.domain.queries.DigiPersoQuery.QUERY_NAME
import com.tokopedia.gql_query_annotation.GqlQuery

/**
 * created by @bayazidnasir on 19/8/2022
 */

@GqlQuery(QUERY_NAME, QUERY)
internal object DigiPersoQuery {
    const val QUERY_NAME = "QueryDigiPerso"
    const val QUERY = """
        query digiPersoGetPersonalizedItems(${"$"}input: DigiPersoGetPersonalizedItemsRequest!) {
          digiPersoGetPersonalizedItems(input: ${"$"}input) {
            title
            mediaURL
            appLink
            webLink
            textLink
            bannerAppLink
            bannerWebLink
            option1
            option2
            option3
            tracking {
              action
              data
            }
            items {
              id
              title
              mediaURL
              mediaUrlType
              subtitle
              subtitleMode
              label1
              label1Mode
              label2
              label3
              appLink
              webLink
              backgroundColor
              trackingData {
                productID
                operatorID
                businessUnit
                categoryName
                categoryID
                itemType
                itemLabel
                __typename
              }
            }
          }
        }
    """
}