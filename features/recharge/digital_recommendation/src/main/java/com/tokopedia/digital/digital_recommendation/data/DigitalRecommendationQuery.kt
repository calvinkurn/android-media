package com.tokopedia.digital.digital_recommendation.data

/**
 * @author by furqan on 20/09/2021
 */
class DigitalRecommendationQuery {
    companion object {
        val DG_RECOMMENDATION = """
            query digiPersoGetPersonalizedItems(${'$'}input: DigiPersoGetPersonalizedItemsRequest!) {
              digiPersoGetPersonalizedItems(input: ${'$'}input) {
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
                trackingData{
                  userType
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
        """.trimIndent()
    }
}