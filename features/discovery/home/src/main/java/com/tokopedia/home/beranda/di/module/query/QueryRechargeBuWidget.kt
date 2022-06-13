package com.tokopedia.home.beranda.di.module.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.home.beranda.di.module.query.QueryRechargeBuWidget.RECHARGE_BU_WIDGET_QUERY
import com.tokopedia.home.beranda.di.module.query.QueryRechargeBuWidget.RECHARGE_BU_WIDGET_QUERY_NAME

@GqlQuery(RECHARGE_BU_WIDGET_QUERY_NAME, RECHARGE_BU_WIDGET_QUERY)
internal object QueryRechargeBuWidget {
    const val RECHARGE_BU_WIDGET_QUERY_NAME = "RechargeBuWidgetQuery"
    const val RECHARGE_BU_WIDGET_QUERY = "query getBUWidget(\$widgetSource: WidgetSource!) {\n" +
            "                getBUWidget(widgetSource: \$widgetSource) {\n" +
            "                  title\n" +
            "                  subtitle\n" +
            "                  start_time\n" +
            "                  end_time\n" +
            "                  media_url\n" +
            "                  app_link\n" +
            "                  web_link\n" +
            "                  banner_app_link\n" +
            "                  banner_web_link\n" +
            "                  text_link\n" +
            "                  option_1\n" +
            "                  option_2\n" +
            "                  option_3\n" +
            "                  tracking{\n" +
            "                    action\n" +
            "                    data\n" +
            "                  }\n" +
            "                  items {\n" +
            "                    id\n" +
            "                    title\n" +
            "                    media_url\n" +
            "                    media_url_type\n" +
            "                    background_color\n" +
            "                    subtitle\n" +
            "                    subtitle_mode\n" +
            "                    label_1\n" +
            "                    label_1_mode\n" +
            "                    label_2\n" +
            "                    label_3\n" +
            "                    app_link\n" +
            "                    web_link\n" +
            "                    tracking{\n" +
            "                      action\n" +
            "                      data\n" +
            "                    }\n" +
            "                    tracking_data{\n" +
            "                      product_id\n" +
            "                      operator_id\n" +
            "                      business_unit\n" +
            "                      item_type\n" +
            "                      item_label\n" +
            "                      client_number\n" +
            "                      category_id\n" +
            "                      category_name\n" +
            "                    }\n" +
            "                    sold_percentage_value\n" +
            "                    sold_percentage_label\n" +
            "                    sold_percentage_label_color\n" +
            "                    show_sold_percentage\n" +
            "                  }\n" +
            "                }\n" +
            "            }"
}