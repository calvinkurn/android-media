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
                mediaURLType
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
                  mediaURLTitle
                  iconURL
                  subtitle
                  subtitleMode
                  label1
                  label1Mode
                  label2
                  label3
                  appLink
                  webLink
                  backgroundColor
                  campaignLabelText
                  campaignLabelTextColor
                  campaignLabelBackgroundURL
                  productInfo1 {
                    text
                    color
                  }
                  productInfo2 {
                    text
                    color
                  }
                  ratingType
                  rating
                  review
                  soldPercentageValue
                  soldPercentageLabel
                  soldPercentageLabelColor
                  showSoldPercentage
                  slashedPrice
                  discount
                  cashback
                  specialDiscount
                  price
                  pricePrefix
                  priceSuffix
                  specialInfoText
                  specialInfoColor
                  trackingData {
                    productID
                    operatorID
                    businessUnit
                    categoryName
                    categoryID
                    itemType
                    itemLabel
                    pricePlain
                    __typename
                  }
                }
              }
            }
        """.trimIndent()
    }
}