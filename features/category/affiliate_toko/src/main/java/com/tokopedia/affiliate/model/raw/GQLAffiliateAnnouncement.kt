package com.tokopedia.affiliate.model.raw

val GQL_Affiliate_Announcement: String = """query getAffiliateAnnouncementV2(){
    getAffiliateAnnouncementV2(){
        Data{
        Status
      TickerType
      TickerData {
        AnnouncementTitle
        AnnouncementDescription
        CtaText
        CtaLink{
          IosURL
          AndroidURL
          DesktopURL
          MobileURL
        }
      }
      Error {
        Message
        CtaText
        CtaLink{
          IosURL
          AndroidURL
          DesktopURL
          MobileURL
        }
      }
    }
  }
}""".trimIndent()