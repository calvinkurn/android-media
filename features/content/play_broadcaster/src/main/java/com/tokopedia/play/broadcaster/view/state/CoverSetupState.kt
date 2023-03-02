package com.tokopedia.play.broadcaster.view.state

import android.net.Uri
import com.tokopedia.play.broadcaster.ui.model.CoverSource

/**
 * Created by jegul on 18/06/20
 */
sealed class CoverSetupState {

    data class GeneratedCover(val coverImage: String) : CoverSetupState()

    sealed class Cropped : CoverSetupState() {

        abstract val coverImage: Uri
        abstract val coverSource: CoverSource

        data class Draft(override val coverImage: Uri, override val coverSource: CoverSource) : Cropped()
        data class Uploaded(val localImage: Uri?, override val coverImage: Uri, override val coverSource: CoverSource) : Cropped()
    }
    sealed class Cropping : CoverSetupState() {

        abstract val coverSource: CoverSource

        data class Product(val productId: String, val imageUrl: String) : Cropping() {
            override val coverSource: CoverSource
                get() = CoverSource.Product(productId)
        }
        data class Image(val coverImage: Uri, override val coverSource: CoverSource) : Cropping()
    }
    object Blank : CoverSetupState()
}
