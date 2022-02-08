package com.tokopedia.play.broadcaster.setup.product.model

/**
 * Created by kenny.hadisaputra on 08/02/22
 */
sealed class PlayBroProductChooserEvent {

    object SaveProductSuccess : PlayBroProductChooserEvent()
    data class ShowError(val error: Throwable) : PlayBroProductChooserEvent()
}