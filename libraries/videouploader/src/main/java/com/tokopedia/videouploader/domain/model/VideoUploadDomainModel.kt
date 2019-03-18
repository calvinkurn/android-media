package com.tokopedia.videouploader.domain.model

/**
 * @author by nisie on 13/03/19.
 */
class VideoUploadDomainModel<T>(val type : Class<T>){
    var dataResultVideoUpload: T? = null
}