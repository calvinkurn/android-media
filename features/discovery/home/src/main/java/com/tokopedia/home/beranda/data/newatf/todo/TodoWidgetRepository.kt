package com.tokopedia.home.beranda.data.newatf.todo

import android.annotation.SuppressLint
import android.os.Bundle
import com.tokopedia.home.beranda.data.datasource.local.dao.AtfDao
import com.tokopedia.home.beranda.data.newatf.AtfData
import com.tokopedia.home.beranda.data.newatf.AtfMetadata
import com.tokopedia.home.beranda.data.newatf.BaseAtfRepository
import com.tokopedia.home.beranda.di.HomeScope
import com.tokopedia.home.beranda.domain.interactor.repository.HomeChooseAddressRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeTodoWidgetRepository
import com.tokopedia.home.constant.AtfKey
import com.tokopedia.home.util.QueryParamUtils.convertToLocationParams
import com.tokopedia.home_component.usecase.todowidget.GetTodoWidgetUseCase
import com.tokopedia.home_component.usecase.todowidget.HomeTodoWidgetData
import javax.inject.Inject

/**
 * Created by Frenzel
 */
@HomeScope
class TodoWidgetRepository @Inject constructor(
    private val todoWidgetRepository: HomeTodoWidgetRepository,
    private val homeChooseAddressRepository: HomeChooseAddressRepository,
    atfDao: AtfDao,
): BaseAtfRepository() {

    @SuppressLint("PII Data Exposure")
    override suspend fun getData(atfMetadata: AtfMetadata) {
        val todoParam = Bundle().apply {
            putString(
                GetTodoWidgetUseCase.PARAM,
                atfMetadata.param
            )
            putString(
                GetTodoWidgetUseCase.LOCATION_PARAM,
                homeChooseAddressRepository.getRemoteData()
                    ?.convertToLocationParams()
            )
        }
        val (data, status) = try {
            todoWidgetRepository.getRemoteData(todoParam).getHomeTodoWidget to AtfKey.STATUS_SUCCESS
        } catch (_: Exception) {
            HomeTodoWidgetData.GetHomeTodoWidget() to AtfKey.STATUS_ERROR
        }
        val atfData = AtfData(
            atfMetadata = atfMetadata,
            atfContent = data,
            atfStatus = status,
            isCache = false,
        )
        emitData(atfData)
    }
}
