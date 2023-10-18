package com.tokopedia.common_electronic_money.domain.query

import com.tokopedia.common_electronic_money.domain.query.RechargeEmoneyInquiryLogMutation.EMONEY_INQUIRY_LOG_MUTATION
import com.tokopedia.common_electronic_money.domain.query.RechargeEmoneyInquiryLogMutation.EMONEY_INQUIRY_LOG_OPERATION_NAME
import com.tokopedia.gql_query_annotation.GqlQuery

@GqlQuery(EMONEY_INQUIRY_LOG_OPERATION_NAME, EMONEY_INQUIRY_LOG_MUTATION)
object RechargeEmoneyInquiryLogMutation {
    const val EMONEY_INQUIRY_LOG_OPERATION_NAME = "rechargeEmoneyLog"
    const val EMONEY_INQUIRY_LOG_MUTATION = """
        mutation $EMONEY_INQUIRY_LOG_OPERATION_NAME(${'$'}log: RechargeEmoneyInquiryLogRequest!){
          rechargeEmoneyInquiryLog(log:${'$'}log){
            inquiry_id
            status
            message
          }
        }
    """
}
