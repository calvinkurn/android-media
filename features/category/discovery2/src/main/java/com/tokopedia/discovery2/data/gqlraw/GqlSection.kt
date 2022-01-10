package com.tokopedia.discovery2.data.gqlraw

const val GQL_SECTION: String = """querysectionInfo(${'$'}device: String!," +
        "${'$'}identifier: String!," +
        "${'$'}section_id: String!," +
        "${'$'}filters: String!){" +
        "  sectionInfo(device: ${'$'}device," +
        "  identifier: ${'$'}identifier," +
        "  section_id: ${'$'}section_id," +
        "  filters: ${'$'}filters){" +
        "    data" +
        "  }" +
        "}"""