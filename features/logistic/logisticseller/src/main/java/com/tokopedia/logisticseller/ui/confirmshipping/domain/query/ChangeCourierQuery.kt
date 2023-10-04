package com.tokopedia.logisticseller.ui.confirmshipping.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.usecase.RequestParams

object ChangeCourierQuery : GqlQueryInterface {

    private const val OPERATION_NAME = "mpLogisticChangeCourier"
    private val CHANGE_COURIER_QUERY = """
        mutation $OPERATION_NAME(${'$'}orderId: String!, ${'$'}shippingRef: String!, ${'$'}agencyId: Int!, ${'$'}spId: Int!){
              $OPERATION_NAME(input: {
                                order_id: ${'$'}orderId,
                                shipping_ref: ${'$'}shippingRef,
                                agency_id: ${'$'}agencyId,
                                sp_id: ${'$'}spId
                                }) {
                    message
              }
        }
     """.trimIndent()

    private const val ORDER_ID_KEY = "orderId"
    private const val SHIPPING_REF_KEY = "shippingRef"
    private const val AGENCY_ID_KEY = "agencyId"
    private const val SP_ID_KEY = "spId"

     fun createParamChangeCourier(orderId: String, shippingRef: String, agencyId: Long, spId: Long): Map<String, Any> {
        return RequestParams.create().apply {
            putString(ORDER_ID_KEY, orderId)
            putString(SHIPPING_REF_KEY, shippingRef)
            putLong(AGENCY_ID_KEY, agencyId)
            putLong(SP_ID_KEY, spId)
        }.parameters
    }

    override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)

    override fun getQuery(): String = CHANGE_COURIER_QUERY

    override fun getTopOperationName(): String = OPERATION_NAME

}
