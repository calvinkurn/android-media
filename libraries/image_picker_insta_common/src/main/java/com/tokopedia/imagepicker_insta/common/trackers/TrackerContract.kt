package com.tokopedia.imagepicker_insta.common.trackers

import androidx.annotation.StringDef
import com.tokopedia.imagepicker_insta.common.trackers.MediaType.Companion.IMAGE
import com.tokopedia.imagepicker_insta.common.trackers.MediaType.Companion.VIDEO
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.Analytics

interface TrackerContract {
    fun getTracker(): Analytics {
        return TrackApp.getInstance().gtm
    }

    fun onNextButtonClick()
    fun onBackButtonFromPicker()
    fun onCameraButtonFromPickerClick()
    fun onRecordButtonClick(@MediaType mediaType: String)
}

@Retention(AnnotationRetention.SOURCE)
@StringDef(IMAGE, VIDEO)
annotation class MediaType {
    companion object {
        const val IMAGE = "image"
        const val VIDEO = "video"
    }
}