package com.tokopedia.mediauploader.data.params

data class MediaUploaderParam(
    var uploadUrl: String = "",
    var filePath: String = "",
    var timeOut: String = ""
) {

    fun hasEmptyParams(): Boolean {
        return uploadUrl.isEmpty() && filePath.isEmpty() && timeOut.isEmpty()
    }

}