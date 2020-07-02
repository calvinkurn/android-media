package com.tokopedia.kol.feature.comment.data.raw

const val GQL_MUTATION_SEND_REPORT: String = "mutation SendReport(\$content: Int!, \$contentType: String!, \$reasonType: String!, \$reasonMessage: String!) {\n" +
        "  feed_report_submit(content: \$content, contentType: \$contentType, reasonType: \$reasonType, reasonMessage: \$reasonMessage) {\n" +
        "    data {\n" +
        "      success\n" +
        "    }\n" +
        "    error\n" +
        "    error_message\n" +
        "    error_type\n" +
        "  }\n" +
        "}\n"