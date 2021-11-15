package com.tokopedia.affiliate.model.raw

val GQL_Affiliate_On_Boarding: String = """mutation onboardAffiliate(${'$'}channel: OnBoardingRequest!) {
  onboardAffiliate(channel: ${"$"}channel) {
     data {
      status 
      error {
        error_type 
        message
        cta_text
        cta_link {
          desktop
          mobile
          android
          ios
        }
      }
    }
  }
}""".trimIndent()
