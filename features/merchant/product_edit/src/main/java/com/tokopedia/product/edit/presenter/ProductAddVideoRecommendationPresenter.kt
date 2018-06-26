package com.tokopedia.product.edit.presenter

import android.content.Context

import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.network.CacheUtil
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.listener.ProductAddVideoRecommendationView
import com.tokopedia.product.edit.model.VideoRecommendationData
import com.tokopedia.product.edit.model.VideoRecommendationResult
import com.tokopedia.usecase.RequestParams

import java.lang.reflect.Type
import java.util.HashMap

import rx.Subscriber

/**
 * Created by hendry on 25/06/18.
 */

class ProductAddVideoRecommendationPresenter : BaseDaggerPresenter<ProductAddVideoRecommendationView>() {

    private val graphqlUseCase: GraphqlUseCase = GraphqlUseCase()

    // just for test
    private// get json string which already cached
    val mockUpData: List<VideoRecommendationData>?
        get() {
            val type = object : TypeToken<VideoRecommendationResult>() {

            }.type
            val jsonCachedString = "{\"videosearch\":{\"data\":[{\"id\":\"4Y7bqswecUA\",\"title\":\"tas hp\"},{\"id\":\"1dO58V-KWmI\",\"title\":\"Tas Ransel Keren\"},{\"id\":\"zFDM0IvFd98\",\"title\":\"Tas Armany  grosir tas tajur blogspot com\"}],\"error\":null}}"
            val videoRecommendationResult = CacheUtil.convertStringToModel<VideoRecommendationResult>(jsonCachedString, type)
            return videoRecommendationResult!!.videoSearch!!.data
        }

    fun getVideoRecommendation(query: String, size: Int) {

        val variables = HashMap<String, Any>()
        variables[QUERY] = query
        variables[SIZE] = size

        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(view.contextView.resources,
                R.raw.gql_video_recommendation), VideoRecommendationResult::class.java, variables)

//        val graphqlCacheStrategy = GraphqlCacheStrategy.Builder(CacheType.CACHE_ONLY)
//                .setExpiryTime(GraphqlConstant.ExpiryTimes.HOUR.`val`())
//                .setSessionIncluded(true)
//                .build()
//        graphqlUseCase.setCacheStrategy(graphqlCacheStrategy)

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

    companion object {
        val QUERY = "query"
        val SIZE = "size"
    }
}
