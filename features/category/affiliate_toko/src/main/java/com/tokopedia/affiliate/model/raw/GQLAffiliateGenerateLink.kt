package com.tokopedia.affiliate.model.raw

val GQL_Affiliate_Generate_Link: String = """mutation generateAffiliateLink(${'$'}input: GenerateLinkRequest!) {
  generateAffiliateLink(input: ${"$"}input) {
     Data {
      Status
      Type
      Error
      Identifier
      IdentifierType
      LinkID
      URL {
        ShortURL
        RegularURL
      }
    }
  }
}""".trimIndent()
