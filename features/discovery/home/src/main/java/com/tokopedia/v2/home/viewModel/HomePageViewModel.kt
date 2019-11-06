package com.tokopedia.v2.home.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.v2.home.base.HomeRepository
import com.tokopedia.v2.home.model.pojo.HomeData
import com.tokopedia.v2.home.model.vo.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class HomePageViewModel @Inject constructor (
        private val homeRepository: HomeRepository,
        @Named("Main") val dispatcher: CoroutineDispatcher
): BaseViewModel(dispatcher) {

    private val _homeData = MediatorLiveData<Resource<HomeData>>()
    val homeData: LiveData<Resource<HomeData>> get() = _homeData
    private var homeSource: LiveData<Resource<HomeData>> = MutableLiveData()

    fun getData() {
        launch (dispatcher){
            _homeData.removeSource(homeSource)
            homeSource = homeRepository.getHomeDataWithCache()
            _homeData.addSource(homeSource){
                _homeData.value = it
            }
        }
    }
}