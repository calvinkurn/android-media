package com.tokopedia.affiliate.model.raw

val GQL_Affiliate_Announcement: String = """query getAffiliateAnnouncementV2(${"$"}Page:Int!){
    getAffiliateAnnouncementV2(Page:${"$"}Page){
        Data {
          Id
          Status
          TickerType
          TickerSubType
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
            CtaTextSecondary
            CtaLinkSecondary{
              IosURL
              AndroidURL
              DesktopURL
              MobileURL
            }
            IllustrationURL
          }
        }
    }
}""".trimIndent()