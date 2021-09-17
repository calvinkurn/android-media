package com.tokopedia.logisticCommon.data.query

object CustomProductLogisticQuery {

    val getCPL = """
        query ongkirGetCPL(${'$'}input: OngkirGetCPLInput!){
          ongkirGetCPL (input:${'$'}input) {
            data {
              cpl_product {
                product_id
                cpl_status
                shipper_services
              }
              shipper_list {
                header
                description
                shipper {
                  shipper_id
                  shipper_name
                  logo
                  shipper_product {
                    shipper_product_id
                    shipper_product_name
                    ui_hidden
                  }
                }
              }
            } 
            errors {
              id
              title
              status
            }
          }
        }
    """.trimIndent()
}