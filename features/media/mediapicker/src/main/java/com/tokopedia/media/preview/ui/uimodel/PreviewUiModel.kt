package com.tokopedia.media.preview.ui.uimodel

import android.content.Context
import com.tokopedia.media.common.uimodel.MediaUiModel
import com.tokopedia.media.preview.ui.player.PickerVideoPlayer

class PreviewUiModel(var data: MediaUiModel) {

    var mVideoPlayer: PickerVideoPlayer? = null

    fun videoPlayer(context: Context): PickerVideoPlayer {
        return mVideoPlayer?: PickerVideoPlayer(context).also {
            mVideoPlayer = it
        }
    }

}