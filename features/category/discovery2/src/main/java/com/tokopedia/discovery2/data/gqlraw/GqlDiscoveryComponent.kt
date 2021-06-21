package com.tokopedia.discovery2.data.gqlraw

const val GQL_COMPONENT: String = """query ComponentInfo(${'$'}device: String!, ${'$'}component_id: String!, ${'$'}identifier: String!, ${'$'}filters: String) {
  componentInfo(device: ${'$'}device, component_id: ${'$'}component_id, identifier: ${'$'}identifier, filters: ${'$'}filters) {
  data
  }
}
"""

const val GQL_COMPONENT_QUERY_NAME : String = "componentInfo"