package com.tokopedia.media.picker.analytics.gallery

interface GalleryAnalytics {

    fun selectGalleryItem()

    fun clickNextButton()

    fun clickCloseButton()

    fun clickDropDown()

    fun clickGalleryThumbnail()

    fun clickCameraTab()

    fun galleryMaxPhotoLimit()

    fun galleryMaxVideoLimit()

    fun maxVideoDuration()

    fun maxImageSize()

    fun maxVideoSize()

    fun clickAlbumFolder(
        albumName: String
    )

    fun minVideoDuration()

    fun minImageResolution()

}