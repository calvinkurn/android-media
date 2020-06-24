package com.tokopedia.play.broadcaster.view.contract

import com.tokopedia.play.broadcaster.ui.model.PlayCoverUiModel

/**
 * Created by jegul on 18/06/20
 */
interface CropImageCallback {

    fun onCropSuccess(cover: PlayCoverUiModel)

    fun onCropFailure(reason: Throwable)
}