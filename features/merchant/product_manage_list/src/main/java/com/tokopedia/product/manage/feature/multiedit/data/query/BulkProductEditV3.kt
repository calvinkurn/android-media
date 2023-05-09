package com.tokopedia.product.manage.feature.multiedit.data.query

internal object BulkProductEditV3 {

   const val QUERY = """
        mutation BulkProductEditV3(${'$'}input:[ProductInputV3]!, ${'$'}additionalParams: AdditionalParamsBulkProductV3) {
          BulkProductEditV3(input:${'$'}input, additionalParams: ${'$'}additionalParams) 
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
