package com.tokopedia.logisticorder.usecase.query

object TrackingPageQuery {

    val getTrackingPage = """
        query logistic_tracking (${'$'}input: MpLogisticTrackingInputParams!) {
          logistic_tracking(input: ${'$'}input) {
            message_error
            status
            data {
              track_order {
                detail {
            	  shipper_city
        		  shipper_name
                  receiver_city
                  receiver_name
                  send_date_time
                  send_date
                  send_time
                  service_code
        		  tracking_url
                }
                track_history {
                  date_time
                  date
                  status
                  city
                  time
                  proof {
                    image_id
                  }
                }
                change
                status
                order_status
                no_history
                receiver_name
                shipping_ref_num
                invalid
              }
              page {
                additional_info {
                  title
                  notes
                  url_detail
                  url_text
                }
              }
              tipping {
                status
                status_title
                status_subtitle
                last_driver {
                  photo
                  name
                  phone
                  license_number
                  is_changed
                }
              }
            }
          }
        }

    """.trimIndent()

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
        query logistic_driver_tip_info (${'$'}input: Input! ){
          logistic_driver_tip_info(input: ${'$'}input) {
            message_error
            status
            data {
              last_driver {
                name
                phone
                photo
                license_number
                is_changed
              }
              status
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
        }
    """.trimIndent()
}