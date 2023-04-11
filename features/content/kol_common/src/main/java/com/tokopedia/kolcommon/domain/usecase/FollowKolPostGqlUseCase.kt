package com.tokopedia.kolcommon.domain.usecase

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.kolcommon.data.pojo.follow.FollowKolQuery
import com.tokopedia.usecase.RequestParams

import javax.inject.Inject

/**
 * @author by yfsx on 11/7/18.
 */
@Deprecated("Use ProfileFollowUseCase and ProfileUnfollowedUseCase class instead")
class FollowKolPostGqlUseCase @Inject constructor() : GraphqlUseCase() {

    companion object {

        private val PARAM_USER_ID = "userID"
        private const val PARAM_ACTION = "action"
        const val PARAM_FOLLOW = 1
        const val PARAM_UNFOLLOW = 0
        const val SUCCESS_STATUS = 1

        @JvmStatic
        fun getParam(userId: Int, status: Int): RequestParams {
            val params = RequestParams.create()
            params.putInt(PARAM_USER_ID, userId)
            params.putInt(PARAM_ACTION, status)
            return params
        }
    }

    //region query
    private val query by lazy {
        val userID = "\$userID"
        val action = "\$action"

        """
            mutation FollowKol($userID: Int!, $action: Int!) {
                do_follow_kol(userID: $userID, action: $action) {
                    __typename
                    error
                    data {
                        __typename
                        status
                    }
                }
            }
        """.trimIndent()
    }
    //endregion

    fun getRequest(userId: Int, status: Int): GraphqlRequest  {
        return GraphqlRequest(query, FollowKolQuery::class.java,
                getParam(userId, status).parameters, false)
    }
}
