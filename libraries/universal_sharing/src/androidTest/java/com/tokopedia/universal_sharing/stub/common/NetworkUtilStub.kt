package com.tokopedia.universal_sharing.stub.common

import android.content.Context
import com.tokopedia.universal_sharing.util.NetworkUtil
import javax.inject.Inject

class NetworkUtilStub @Inject constructor() : NetworkUtil() {

    var isConnectedToNetwork: Boolean = true

    override fun isNetworkAvailable(context: Context): Boolean {
        return isConnectedToNetwork
    }
}
