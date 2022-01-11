package com.tokopedia.discovery2.data.gqlraw

const val GQL_SECTION: String = """query discoverySectionInfo(${'$'}identifier: String!, ${'$'}section_id: String!, ${'$'}device: String!, ${'$'}version: String, ${'$'}filters: String) { 
    discoverySectionInfo(identifier:${'$'}identifier, section_id:${'$'}section_id, device:${'$'}device, version:${'$'}version, filters:${'$'}filters){ 
        data 
    } 
}"""

const val SECTION_QUERY_NAME = "discoverySectionInfo"