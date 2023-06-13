package com.tokopedia.common_sdk_affiliate_toko.raw

val GQL_Check_Cookie = """
   query checkAffiliateCookie(${'$'}input: CheckAffiliateCookieRequest!){
  checkAffiliateCookie(input:${'$'}input){
    Data{
      Status
      Error{
        ErrorType
        Message
        CtaText
        CtaLink{
          DesktopURL
          AndroidURL
          MobileURL
          IosURL
        }
      }
    }
    AffiliateUUID
  }
}
""".trimIndent()