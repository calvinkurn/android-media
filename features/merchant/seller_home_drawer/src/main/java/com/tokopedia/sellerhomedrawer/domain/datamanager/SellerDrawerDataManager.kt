package com.tokopedia.sellerhomedrawer.domain.datamanager

import com.tokopedia.user.session.UserSession

interface SellerDrawerDataManager {

    fun getTokoCash()

    fun unsubscribe()

    fun getUserAttributes(sessionHandler: UserSession)

    fun getSellerUserAttributes(sessionHandler: UserSession)
}