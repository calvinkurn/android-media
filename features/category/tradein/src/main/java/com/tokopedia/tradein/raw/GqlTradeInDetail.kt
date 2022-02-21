package com.tokopedia.tradein.raw

const val GQL_TRADE_IN_DETAIL: String = """query getTradeInDetail(${'$'}params: GetTradeInDetailInput!) {
  getTradeInDetail(params: ${'$'}params) {
    BannerURL
    OriginalPriceFmt
    ActivePromo {
      Code
      Title
      Subtitle
    }
    ErrMessage
    LogisticMessage
    LogisticOptions {
      IsDiagnosed
      IsAvailable
      IsPreferred
      Is3PL
      DiscountPercentageFmt
      Title
      EstimatedPriceFmt
      DiagnosticPriceFmt
      FinalPriceFmt
      Subtitle
      ExpiryTime
      DiagnosticReview
    }
    DeviceAttribute {
      Imei
      Model
      ModelId
      Brand
      Storage
      Ram
      Grade
    }
  }
}
"""