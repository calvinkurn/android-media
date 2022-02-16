package com.tokopedia.tradein.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.laku6.tradeinsdk.api.Laku6TradeIn
import com.tokopedia.tradein.TradeinConstants
import com.tokopedia.tradein.model.Laku6DeviceModel
import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class TradeInHomePageVM @Inject constructor(private val userSession: UserSessionInterface) : BaseTradeInViewModel(), CoroutineScope {

    val askUserLogin = MutableLiveData<Int>()
    val laku6DeviceModel = MutableLiveData<Laku6DeviceModel>()
    private lateinit var laku6TradeIn: Laku6TradeIn
    var is3PLSelected : Boolean = false

    fun setLaku6(context: Context) {
        var campaignId = TradeinConstants.CAMPAIGN_ID_PROD
        if (TokopediaUrl.getInstance().TYPE == Env.STAGING) campaignId =
            TradeinConstants.CAMPAIGN_ID_STAGING
        laku6TradeIn = Laku6TradeIn.getInstance(
            context, campaignId,
            TokopediaUrl.getInstance().TYPE == Env.STAGING, TradeinConstants.TRADEIN_EXCHANGE
        )
        laku6TradeIn.setTokopediaTestType(TradeinConstants.TRADEIN_EXCHANGE)
    }

    fun isPermissionGranted(): Boolean {
        return laku6TradeIn.permissionGranted()
    }

    fun getDeviceModel() {
        laku6DeviceModel.value = Gson().fromJson(laku6TradeIn.deviceModel.toString(), Laku6DeviceModel::class.java)
    }

    fun checkLogin() {
        if (!userSession.isLoggedIn)
            askUserLogin.value = TradeinConstants.LOGIN_REQUIRED
        else {
            askUserLogin.value = TradeinConstants.LOGGED_IN
        }
    }
}