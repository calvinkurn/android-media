package com.tokopedia.affiliate.model.raw

val GQL_Affiliate_Validate: String = """query validateAffiliateUserStatus(${"$"}email : String!){
  validateAffiliateUserStatus(email: ${"$"}email) {
    Data {
      Status
      IsRegistered
      IsEligible
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
    }
  }
}""".trimIndent()