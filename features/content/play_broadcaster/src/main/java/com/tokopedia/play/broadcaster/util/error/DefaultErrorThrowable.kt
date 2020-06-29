package com.tokopedia.play.broadcaster.util.error


/**
 * Created by mzennis on 29/06/20.
 */
class DefaultErrorThrowable : Throwable() {

    override val message: String
        get() = "Ada sedikit kendala pada sistem."

    override fun getLocalizedMessage(): String {
        return message
    }
}