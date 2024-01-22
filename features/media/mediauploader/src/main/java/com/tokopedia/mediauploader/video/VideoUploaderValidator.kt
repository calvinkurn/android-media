package com.tokopedia.mediauploader.video

import com.tokopedia.mediauploader.Validator
import com.tokopedia.mediauploader.UploaderValidator
import com.tokopedia.mediauploader.common.data.consts.FILE_NOT_FOUND
import com.tokopedia.mediauploader.common.data.consts.UNKNOWN_ERROR
import com.tokopedia.mediauploader.common.data.consts.formatNotAllowedMessage
import com.tokopedia.mediauploader.common.data.consts.maxFileSizeMessage
import com.tokopedia.mediauploader.common.util.isMaxFileSize
import com.tokopedia.mediauploader.video.data.entity.VideoPolicy
import java.io.File

object VideoUploaderValidator : UploaderValidator<VideoPolicy> {

    override operator fun invoke(file: File, policy: VideoPolicy?): Validator {
        val path = file.path

        if (policy == null) return Validator(false, UNKNOWN_ERROR)
        if (file.exists().not()) return Validator(false, FILE_NOT_FOUND)
        if (file.isMaxFileSize(policy.maxFileSize)) return Validator(false, maxFileSizeMessage(policy.maxFileSize))
        if (extensionsAllowed(path, policy.extension).not()) return Validator(false, formatNotAllowedMessage(policy.extension))

        return Validator(true, "")
    }
}
