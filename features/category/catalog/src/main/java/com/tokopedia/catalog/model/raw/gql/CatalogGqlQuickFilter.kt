package com.tokopedia.catalog.model.raw.gql

const val CATALOG_GQL_QUICK_FILTER: String = """query filter_sort_product(${'$'}params: String!){
  filter_sort_product(params: ${'$'}params) {
    data{
      filter{
        title
        template_name
        search{
          searchable
          placeholder
        }
        options{
          name
          key
          icon
          value
          inputType
          totalData
          valMax
          valMin
          hexColor
          child{
            key
            value
            name
            icon
            inputType
            totalData
            isPopular
            child {
            key
            value
            name
            icon
            inputType
            totalData
            }
          }
          isPopular
          isNew
          Description
        }
      }
      sort{
        name
        key
        value
        inputType
      }
    }
  }
}"""