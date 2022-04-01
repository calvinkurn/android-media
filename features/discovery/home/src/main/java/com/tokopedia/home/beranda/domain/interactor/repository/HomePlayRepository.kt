package com.tokopedia.home.beranda.domain.interactor.repository

import android.os.Bundle
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.home.beranda.domain.interactor.HomeRepository
import com.tokopedia.play.widget.domain.PlayWidgetUseCase
import com.tokopedia.play.widget.ui.PlayWidgetState
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel
import com.tokopedia.play.widget.util.PlayWidgetTools
import dagger.Lazy
import javax.inject.Inject

class HomePlayRepository @Inject constructor(
        private val playWidgetTools: Lazy<PlayWidgetTools>,
        private val homeDispatcher: Lazy<CoroutineDispatchers>
        ): HomeRepository<PlayWidgetState> {
    override suspend fun getRemoteData(bundle: Bundle): PlayWidgetState {
        val response = playWidgetTools.get().getWidgetFromNetwork(
                PlayWidgetUseCase.WidgetType.Home,
                homeDispatcher.get().io
        )
        return playWidgetTools.get().mapWidgetToModel(response)
    }

    override suspend fun getCachedData(bundle: Bundle): PlayWidgetState {
        return getRemoteData()
    }
}