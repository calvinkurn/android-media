package com.tokopedia.home.beranda.di.module.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.home.beranda.di.module.query.QueryBusinessWidgetData.BUSINESS_UNIT_DATA_QUERY
import com.tokopedia.home.beranda.di.module.query.QueryBusinessWidgetData.BUSINESS_UNIT_DATA_QUERY_NAME

@GqlQuery(BUSINESS_UNIT_DATA_QUERY_NAME, BUSINESS_UNIT_DATA_QUERY)
internal object QueryBusinessWidgetData {
    const val BUSINESS_UNIT_DATA_QUERY_NAME = "BusinessUnitDataQuery"
    const val BUSINESS_UNIT_DATA_QUERY : String = "query HomeWidgetData(\$tabId:Int){\n" +
            "  home_widget {\n" +
            "    widget_grid(tabID:\$tabId) {\n" +
            "      id\n" +
            "      name\n" +
            "      image_url\n" +
            "      url\n" +
            "      applink\n" +
            "      title_1\n" +
            "      desc_1\n" +
            "      title_2\n" +
            "      desc_2\n" +
            "      tag_name\n" +
            "      tag_type\n" +
            "      price\n" +
            "      original_price\n" +
            "      price_prefix\n" +
            "      template_id\n" +
            "      widget_tracking {\n" +
            "         business_unit\n" +
            "         user_type\n" +
            "         item_type\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}\n" +
            "\n"
}
