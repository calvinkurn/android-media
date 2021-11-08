package com.tokopedia.common_tradein.raw

const val GQL_GET_DEVICE_DIAG: String = """query getTradeInDeviceDiagV2(${'$'}params:TradeInDeviceDiagInputV2!){
 getTradeInDeviceDiagV2(params:${'$'}params)
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