package com.tokopedia.oldcatalog.model.raw.gql

const val GQL_CATALOG_QUERY: String = """query catalogGetDetailModular(${'$'}catalog_id: String!,${'$'}comparison_id: String!,${'$'}user_id: String!, ${'$'}device: String!) {
  catalogGetDetailModular(catalog_id: ${'$'}catalog_id,comparison_id: ${'$'}comparison_id,user_id: ${'$'}user_id, device: ${'$'}device) {
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
      productSortingStatus
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
    comparisonInfo{
      id
      name
      brand
      url
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
      fullSpec {
        name
        icon
        row {
          key
          value
        }
      }
      topSpec {
        key
        value
        icon
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
        ... on CatalogModularComparisonNew {
          spec_list {
            title
            sub_card {
              sub_title
              left_data
              right_data
            }
          }
          compared_data {
            id
            name
            brand
            url
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
          }
        }
        ... on CatalogModularProductReview{
          avgRating
          totalHelpfulReview
          reviews {
            rating
            informativeScore
            reviewerName
            reviewDate
            reviewText
            reviewImageUrl
            reviewId
            productUrl
          }
        }
        ... on CatalogLibraryEntrypointResponse {
          category_name
          category_identifier
          catalog_count
          catalogs {
            id
            name
            brand
            brand_id
            categoryID
            imageUrl
            url
            mobileUrl
            applink
            marketPrice {
              min
              max
              minFmt
              maxFmt
            }
          }
        }
      }
    }
  }
}
"""
