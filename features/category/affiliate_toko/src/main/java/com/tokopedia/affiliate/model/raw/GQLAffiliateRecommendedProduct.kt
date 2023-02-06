package com.tokopedia.affiliate.model.raw

val GQL_Affiliate_Recommended_Product: String = """query recommendedAffiliateProduct(${"$"}identifier: String!,${"$"}page: Int,${"$"}limit: Int){
  recommendedAffiliateProduct(identifier: ${"$"}identifier,page: ${"$"}page,limit: ${"$"}limit) {
    data {
      status
      cards {
        id
        title
        items {
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
          cardUrl {
            DesktopURL
            MobileURL
            AndroidURL
            IosURL
          }
          commission {
            percentage
            amount
            threshold
            amountFormatted
            percentageFormatted
            sellerAmount
            sellerAmountFormatted
            sellerPercentage
            sellerPercentageFormatted
            expiryDate
            expiryDateFormatted
          }
          footer {
            footerType
            footerIcon
            footerText
          }
          rating
          productID
          ssaStatus
        }
      }
      pageInfo {
        hasNext
        hasPrev
        currentPage
        totalPage
        totalCount
      }
    }
    error {
      ErrorType
      Message
      CtaText
      CtaLink {
        DesktopURL
        MobileURL
        AndroidURL
        IosURL
      }
    }
  }
}
""".trimIndent()
