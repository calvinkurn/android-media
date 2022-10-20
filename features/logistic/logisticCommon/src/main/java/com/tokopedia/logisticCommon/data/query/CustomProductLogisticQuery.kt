package com.tokopedia.logisticCommon.data.query

object CustomProductLogisticQuery {

    val getCPL = """
        query ongkirGetCPLEditor(${'$'}input: OngkirGetCPLEditorInput!){
          ongkirGetCPLEditor (input:${'$'}input) {
            data {
              shipper_list {
                header
                description
                whitelabels {
                  title
                  description
                  shipper_product_ids
                  is_active
                }
                shippers {
                  shipper_id
                  shipper_name
                  logo
                  shipper_product {
                    shipper_product_id
                    shipper_product_name
                    shipper_service_name
                    ui_hidden
                    is_active
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
