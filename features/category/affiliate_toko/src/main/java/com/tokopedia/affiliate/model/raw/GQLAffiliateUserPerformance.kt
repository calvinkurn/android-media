package com.tokopedia.affiliate.model.raw

val GQL_Affiliate_USER_PERFORMANCE: String = """query getAffiliatePerformance(${'$'}dayRange: String!) {
  getAffiliatePerformance(dayRange: ${"$"}dayRange) {
    Data {
      Status
       Error {
        ErrorType
        Message
        CtaText
        CtaLink {
          DesktopURL
          AndroidURL
          IosURL
          MobileURL
        }
      }
      Data {
        AffiliateID
      	DayRange
        EndTime
        StartTime
        Metrics {
          MetricType
          MetricTitle
         	MetricValue
          MetricValueFmt
          MetricDifferenceValue
          MetricDifferenceValueFmt
          Order
          Tooltip{
            Description
          }
        }
      }
    }
  }
}""".trimIndent()