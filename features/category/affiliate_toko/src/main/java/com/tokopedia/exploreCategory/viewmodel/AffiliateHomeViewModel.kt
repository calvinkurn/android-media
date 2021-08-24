package com.tokopedia.exploreCategory.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class AffiliateHomeViewModel @Inject constructor(
        private val userSessionInterface: UserSessionInterface
) : BaseViewModel() {
    private var shimmerVisibility = MutableLiveData<Boolean>()
    private var productCards = MutableLiveData<ArrayList<Int>>()
    fun getShimmerVisibility(): LiveData<Boolean> = shimmerVisibility
    fun getProductCards(): LiveData<ArrayList<Int>> = productCards

    fun getAffiliateProductCards() {
        //TODO add api
        shimmerVisibility.value = true
        productCards.value = arrayListOf()
        shimmerVisibility.value = false
    }

    fun getUserName(): String {
        return userSessionInterface.name
    }

    fun getUserProfilePicture(): String {
        return userSessionInterface.profilePicture
    }

    fun isUserLoggedIn(): Boolean {
        return userSessionInterface.isLoggedIn
    }
}
