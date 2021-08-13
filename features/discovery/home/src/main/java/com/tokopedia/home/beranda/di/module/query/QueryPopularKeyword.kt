package com.tokopedia.home.beranda.di.module.query

object QueryPopularKeyword {
    val popularKeywordQuery = "query PopularKeywords(\$count: Int!, \$page: Int!) {\n" +
            "                popular_keywords(count: \$count, page: \$page) {\n" +
            "                    recommendation_type\n" +
            "                    title\n" +
            "                    sub_title\n" +
            "                    keywords {\n" +
            "                      url\n" +
            "                      image_url\n" +
            "                      keyword\n" +
            "                      mobile_url\n" +
            "                      product_count\n" +
            "                      product_count_formatted\n" +
            "                    }\n" +
            "                }\n" +
            "            }"
}