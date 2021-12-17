package com.tokopedia.home.beranda.di.module.query

internal object QueryHomeWallet {
    val tokopointsQuery: String = "query(\$apiVersion:String){\n" +
            "    tokopointsDrawer(apiVersion: \$apiVersion){\n" +
            "        iconImageURL\n" +
            "        redirectURL\n" +
            "        redirectAppLink\n" +
            "        sectionContent{\n" +
            "            type\n" +
            "            textAttributes{\n" +
            "                text\n" +
            "                color\n" +
            "                isBold\n" +
            "            }\n" +
            "            tagAttributes{\n" +
            "                text\n" +
            "                backgroundColor\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "}"
}