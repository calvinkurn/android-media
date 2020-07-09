package com.tokopedia.play_common.domain.usecases

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play_common.domain.mapper.PlayWidgetMapper
import com.tokopedia.play_common.domain.model.PlayGetWidgetEntity
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselDataModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase

class GetPlayWidgetUseCase(
        private val graphqlUseCase: GraphqlUseCase<PlayGetWidgetEntity>
): UseCase<PlayBannerCarouselDataModel>(){

    private val query = """
        query qPlayGetWidgetV2(
          ${'$'}widgetType: String, ${'$'}authorId:String, ${'$'}authorType:String
        ){
            playGetWidgetV2(
              req: {
                widgetType:     ${'$'}widgetType,
                authorID:     ${'$'}authorId, 
                authorType:     ${'$'}authorType
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
                  startTime
                  config{
                    hasPromo
                    isReminderSet
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
                maxAutoplayCell
              }
            } 
          }
        
    """
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