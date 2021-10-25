package com.tokopedia.affiliate.model.raw

val GQL_Affiliate_Recommended_Product : String = """query recommendedAffiliateProduct(${"$"}userID: [String]!, ${"$"}identifier: [String]!){
  searchAffiliate(userID: ${"$"}userID, identifier: ${"$"}identifier) {
    data {
      status
      error {
        errorType
        title
        message
        errorImage {
          DesktopURL
          MobileURL
          AndroidURL
          IosURL
        }
        errorStatus
        errorCta {
          ctaText
          ctaType
          ctaAction
          ctaLink {
            DesktopURL
            MobileURL
            AndroidURL
            IosURL
          }
        }
      }
      cards {
        id
        hasMore
        title
        items {
          productID
          title
          image {
            DesktopURL
            MobileURL
            AndroidURL
            IosURL
          }
          additionalInformation {
            htmlText
            type
            color
          }
          cardUrl
          commission {
            amount
            amountFormatted
            percentage
            percentageFormatted
          }
          footer {
            footerType
            footerIcon
            footerText
          }
          rating
          status {
            messages {
              title
              description
              messageType
            }
            isLinkGenerationAllowed
          }
        }
      }
    }
  }
}""".trimIndent()
