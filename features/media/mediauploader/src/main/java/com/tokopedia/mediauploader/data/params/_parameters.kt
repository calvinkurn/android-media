package com.tokopedia.mediauploader.data.params

import com.tokopedia.mediauploader.data.state.ProgressCallback
import okhttp3.MultipartBody

interface SourceIdParam {
    /*
    * this is for a specific case,
    * such as multipart (large) video upload,
    * for init the uploader.
    * */
    var sourceId: String
}

interface CommonParam {
    // this is the common data needed
    var uploadUrl: String
    var filePath: String
    var timeOut: String
}

abstract class ParamValidator : CommonParam {

    fun hasNotParams(): Boolean {
        return uploadUrl.isEmpty() && filePath.isEmpty() && timeOut.isEmpty()
    }

}