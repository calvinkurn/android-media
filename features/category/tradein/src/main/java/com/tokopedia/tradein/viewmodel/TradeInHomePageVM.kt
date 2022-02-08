package com.tokopedia.tradein.viewmodel

import androidx.lifecycle.MutableLiveData
import com.laku6.tradeinsdk.api.Laku6TradeIn
import com.tokopedia.common_tradein.model.TradeInParams
import com.tokopedia.tradein.TradeinConstants
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class TradeInHomePageVM @Inject constructor(private val userSession: UserSessionInterface) : BaseTradeInViewModel(), CoroutineScope {

    val askUserLogin = MutableLiveData<Int>()

    override fun doOnCreate() {
        super.doOnCreate()
        checkLogin()
    }

    private fun checkLogin() {
        if (!userSession.isLoggedIn)
            askUserLogin.value = TradeinConstants.LOGIN_REQUIRED
        else {
            askUserLogin.value = TradeinConstants.LOGGED_IN
        }
    }
}