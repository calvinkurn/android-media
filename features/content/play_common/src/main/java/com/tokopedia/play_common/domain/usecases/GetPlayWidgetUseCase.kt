package com.tokopedia.play_common.domain.usecases

import android.text.TextUtils
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.play_common.domain.mapper.PlayWidgetMapper
import com.tokopedia.play_common.domain.model.PlayGetWidgetEntity
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselDataModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase

class GetPlayWidgetUseCase(
        private val graphqlRepository: GraphqlRepository
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

    override suspend fun executeOnBackground(): PlayBannerCarouselDataModel {
        val cacheStrategy =
                GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()

        val request = GraphqlRequest(
                query,
                PlayGetWidgetEntity::class.java,
                params.parameters
        )

        val response = graphqlRepository.getReseponse(listOf(request), cacheStrategy)

        response.getError(PlayGetWidgetEntity::class.java)?.let {
            if (it.isNotEmpty()) {
                if (!TextUtils.isEmpty(it[0].message)) {
                    throw Exception(it[0].message)
                }
            }
        }
        val data = response.getData<PlayGetWidgetEntity>(PlayGetWidgetEntity::class.java)
        return PlayWidgetMapper.mapperToPlayBannerCarouselDataModel(data.playGetWidgetV2)
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