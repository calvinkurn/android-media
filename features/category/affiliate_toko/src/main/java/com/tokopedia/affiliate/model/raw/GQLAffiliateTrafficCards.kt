package com.tokopedia.affiliate.model.raw

val GQL_Affiliate_Traffic_Cards: String  = """query getAffiliateTrafficCommissionDetailCards(${"$"}transactionDate: String!, ${"$"}lastID: String!, ${"$"}limit: Int, ${"$"}pageType: String!) {
  getAffiliateTrafficCommissionDetailCards(transactionDate: ${"$"}transactionDate, lastID: ${"$"}lastID, limit: ${"$"}limit, pageType: ${"$"}pageType) {
    Data {
      LastID
      HasNext
      Status
      Error {
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
      TrafficCommissionCardDetail {
        CardTitle
        CardDescription
        Image {
          DesktopURL
          MobileURL
          AndroidURL
          IosURL
        }
      }
    }
  }
}""".trimIndent()