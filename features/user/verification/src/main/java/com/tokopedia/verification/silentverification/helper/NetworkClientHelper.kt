package com.tokopedia.verification.silentverification.helper

import android.content.Context
import com.tokopedia.verification.silentverification.view.NetworkRequestListener

/**
 * Created by Yoris on 02/11/21.
 */

interface NetworkClientHelper {
    fun makeNetworkRequest(context: Context, listener: NetworkRequestListener)
}
