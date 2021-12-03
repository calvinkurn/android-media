package com.tokopedia.affiliate.model.raw

val GQL_Affiliate_Announcement: String = """query getAffiliateAnnouncement(){
getAffiliateAnnouncement() {
    Data {
      Status
      Type
      AnnouncementTitle
      AnnouncementDescription
      CtaText
      CtaLink {
        DesktopURL
        MobileURL
        AndroidURL
        IosURL
      }
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