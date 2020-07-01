package com.tokopedia.play.broadcaster.view.state

import android.net.Uri
import com.tokopedia.play.broadcaster.ui.model.CoverSource

/**
 * Created by jegul on 18/06/20
 */
sealed class CoverSetupState {

    data class Cropped(val coverImage: Uri, val coverSource: CoverSource, val state: SetupDataState) : CoverSetupState()
    sealed class Cropping : CoverSetupState() {

        abstract val coverSource: CoverSource

        data class Product(val productId: Long, val imageUrl: String) : Cropping() {
            override val coverSource: CoverSource
                get() = CoverSource.Product(productId)
        }
        data class Image(val coverImage: Uri, override val coverSource: CoverSource) : Cropping()
    }
    object Blank : CoverSetupState()
}

val CoverSetupState.isCropping: Boolean
    get() = this is CoverSetupState.Cropping