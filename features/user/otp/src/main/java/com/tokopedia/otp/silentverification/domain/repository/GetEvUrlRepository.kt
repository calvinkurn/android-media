package com.tokopedia.otp.silentverification.domain.repository

import android.net.Network
import okhttp3.Call

/**
 * Created by Yoris on 02/11/21.
 */

interface GetEvUrlRepository {
    fun getEvUrl(network: Network, url: String): Call
}