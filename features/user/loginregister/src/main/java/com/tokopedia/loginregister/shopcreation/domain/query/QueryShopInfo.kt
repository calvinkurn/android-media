package com.tokopedia.loginregister.shopcreation.domain.query

/**
 * Created by Ade Fulki on 2020-02-06.
 * ade.hadian@tokopedia.com
 */

object QueryShopInfo {

    private const val shopID = "\$shopID"

    fun getQuery(): String = """
        query shopInfoByID ($shopID: Int!){
          shopInfoByID(input:{shopIDs:[$shopID], fields:["other-shiploc"]}) {
            result {
              shippingLoc {
                provinceID
              }
            }
          }
        }
    """.trimIndent()
}