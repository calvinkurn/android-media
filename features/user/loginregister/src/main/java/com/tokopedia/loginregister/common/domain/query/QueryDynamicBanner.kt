package com.tokopedia.loginregister.common.domain.query

object QueryDynamicBanner {

    private const val page = "\$page"

    fun getQuery(): String {
        return "" +
        "query getDynamicBanner($page: String!) {" +
            "GetAuthBanner(page: $page) {" +
                "banner_img_url" +
                "success" +
                "error_message " +
            "} " +
        "}".trimIndent()
    }
}