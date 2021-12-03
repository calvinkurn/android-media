package com.tokopedia.affiliate.model.raw

val GQL_Affiliate_Performance_List: String = """query getAffiliatePerformanceList(${"$"}dayRange: String!,${"$"}lastID: String!) {
  getAffiliatePerformanceList(dayRange: ${"$"}dayRange,lastID: ${"$"}lastID) {
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
        LastID
        Items {
          DefaultLinkURL
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
