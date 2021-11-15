package com.tokopedia.affiliate.model.raw

val GQL_Affiliate_Performance_List: String = """query getAffiliateItemsPerformanceList(${"$"}dateRange: DateRangeRequest,${"$"}page: Int,${"$"}limit: Int){
  getAffiliateItemsPerformanceList(dateRange: ${"$"}dateRange,page: ${"$"}page,limit: ${"$"}limit) {
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
          Metrics {                    
            MetricType                  
            MetricTitle                 
            MetricValue                 
            MetricDifferenceValue       
            Trend                       
          }
        }
      }
    }
  }
}""".trimIndent()
