package com.tokopedia.kol.feature.comment.data.raw

const val GQL_QUERY_GET_KOL_COMMENT: String = """query GetKolComment(${'$'}idPost: Int!, ${'$'}limit: Int!, ${'$'}cursor: String) {
  get_user_post_comment(idPost: ${'$'}idPost, cursor: ${'$'}cursor, limit: ${'$'}limit) {
    postKol {
      id
      headerTitle
      description
      commentCount
      likeCount
      isLiked
      isFollowed
      userName
      userPhoto
      userId
      userInfo
      userUrl
      createTime
      showComment
      userBadges
      content {
        imageurl
        tags {
          id
          type
          link
          price
          url
          caption
        }
      }
      source {
        origin
        type
      }
    }
    comments {
      id
      userID
      userName
      userPhoto
      isKol
      isCommentOwner
      create_time
      comment
      userBadge
      isShop
      allowReport
    }
    lastCursor
    error
  }
}"""
