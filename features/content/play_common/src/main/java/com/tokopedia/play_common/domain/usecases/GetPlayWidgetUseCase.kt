package com.tokopedia.play_common.domain.usecases

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play_common.domain.mapper.PlayWidgetMapper
import com.tokopedia.play_common.domain.model.PlayGetWidgetEntity
import com.tokopedia.play_common.widget.playBannerCarousel.model.*
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase

class GetPlayWidgetUseCase(
        private val graphqlUseCase: GraphqlUseCase<PlayGetWidgetEntity>
): UseCase<PlayBannerCarouselDataModel>(){

    private val widgetType = "\$widgetType"
    private val authorId = "\$authorId"
    private val authorType = "\$authorType"
    private val query = """
        query qPlayGetWidgetV2(
          $widgetType: String, $authorId:String, $authorType:String
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
                  widgetType
                  appLink
                  webLink
                  config{
                    hasPromo
                  }
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
                      visible
                    }
                  }
                }
                __typename ... on PlayWidgetBanner {
                  backgroundURL
                  appLink
                  webLink      
                }
              }
              meta {
                isAutoRefresh
                autoRefreshTimer
                widgetTitle
                buttonText
                widgetBackground
            	autoplayAmount
                autoplay
                buttonApplink
                buttonWeblink
                overlayImage
                overlayImageAppLink
                overlayImageWebLink
                gradient
                serverTimeOffset
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
        return PlayWidgetMapper.mapperToPlayBannerCarouselDataModel(graphqlUseCase.executeOnBackground().playGetWidgetV2)
        return PlayBannerCarouselDataModel(
                title = "Yuk, tonton sekarang!",
                seeMoreApplink = "https://cobacoba.com",
                backgroundUrl = "https://i.ibb.co/ZdMn09S/bg1.jpg",
                imageUrl = "https://i.ibb.co/Wk4YrQR/imageorang.png",
                isAutoPlay = true,
                gradients = listOf("#2A7C82", "#1C3A41"),
                isAutoRefreshTimer = 10000,
                isAutoPlayAmount = 2,
                isAutoRefresh = false,
                channelList = listOf(
                        PlayBannerCarouselOverlayImageDataModel(
                                applink = "",
                                imageUrl = "",
                                weblink = ""
                        ),
                        PlayBannerCarouselItemDataModel(
                                channelTitle = "Google Assistant review with me",
                                channelCreator = "Google",
                                applink = "https://cobacoba.com",
                                countView = "10rb",
                                isLive = true,
                                isShowTotalView = true,
                                widgetType = PlayBannerWidgetType.VOD,
                                isPromo = true,
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
                                widgetType = PlayBannerWidgetType.VOD,
                                isPromo = true,
                                coverUrl = "https://i.ibb.co/hgg0W75/Screen-Shot-2020-04-28-at-13-16-3.png",
                                videoUrl = "https://vod2.tokopedia.net/d83dd4e1602e47179378001f9ce07ed9/5e454ee1f28e49ab9e756501744df27c-4710b29f06e1c06e6b198e50594de075-fd.m3u8"
                        ),
                        PlayBannerCarouselItemDataModel(
                                channelTitle = "Google Assistant review with me",
                                channelCreator = "Google",
                                applink = "https://cobacoba.com",
                                countView = "10rb",
                                isLive = true,
                                isShowTotalView = true,
                                widgetType = PlayBannerWidgetType.VOD,
                                isPromo = false,
                                coverUrl = "https://i.ibb.co/hgg0W75/Screen-Shot-2020-04-28-at-13-16-3.png",
                                videoUrl = "https://vod2.tokopedia.net/d1be7fbc9e36499ab50cab2f7554f9f4/686f19c4a1434f9a9d1e52c055f27caa-11c234d89a25a6b24bb5b496f2ea02f4-fd.m3u8"
                        ),
                        PlayBannerCarouselItemDataModel(
                                channelTitle = "Google Assistant review with me",
                                channelCreator = "Google",
                                applink = "https://cobacoba.com",
                                countView = "10rb",
                                isLive = false,
                                isShowTotalView = true,
                                isPromo = false,
                                coverUrl = "https://i.ibb.co/hgg0W75/Screen-Shot-2020-04-28-at-13-16-3.png",
                                videoUrl = "https://vod2.tokopedia.net/6631531405a44f70aac24123784f8407/d09df6538e02440ab306a5d6399d316e-f5bde57f0c52d84b4bcc40caf18aee5f-fd.m3u8",
                                widgetType = PlayBannerWidgetType.UPCOMING
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