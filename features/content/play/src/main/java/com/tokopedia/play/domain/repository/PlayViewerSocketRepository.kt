package com.tokopedia.play.domain.repository

import com.tokopedia.play.data.SocketCredential

/**
 * Created by kenny.hadisaputra on 09/03/22
 */
interface PlayViewerSocketRepository {

    suspend fun getSocketCredential(): SocketCredential
}