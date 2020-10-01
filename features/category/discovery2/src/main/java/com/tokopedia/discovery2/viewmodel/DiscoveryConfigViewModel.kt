package com.tokopedia.discovery2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.discovery2.repository.discoveryPage.DiscoveryUIConfigGQLRepository
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject


class DiscoveryConfigViewModel @Inject constructor(private val discoveryUIConfigRepo: DiscoveryUIConfigGQLRepository) : BaseViewModel(), CoroutineScope {
    private var discoveryRNConfig = MutableLiveData<Unit>();
    private var discoveryNativeConfig = MutableLiveData<Unit>();

    companion object {
        const val NATIVE = "native"
        const val REACT_NATIVE = "react-native"
        private var config: String? = null
    }

    fun getDiscoveryRNConfigLiveData(): LiveData<Unit> = discoveryRNConfig

    fun getDiscoveryNativeConfigLiveData(): LiveData<Unit> = discoveryNativeConfig


    override fun doOnCreate() {
        super.doOnCreate()
        config?.let {
           setUIConfig(config)
        }?: getDiscoveryUIConfig()

    }

    fun getDiscoveryUIConfig() {
        launchCatchError(
                block = {
                    val data = discoveryUIConfigRepo.getDiscoveryUIConfigData()
                    data.discoveryPageUIConfig?.data?.config?.let { setUIConfig(it) }?:setUIConfig(REACT_NATIVE)
                },
                onError = {
                    setUIConfig(REACT_NATIVE)
                }
        )
    }


    private fun setUIConfig(config: String?) {
        when(config) {
            REACT_NATIVE->discoveryRNConfig.postValue(Unit)
            NATIVE->discoveryNativeConfig.postValue(Unit)
        }
        DiscoveryConfigViewModel.config = config;
    }
}