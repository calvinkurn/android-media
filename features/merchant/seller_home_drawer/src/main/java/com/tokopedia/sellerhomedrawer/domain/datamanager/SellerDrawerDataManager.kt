package com.tokopedia.sellerhomedrawer.domain.datamanager

import com.tokopedia.user.session.UserSession

interface SellerDrawerDataManager {

    fun getTokoCash()

    fun unsubscribe()

    fun getSellerUserAttributes(userSession: UserSession)
}