package com.tokopedia.rechargegeneral.util

object RechargeGeneralGqlQuery {

    val catalogOperatorSelectGroup = """
        query catalogOperatorSelectGroup(${'$'}menuID: Int!) {
          rechargeCatalogOperatorSelectGroup(menuID:${'$'}menuID, platformID: 7){
            text
            style
            operatorGroup{
              name
              operators{
                type
                id
                attributes{
                  name
                  operator_labels
                  image_url
                  description
                }
              }
            }
          }
        }
    """.trimIndent()
}