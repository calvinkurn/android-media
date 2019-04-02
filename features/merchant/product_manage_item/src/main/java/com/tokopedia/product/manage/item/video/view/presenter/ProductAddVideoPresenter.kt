package com.tokopedia.product.manage.item.video.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.product.manage.item.R
import com.tokopedia.product.manage.item.video.domain.GetYoutubeVideoDetailUseCase
import com.tokopedia.product.manage.item.video.domain.GetYoutubeVideoListDetailUseCase
import com.tokopedia.product.manage.item.video.domain.model.videorecommendation.VideoRecommendationData
import com.tokopedia.product.manage.item.video.domain.model.videorecommendation.VideoRecommendationResult
import com.tokopedia.product.manage.item.video.domain.model.youtube.YoutubeVideoModel
import com.tokopedia.product.manage.item.video.view.listener.ProductAddVideoView
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import java.lang.reflect.Type

class ProductAddVideoPresenter : BaseDaggerPresenter<ProductAddVideoView>() {

    private val graphqlUseCase: GraphqlUseCase = GraphqlUseCase()
    private val getYoutubeVideoDetailUseCase: GetYoutubeVideoDetailUseCase by lazy { GetYoutubeVideoDetailUseCase(view.contextView) }
    private val getYoutubeVideoListDetailUseCase: GetYoutubeVideoListDetailUseCase by lazy { GetYoutubeVideoListDetailUseCase(view.contextView) }

    fun getVideoRecommendation(query: String, size: Int) {
        val variables = HashMap<String, Any>()
        variables[QUERY] = query
        variables[SIZE] = size

        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(view.contextView.resources,
                R.raw.gql_video_recommendation), VideoRecommendationResult::class.java, variables, false)

        val graphqlCacheStrategy = GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
                .setExpiryTime(GraphqlConstant.ExpiryTimes.HOUR.`val`())
                .setSessionIncluded(true)
                .build()
        graphqlUseCase.setCacheStrategy(graphqlCacheStrategy)

        graphqlUseCase.addRequest(graphqlRequest)

        graphqlUseCase.execute(RequestParams.EMPTY, object : Subscriber<GraphqlResponse>() {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                if (isViewAttached) {
                    view.onEmptyGetVideoRecommendation()
                    view.onErrorGetVideoData(e)
                }
            }

            override fun onNext(objects: GraphqlResponse) {
                if (isViewAttached) {
                    val data = objects.getData<VideoRecommendationResult>(VideoRecommendationResult::class.java)
                    val videoIDs : ArrayList<String> = ArrayList()
                    if(data.videoSearch?.data!=null){
                        for(videoRecommendationData : VideoRecommendationData in data.videoSearch?.data!!){
                            videoIDs.add(videoRecommendationData.id!!)
                        }
                    } else {
                        view.onEmptyGetVideoRecommendation()
                    }
                    getYoutubeVideoData(videoIDs, VIDEO_RECOMMENDATION_FEATURED)
                }
            }
        })
    }

    override fun detachView() {
        graphqlUseCase.unsubscribe()
        getYoutubeVideoDetailUseCase.unsubscribe()
        getYoutubeVideoListDetailUseCase.unsubscribe()
        super.detachView()
    }

    fun getYoutubeDataVideoChosen(videoIDs: ArrayList<String>) {
        getYoutubeVideoData(videoIDs, VIDEO_CHOSEN)
    }

    fun getYoutubaDataVideoUrl(videoID: String) {
        getYoutubeVideoData(videoID)
    }

    fun getYoutubeVideoData(youtubeVideoIdList: ArrayList<String>, from: Int) {
        getYoutubeVideoListDetailUseCase.execute(GetYoutubeVideoListDetailUseCase.generateRequestParam(youtubeVideoIdList),
                object : Subscriber<List<Map<Type, RestResponse>>>() {
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
                    if(from == VIDEO_CHOSEN) {
                        view.onSuccessGetYoutubeDataVideoChosen(youtubeVideoModelArrayList)
                    }
                    else
                        view.onSuccessGetYoutubeDataVideoRecommendation(youtubeVideoModelArrayList)
                }
            }
        })

    }

    private fun getYoutubeVideoData(youtubeVideoId: String) {
        getYoutubeVideoDetailUseCase.execute(GetYoutubeVideoDetailUseCase.generateRequestParam(youtubeVideoId),
                object : Subscriber<Map<Type, RestResponse>>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                if (isViewAttached) {
                    view.onErrorGetVideoData(e)
                }
            }

            override fun onNext(map: Map<Type, RestResponse>) {
                if (isViewAttached) {
                    view.onSuccessGetYoutubeDataVideoUrl(convertToModel(map))
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

        const val VIDEO_CHOSEN = 0
        const val VIDEO_RECOMMENDATION_FEATURED = 1
    }
}