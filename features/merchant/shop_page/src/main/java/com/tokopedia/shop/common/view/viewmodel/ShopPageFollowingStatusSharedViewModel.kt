package com.tokopedia.shop.common.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.shop.pageheader.data.model.FollowShop

class ShopPageFollowingStatusSharedViewModel : ViewModel() {

    val shopPageFollowingStatusLiveData: LiveData<FollowShop>
        get() = _shopPageFollowingStatusLiveData
    private val _shopPageFollowingStatusLiveData = MutableLiveData<FollowShop>()

    fun setShopPageFollowingStatus(followShop: FollowShop?){
        _shopPageFollowingStatusLiveData.postValue(followShop)
    }
}