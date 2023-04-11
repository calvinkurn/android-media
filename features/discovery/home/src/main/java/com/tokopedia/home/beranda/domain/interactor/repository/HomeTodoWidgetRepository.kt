package com.tokopedia.home.beranda.domain.interactor.repository

import android.os.Bundle
import com.tokopedia.home.beranda.domain.interactor.HomeRepository
import com.tokopedia.home_component.usecase.todowidget.GetTodoWidgetUseCase
import com.tokopedia.home_component.usecase.todowidget.HomeTodoWidgetData
import dagger.Lazy
import javax.inject.Inject

/**
 * Created by frenzel
 */
class HomeTodoWidgetRepository @Inject constructor(
    private val getTodoWidgetUseCase: Lazy<GetTodoWidgetUseCase>
) :
    HomeRepository<HomeTodoWidgetData.HomeTodoWidget> {

    override suspend fun getRemoteData(bundle: Bundle): HomeTodoWidgetData.HomeTodoWidget {
        getTodoWidgetUseCase.get().generateParam(bundle)
        return getTodoWidgetUseCase.get().executeOnBackground()
    }

    override suspend fun getCachedData(bundle: Bundle): HomeTodoWidgetData.HomeTodoWidget {
        return HomeTodoWidgetData.HomeTodoWidget()
    }
}
