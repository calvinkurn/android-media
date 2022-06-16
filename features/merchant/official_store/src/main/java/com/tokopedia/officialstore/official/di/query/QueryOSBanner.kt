package com.tokopedia.officialstore.official.di.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.officialstore.official.di.query.QueryOSBanner.OS_BANNER_QUERY
import com.tokopedia.officialstore.official.di.query.QueryOSBanner.OS_BANNER_QUERY_NAME

/**
 * Created by dhaba
 */
@GqlQuery(OS_BANNER_QUERY_NAME, OS_BANNER_QUERY)
internal object QueryOSBanner {
    const val OS_BANNER_QUERY_NAME = "OSBannerQuery"
    const val OS_BANNER_QUERY = "query OfficialStoreBanners(\$page: String) {\n" +
            "  slides(device: 4) {\n" +
            "\t\tslides(page: \$page) {\n" +
            "          id\n" +
            "          title\n" +
            "          image_url\n" +
            "          topads_view_url\n" +
            "          redirect_url\n" +
            "          applink\n" +
            "      \t  galaxy_attribution\n" +
            "          persona\n" +
            "       \t  category_persona\n" +
            "       \t  brand_id\n" +
            "       \t  campaignCode\n" +
            "        }\n" +
            "    }\n" +
            "}"
}