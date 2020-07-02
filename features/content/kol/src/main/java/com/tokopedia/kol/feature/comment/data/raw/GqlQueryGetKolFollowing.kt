package com.tokopedia.kol.feature.comment.data.raw

const val GQL_QUERY_GET_KOL_FOLLOWING: String = """query GetKolFollowingList(${'$'}userID: Int!, ${'$'}cursor: String, ${'$'}limit: Int!) {
    get_user_kol_following(userID: ${'$'}userID, cursor: ${'$'}cursor, limit: ${'$'}limit) {
        __typename
        profileKol {
            __typename
            following
            followers
            followed
            iskol
            id
            info
            bio
            name
            photo
        }
        users {
            __typename
            id
            followed
            name
            info
            photo
            userUrl
            userApplink
            isInfluencer
        }
        error
        lastCursor
    }
}"""