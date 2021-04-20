package com.tokopedia.catalog.model.raw.gql

const val GQL_CATALOG_QUERY: String = """query catalogGetDetailModular(${'$'}catalog_id: String!) {
  catalogGetDetailModular(catalog_id: ${'$'}catalog_id) {
    basicInfo{
      id
      departmentId
      name
      brand
      tag
      description
      shortDescription
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
        ... on CatalogModularTopSpec {
          key
          value
          icon
        }
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