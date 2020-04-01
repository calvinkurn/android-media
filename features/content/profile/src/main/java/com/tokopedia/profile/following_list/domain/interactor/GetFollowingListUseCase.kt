package com.tokopedia.profile.following_list.domain.interactor

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.profile.following_list.data.mapper.FollowingListMapper
import com.tokopedia.profile.following_list.data.pojo.GetKolFollowingData
import com.tokopedia.profile.following_list.domain.model.FollowingResultDomain
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase

import javax.inject.Inject

import rx.Observable

/**
 * Created by yfsx on 28/12/17.
 */
class GetFollowingListUseCase @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase,
        private val mapper: FollowingListMapper
) : UseCase<FollowingResultDomain>() {

    companion object {

        private const val PARAM_ID = "userID"
        private const val PARAM_LIMIT = "limit"

        private const val DEFAULT_LIMIT = 10

        @JvmStatic
        fun getParam(id: Int): RequestParams {
            val params = RequestParams.create()
            params.putInt(PARAM_ID, id)
            params.putInt(PARAM_LIMIT, DEFAULT_LIMIT)
            return params
        }
    }

    private val query by lazy {
        val userID = "\$userID"
        val cursor = "\$cursor"
        val limit = "\$limit"

        """
            query GetKolFollowingList($userID: Int!, $cursor: String, $limit: Int!) {
                get_user_kol_following(userID: $userID, cursor: $cursor, limit: $limit) {
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
            }
        """.trimIndent()
    }

    override fun createObservable(requestParams: RequestParams): Observable<FollowingResultDomain> {
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(
                GraphqlRequest(
                        query,
                        GetKolFollowingData::class.java,
                        requestParams.parameters
                )
        )
        return graphqlUseCase.createObservable(requestParams)
                .map(mapper)
    }
}

