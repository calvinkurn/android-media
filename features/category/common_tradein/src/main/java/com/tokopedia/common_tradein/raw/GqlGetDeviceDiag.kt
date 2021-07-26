package com.tokopedia.common_tradein.raw

const val GQL_GET_DEVICE_DIAG: String = """query getTradeInDeviceDiag(${'$'}params:TradeInDeviceDiagInput!){
 getTradeInDeviceDiag(params:${'$'}params)
 {
     IsEligible
     OldPrice
     RemainingPrice
     DeviceDiagId
     DeviceAttr{
       ModelId
       Imei
       Model
       Brand
       Storage
       Ram
       Grade
     }
     DeviceReview
     ExpiryTimeFmt
     ConvenienceFee{
       FeeId
       TotalFee
     }
     CommissionFee{
       FeeId
       Percentage
     }
     Message
   }
}
"""