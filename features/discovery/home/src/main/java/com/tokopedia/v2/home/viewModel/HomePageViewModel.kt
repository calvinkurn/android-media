package com.tokopedia.v2.home.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.v2.home.base.HomeRepository
import com.tokopedia.v2.home.base.adapterdelegate.ModelViewType
import com.tokopedia.v2.home.model.pojo.DynamicHomeIcon
import com.tokopedia.v2.home.model.pojo.HomeData
import com.tokopedia.v2.home.model.pojo.HomeFlagType
import com.tokopedia.v2.home.model.pojo.Tickers
import com.tokopedia.v2.home.model.vo.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

class HomePageViewModel @Inject constructor (
        private val homeRepository: HomeRepository,
        private val userSessionInterface: UserSessionInterface,
        @Named("Main") val dispatcher: CoroutineDispatcher
): BaseViewModel(dispatcher) {

    private val _homeData = MediatorLiveData<Resource<List<ModelViewType>>>()
    val homeData: LiveData<Resource<List<ModelViewType>>> get() = _homeData
    private var homeSource: LiveData<Resource<HomeData>> = MutableLiveData()

    fun getData() {
        launch (dispatcher){
            _homeData.removeSource(homeSource)
            homeSource = homeRepository.getHomeDataWithCache()
            _homeData.addSource(homeSource){
                _homeData.value = Resource(it.status, mapper(it.data), it.error)
            }
        }
    }

    private fun mapper(homeData: HomeData?): List<ModelViewType>{
        val list = mutableListOf<ModelViewType>()
        homeData?.let {
            list.add(BannerDataModel(homeData.banner))
            if(!homeData.ticker.tickers.isNotEmpty()){
//                homeData.ticker.tickers
                list.add(TickerDataModel(
                        listOf(
                                Tickers(message = "JNE ERROR!", id = "12", color = "#FF0000"),
                                Tickers(message = "JNE ERROR 2!", id = "13", color = "#FF0000"),
                                Tickers(message = "JNE ERROR 3!", id = "14", color = "#FF0000")
                        )
                ))
            }
            if(homeData.homeFlag.getFlag(HomeFlagType.HAS_TOKOPOINTS)){
               list.add(
                   if(userSessionInterface.isLoggedIn)
                       WalletDataModel()
                   else WalletNonLoginDataModel()
               )
            }
            list.add(DynamicIconDataModel(mappingDynamicIcons(homeData.dynamicHomeIcon), homeData.homeFlag.getFlag(HomeFlagType.DYNAMIC_ICON_WRAP)))
        }
        return list
    }

    private fun mappingDynamicIcons(dynamicHomeIcon: DynamicHomeIcon): List<DynamicIconDataModel.IconDataModel>{
        val icons = mutableListOf<DynamicIconDataModel.IconDataModel>()
        icons.addAll(
                dynamicHomeIcon.dynamicIcon.map {
                    DynamicIconDataModel.IconDataModel(it.id, it.name, it.applinks, it.imageUrl, it.url, it.bu_identifier)
                }
        )
        icons.addAll(
                dynamicHomeIcon.useCaseIcon.map {
                    DynamicIconDataModel.IconDataModel(it.id, it.name, it.applinks, it.imageUrl, it.url, it.bu_identifier)
                }
        )
        return icons
    }
}