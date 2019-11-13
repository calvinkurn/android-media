package com.tokopedia.kol.feature.post.data.query

import com.tokopedia.feedcomponent.data.query.QueryProvider

/**
 * Created by jegul on 2019-11-05
 */
object LikeKolPostQueryProvider : QueryProvider {

    override fun getQuery(): String {

        val idPost = "\$idPost"
        val action = "\$action"

        return """
            mutation LikeKolPost($idPost: Int!, $action: Int!) {
                do_like_kol_post(idPost: $idPost, action: $action) {
                    error
                    data {
                        success
                    }
                }
            }
        """.trimIndent()
    }
}