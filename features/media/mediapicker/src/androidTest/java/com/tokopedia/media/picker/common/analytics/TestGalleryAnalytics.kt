package com.tokopedia.media.picker.common.analytics

import com.tokopedia.media.picker.analytics.gallery.GalleryAnalytics

class TestGalleryAnalytics : GalleryAnalytics {
    override fun selectGalleryItem() {}
    override fun clickCloseButton() {}
    override fun clickNextButton() {}
    override fun clickCameraTab() {}
    override fun clickDropDown() {}
    override fun maxImageSize() {}
    override fun maxVideoSize() {}
    override fun maxVideoDuration() {}
    override fun minVideoDuration() {}
    override fun minImageResolution() {}
    override fun galleryMaxPhotoLimit() {}
    override fun galleryMaxVideoLimit() {}
    override fun clickGalleryThumbnail() {}
    override fun clickAlbumFolder(albumName: String) {}
}