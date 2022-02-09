package com.tokopedia.play.broadcaster.setup.product.model

/**
 * Created By : Jonathan Darwin on February 09, 2022
 */
sealed class PlayBroProductSetupUiEvent {

    data class Error(val throwable: Throwable, val action: (()->Unit)? = null): PlayBroProductSetupUiEvent()
}