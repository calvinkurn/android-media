package com.tokopedia.sellerhome.settings.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sellerhome.settings.domain.usecase.GetAllShopInfoUseCase
import com.tokopedia.sellerhome.settings.view.uimodel.shopinfo.SettingShopInfoUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Named
class OtherMenuViewModel @Inject constructor(
        @Named("Main") dispatcher: CoroutineDispatcher,
        private val getAllShopInfoUseCase: GetAllShopInfoUseCase
): BaseViewModel(dispatcher) {

    companion object {
        private const val DELAY_TIME = 5000L
    }

    private val _settingShopInfoLiveData = MutableLiveData<Result<SettingShopInfoUiModel>>()
    private val _isToasterAlreadyShown = MutableLiveData<Boolean>().apply { value = false }
    private val _isStatusBarInitialState = MutableLiveData<Boolean>().apply { value = true }

    val settingShopInfoLiveData: LiveData<Result<SettingShopInfoUiModel>>
        get() = _settingShopInfoLiveData
    val isStatusBarInitialState: LiveData<Boolean>
        get() = _isStatusBarInitialState
    val isToasterAlreadyShown: LiveData<Boolean>
        get() = _isToasterAlreadyShown

    fun getAllSettingShopInfo(isToasterRetry: Boolean = false) {
        if (isToasterRetry) {
            launch(coroutineContext) {
                checkDelayErrorResponseTrigger()
            }
        }
        getAllShopInfoData()
    }

    fun setIsStatusBarInitialState(isInitialState: Boolean) {
        _isStatusBarInitialState.value = isInitialState
    }

    private fun getAllShopInfoData() {
        launchCatchError(block = {
            _settingShopInfoLiveData.value = Success(withContext(Dispatchers.IO) {
                getAllShopInfoUseCase.executeOnBackground()
            })
        }, onError = {
            _settingShopInfoLiveData.value = Fail(it)
        })
    }

    private suspend fun checkDelayErrorResponseTrigger() {
        _isToasterAlreadyShown.value?.let { isToasterAlreadyShown ->
            if (!isToasterAlreadyShown){
                _isToasterAlreadyShown.value = true
                delay(DELAY_TIME)
                _isToasterAlreadyShown.value = false
            }
        }
    }

}

