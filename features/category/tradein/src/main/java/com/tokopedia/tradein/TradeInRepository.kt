package com.tokopedia.tradein

import com.tokopedia.common.network.util.NetworkClient
import com.tokopedia.tradein_common.repository.BaseRepository
import com.tokopedia.user.session.UserSessionInterface

class TradeInRepository: BaseRepository() {

    private lateinit var userSession: UserSessionInterface

    fun getUserLoginState(): UserSessionInterface {
        if (!(::userSession.isInitialized)) {
            userSession = NetworkClient.getsUserSession()
        }
        return userSession
    }
}