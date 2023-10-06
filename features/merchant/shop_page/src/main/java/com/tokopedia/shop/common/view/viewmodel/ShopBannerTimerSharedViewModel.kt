package com.tokopedia.shop.common.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.shop.home.view.model.ShopWidgetDisplayBannerTimerUiModel

class ShopBannerTimerSharedViewModel : ViewModel() {

    val sharedBannerTimerUiModel: LiveData<ShopWidgetDisplayBannerTimerUiModel>
        get() = _sharedBannerTimerUiModel
    private val _sharedBannerTimerUiModel = MutableLiveData<ShopWidgetDisplayBannerTimerUiModel>()

    fun updateSharedBannerTimerData(bannerTimerUiModel: ShopWidgetDisplayBannerTimerUiModel) {
        _sharedBannerTimerUiModel.postValue(bannerTimerUiModel)
    }
}
