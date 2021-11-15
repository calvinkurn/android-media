package com.tokopedia.affiliate.model.raw

val GQL_Affiliate_Performance_List: String = """query getAffiliatePerformanceList(${"$"}dayRange: String!) {
  getAffiliatePerformanceList(dayRange: ${"$"}dayRange) {
    Data {
      Status
      Error {
        ErrorType
        Message
        CtaText
        CtaLink {
          DesktopURL
          IosURL
          AndroidURL
          MobileURL
        }
      }
      Data {
        AffiliateID
        ItemTotalCount
        ItemTotalCountFmt
        StartTime
        EndTime
        DayRange
        Items {
          ItemID
          ItemType
          ItemTitle
          Status
          Image {
            DesktopURL
            MobileURL
            AndroidURL
            IosURL
          }
          Metrics {
            MetricType
            MetricTitle
            MetricValue
            MetricValueFmt
            MetricDifferenceValue
            MetricDifferenceValueFmt
            Order
          }
        }
      }
    }
  }
}""".trimIndent()
