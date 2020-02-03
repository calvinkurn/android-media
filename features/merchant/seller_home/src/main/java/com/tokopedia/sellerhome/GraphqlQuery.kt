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
            "      colorState\n" +
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

    const val GET_PROGRESS_DATA = "query getProgressData(\$shopID: Int!, \$dataKey: [String!]!, \$date: String!) {\n" +
            "getProgressBarData(shopID: \$shopID, dataKey: \$dataKey, date: \$date){\n" +
            "    data {\n" +
            "      dataKey\n" +
            "      valueTxt\n" +
            "      maxValueTxt\n" +
            "      value\n" +
            "      maxValue\n" +
            "      state\n" +
            "      subtitle\n" +
            "      error\n" +
            "      errorMsg\n" +
            "    }\n" +
            "  }\n" +
            "}"
}