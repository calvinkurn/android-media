package com.tokopedia.shop.common.view.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.shop.R
import com.tokopedia.shop.common.data.source.cloud.model.followshop.FollowShop

class ShopPageFollowingStatusSharedViewModel : ViewModel() {

    val shopPageFollowingStatusLiveData: LiveData<FollowShop>
        get() = _shopPageFollowingStatusLiveData
    private val _shopPageFollowingStatusLiveData = MutableLiveData<FollowShop>()

    fun setShopPageFollowingStatus(followShop: FollowShop?, context: Context){
        var followingData = followShop
        if (followShop?.isFollowing == false) {
            followingData = followShop.copy(buttonLabel = context.getString(R.string.follow_button_label))
        }
        _shopPageFollowingStatusLiveData.postValue(followingData)
    }
}