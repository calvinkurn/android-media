package com.tokopedia.logisticorder.usecase.query

object TrackingPageQuery {

    val retryBooking = """
        mutation RetryBooking(${'$'}id: String!){
          retryBooking(orderID: ${'$'}id){
            order_id
            order_tx_id
            awbnum
            shipper_id
            shipper_product_id
          }
        }
    """.trimIndent()

    val retryAvailability = """
        query RetryAvailability(${'$'}id: String!){
          retryAvailability(orderID:${'$'}id){
            awbnum
            order_id
            order_tx_id
            deadline_retry
            deadline_retry_unixtime
            show_retry_button
            availability_retry
          }
        }
    """.trimIndent()

    val getDriverTip = """
        mutation mpLogisticDriverTipInfo (${'$'}input: MPLogisticDriverTipInfoInputs! ){
          mpLogisticDriverTipInfo(input: ${'$'}input) {
             status
             last_driver {
               photo
               name
               phone
               license_number
               is_changed
             }
             prepayment {
               info
               preset_amount
               max_amount
               min_amount
               payment_link
             }
             payment {
               amount
               amount_formatted
               method
               method_icon
             }
           }
         }
    """.trimIndent()
}
