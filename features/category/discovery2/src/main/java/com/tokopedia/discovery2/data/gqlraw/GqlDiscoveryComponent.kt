package com.tokopedia.discovery2.data.gqlraw

const val GQL_COMPONENT: String = """query ComponentInfo(${'$'}device: String!, ${'$'}component_id: String!, ${'$'}identifier: String!, ${'$'}filters: String, ${'$'}refresh_type: String!, , ${'$'}current_session_id: String!) {
  componentInfo(device: ${'$'}device, component_id: ${'$'}component_id, identifier: ${'$'}identifier, filters: ${'$'}filters, refresh_type: ${'$'}refresh_type, current_session_id: ${'$'}current_session_id,) {
  data
  }
}
"""

const val GQL_COMPONENT_QUERY_NAME: String = "componentInfo"
