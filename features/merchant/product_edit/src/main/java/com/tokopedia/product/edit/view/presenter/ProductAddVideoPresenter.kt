package com.tokopedia.product.edit.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.domain.interactor.GetYoutubeVideoDetailUseCase
import com.tokopedia.product.edit.domain.interactor.GetYoutubeVideoListDetailUseCase
import com.tokopedia.product.edit.view.listener.ProductAddVideoView
import com.tokopedia.product.edit.domain.mapper.VideoMapper
import com.tokopedia.product.edit.domain.model.videorecommendation.VideoRecommendationData
import com.tokopedia.product.edit.domain.model.videorecommendation.VideoRecommendationResult
import com.tokopedia.product.edit.domain.model.youtube.YoutubeVideoModel
import com.tokopedia.product.edit.view.viewmodel.EmptyVideoViewModel
import com.tokopedia.product.edit.view.viewmodel.ProductAddVideoBaseViewModel
import com.tokopedia.product.edit.view.viewmodel.SectionVideoRecommendationViewModel
import com.tokopedia.product.edit.view.viewmodel.TitleVideoChosenViewModel
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import java.lang.reflect.Type
import java.util.ArrayList
import java.util.HashMap

class ProductAddVideoPresenter : BaseDaggerPresenter<ProductAddVideoView>() {

    private val graphqlUseCase: GraphqlUseCase = GraphqlUseCase()
    private val getYoutubeVideoDetailUseCase: GetYoutubeVideoDetailUseCase by lazy { GetYoutubeVideoDetailUseCase(view.contextView) }
    private val getYoutubeVideoListDetailUseCase: GetYoutubeVideoListDetailUseCase by lazy { GetYoutubeVideoListDetailUseCase(view.contextView) }
    private val mapper = VideoMapper()
    private lateinit var productName: String

    fun setProductName(productName: String) {
        this.productName = productName
    }

    fun getVideoRecommendation(query: String, size: Int) {

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
                        val productAddVideoBaseViewModels : ArrayList<ProductAddVideoBaseViewModel> = ArrayList()
                        if(!youtubeVideoModelArrayList.isEmpty()){
                            productAddVideoBaseViewModels.addAll(mapper.transformDataToVideoViewModel(youtubeVideoModelArrayList))
                            val titleVideoChosenViewModel = TitleVideoChosenViewModel()
                            productAddVideoBaseViewModels.add(0, titleVideoChosenViewModel)
                        } else {
                            val emptyVideoViewModel = EmptyVideoViewModel()
                            productAddVideoBaseViewModels.add(0, emptyVideoViewModel)
                        }
                        if(!productName.isEmpty()){
                            val sectionVideoRecommendationViewModel = SectionVideoRecommendationViewModel()
                            productAddVideoBaseViewModels.add(0, sectionVideoRecommendationViewModel)
                        }
                        view.renderListData(productAddVideoBaseViewModels)
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