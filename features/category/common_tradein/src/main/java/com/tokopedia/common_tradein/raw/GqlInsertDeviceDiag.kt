package com.tokopedia.common_tradein.raw

const val GQL_INSERT_DEVICE_DIAG: String = """mutation getTradeInDeviceDiag(${'$'}params:TradeInInsertDeviceDiagInput!){
 insertTradeInDeviceDiag(params:${'$'}params)
 {
    	IsEligible
    	RemainingPrice
    	Message
  	}
}
"""