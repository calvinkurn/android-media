package com.tokopedia.catalog.model.raw

const val GQL_CATALOG_QUERY: String = """query catalogGetDetailModular(${'$'}catalog_id: String!) {
  catalogGetDetailModular(catalog_id: ${'$'}catalog_id) {
    basicInfo{
      id
      departmentId
      name
      brand
      tag
      description
      url
      mobileUrl
      catalogImage {
        imageUrl
        isPrimary
      }
      marketPrice {
        min
        max
        minFmt
        maxFmt
        date
        name
      }
      longDescription {
        title
        description
      }
    }
    components{
      id
      name
      type
      sticky
      data{
        ... on CatalogModularSpecification {
          name
          icon
          row{
            key
            value
          }
        }
      }
    }
  }
}
"""