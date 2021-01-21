package com.tokopedia.tradein.viewmodel

import android.os.Build
import androidx.lifecycle.MutableLiveData
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class TradeInInitialPriceViewModel @Inject constructor(
        userSession: UserSessionInterface) : BaseTradeInViewModel(), CoroutineScope {

    private val fcmDeviceId: String = userSession.deviceId
    var imeiStateLiveData: MutableLiveData<Boolean> = MutableLiveData()

    fun checkAndroid10(deviceId: String?) {
        imeiStateLiveData.value = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && (deviceId == null || deviceId == fcmDeviceId)
    }
}
