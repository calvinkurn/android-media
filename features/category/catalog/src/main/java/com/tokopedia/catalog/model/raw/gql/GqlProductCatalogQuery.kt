package com.tokopedia.catalog.model.raw.gql

const val GQL_CATALOG_QUERY: String = """query catalogGetDetailModular(${'$'}catalog_id: String!,${'$'}user_id: String!, ${'$'}device: String!) {
  catalogGetDetailModular(catalog_id: ${'$'}catalog_id,user_id: ${'$'}user_id, device: ${'$'}device) {
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
        ... on CatalogModularVideo{
          url
          type
          thumbnail
          author
          title
          videoId
        }
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
        ... on CatalogModularRecommendation{
          id
          name
          brand
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
          topSpec {
            key
            value
            icon
          }
          fullSpec {
            name
            icon
            row {
              key
              value
            }
          }
        }
      }
    }
  }
}
"""