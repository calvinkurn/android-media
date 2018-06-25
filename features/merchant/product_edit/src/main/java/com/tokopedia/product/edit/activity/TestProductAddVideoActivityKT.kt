package com.tokopedia.product.edit.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log

import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.data.model.response.DataResponse
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.network.CacheUtil
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.model.VideoRecommendationData
import com.tokopedia.product.edit.model.VideoRecommendationResult

import java.lang.reflect.Type
import java.util.HashMap

import rx.Subscriber

/**
 * Created by hendry on 25/06/18.
 */

class TestProductAddVideoActivityKT : BaseSimpleActivity() {


    private// get json string which already cached
    val mockUpData: List<VideoRecommendationData>
        get() {
            val type = object : TypeToken<VideoRecommendationResult>() {

            }.type
            val jsonCachedString = "{\"videosearch\":{\"data\":[{\"id\":\"4Y7bqswecUA\",\"title\":\"tas hp\"},{\"id\":\"1dO58V-KWmI\",\"title\":\"Tas Ransel Keren\"},{\"id\":\"zFDM0IvFd98\",\"title\":\"Tas Armany  grosir tas tajur blogspot com\"}],\"error\":null}}"
            val videoRecommendationResult = CacheUtil.convertStringToModel<VideoRecommendationResult>(jsonCachedString, type)
            return videoRecommendationResult!!.videoSearch.data
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        GraphqlClient.init(applicationContext)
        getData()
    }

    private fun getData() {
        val graphqlUseCase = GraphqlUseCase()

        val variables = HashMap<String, Any>()
        variables["query"] = "tas"
        variables["size"] = 3

        val type = object : TypeToken<DataResponse<VideoRecommendationResult>>() {

        }.type
        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(resources,
                R.raw.gql_video_recommendation), type, variables)

        val graphqlCacheStrategy = GraphqlCacheStrategy.Builder(CacheType.CACHE_ONLY)
                .setExpiryTime(GraphqlConstant.ExpiryTimes.HOUR.`val`())
                .setSessionIncluded(true)
                .build()
        graphqlUseCase.setCacheStrategy(graphqlCacheStrategy)

        graphqlUseCase.setRequest(graphqlRequest)

        graphqlUseCase.execute(object : Subscriber<GraphqlResponse>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                // TODO just for test, remove below
                onSuccessGetVideoRecommendation(mockUpData)
            }

            override fun onNext(objects: GraphqlResponse) {
                //DataResponse<VideoRecommendationResult> data = objects.getData(DataResponse<VideoRecommendationResult>.class);
                // TODO just for test, change mockup to original
                onSuccessGetVideoRecommendation(mockUpData)
            }
        })
    }

    private fun onSuccessGetVideoRecommendation(videoRecommendationDataList: List<VideoRecommendationData>) {
        Log.i("Result", videoRecommendationDataList.toString())
        //TODO update UI
    }

    override fun getNewFragment(): Fragment? {
        return null
    }

}
