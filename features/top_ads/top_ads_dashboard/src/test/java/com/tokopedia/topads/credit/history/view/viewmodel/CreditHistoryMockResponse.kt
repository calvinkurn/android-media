package com.tokopedia.topads.credit.history.view.viewmodel

object CreditHistoryMockResponse {
    const val success = "{\n" +
            "    \"topadsCreditHistory\": {\n" +
            "      \"data\": {\n" +
            "        \"total_used\": 0,\n" +
            "        \"total_addition\": -2100,\n" +
            "        \"total_used_fmt\": \"Rp0\",\n" +
            "        \"total_addition_fmt\": \"- Rp2.100\",\n" +
            "        \"credit_history\": [\n" +
            "          {\n" +
            "            \"amount\": 1050,\n" +
            "            \"amount_fmt\": \"Rp1.050\",\n" +
            "            \"date\": \"2018-10-17T04:11:35\",\n" +
            "            \"show_timestamp\": true,\n" +
            "            \"description\": \"Kredit TopAds Gratis Kadaluarsa\",\n" +
            "            \"is_reduction\": true,\n" +
            "            \"transaction_type\": 10\n" +
            "          },\n" +
            "          {\n" +
            "            \"amount\": 1050,\n" +
            "            \"amount_fmt\": \"Rp1.050\",\n" +
            "            \"date\": \"2018-10-17T00:06:10\",\n" +
            "            \"show_timestamp\": true,\n" +
            "            \"description\": \"Kredit TopAds Gratis Kadaluarsa\",\n" +
            "            \"is_reduction\": true,\n" +
            "            \"transaction_type\": 10\n" +
            "          },\n" +
            "          {\n" +
            "            \"amount\": 1050,\n" +
            "            \"amount_fmt\": \"Rp1.050\",\n" +
            "            \"date\": \"2018-10-12T00:05:33\",\n" +
            "            \"show_timestamp\": true,\n" +
            "            \"description\": \"Kredit TopAds Gratis Kadaluarsa\",\n" +
            "            \"is_reduction\": true,\n" +
            "            \"transaction_type\": 10\n" +
            "          },\n" +
            "          {\n" +
            "            \"amount\": 1050,\n" +
            "            \"amount_fmt\": \"Rp1.050\",\n" +
            "            \"date\": \"2018-10-04T00:05:27\",\n" +
            "            \"show_timestamp\": true,\n" +
            "            \"description\": \"Kredit TopAds Gratis Kadaluarsa\",\n" +
            "            \"is_reduction\": true,\n" +
            "            \"transaction_type\": 10\n" +
            "          },\n" +
            "          {\n" +
            "            \"amount\": 1050,\n" +
            "            \"amount_fmt\": \"Rp1.050\",\n" +
            "            \"date\": \"2018-10-01T11:10:56\",\n" +
            "            \"show_timestamp\": true,\n" +
            "            \"description\": \"Kredit TopAds Gratis\",\n" +
            "            \"is_reduction\": false,\n" +
            "            \"transaction_type\": 9\n" +
            "          },\n" +
            "          {\n" +
            "            \"amount\": 1050,\n" +
            "            \"amount_fmt\": \"Rp1.050\",\n" +
            "            \"date\": \"2018-10-01T10:53:59\",\n" +
            "            \"show_timestamp\": true,\n" +
            "            \"description\": \"Kredit TopAds Gratis\",\n" +
            "            \"is_reduction\": false,\n" +
            "            \"transaction_type\": 9\n" +
            "          }\n" +
            "        ]\n" +
            "      },\n" +
            "      \"errors\": []\n" +
            "    }\n" +
            "  }"

    const val fail = "{\n" +
            "    \"topadsCreditHistory\": {\n" +
            "      \"data\": {\n" +
            "        \"total_used\": 0,\n" +
            "        \"total_addition\": 0,\n" +
            "        \"total_used_fmt\": \"\",\n" +
            "        \"total_addition_fmt\": \"\",\n" +
            "        \"credit_history\": []\n" +
            "      },\n" +
            "      \"errors\": [\n" +
            "        {\n" +
            "          \"code\": \"210100\",\n" +
            "          \"detail\": \"User id 24095630 is not allowed to access topads dashboard\",\n" +
            "          \"title\": \"Unauthorized Access\"\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  }"

    const val gqlQuery: String = "query topAdsCreditHistory(\$shopId: Int!, \$userId: Int!, \$startDate: String, \$endDate: String) {\n" +
            "    topadsCreditHistory(shopId: \$shopId, userId: \$userId, startDate: \$startDate, endDate: \$endDate){\n" +
            "        data{\n" +
            "            total_used\n" +
            "            total_addition\n" +
            "            total_used_fmt\n" +
            "            total_addition_fmt\n" +
            "            credit_history {\n" +
            "                amount\n" +
            "                amount_fmt\n" +
            "                date\n" +
            "                show_timestamp\n" +
            "                description\n" +
            "                is_reduction\n" +
            "                transaction_type\n" +
            "            }\n" +
            "        }\n" +
            "        errors {\n" +
            "            code\n" +
            "            detail\n" +
            "            title\n" +
            "        }\n" +
            "    }\n" +
            "}\n"
}