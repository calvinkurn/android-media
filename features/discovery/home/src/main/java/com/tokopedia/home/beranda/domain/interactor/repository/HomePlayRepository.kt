package com.tokopedia.home.beranda.domain.interactor.repository

import android.os.Bundle
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.home.beranda.domain.interactor.HomeRepository
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.helper.HomePlayWidgetHelper
import com.tokopedia.play.widget.domain.PlayWidgetUseCase
import com.tokopedia.play.widget.ui.PlayWidgetState
import com.tokopedia.play.widget.ui.mapper.PlayWidgetUiMapper
import com.tokopedia.play.widget.util.PlayWidgetTools
import dagger.Lazy
import javax.inject.Inject

class HomePlayRepository @Inject constructor(
    private val playWidgetTools: Lazy<PlayWidgetTools>,
    private val homeDispatcher: Lazy<CoroutineDispatchers>,
    private val helper: HomePlayWidgetHelper,
) : HomeRepository<PlayWidgetState> {
    override suspend fun getRemoteData(bundle: Bundle): PlayWidgetState {
        val layout = bundle.getString(KEY_WIDGET_LAYOUT) ?: DynamicHomeChannel.Channels.LAYOUT_PLAY_CAROUSEL_BANNER
        val type = if (helper.isCarousel(layout)) {
            PlayWidgetUseCase.WidgetType.HomeV2
        } else {
            PlayWidgetUseCase.WidgetType.Home
        }

        val response = playWidgetTools.get().getWidgetFromNetwork(type, homeDispatcher.get().io)
        return playWidgetTools.get().mapWidgetToModel(
            response,
            extraInfo = PlayWidgetUiMapper.ExtraInfo(
                showProduct = helper.isCarouselVariantWithProduct(layout),
            )
        )
    }

    override suspend fun getCachedData(bundle: Bundle): PlayWidgetState {
        return getRemoteData()
    }

    companion object {
        internal const val KEY_WIDGET_LAYOUT = "widget_layout"
    }
}
