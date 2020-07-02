package com.tokopedia.find_native.data.raw

const val GQL_NAV_RELATED_LINK: String = "query findRelatedLink(\$keyword: String){\n" +
        "   categoryTkpdFindRelated(q: \$keyword){\n" +
        "    relatedHotlist {\n" +
        "      id\n" +
        "      url: URL\n" +
        "      text: name\n" +
        "    }\n" +
        "    relatedCategory {\n" +
        "      id\n" +
        "      url: URL\n" +
        "      text: name\n" +
        "    }\n" +
        "  }\n" +
        "}"
