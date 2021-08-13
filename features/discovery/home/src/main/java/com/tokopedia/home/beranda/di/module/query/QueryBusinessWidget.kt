package com.tokopedia.home.beranda.di.module.query

object QueryBusinessWidget {
    val businessWidgetQuery : String = "query HomeWidget() {\n" +
            "  home_widget {\n" +
            "    widget_tab {\n" +
            "      id\n" +
            "      name\n" +
            "    }\n" +
            "    widget_header{\n" +
            "        back_color\n" +
            "    }\n" +
            "  }\n" +
            "}\n"

    val businessUnitDataQuery : String = "query(\$tabId:Int){\n" +
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
            "    }\n" +
            "  }\n" +
            "}\n" +
            "\n"
}