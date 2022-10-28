package com.tokopedia.media.picker.analytics.gallery

interface GalleryAnalytics {
    fun selectGalleryItem()
    fun clickCloseButton()
    fun clickNextButton()
    fun clickCameraTab()
    fun clickDropDown()
    fun maxImageSize()
    fun maxVideoSize()
    fun maxVideoDuration()
    fun minVideoDuration()
    fun minImageResolution()
    fun galleryMaxPhotoLimit()
    fun galleryMaxVideoLimit()
    fun clickGalleryThumbnail()
    fun clickAlbumFolder(albumName: String)
}