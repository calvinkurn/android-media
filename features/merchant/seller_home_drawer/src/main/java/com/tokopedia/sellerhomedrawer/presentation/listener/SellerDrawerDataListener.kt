package com.tokopedia.sellerhomedrawer.presentation.listener

import android.app.Activity
import com.tokopedia.sellerhomedrawer.data.SellerDrawerTokoCash
import com.tokopedia.sellerhomedrawer.data.header.SellerDrawerDeposit
import com.tokopedia.sellerhomedrawer.data.header.SellerDrawerNotification
import com.tokopedia.sellerhomedrawer.data.header.SellerDrawerProfile
import com.tokopedia.sellerhomedrawer.data.header.SellerDrawerTopPoints

interface SellerDrawerDataListener {
    fun onGetDeposit(drawerDeposit: SellerDrawerDeposit)

    fun onErrorGetDeposit(errorMessage: String)

    fun onGetNotificationDrawer(drawerNotification: SellerDrawerNotification)

    fun onErrorGetNotificationDrawer(errorMessage: String)

    fun onGetTokoCash(drawerTokoCash: SellerDrawerTokoCash)

    fun onErrorGetTokoCash(errorMessage: String)

    fun onGetTopPoints(drawerTopPoints: SellerDrawerTopPoints)

    fun onErrorGetTopPoints(errorMessage: String)

    fun onGetProfile(drawerProfile: SellerDrawerProfile)

    fun onErrorGetProfile(errorMessage: String)

    fun getString(resId: Int): String

    fun getActivity(): Activity

    fun onErrorGetProfileCompletion(errorMessage: String)

    fun onSuccessGetProfileCompletion(completion: Int)

    fun onErrorGetNotificationTopchat(errorMessage: String)

    fun onSuccessGetTopChatNotification(notifUnreads: Int)
}