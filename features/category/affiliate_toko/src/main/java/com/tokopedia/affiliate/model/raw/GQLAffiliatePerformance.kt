package com.tokopedia.affiliate.model.raw

val GQL_Affiliate_Performance: String = """query getAffiliateItemsPerformanceList(${"$"}page: Int,${"$"}limit: Int){
  getAffiliateItemsPerformanceList(page: ${"$"}page,limit: ${"$"}limit) {
    Data {
      Status
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
      SectionData {
        AffiliateID
        SectionTitle
        ItemTotalCount
        ItemTotalCountFmt
        StartTime
        EndTime
        DayRange
        Items {
          LinkID
          ItemID
          ItemType
          ItemTitle
          DefaultLinkURL
          Status
          SSAStatus
          Label {
            LabelType
            LabelText
          }
          Message
          SSAMessage
          Image {
            DesktopURL
            MobileURL
            AndroidURL
            IosURL
          }
          Footer {
            FooterType
            FooterIcon
            FooterText
          }
        }
      }
    }
  }
}""".trimIndent()
