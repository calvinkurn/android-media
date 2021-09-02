package com.tokopedia.exploreCategory.model.raw

const val GQL_Affiliate_Generate_Link: String = """{
  mutation generateAffiliateLink(
    affiliateID: "1241235"
    channel: ["instagram"]
    link: [{ type: "pdp", url:"https://www.tokopedia.com/product", identifier: "1234", params: [""] }]
  ) {
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