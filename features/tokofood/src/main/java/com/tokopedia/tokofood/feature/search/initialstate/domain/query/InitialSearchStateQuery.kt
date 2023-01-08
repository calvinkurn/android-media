package com.tokopedia.tokofood.feature.search.initialstate.domain.query

const val INITIAL_SEARCH_STATE_QUERY = """
    query tokofoodInitSearchState(${'$'}location: String!) {
      tokofoodInitSearchState(location: ${'$'}location) {
      sections {
        id
        header
        labelText
        labelAction
        type
        items {
          id
          imageURL
          applink
          template
          title
          subtitle
          label
          labelType
          shortcutImage
          shortcutAction
        }
      }
  }
}
"""

const val REMOVE_SEARCH_HISTORY_QUERY = """
  mutation tokofoodRemoveSearchHistory(${'$'}key: String!){
      tokofoodRemoveSearchHistory(key: ${'$'}key) {
        message
        success
      }
}
"""