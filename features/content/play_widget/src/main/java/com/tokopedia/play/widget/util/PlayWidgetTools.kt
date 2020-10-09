package com.tokopedia.play.widget.util

import com.tokopedia.play.widget.data.PlayWidget
import com.tokopedia.play.widget.domain.PlayWidgetUseCase
import com.tokopedia.play.widget.ui.mapper.PlayWidgetMapper
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel
import com.tokopedia.play.widget.ui.type.PlayWidgetSize
import dagger.Lazy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Provider
import kotlin.coroutines.CoroutineContext

/**
 * Created by jegul on 08/10/20
 */
class PlayWidgetTools @Inject constructor(
        private val useCase: PlayWidgetUseCase,
        private val mapperProviders: Map<PlayWidgetSize, @JvmSuppressWildcards PlayWidgetMapper>
) {

    suspend fun getWidgetFromNetwork(
            widgetType: PlayWidgetUseCase.WidgetType,
            coroutineContext: CoroutineContext = Dispatchers.IO): PlayWidget {
        return withContext(coroutineContext) {
            useCase.params = PlayWidgetUseCase.createParams(widgetType)
            useCase.executeOnBackground()
        }
    }

    suspend fun mapWidgetToModel(widgetResponse: PlayWidget, coroutineContext: CoroutineContext = Dispatchers.Default): PlayWidgetUiModel {
        return withContext(coroutineContext) {
            val mapper = mapperProviders[PlayWidgetSize.getByTypeString(widgetResponse.meta.template)] ?: throw IllegalStateException("Mapper cannot be null")
            mapper.mapWidget(widgetResponse)
        }
    }
}