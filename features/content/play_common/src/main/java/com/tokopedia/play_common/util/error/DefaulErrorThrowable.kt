package com.tokopedia.play_common.util.error

/**
 * Created by mzennis on 29/06/20.
 */
class DefaultErrorThrowable(
    private val errorMessage: String = DEFAULT_MESSAGE
): Throwable() {

    override val message: String
        get() = errorMessage

    override fun getLocalizedMessage(): String {
        return message
    }

    companion object {
        const val DEFAULT_MESSAGE = "Ada sedikit kendala pada sistem."
    }
}
