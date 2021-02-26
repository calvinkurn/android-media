package com.tokopedia.brizzi.util

object GqlMutation {

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