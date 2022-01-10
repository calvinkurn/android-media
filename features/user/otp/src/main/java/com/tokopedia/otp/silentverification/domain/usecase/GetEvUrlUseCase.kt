package com.tokopedia.otp.silentverification.domain.usecase

import android.net.Network
import com.tokopedia.otp.silentverification.domain.repository.GetEvUrlRepository
import com.tokopedia.usecase.coroutines.UseCase
import okhttp3.Call
import javax.inject.Inject

/**
 * Created by Yoris on 02/11/21.
 */

class GetEvUrlUseCase @Inject constructor(private val getEvUrlRepository: GetEvUrlRepository): UseCase<String>() {

    private lateinit var network: Network
    private var url: String = ""
    private var call: Call? = null

    override suspend fun executeOnBackground(): String {
        if(::network.isInitialized && url.isNotEmpty()) {
            call = getEvUrlRepository.getEvUrl(network, url)
            val result = call!!.execute()
            return result.body?.string() ?: ""
        }
        return ""
    }

    fun cancelEvUrl() {
        if (call?.isExecuted == true) {
            call?.cancel()
        }
    }


    fun setNetworkSocketFactory(network: Network) {
        this.network = network
    }

    fun setUrl(url: String) {
        this.url = url
    }
}