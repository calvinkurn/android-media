package com.tokopedia.kol.feature.comment.data.raw

const val GQL_QUERY_GET_USER_KOL_POST: String = """query GetUserKolPost(${'$'}userID: Int!, ${'$'}limit: Int!, ${'$'}cursor: String) {
    get_user_kol_post(userID: ${'$'}userID, cursor: ${'$'}cursor, limit: ${'$'}limit) {
        error
        data {
          id
          description
          commentCount
          showComment
          likeCount
          isLiked
          isFollow
          createTime
          userName
          userPhoto
          userInfo
          content {
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
        }
        lastCursor
    }
}"""