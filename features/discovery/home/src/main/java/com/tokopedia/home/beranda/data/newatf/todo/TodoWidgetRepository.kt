package com.tokopedia.home.beranda.data.newatf.todo

import android.annotation.SuppressLint
import android.os.Bundle
import com.tokopedia.home.beranda.data.datasource.local.dao.AtfDao
import com.tokopedia.home.beranda.data.newatf.AtfData
import com.tokopedia.home.beranda.data.newatf.AtfMetadata
import com.tokopedia.home.beranda.data.newatf.AtfRepository
import com.tokopedia.home.beranda.di.HomeScope
import com.tokopedia.home.beranda.domain.interactor.repository.HomeChooseAddressRepository
import com.tokopedia.home.beranda.domain.interactor.repository.HomeTodoWidgetRepository
import com.tokopedia.home.util.QueryParamUtils.convertToLocationParams
import com.tokopedia.home_component.usecase.todowidget.GetTodoWidgetUseCase
import javax.inject.Inject

@HomeScope
class TodoWidgetRepository @Inject constructor(
    private val todoWidgetRepository: HomeTodoWidgetRepository,
    private val homeChooseAddressRepository: HomeChooseAddressRepository,
    atfDao: AtfDao,
): AtfRepository(atfDao) {

    @SuppressLint("PII Data Exposure")
    override suspend fun getData(atfMetadata: AtfMetadata) {
        val iconParam = Bundle().apply {
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
        val data = todoWidgetRepository.getRemoteData(iconParam)
        val atfData = AtfData(atfMetadata, data.getHomeTodoWidget, isCache = false)
        emitData(atfData)
    }
}
