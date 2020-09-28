package com.tokopedia.play.broadcaster.util.error


/**
 * Created by mzennis on 30/06/20.
 */
class DefaultNetworkThrowable: Throwable() {

    override val message: String
        get() = "Koneksi internetmu terputus, nih."

    override fun getLocalizedMessage(): String {
        return message
    }
}