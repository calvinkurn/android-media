package com.tokopedia.affiliate.model

val GQL_Affiliate_Date_FILTER: String = """query getAffiliateDateFilter{
  getAffiliateDateFilter{
  	Ticker{
      TickerType
      TickerDescription
    }
    GetAffiliateDateFilter{
      FilterType
      FilterTitle
      FilterValue
      FilterDescription
      UpdateDescription
    }
  }
}
""".trimIndent()
