package com.tokopedia.tradein.raw

const val GQL_TRADE_IN_DETAIL: String = """query getTradeInDetail(${'$'}params: GetTradeInDetailInput!) {
  getTradeInDetail(params: ${'$'}params) {
        IsFraud
        ErrCode
        ErrMessage
        ErrTitle
        BannerURL
        OriginalPriceFmt
        ActivePromo {
            Code
            Title
            Subtitle
        }
        LogisticMessage
        LogisticOptions{
            IsDiagnosed
            IsAvailable
            IsPreferred
            Is3PL
            DiscountPercentageFmt
            Title
            EstimatedPriceFmt
            DiagnosticPriceFmt
            FinalPriceFmt
            DiagnosticPrice
            FinalPrice
            Subtitle
            ExpiryTime
            DiagnosticReview{
              Field
              Value
            }
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