package com.tokopedia.v2.home.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.v2.home.base.HomeRepository
import com.tokopedia.v2.home.base.adapterdelegate.ModelViewType
import com.tokopedia.v2.home.model.pojo.home.DynamicHomeIcon
import com.tokopedia.v2.home.model.pojo.home.HomeData
import com.tokopedia.v2.home.model.pojo.home.HomeFlagType
import com.tokopedia.v2.home.model.pojo.home.Tickers
import com.tokopedia.v2.home.model.vo.*
import com.tokopedia.v2.home.util.HomeLiveData
import com.tokopedia.v2.home.util.copy
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

class HomePageViewModel @Inject constructor (
        private val homeRepository: HomeRepository,
        private val userSessionInterface: UserSessionInterface,
        private val remoteConfig: RemoteConfig,
        @Named("Main") val dispatcher: CoroutineDispatcher
): BaseViewModel(dispatcher) {

    private var walletPosition = -1
    private val _homeData = HomeLiveData<List<ModelViewType>>(listOf())
    val homeData: LiveData<List<ModelViewType>> get() = _homeData
    private var homeSource: LiveData<Resource<HomeData>> = MutableLiveData()

    fun getData() {
        walletPosition = -1
        launch (dispatcher){
            _homeData.removeSource(homeSource)
            homeSource = homeRepository.getHomeDataWithCache()
            _homeData.addSource(homeSource){
                _homeData.value = mapper(it.data)
                if(it.status == Resource.Status.SUCCESS && it.data?.homeFlag?.getFlag(HomeFlagType.HAS_TOKOPOINTS) == true) {
                    getWalletData()
                    getTokopointData()
                }
            }
        }
    }

    private fun getWalletData(){
        launchCatchError(block = {
            val walletData = homeRepository.getWalletData()

            val newModelList = _homeData.value.copy().toMutableList()
            val walletDataModel = _homeData.value[walletPosition]

            if(walletDataModel is WalletDataModel){
                newModelList[walletPosition] = walletDataModel.copy(walletBalance = walletData)
                _homeData.value = newModelList
            }
        }) {
            val newModelList = _homeData.value.copy().toMutableList()
            val walletDataModel = _homeData.value[walletPosition]

            if(walletDataModel is WalletDataModel){
                newModelList[walletPosition] = walletDataModel.copy(walletBalance = walletDataModel.walletBalance.copy(status = Resource.Status.ERROR))
                _homeData.value = newModelList
            }
        }
    }

    private fun getTokopointData(){
        launchCatchError(block = {
            val tokopointData = homeRepository.getTokopointData()
            val newModelList = _homeData.value.copy().toMutableList()
            val walletDataModel = _homeData.value[walletPosition]
            if(walletDataModel is WalletDataModel){
                newModelList[walletPosition] = walletDataModel.copy(tokopoint = tokopointData)
                _homeData.value = newModelList
            }
        }) {
            val newModelList = _homeData.value.copy().toMutableList()
            val walletDataModel = _homeData.value[walletPosition]
            if(walletDataModel is WalletDataModel){
                newModelList[walletPosition] = walletDataModel.copy(tokopoint = walletDataModel.tokopoint.copy(status = Resource.Status.ERROR))
                _homeData.value = newModelList
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
                if(userSessionInterface.isLoggedIn) {
                     if(walletPosition != -1 && _homeData.value.size > walletPosition){
                         val walletDataModel = _homeData.value[walletPosition]
                         walletPosition = list.size
                         list.add(walletDataModel)
                     }else {
                         walletPosition = list.size
                         list.add(WalletDataModel(
                                 walletBalance = WalletDataModel.WalletAction(
                                         status = Resource.Status.LOADING
                                 ),
                                 tokopoint = WalletDataModel.TokopointAction(
                                         status = Resource.Status.LOADING
                                 )
                         ))
                     }
                } else {
                   list.add(WalletNonLoginDataModel())
               }
            }
            if(remoteConfig.getBoolean(RemoteConfigKey.SHOW_HOME_GEOLOCATION_COMPONENT, true)){
                val title = remoteConfig.getString(RemoteConfigKey.HOME_GEOLOCATION_COMPONENT_TITLE, "Lihat produk menarik di dekatmu")
                val description = remoteConfig.getString(RemoteConfigKey.HOME_GEOLOCATION_COMPONENT_DESCRIPTION, "Mulai dengan <a href=\"/\">aktifkan lokasi")
                list.add(GeoLocationDataModel(title, description))
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