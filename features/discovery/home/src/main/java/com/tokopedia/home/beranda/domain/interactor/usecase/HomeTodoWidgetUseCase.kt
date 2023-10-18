package com.tokopedia.home.beranda.domain.interactor.usecase

import android.os.Bundle
import com.tokopedia.home.beranda.domain.interactor.repository.HomeChooseAddressRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeTodoWidgetRepository
import com.tokopedia.home.beranda.helper.LazyLoadDataMapper
import com.tokopedia.home_component.usecase.todowidget.GetTodoWidgetUseCase
import com.tokopedia.home_component.visitable.TodoWidgetListDataModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils.convertToLocationParams
import javax.inject.Inject

/**
 * Created by frenzel
 */
class HomeTodoWidgetUseCase @Inject constructor(
    private val todoWidgetRepository: HomeTodoWidgetRepository,
    private val homeChooseAddressRepository: HomeChooseAddressRepository
) {

    suspend fun onTodoWidgetRefresh(currentTodoWidgetListDataModel: TodoWidgetListDataModel): TodoWidgetListDataModel {
        return try {
            val results = todoWidgetRepository.getRemoteData(
                Bundle().apply {
                    putString(
                        GetTodoWidgetUseCase.LOCATION_PARAM,
                        homeChooseAddressRepository.getRemoteData()?.convertToLocationParams()
                    )
                    putString(
                        GetTodoWidgetUseCase.PARAM,
                        currentTodoWidgetListDataModel.widgetParam
                    )
                }
            )

            val resultList = LazyLoadDataMapper.mapTodoWidgetData(results.getHomeTodoWidget.todos)

            currentTodoWidgetListDataModel.copy(
                todoWidgetList = resultList,
                status = TodoWidgetListDataModel.STATUS_SUCCESS
            )
        } catch (_: Exception) {
            currentTodoWidgetListDataModel.copy(status = TodoWidgetListDataModel.STATUS_ERROR)
        }
    }
}
