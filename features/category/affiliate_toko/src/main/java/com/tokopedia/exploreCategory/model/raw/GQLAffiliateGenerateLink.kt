package com.tokopedia.exploreCategory.model.raw

const val GQL_Affiliate_Generate_Link: String = """mutation generateAffiliateLink(${"$"}affiliateID: String!, ${"$"}channel: [String], ${"$"}link: [Link]) {
  generateAffiliateLink(affiliateID: ${"$"}affiliateID, channel: ${"$"}channel, link: ${"$"}link) {
    status
    data {
      error
      channelID
      url {
        original
        regular
        short
      }
    }
    error {
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
}"""