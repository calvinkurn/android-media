package com.tokopedia.kol.feature.comment.data.raw

const val GQL_QUERY_GET_KOL_COMMENT: String = "query GetKolComment(\$idPost: Int!, \$limit: Int!, \$cursor: String) {\n" +
        "  get_user_post_comment(idPost: \$idPost, cursor: \$cursor, limit: \$limit) {\n" +
        "    postKol {\n" +
        "      id\n" +
        "      headerTitle\n" +
        "      description\n" +
        "      commentCount\n" +
        "      likeCount\n" +
        "      isLiked\n" +
        "      isFollowed\n" +
        "      userName\n" +
        "      userPhoto\n" +
        "      userId\n" +
        "      userInfo\n" +
        "      userUrl\n" +
        "      createTime\n" +
        "      showComment\n" +
        "      userBadges\n" +
        "      content {\n" +
        "        imageurl\n" +
        "        tags {\n" +
        "          id\n" +
        "          type\n" +
        "          link\n" +
        "          price\n" +
        "          url\n" +
        "          caption\n" +
        "        }\n" +
        "      }\n" +
        "      source {\n" +
        "        origin\n" +
        "        type\n" +
        "      }\n" +
        "    }\n" +
        "    comments {\n" +
        "      id\n" +
        "      userID\n" +
        "      userName\n" +
        "      userPhoto\n" +
        "      isKol\n" +
        "      isCommentOwner\n" +
        "      create_time\n" +
        "      comment\n" +
        "      userBadge\n" +
        "      isShop\n" +
        "    }\n" +
        "    lastCursor\n" +
        "    error\n" +
        "  }\n" +
        "}\n"