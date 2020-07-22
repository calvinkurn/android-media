package raw

const val GQL_PRODUCT_CATALOG_QUERY: String = """query ProductCatalogQuery(${'$'}catalog_id: String!) {
  ProductCatalogQuery(catalog_id: ${'$'}catalog_id) {
    header {
      process_time
      status
    }
    data {
      catalog {
        id
        department_id
        name
        description
        identifier
        release_date
        update_time
        url
        catalog_image {
          image_url
          is_primary
        }
        topthreespec {
          value
        }
        market_price {
          min
          max
          min_fmt
          max_fmt
          date
          name
        }
        expert_review {
          source
          review
          good
          bad
          rating
          url
          image_url
          max_score
          source_id
        }
        specification {
          name
          row {
            key
            value
          }
        }
        long_description {
          title
          description
        }
      }
    }
  }
}
"""