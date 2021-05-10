package com.tokopedia.logisticorder.usecase.query

object TrackingPageQuery {

    val getTrackingPage = """
        query logistic_tracking (${'$'}orderId: String!, ${'$'}from: String!) {
          logistic_tracking(input: {order_id: ${'$'}orderId, from: ${'$'}from}) {
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
            }
          }
        }

    """.trimIndent()

    val retryBooking = """
        mutation RetryBooking(${'$'}id: String!){
          retryBooking(orderID: {'$'}id){
            order_id
            order_tx_id
            awbnum
            shipper_id
            shipper_product_id
          }
        }
    """.trimIndent()
}