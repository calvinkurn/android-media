package com.tokopedia.sellerhome

/**
 * Created By @ilhamsuaib on 2020-01-15
 */
object GraphqlQuery {

    const val GET_LAYOUT = "{" +
            "apakah query disimpan di constant atau di raw resource?" +
            "}"

    const val GET_LINE_GRAPH_DATA = "query getLineGraphData(\$shopID: String!, \$dataKey: [String!]!, \$startDate: String!, \$endDate: String!) {\n" +
            "  getLineGraphData(shopID: \$shopID, dataKey: \$dataKey, startDate: \$startDate, endDate: \$endDate) {\n" +
            "    data {\n" +
            "      dataKey\n" +
            "      header\n" +
            "      description\n" +
            "      yLabels {\n" +
            "        yVal\n" +
            "        yLabel\n" +
            "      }\n" +
            "      list {\n" +
            "        yVal\n" +
            "        yLabel\n" +
            "        xLabel\n" +
            "      }\n" +
            "      error\n" +
            "    }\n" +
            "  }\n" +
            "}\n"
}