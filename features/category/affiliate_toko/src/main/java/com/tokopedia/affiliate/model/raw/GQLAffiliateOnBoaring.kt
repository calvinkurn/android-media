package com.tokopedia.affiliate.model.raw

val GQL_Affiliate_On_Boarding: String = """mutation onboardAffiliate(${'$'}channel: [OnboardAffiliateChannelRequest!]!) {
  onboardAffiliate(input: {Channel : ${"$"}channel}) {
     data: Data {
      status: Status
      error: Error {
        message: Message
        ctaText: CtaText
        errorType: ErrorType
        ctaLink: CtaLink {
          desktopURL: DesktopURL
          mobileURL: MobileURL
        }
      }
    }
  }
}""".trimIndent()
