package com.tokopedia.play.widget.di

import com.tokopedia.play.widget.ui.mapper.PlayWidgetMapper
import com.tokopedia.play.widget.ui.mapper.PlayWidgetMediumUiMapper
import com.tokopedia.play.widget.ui.mapper.PlayWidgetSmallUiMapper
import com.tokopedia.play.widget.ui.type.PlayWidgetSize
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by jegul on 08/10/20
 */
@Module
abstract class PlayWidgetMapperModule {

    @Binds
    @IntoMap
    @PlayWidgetSizeKey(PlayWidgetSize.Small)
    abstract fun getPlayWidgetSmallMapper(mapper: PlayWidgetSmallUiMapper): PlayWidgetMapper

    @Binds
    @IntoMap
    @PlayWidgetSizeKey(PlayWidgetSize.Medium)
    abstract fun getPlayWidgetMediumMapper(mapper: PlayWidgetMediumUiMapper): PlayWidgetMapper
}