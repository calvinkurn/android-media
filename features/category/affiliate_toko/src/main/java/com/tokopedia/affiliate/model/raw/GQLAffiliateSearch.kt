package com.tokopedia.affiliate.model.raw

val GQL_Affiliate_Search: String = """query searchAffiliate(${"$"}filter: [String]!){
  searchAffiliate(filter: ${"$"}filter) {
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
        pageType
        itemID
        items {
          productID
          title
          titleEmblem
          ssaStatus
          label {
            LabelType
            LabelText
          }
          message
          ssaMessage
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
}
""".trimIndent()
