package com.tokopedia.kolcommon.domain.usecase

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * @author by nisie on 11/3/17.
 */
@Deprecated("please use com.tokopedia.kolcommon.domain.interactor.SubmitLikeContentUseCase")
class LikeKolPostUseCase @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase
) : UseCase<Boolean>() {

    companion object {

        private const val PARAM_ID_POST = "idPost"
        private const val PARAM_ACTION = "action"

        private const val LIKE_SUCCESS = 1

        fun getParam(postId: Long, action: LikeKolPostAction): RequestParams {
            return RequestParams.create().apply {
                putLong(PARAM_ID_POST, postId)
                putInt(PARAM_ACTION, action.actionValue)
            }
        }
    }

    //region query
    private val query by lazy {
        val idPost = "\$idPost"
        val action = "\$action"

        """
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
    //endregion

    override fun createObservable(requestParams: RequestParams): Observable<Boolean> {
        graphqlUseCase.clearRequest()
        val request = GraphqlRequest(query, com.tokopedia.kolcommon.data.pojo.like.LikeKolPostData::class.java, requestParams.parameters)
        graphqlUseCase.addRequest(request)
        return graphqlUseCase.createObservable(RequestParams.EMPTY).map {
            val likeKolPostData = it.getData<com.tokopedia.kolcommon.data.pojo.like.LikeKolPostData>(com.tokopedia.kolcommon.data.pojo.like.LikeKolPostData::class.java)
            return@map likeKolPostData.doLikeKolPost.data.success == LIKE_SUCCESS
        }
    }

    enum class LikeKolPostAction(val actionValue: Int) {
        Unlike(0),
        Like(1)
    }
}
