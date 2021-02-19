package com.tokopedia.catalog.model.raw

const val CATALOG_GQL_QUICK_FILTER: String = """query DynamicAttributes(${'$'}source: String, ${'$'}q: String, ${'$'}filter: DAFilterQueryType) {
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
            }
          }
          isPopular
          isNew
          Description
        }
      }
    }
  }
}"""