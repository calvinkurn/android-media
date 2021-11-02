package com.tokopedia.otp.silentverification.domain.usecase

import android.net.Network
import com.tokopedia.otp.silentverification.domain.repository.GetEvUrlRepository
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created by Yoris on 02/11/21.
 */

class GetEvUrlUseCase @Inject constructor(private val getEvUrlRepository: GetEvUrlRepository): UseCase<String>() {

    private lateinit var network: Network
    private var url: String = ""

    override suspend fun executeOnBackground(): String {
        if(::network.isInitialized && url.isNotEmpty()) {
            val result = getEvUrlRepository.getEvUrl(network, url).execute()
            return result.body()?.string() ?: ""
        }
        return ""
    }

    fun setNetworkSocketFactory(network: Network) {
        this.network = network
    }

    fun setUrl(url: String) {
        this.url = url
    }
}