package com.tokopedia.home.beranda.di.module.query

object QueryStickyLogin {
    val stickyLoginQuery : String = "query get_ticker(\$page: String!) {\n" +
            "  ticker {\n" +
            "    tickers(page: \$page) {\n" +
            "      message\n" +
            "      layout\n" +
            "    }\n" +
            "  }\n" +
            "}"
}