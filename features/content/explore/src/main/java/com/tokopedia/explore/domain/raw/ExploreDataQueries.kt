package com.tokopedia.explore.domain.raw

object ExploreDataQueries {
    const val GET_EXPLORE_DATA_QUERY: String = """
    query GetExploreData (${'$'}limit: Int!, ${'$'}cursor: String!, ${'$'}idcategory: Int!, ${'$'}search: String!){
  get_discovery_kol_data(limit: ${'$'}limit, cursor: ${'$'}cursor, idcategory: ${'$'}idcategory, search: ${'$'}search) {
    error
    categories {
      id
      name
    }
    postKol {
      isLiked
      isFollow
      id
      commentCount
      likeCount
      createTime
      description
      content {
        type
        imageurl
        tags {
          id
          type
          url
          link
          price
          caption
        }
      }
      userName
      userInfo
      userIsFollow
      userPhoto
      userUrl
      userId
      tracking {
        clickURL
        viewURL
        type
        source
      }
    }
    lastCursor
    }
}
"""

    const val DEFAULT_CATEGORY_ID = 0
    const val PARAM_CATEGORY_ID = "idcategory"
    const val PARAM_CURSOR = "cursor"
    const val PARAM_SEARCH = "search"
    const val PARAM_LIMIT = "limit"
    const val LIMIT = 18
}