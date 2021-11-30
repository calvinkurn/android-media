package com.tokopedia.product.manage.common.constant

object GetProductV3QueryConstant {
    val BASE_QUERY = """
            query %1s(%2s) {
                getProductV3(%3s) {
                    %4s
                  }
                }
        """.trimIndent()
}