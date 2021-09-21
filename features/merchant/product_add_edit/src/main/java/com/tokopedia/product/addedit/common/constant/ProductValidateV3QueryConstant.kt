package com.tokopedia.product.addedit.common.constant

object ProductValidateV3QueryConstant {
    val BASE_QUERY = """
            mutation ProductValidateV3(%1s) {
                  ProductValidateV3(%2s) {
                    header {
                            messages
                            reason
                            errorCode
                    }
                    isSuccess
                    data {
                       %3s
                    }
                  }
             }
        """.trimIndent()
}