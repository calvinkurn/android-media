package com.tokopedia.play.broadcaster.util.error


/**
 * Created by mzennis on 30/06/20.
 */
class DefaultNetworkException: Exception() {

    override val message: String
        get() = "Koneksi internetmu terputus, nih."

    override fun getLocalizedMessage(): String {
        return message
    }
}