package com.tokopedia.power_merchant.subscribe.domain.query

internal object ValidatePowerMerchant {

    val QUERY = """
        query goldValidateShopBeforePM{
          goldValidateShopBeforePM{
            header{
              process_time
              messages
              reason
              error_code
            }
            data
          }
        }
    """.trimIndent()
}