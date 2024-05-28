package com.tokopedia.feedplus.detail.data

import com.tokopedia.feedplus.browse.data.model.HeaderDetailModel

/**
 * Created by meyta.taliti on 07/09/23.
 */
interface FeedDetailRepository {

    suspend fun getHeader(source: String): HeaderDetailModel
}
