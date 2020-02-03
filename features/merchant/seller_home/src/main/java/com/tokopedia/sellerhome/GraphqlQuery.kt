package com.tokopedia.sellerhome

/**
 * Created By @ilhamsuaib on 2020-01-15
 */
object GraphqlQuery {

    const val GET_LAYOUT = "{" +
            "apakah query disimpan di constant atau di raw resource?" +
            "}"

    const val GET_CARD_DATA = "query getCardWidgetData(\$shopID: Int!, \$dataKey: [String!]!, \$startDate: String!, \$endDate: String!) {\n" +
            "  getCardWidgetData(shopID: \$shopID, dataKey: \$dataKey, startDate: \$startDate, endDate: \$endDate) {\n" +
            "    data {\n" +
            "      dataKey\n" +
            "      value\n" +
            "      description\n" +
            "      state\n" +
            "      errorMsg\n" +
            "    }\n" +
            "  }\n" +
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

    const val GET_PROGRESS_DATA = "getProgressBarData(shopID: 481002, date:\"2019-10-01\", dataKey:[\"shopScore\"]){\n" +
            "    data {\n" +
            "      dataKey\n" +
            "      valueTxt\n" +
            "      maxValueTxt\n" +
            "      value\n" +
            "      maxValue\n" +
            "      state\n" +
            "      error\n" +
            "      errorMsg\n" +
            "    }\n" +
            "  }"
}