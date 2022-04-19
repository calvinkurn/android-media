package com.tokopedia.home.beranda.di.module.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.home.beranda.di.module.query.QueryListTokopoints.TOKOPOINTS_LIST_QUERY
import com.tokopedia.home.beranda.di.module.query.QueryListTokopoints.TOKOPOINTS_LIST_QUERY_NAME

@GqlQuery(TOKOPOINTS_LIST_QUERY_NAME, TOKOPOINTS_LIST_QUERY)
internal object QueryListTokopoints {
    const val TOKOPOINTS_LIST_QUERY_NAME = "TokopoinstListQuery"
    const val TOKOPOINTS_LIST_QUERY: String = "query tokopointsDrawerList(\$apiVersion:String){\n" +
            "    tokopointsDrawerList(apiVersion: \$apiVersion){\n" +
            "        offFlag\n" +
            "        drawerList{" +
            "           type\n" +
            "           iconImageURL\n" +
            "           redirectURL\n" +
            "           redirectAppLink\n" +
            "           sectionContent{\n" +
            "               type\n" +
            "               textAttributes{\n" +
            "                    text\n" +
            "                 color\n" +
            "                 isBold\n" +
            "               }\n" +
            "               tagAttributes{\n" +
            "                   text\n" +
            "                   backgroundColor\n" +
            "               }\n" +
            "           }\n" +
            "        }\n" +
            "       coachMarkList {\n" +
            "          type\n" +
            "          coachMarkContent {\n" +
            "          title\n" +
            "          content\n" +
            "        }\n" +
            "      }\n" +
            "   }\n" +
            "}"
}