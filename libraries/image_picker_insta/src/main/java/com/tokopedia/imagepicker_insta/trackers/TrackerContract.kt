package com.tokopedia.imagepicker_insta.trackers

import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.Analytics

interface TrackerContract {
    fun getTracker(): Analytics {
        return TrackApp.getInstance().gtm
    }
    fun onNextButtonClick()
    fun onBackButtonFromPicker()
    fun onCameraButtonFromPickerClick()
    fun onRecordButtonClick()
}