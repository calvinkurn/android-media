package com.tokopedia.product.edit.presenter

import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.network.CacheUtil
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.listener.ProductAddVideoView
import com.tokopedia.product.edit.model.VideoRecommendationResult
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import java.util.HashMap

class ProductAddVideoPresenter : BaseDaggerPresenter<ProductAddVideoView>() {

    private val graphqlUseCase: GraphqlUseCase = GraphqlUseCase()

    fun getVideoRecommendationFeatured(query: String, size: Int) {

        val variables = HashMap<String, Any>()
        variables[QUERY] = query
        variables[SIZE] = size

        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(view.contextView.resources,
                R.raw.gql_video_recommendation), VideoRecommendationResult::class.java, variables)

        graphqlUseCase.setRequest(graphqlRequest)

        graphqlUseCase.execute(RequestParams.EMPTY, object : Subscriber<GraphqlResponse>() {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                if (isViewAttached) {
                    view.onErrorGetVideoRecommendation(e)
                }
            }

            override fun onNext(objects: GraphqlResponse) {
                if (isViewAttached) {
                    val data = objects.getData<VideoRecommendationResult>(VideoRecommendationResult::class.java)
                    view.onSuccessGetVideoRecommendation(data.videoSearch?.data!!)
                }
            }
        })
    }

    override fun detachView() {
        super.detachView()
    }

    companion object {
        val QUERY = "query"
        val SIZE = "size"
    }
}