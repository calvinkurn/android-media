package com.tokopedia.sellerhomedrawer.domain.datamanager

import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface

interface SellerDrawerDataManager {

    fun getTokoCash()

    fun unsubscribe()

    fun getSellerUserAttributes(userSession: UserSessionInterface)
}