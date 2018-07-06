package com.tokopedia.product.edit.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.domain.interactor.GetYoutubeVideoListDetailUseCase
import com.tokopedia.product.edit.view.listener.ProductAddVideoRecommendationView
import com.tokopedia.product.edit.domain.model.videorecommendation.VideoRecommendationData
import com.tokopedia.product.edit.domain.model.videorecommendation.VideoRecommendationResult
import com.tokopedia.product.edit.domain.model.youtube.YoutubeVideoModel
import com.tokopedia.usecase.RequestParams

import java.lang.reflect.Type
import java.util.HashMap

import rx.Subscriber
import java.util.ArrayList

/**
 * Created by hendry on 25/06/18.
 */

class ProductAddVideoRecommendationPresenter : BaseDaggerPresenter<ProductAddVideoRecommendationView>() {

    private val graphqlUseCase: GraphqlUseCase = GraphqlUseCase()
    private val getYoutubeVideoListDetailUseCase: GetYoutubeVideoListDetailUseCase by lazy { GetYoutubeVideoListDetailUseCase(view.contextView) }

    fun getYoutubeDataVideoRecommendation(query: String, size: Int) {

        val variables = HashMap<String, Any>()
        variables[QUERY] = query
        variables[SIZE] = size

        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(view.contextView.resources,
                R.raw.gql_video_recommendation), VideoRecommendationResult::class.java, variables)

        graphqlUseCase.addRequest(graphqlRequest)

        graphqlUseCase.execute(RequestParams.EMPTY, object : Subscriber<GraphqlResponse>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                if (isViewAttached) {
//                    view.onErrorGetVideoRecommendation(e)
                }
            }

            override fun onNext(objects: GraphqlResponse) {
                if (isViewAttached) {
                    val data = objects.getData<VideoRecommendationResult>(VideoRecommendationResult::class.java)
                    val videoIDs : ArrayList<String> = ArrayList()
                    for(videoRecommendationData : VideoRecommendationData in data.videoSearch?.data!!){
                        videoIDs.add(videoRecommendationData.id!!)
                    }
                    getYoutubeVideoData(videoIDs)
                }
            }
        })
    }

    fun getYoutubeVideoData(youtubeVideoIdList: ArrayList<String>) {
        getYoutubeVideoListDetailUseCase.execute(GetYoutubeVideoListDetailUseCase.generateRequestParam(youtubeVideoIdList),
                object : Subscriber<List<Map<Type, RestResponse>>>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                if (isViewAttached) {
//                    view.onErrorGetVideoRecommendation(e)
                }
            }

            override fun onNext(maps: List<Map<Type, RestResponse>>) {
                if (isViewAttached) {
                    val youtubeVideoModelArrayList = ArrayList<YoutubeVideoModel>()
                    for (map in maps) {
                        youtubeVideoModelArrayList.add(convertToModel(map))
                    }
//                    view.onSuccessGetYoutubeDataVideoRecommendation(youtubeVideoModelArrayList)
                }
            }
        })
    }

    private fun convertToModel(typeRestResponseMap: Map<Type, RestResponse>): YoutubeVideoModel {
        val restResponse = typeRestResponseMap[YoutubeVideoModel::class.java]
        return restResponse!!.getData()
    }

    companion object {
        val QUERY = "query"
        val SIZE = "size"
    }
}
