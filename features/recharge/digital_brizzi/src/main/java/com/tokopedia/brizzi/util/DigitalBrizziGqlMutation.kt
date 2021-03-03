package com.tokopedia.brizzi.util

object DigitalBrizziGqlMutation {

    val emoneyLogBrizzi = """
        mutation rechargeBrizziLog(${'$'}log: RechargeEmoneyInquiryLogRequest!){
          rechargeEmoneyInquiryLog(log:${'$'}log){
            inquiry_id
            status
            message
          }
        }
    """.trimIndent()
}