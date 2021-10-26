package com.tokopedia.affiliate.model.raw

val GQL_Affiliate_Announcement: String = """query getAffiliateAnnouncement(${"$"}userID : String!){
  getAffiliateAnnouncement(userID: ${"$"}userID) {
    data {
      status
      type
      announcement
      cta_text
      cta_link {
        desktop
        mobile
        android
        ios
      }
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