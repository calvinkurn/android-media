package com.tokopedia.profile.following_list.domain.interactor

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.profile.following_list.data.mapper.FollowerMapper
import com.tokopedia.profile.following_list.data.pojo.FollowerListData
import com.tokopedia.profile.following_list.view.viewmodel.UserFollowingResultViewModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * @author by yoasfs on 2019-08-22
 */
class GetFollowerListUseCase @Inject constructor(
        @ApplicationContext private val context: Context,
        private val useCase: GraphqlUseCase,
        private val mapData: FollowerMapper
): UseCase<UserFollowingResultViewModel>() {

    companion object {
        private const val PARAM_ID = "userID"
        private const val PARAM_CURSOR = "cursor"
        private const val PARAM_LIMIT = "limit"

        private const val DEFAULT_LIMIT = 10

        @JvmStatic
        fun getFollowerListParam(id: Int, cursor: String): RequestParams {
            val params = RequestParams.create()
            params.putInt(PARAM_ID, id)
            params.putString(PARAM_CURSOR, cursor)
            params.putInt(PARAM_LIMIT, DEFAULT_LIMIT)
            return params
        }
    }

    //region query
    private val query by lazy {
        val userID = "\$userID"
        val cursor = "\$cursor"
        val limit = "\$limit"

        """
            query feedGetUserFollowers($userID: Int!, $cursor: String, $limit: Int){
                feedGetUserFollowers(userID: $userID, cursor: $cursor, limit: $limit) {
                    meta {
                        nextCursor
                        currentTotal
                        currentCursor
                        limit
                    }
                    data {
                        id
                        name
                        photo
                        badges
                        applink
                        isFollow
                    }
                }
            }
        """.trimIndent()
    }
    //endregion

    override fun createObservable(requestParams: RequestParams): Observable<UserFollowingResultViewModel> {
        val request = GraphqlRequest(query,
                FollowerListData::class.java,
                requestParams.parameters)
        useCase.clearRequest()
        useCase.addRequest(request)
        return useCase.createObservable(RequestParams.EMPTY).map(mapData)
    }

}