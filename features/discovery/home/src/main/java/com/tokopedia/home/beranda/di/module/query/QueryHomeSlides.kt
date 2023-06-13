package com.tokopedia.home.beranda.di.module.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.home.beranda.di.module.query.QueryHomeSlides.HOME_SLIDES_QUERY
import com.tokopedia.home.beranda.di.module.query.QueryHomeSlides.HOME_SLIDES_QUERY_NAME

@Deprecated("Has been migrated to banner v2")
@GqlQuery(HOME_SLIDES_QUERY_NAME, HOME_SLIDES_QUERY)
internal object QueryHomeSlides {
    const val HOME_SLIDES_QUERY_NAME = "HomeSlidesQuery"
    const val HOME_SLIDES_QUERY: String = "" +
            "query homeSlides(\$page: String, \$location: String)\n" +
            "        {\n" +
            "          slides {\n" +
            "            meta { total_data }\n" +
            "            slides(page: \$page, location: \$location) {\n" +
            "              id\n" +
            "              galaxy_attribution\n" +
            "              persona\n" +
            "              brand_id\n" +
            "              category_persona\n" +
            "              image_url\n" +
            "              redirect_url\n" +
            "              applink\n" +
            "              topads_view_url\n" +
            "              promo_code\n" +
            "              creative_name\n" +
            "              type\n" +
            "              category_id\n" +
            "              campaignCode\n" +
            "            }\n" +
            "          }\n" +
            "        }"
}
