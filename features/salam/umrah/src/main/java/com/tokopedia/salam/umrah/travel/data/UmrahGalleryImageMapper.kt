package com.tokopedia.salam.umrah.travel.data

object UmrahGalleryImageMapper {
    fun galleryImageSource(data: List<Media>): List<String>{
        return data.map {
            with(it) {
               source
            }
        }
    }

    fun galleryImageThumbnail(data: List<Media>): List<String>{
        return data.map {
            with(it) {
                thumbnail
            }
        }
    }
}