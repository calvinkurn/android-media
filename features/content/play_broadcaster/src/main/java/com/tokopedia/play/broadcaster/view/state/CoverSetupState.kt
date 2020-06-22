package com.tokopedia.play.broadcaster.view.state

import android.net.Uri
import com.tokopedia.play.broadcaster.ui.model.CoverSourceEnum

/**
 * Created by jegul on 18/06/20
 */
sealed class CoverSetupState {

    data class Cropped(val coverImage: Uri, val coverSource: CoverSourceEnum) : CoverSetupState()
    sealed class Cropping : CoverSetupState() {

        abstract val coverSource: CoverSourceEnum

        data class Product(val productId: Long, val imageUrl: String) : Cropping() {
            override val coverSource: CoverSourceEnum
                get() = CoverSourceEnum.PRODUCT
        }
        data class Image(val coverImage: Uri, override val coverSource: CoverSourceEnum) : Cropping()
    }
    object Blank : CoverSetupState()
}

val CoverSetupState.isCropping: Boolean
    get() = this is CoverSetupState.Cropping