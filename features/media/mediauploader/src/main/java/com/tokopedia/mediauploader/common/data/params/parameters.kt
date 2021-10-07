package com.tokopedia.mediauploader.common.data.params

interface SourceIdParam {
    /*
    * this is for a specific case,
    * such as multipart (large) video upload,
    * for init the uploader.
    * */
    var sourceId: String
}

interface VideoLargeParam {
    var partNumber: String
    var uploadId: String
}

interface CommonParam {
    // this is the common data needed
    var uploadUrl: String
    var filePath: String
    var timeOut: String
}

abstract class ParamValidator : CommonParam {

    fun hasNoParams(): Boolean {
        return uploadUrl.isEmpty() && filePath.isEmpty() && timeOut.isEmpty()
    }

}