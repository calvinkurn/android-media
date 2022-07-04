package com.tokopedia.media.picker.ui.activity.picker.listeners

interface ToasterListener {
    fun onShowMediaLimitReachedGalleryToast()
    fun onShowVideoLimitReachedGalleryToast()
    fun onShowMediaLimitReachedCameraToast()
    fun onShowVideoLimitReachedCameraToast()
    fun onShowVideoMinDurationToast()
    fun onShowVideoMaxDurationToast()
    fun onShowVideoMaxFileSizeToast()
    fun onShowImageMinResToast()
    fun onShowImageMaxResToast()
    fun onShowImageMaxFileSizeToast()
    fun onShowFailToVideoRecordToast()
}