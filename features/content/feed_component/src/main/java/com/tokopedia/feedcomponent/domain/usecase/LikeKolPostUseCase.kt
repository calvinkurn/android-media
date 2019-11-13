package com.tokopedia.feedcomponent.domain.usecase

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.feedcomponent.data.pojo.like.LikeKolPostData
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase

import javax.inject.Inject

import rx.Observable
import javax.inject.Named

/**
 * @author by nisie on 11/3/17.
 */

class LikeKolPostUseCase @Inject constructor(
        @Named(MUTATION_LIKE_KOL_POST) private val query: String,
        private val graphqlUseCase: GraphqlUseCase
) : UseCase<Boolean>() {

    enum class LikeKolPostAction(val actionValue: Int) {
        Unlike(0),
        Like(1)
    }

    companion object {

        const val MUTATION_LIKE_KOL_POST = "mutation_like_kol_post"

        private const val PARAM_ID_POST = "idPost"
        private const val PARAM_ACTION = "action"

        private const val LIKE_SUCCESS = 1

        fun getParam(postId: Int, action: LikeKolPostAction): RequestParams {
            return RequestParams.create().apply {
                putInt(PARAM_ID_POST, postId)
                putInt(PARAM_ACTION, action.actionValue)
            }
        }
    }

    override fun createObservable(requestParams: RequestParams): Observable<Boolean> {
        graphqlUseCase.clearRequest()
        val request = GraphqlRequest(query, LikeKolPostData::class.java, requestParams.parameters)
        graphqlUseCase.addRequest(request)
        return graphqlUseCase.createObservable(RequestParams.EMPTY).map {
            val likeKolPostData = it.getData<LikeKolPostData>(LikeKolPostData::class.java)
            return@map likeKolPostData.doLikeKolPost.data.success == LIKE_SUCCESS
        }
    }
}
