package com.tokopedia.otp.silentverification.domain.usecase

import android.net.Network
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.otp.silentverification.domain.repository.GetEvUrlRepository
import com.tokopedia.usecase.coroutines.UseCase
import kotlinx.coroutines.Dispatchers
import okhttp3.Call
import okhttp3.Dispatcher
import javax.inject.Inject

/**
 * Created by Yoris on 02/11/21.
 */

class GetEvUrlUseCase @Inject constructor(private val getEvUrlRepository: GetEvUrlRepository) : CoroutineUseCase<Unit, String>(
    Dispatchers.IO){

    private lateinit var network: Network
    private var url: String = ""

    fun setNetworkSocketFactory(network: Network) {
        this.network = network
    }

    fun setUrl(url: String) {
        this.url = url
    }

    override fun graphqlQuery(): String {
        return ""
    }

    override suspend fun execute(params: Unit): String {
        if(::network.isInitialized && url.isNotEmpty()) {
            val result = getEvUrlRepository.getEvUrl(network, url).execute()
            return result.body?.string() ?: ""
        }
        return ""
    }
}