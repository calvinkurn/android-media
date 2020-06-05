package com.tokopedia.play_common.domain.usecases

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play_common.domain.model.PlayGetWidgetEntity
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselBannerDataModel
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselDataModel
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselEmptyDataModel
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselItemDataModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase

class GetPlayWidgetUseCase(
        private val graphqlUseCase: GraphqlUseCase<PlayGetWidgetEntity>
): UseCase<PlayBannerCarouselDataModel>(){

    private val widgetType = "\$widgetType"
    private val authorId = "\$authorId"
    private val authorType = "\$authorType"
    private val query = """
        {
          query playGetWidgetV2(
            widgetType: String, authodId:String, authorType:String
          ){
            playGetWidgetV2(
              req: {
                widgetType:     $widgetType    ,
                authorID:     $authorId    , 
                authorType:     $authorType
              }
            )
            {  
              data {
                __typename... on PlayWidgetChannel{
                  ID
                  title
                  startTime
                  endTime
                  widgetType
                  appLink
                  webLink
                  partner {
                    ID
                    type
                    name
                  }
                  video {
                    ID
                    orientation
                    type
                    coverUrl
                    streamSource
                    isAutoplay
                    isShowTotalView
                    isLive
                    bufferControl {
                      maxBufferInSecond
                      minBufferInSecond
                      bufferForPlayback
                      bufferForPlaybackAfterRebuffer
                    }      
                  }
                  stats {
                    view {
                      value
                      formatted
                    }
                  }
                }
                __typename ... on PlayWidgetBanner {
                  backgroundURL
                  title
                  buttonColor
                  buttonText
                  buttonAppLink
                  buttonWebLink
                }
              }
              meta {
                isAutoRefresh
                autoRefreshTimer
                widgetTitle
                buttonText
                widgetBackground
              }
            } 
          }
        }
    """.trimIndent()
    private val params = RequestParams.create()

    init {
        graphqlUseCase.setGraphqlQuery(query)
        graphqlUseCase.setTypeClass(PlayGetWidgetEntity::class.java)
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        params.parameters.clear()
    }

    override suspend fun executeOnBackground(): PlayBannerCarouselDataModel {
        graphqlUseCase.setRequestParams(params.parameters)
//        return PlayWidgetMapper.mapperToPlayBannerCarouselDataModel(graphqlUseCase.executeOnBackground().playGetWidgetV2)
        return PlayBannerCarouselDataModel(
                title = "Yuk, tonton sekarang!",
                seeMoreApplink = "https://cobacoba.com",
                backgroundUrl = "https://i.ibb.co/ZdMn09S/bg1.jpg",
                imageUrl = "https://i.ibb.co/Wk4YrQR/imageorang.png",
                isAutoPlay = true,
                channelList = listOf(
                        PlayBannerCarouselEmptyDataModel(),
                        PlayBannerCarouselItemDataModel(
                                channelTitle = "Google Assistant review with me",
                                channelCreator = "Google",
                                applink = "https://cobacoba.com",
                                countView = "10rb",
                                isLive = true,
                                isShowTotalView = true,
                                promoUrl = "",
                                coverUrl = "https://i.ibb.co/hgg0W75/Screen-Shot-2020-04-28-at-13-16-3.png",
                                videoUrl = "https://vod2.tokopedia.net/2c9d97786944476ca6f851b43657a714/71e3acc038da4fd4887005125c6af51d-42ab79fb4dc46a6c087229f0737690a6-fd.m3u8"
                        ),
                        PlayBannerCarouselItemDataModel(
                                channelTitle = "Google Assistant review with me",
                                channelCreator = "Google",
                                applink = "https://cobacoba.com",
                                countView = "10rb",
                                isLive = true,
                                isShowTotalView = true,
                                promoUrl = "",
                                coverUrl = "https://i.ibb.co/hgg0W75/Screen-Shot-2020-04-28-at-13-16-3.png",
                                videoUrl = "https://vod2.tokopedia.net/2c9d97786944476ca6f851b43657a714/71e3acc038da4fd4887005125c6af51d-42ab79fb4dc46a6c087229f0737690a6-fd.m3u8"
                        ),
                        PlayBannerCarouselItemDataModel(
                                channelTitle = "Google Assistant review with me",
                                channelCreator = "Google",
                                applink = "https://cobacoba.com",
                                countView = "10rb",
                                isLive = true,
                                isShowTotalView = true,
                                promoUrl = "",
                                coverUrl = "https://i.ibb.co/hgg0W75/Screen-Shot-2020-04-28-at-13-16-3.png",
                                videoUrl = "https://vod2.tokopedia.net/2c9d97786944476ca6f851b43657a714/71e3acc038da4fd4887005125c6af51d-42ab79fb4dc46a6c087229f0737690a6-fd.m3u8"
                        ),
                        PlayBannerCarouselItemDataModel(
                                channelTitle = "Google Assistant review with me",
                                channelCreator = "Google",
                                applink = "https://cobacoba.com",
                                countView = "10rb",
                                isLive = true,
                                isShowTotalView = true,
                                promoUrl = "",
                                coverUrl = "https://i.ibb.co/hgg0W75/Screen-Shot-2020-04-28-at-13-16-3.png",
                                videoUrl = "https://vod2.tokopedia.net/2c9d97786944476ca6f851b43657a714/71e3acc038da4fd4887005125c6af51d-42ab79fb4dc46a6c087229f0737690a6-fd.m3u8"
                        ),
                        PlayBannerCarouselBannerDataModel(
                                applink = "",
                                imageUrl = "https://i.ibb.co/Fm5Hwk4/item.png"
                        )
                )
        )
    }

    fun setParams(widgetType: String, authorId: String, authorType: String){
        params.parameters.clear()
        params.putString(PARAM_WIDGET_TYPE, widgetType)
        params.putString(PARAM_AUTHOR_ID, authorId)
        params.putString(PARAM_AUTHOR_TYPE, authorType)
    }

    companion object{
        private const val PARAM_WIDGET_TYPE = "widgetType"
        private const val PARAM_AUTHOR_ID = "authorId"
        private const val PARAM_AUTHOR_TYPE = "authorType"
        const val HOME_WIDGET_TYPE = "HOME"
        const val SHOP_WIDGET_TYPE = "SHOP_PAGE"
        const val HOME_AUTHOR_TYPE = ""
        const val SHOP_AUTHOR_TYPE = "shop"

    }
}