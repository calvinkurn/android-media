package com.tokopedia.play.broadcaster.view.state

import android.net.Uri
import com.tokopedia.play.broadcaster.ui.model.CoverSourceEnum

/**
 * Created by jegul on 18/06/20
 */
sealed class CoverSetupState {

    data class Cropped(val coverImage: Uri) : CoverSetupState()
    data class Cropping(val coverImage: Uri, val coverSource: CoverSourceEnum) : CoverSetupState()
    object Blank : CoverSetupState()
}