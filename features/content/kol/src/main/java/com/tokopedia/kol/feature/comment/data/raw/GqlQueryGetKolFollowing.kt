package com.tokopedia.kol.feature.comment.data.raw

const val GQL_QUERY_GET_KOL_FOLLOWING: String = "query GetKolFollowingList(\$userID: Int!, \$cursor: String, \$limit: Int!) {\n" +
        "    get_user_kol_following(userID: \$userID, cursor: \$cursor, limit: \$limit) {\n" +
        "        __typename\n" +
        "        profileKol {\n" +
        "            __typename\n" +
        "            following\n" +
        "            followers\n" +
        "            followed\n" +
        "            iskol\n" +
        "            id\n" +
        "            info\n" +
        "            bio\n" +
        "            name\n" +
        "            photo\n" +
        "        }\n" +
        "        users {\n" +
        "            __typename\n" +
        "            id\n" +
        "            followed\n" +
        "            name\n" +
        "            info\n" +
        "            photo\n" +
        "            userUrl\n" +
        "            userApplink\n" +
        "            isInfluencer\n" +
        "        }\n" +
        "        error\n" +
        "        lastCursor\n" +
        "    }\n" +
        "}"