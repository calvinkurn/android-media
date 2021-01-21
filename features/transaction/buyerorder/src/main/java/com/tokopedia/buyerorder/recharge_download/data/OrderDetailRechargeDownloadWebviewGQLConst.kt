package com.tokopedia.buyerorder.recharge_download.data

/**
 * @author by furqan on 21/01/2021
 */
object OrderDetailRechargeDownloadWebviewGQLConst {

    val ORDER_DETAIL_RECHARGE_INVOICE = """
        query RechargeEncodeInvoice(${'$'}orderID:Int!) {
          rechargeEncodeInvoice(orderID:${'$'}orderID) {
            data
          }
        }
    """.trimIndent()

}