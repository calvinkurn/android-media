package com.tokopedia.homenav.mainnav.view.presenter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.config.GlobalConfig
import com.tokopedia.homenav.base.diffutil.HomeNavVisitable
import com.tokopedia.homenav.common.dispatcher.NavDispatcherProvider
import com.tokopedia.homenav.common.util.ResultCommandProcessor
import com.tokopedia.homenav.mainnav.view.viewmodel.MainNavigationDataModel
import dagger.Lazy
import kotlinx.coroutines.withContext
import timber.log.Timber

class MainNavLiveDataController (
        private val _mainNavLiveData: MutableLiveData<MainNavigationDataModel>,
        private val baseDispatcher: Lazy<NavDispatcherProvider>
): ResultCommandProcessor {
    override suspend fun updateWidget(visitable: HomeNavVisitable, position: Int) {
        val newMainLiveData = _mainNavLiveData.value?.dataList?.toMutableList() ?: mutableListOf()
        if(newMainLiveData.getOrNull(position)?.id() == visitable.id()){
            newMainLiveData[position] = visitable
        } else {
            newMainLiveData.indexOfFirst { it.id() == visitable.id() }.let { index ->
                newMainLiveData[index] = visitable
            }
        }
        _mainNavLiveData.postValue(_mainNavLiveData.value?.copy(dataList = newMainLiveData))
    }

    override suspend fun addWidget(visitable: HomeNavVisitable, position: Int) {
    }

    override suspend fun deleteWidget(visitable: HomeNavVisitable, position: Int) {
    }

    override suspend fun updateNavData(navigationDataModel: MainNavigationDataModel) {
        withContext(baseDispatcher.get().ui()) {
            _mainNavLiveData.value = navigationDataModel
        }
    }

    private fun logChannelUpdate(message: String){
        if(GlobalConfig.DEBUG) Timber.tag(this.javaClass.simpleName).e(message)
    }
}