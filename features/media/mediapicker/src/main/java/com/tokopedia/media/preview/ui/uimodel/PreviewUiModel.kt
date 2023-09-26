package com.tokopedia.media.preview.ui.uimodel

import android.content.Context
import com.tokopedia.media.preview.ui.player.PickerVideoPlayer
import com.tokopedia.picker.common.uimodel.MediaUiModel

class PreviewUiModel(var data: MediaUiModel) {

    var mVideoPlayer: PickerVideoPlayer? = null

    fun videoPlayer(context: Context): PickerVideoPlayer {
        return mVideoPlayer?: PickerVideoPlayer(context).also {
            mVideoPlayer = it
        }
    }

}
