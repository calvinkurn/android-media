package com.tokopedia.shop.common.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.shop.common.view.model.ShopProductFilterParameter

class ShopPageFollowingStatusSharedViewModel : ViewModel() {

    val shopPageFollowingStatusLiveData: LiveData<Boolean>
        get() = _shopPageFollowingStatusLiveData
    private val _shopPageFollowingStatusLiveData = MutableLiveData<Boolean>()

    fun setShopPageFollowingStatus(isFollow: Boolean){
        _shopPageFollowingStatusLiveData.postValue(isFollow)
    }
}