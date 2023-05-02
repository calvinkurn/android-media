package com.tokopedia.product.manage.feature.multiedit.data.query

internal object BulkProductEditV3 {

   const val QUERY = """
        mutation BulkProductEditV3(${'$'}input:[ProductInputV3]!) {
          BulkProductEditV3(input:${'$'}input) 
          {
            productID,
            result {
              header {
              messages
              reason
              errorCode
            }
            isSuccess
            }
          }
        }
    """
}