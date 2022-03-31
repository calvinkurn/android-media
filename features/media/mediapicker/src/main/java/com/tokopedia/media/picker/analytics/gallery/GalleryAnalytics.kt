package com.tokopedia.media.picker.analytics.gallery

interface GalleryAnalytics {
    fun selectGalleryItem(
        entryPoint: String
    )

    fun clickNextButton(
        entryPoint: String
    )

    fun clickCloseButton(
        entryPoint: String
    )

    fun clickDropDown(
        entryPoint: String
    )

    fun clickGalleryThumbnail(
        entryPoint: String
    )

    fun clickCameraTab(
        entryPoint: String
    )

    fun galleryMaxPhotoLimit(
        entryPoint: String
    )

    fun galleryMaxVideoLimit(
        entryPoint: String
    )

    fun maxVideoDuration(
        entryPoint: String
    )

    fun maxImageSize(
        entryPoint: String
    )

    fun maxVideoSize(
        entryPoint: String
    )

    fun clickAlbumFolder(
        entryPoint: String,
        albumName: String
    )

    fun minVideoDuration(
        entryPoint: String
    )

    fun minImageResolution(
        entryPoint: String
    )
}