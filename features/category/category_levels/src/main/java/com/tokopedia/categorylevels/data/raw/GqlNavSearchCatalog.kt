package com.tokopedia.categorylevels.data.raw

const val GQL_NAV_SEARCH_CATALOG: String = """query searchCatalog(${'$'}query: String, ${'$'}start: Int, ${'$'}per_page: Int, ${'$'}ob: Int, ${'$'}device: String, ${'$'}orderBy: Int, ${'$'}scheme: String, ${'$'}source: String, ${'$'}st: String, ${'$'}rows: Int, ${'$'}filter: AceFilterInput) {
  searchCatalog: searchCatalog(query: ${'$'}query, start: ${'$'}start, per_page: ${'$'}per_page, ob: ${'$'}ob, device: ${'$'}device, orderBy: ${'$'}orderBy, scheme: ${'$'}scheme, source: ${'$'}source, st: ${'$'}st, rows: ${'$'}rows, filter: ${'$'}filter) {
    items {
      id
      name
      price_min
      count_product
      description
      image_uri
      uri
      price_min_raw
      department_id
    }
    count
    status
  }
}
"""