package com.tokopedia.exploreCategory.model.raw

const val GQL_Affiliate_Search: String = """query searchAffiliate(${"$"}affiliateID: String!){
  searchAffiliate(affiliateID: ${"$"}affiliateID) {
    data {
      status
      error {
        message
        cta_image {
          desktop
          mobile
          ios
          android
        }
        error_cta {
          cta_text
          cta_color
          cta_type
          cta_link {
            desktop
            mobile
            android
            ios
          }
        }
      }
      cards {
        id
        has_more
        title
        items {
          title
          image {
            desktop
            mobile
            android
            ios
          }
          additionalInformation {
            htmlText
            type
            color
          }
          commission {
            amountFormatted
            amount
            percentageFormatted
            percentage
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
            }
            isLinkGenerationAllowed
          }
        }
      }
    }
  }
}"""