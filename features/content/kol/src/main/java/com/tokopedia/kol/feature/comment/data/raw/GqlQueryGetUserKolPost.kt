package com.tokopedia.kol.feature.comment.data.raw

const val GQL_QUERY_GET_USER_KOL_POST: String = "query GetUserKolPost(\$userID: Int!, \$limit: Int!, \$cursor: String) {\n" +
        "    get_user_kol_post(userID: \$userID, cursor: \$cursor, limit: \$limit) {\n" +
        "        error\n" +
        "        data {\n" +
        "          id\n" +
        "          description\n" +
        "          commentCount\n" +
        "          showComment\n" +
        "          likeCount\n" +
        "          isLiked\n" +
        "          isFollow\n" +
        "          createTime\n" +
        "          userName\n" +
        "          userPhoto\n" +
        "          userInfo\n" +
        "          content {\n" +
        "            imageurl\n" +
        "            tags {\n" +
        "              id\n" +
        "              type\n" +
        "              url\n" +
        "              link\n" +
        "              price\n" +
        "              caption\n" +
        "            }\n" +
        "          }\n" +
        "        }\n" +
        "        lastCursor\n" +
        "    }\n" +
        "}"