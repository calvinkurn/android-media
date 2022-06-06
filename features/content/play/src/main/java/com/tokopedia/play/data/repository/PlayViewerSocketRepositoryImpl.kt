package com.tokopedia.play.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play.data.SocketCredential
import com.tokopedia.play.domain.GetSocketCredentialUseCase
import com.tokopedia.play.domain.repository.PlayViewerSocketRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 09/03/22
 */
class PlayViewerSocketRepositoryImpl @Inject constructor(
    private val getSocketCredentialUseCase: GetSocketCredentialUseCase,
    private val dispatchers: CoroutineDispatchers,
) : PlayViewerSocketRepository {

    override suspend fun getSocketCredential(): SocketCredential = withContext(dispatchers.io) {
        getSocketCredentialUseCase.executeOnBackground()
    }
}