package com.tokopedia.feedplus.detail.data

/**
 * Created by meyta.taliti on 07/09/23.
 */
interface FeedDetailRepository {

    suspend fun getTitle(source: String): String
}
