package com.tokopedia.catalog.model.raw.gql

const val CATALOG_GQL_DYNAMIC_FILTER: String = """query DynamicAttributes(${'$'}source: String, ${'$'}q: String, ${'$'}filter: DAFilterQueryType) {
  dynamicAttribute(source: ${'$'}source, q: ${'$'}q, filter: ${'$'}filter) {
    data {
      filter {
        title
        template_name
        search {
          searchable
          placeholder
        }
        options {
          name
          key
          icon
          value
          inputType
          totalData
          valMax
          valMin
          hexColor
          child {
            key
            value
            name
            icon
            inputType
            totalData
            child {
              key
              value
              name
              icon
              inputType
              totalData
              child {
                key
                value
                name
                icon
                inputType
                totalData
              }
            }
          }
          isPopular
          isNew
          Description
        }
      }
      sort {
        name
        key
        value
        inputType
      }
    }
  }
}"""