package com.tokopedia.product.edit.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.networklib.data.model.RestResponse
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.domain.GetYoutubeVideoDetailUseCase
import com.tokopedia.product.edit.domain.GetYoutubeVideoListDetailUseCase
import com.tokopedia.product.edit.listener.ProductAddVideoView
import com.tokopedia.product.edit.model.videorecommendation.VideoRecommendationData
import com.tokopedia.product.edit.model.videorecommendation.VideoRecommendationResult
import com.tokopedia.product.edit.model.youtube.YoutubeVideoModel
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import java.lang.reflect.Type
import java.util.ArrayList
import java.util.HashMap

class ProductAddVideoPresenter : BaseDaggerPresenter<ProductAddVideoView>() {

    private val graphqlUseCase: GraphqlUseCase = GraphqlUseCase()
    private val getYoutubeVideoListDetailUseCase: GetYoutubeVideoListDetailUseCase by lazy { GetYoutubeVideoListDetailUseCase(view.contextView) }

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
                    view.onErrorGetVideoData(e)
                }
            }

            override fun onNext(objects: GraphqlResponse) {
                if (isViewAttached) {
                    val data = objects.getData<VideoRecommendationResult>(VideoRecommendationResult::class.java)
                    val videoIDs : ArrayList<String> = ArrayList()
                    for(videoRecommendationData : VideoRecommendationData in data.videoSearch?.data!!){
                        videoIDs.add(videoRecommendationData.id!!)
                    }
                    getYoutubeVideoData(videoIDs, VIDEO_RECOMMENDATION_FEATURED)
                }
            }
        })
    }

    fun getYoutubaDataVideoChoosen(videoIDs: ArrayList<String>) {
        getYoutubeVideoData(videoIDs, VIDEO_CHOOSEN)
    }

    fun getYoutubeVideoData(youtubeVideoIdList: ArrayList<String>, from: Int) {
        getYoutubeVideoListDetailUseCase.execute(GetYoutubeVideoListDetailUseCase.generateRequestParam(youtubeVideoIdList), object : Subscriber<List<Map<Type, RestResponse>>>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                if (isViewAttached) {
                    view.onErrorGetVideoData(e)
                }
            }

            override fun onNext(maps: List<Map<Type, RestResponse>>) {
                if (isViewAttached) {
                    val youtubeVideoModelArrayList = ArrayList<YoutubeVideoModel>()
                    for (map in maps) {
                        youtubeVideoModelArrayList.add(convertToModel(map))
                    }
                    if(from == VIDEO_CHOOSEN)
                        view.onSuccessGetYoutubeDataVideoChoosen(youtubeVideoModelArrayList)
                    else
                        view.onSuccessGetYoutubeDataVideoRecommendationFeatured(youtubeVideoModelArrayList)
                }
            }
        })
    }

    private fun convertToModel(typeRestResponseMap: Map<Type, RestResponse>): YoutubeVideoModel {
        val restResponse = typeRestResponseMap[YoutubeVideoModel::class.java]
        return restResponse!!.getData()
    }


    companion object {
        const val QUERY = "query"
        const val SIZE = "size"

        const val VIDEO_CHOOSEN = 0
        const val VIDEO_RECOMMENDATION_FEATURED = 1
    }
}