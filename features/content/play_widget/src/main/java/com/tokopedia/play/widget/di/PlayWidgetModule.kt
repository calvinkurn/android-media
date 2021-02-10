package com.tokopedia.play.widget.di

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.play.widget.domain.PlayWidgetUpdateChannelUseCase
import com.tokopedia.play_common.transformer.DefaultHtmlTextTransformer
import com.tokopedia.play_common.transformer.HtmlTextTransformer
import dagger.Module
import dagger.Provides

/**
 * Created by jegul on 09/11/20
 */
@Module(includes = [PlayWidgetMapperModule::class])
class PlayWidgetModule {

    @Provides
    fun providePlayWidgetUpdateChannelUseCase(graphqlRepository: GraphqlRepository): PlayWidgetUpdateChannelUseCase {
        return PlayWidgetUpdateChannelUseCase(graphqlRepository)
    }

    @Provides
    fun provideHtmlTextTransformer(): HtmlTextTransformer {
        return DefaultHtmlTextTransformer()
    }
}