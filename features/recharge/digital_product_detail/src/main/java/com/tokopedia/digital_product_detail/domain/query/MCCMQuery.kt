package com.tokopedia.digital_product_detail.domain.query

import com.tokopedia.digital_product_detail.domain.query.MCCMQuery.MCCM_OPERATION_NAME
import com.tokopedia.digital_product_detail.domain.query.MCCMQuery.MCCM_QUERY
import com.tokopedia.gql_query_annotation.GqlQuery

@GqlQuery(
    MCCM_OPERATION_NAME,
    MCCM_QUERY
)
object MCCMQuery {
    const val MCCM_OPERATION_NAME = "MCCMProductsQuery"
    const val MCCM_QUERY = """
         query $MCCM_OPERATION_NAME(${'$'}input: DigiPersoGetPersonalizedItemsRequest!) {
          digiPersoGetPersonalizedItems(input: ${'$'}input) {
            title
            tracking {
                action
                data
            }
            trackingData {
                userType
            }
            items {
                id
                title
                mediaURL
                mediaUrlType
                mediaURLDarkMode
                label1
                label2
                appLink
                webLink
                slashedPrice
                discount
                price
                descriptions
                pricePlain
                slashedPricePlain
                campaignLabelText
                trackingData {
                    productID
                    operatorID
                    businessUnit
                    categoryName
                    categoryID
                    itemType
                    itemLabel
                }
            }
          }
        }
    """
}
