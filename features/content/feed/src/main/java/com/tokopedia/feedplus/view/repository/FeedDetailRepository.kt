package com.tokopedia.feedplus.view.repository

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.feedplus.data.pojo.FeedQuery
import com.tokopedia.feedplus.view.di.RawQueryKeyConstant
import com.tokopedia.usecase.RequestParams
import java.util.HashMap
import javax.inject.Inject
import javax.inject.Named

private const val PARAM_DETAIL_ID = "detailID"
private const val PARAM_PAGE = "pageDetail"
private const val PARAM_USER_ID = "userID"
private const val PARAM_LIMIT_DETAIL = "limitDetail"
private const val LIMIT_DETAIL = 30

class FeedDetailRepository @Inject constructor() {

    @Inject
    lateinit var baseRepository: BaseRepository

    @field:[Inject Named(RawQueryKeyConstant.GQL_QUERY_FEED_DETAIL)]
    lateinit var feedDetailQuery: String

    private fun getFeedDetailParam(detailId: String, page: Int, userId: Int): HashMap<String, Any> {
        val queryMap = HashMap<String, Any>()
        queryMap[PARAM_DETAIL_ID] = detailId
        queryMap[PARAM_PAGE] = page
        queryMap[PARAM_USER_ID] = userId
        queryMap[PARAM_LIMIT_DETAIL] = LIMIT_DETAIL
        return queryMap
    }

    suspend fun fetchFeedDetail(detailId: String, page: Int, userId: Int): FeedQuery {
        return baseRepository.getGQLData(feedDetailQuery, FeedQuery::class.java, getFeedDetailParam(detailId, page, userId))
    }

}