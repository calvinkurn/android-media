package com.tokopedia.otp.silentverification.view

import android.net.Network

/**
 * Created by Yoris on 02/11/21.
 */

interface NetworkRequestListener {
    fun onSuccess(network: Network)
    fun onError(throwable: Throwable)
}