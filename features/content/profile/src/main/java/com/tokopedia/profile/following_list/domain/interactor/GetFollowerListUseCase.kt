package com.tokopedia.profile.following_list.domain.interactor

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.profile.R
import com.tokopedia.profile.following_list.data.mapper.KolFollowerMapper
import com.tokopedia.profile.following_list.data.pojo.FollowerListData
import com.tokopedia.profile.following_list.view.viewmodel.KolFollowingResultViewModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable

/**
 * @author by yoasfs on 2019-08-22
 */
class GetFollowerListUseCase constructor(@ApplicationContext private val context: Context,
                                         private val useCase: GraphqlUseCase,
                                         private val mapData: KolFollowerMapper
): UseCase<KolFollowingResultViewModel>() {

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

    override fun createObservable(requestParams: RequestParams): Observable<KolFollowingResultViewModel> {
        val request = GraphqlRequest(query,
                FollowerListData::class.java,
                requestParams.parameters)
        useCase.clearRequest()
        useCase.addRequest(request)
        return useCase.createObservable(RequestParams.EMPTY).map(mapData)
    }

}