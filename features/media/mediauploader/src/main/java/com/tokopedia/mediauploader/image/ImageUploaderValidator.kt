package com.tokopedia.mediauploader.image

import com.tokopedia.mediauploader.Validator
import com.tokopedia.mediauploader.UploaderValidator
import com.tokopedia.mediauploader.common.data.consts.FILE_NOT_FOUND
import com.tokopedia.mediauploader.common.data.consts.UNKNOWN_ERROR
import com.tokopedia.mediauploader.common.data.consts.formatNotAllowedMessage
import com.tokopedia.mediauploader.common.data.consts.maxFileSizeMessage
import com.tokopedia.mediauploader.common.data.consts.maxResBitmapMessage
import com.tokopedia.mediauploader.common.data.consts.minResBitmapMessage
import com.tokopedia.mediauploader.common.util.isMaxBitmapResolution
import com.tokopedia.mediauploader.common.util.isMaxFileSize
import com.tokopedia.mediauploader.common.util.isMinBitmapResolution
import com.tokopedia.mediauploader.image.data.entity.ImagePolicy
import java.io.File

object ImageUploaderValidator : UploaderValidator<ImagePolicy> {

    override operator fun invoke(file: File, policy: ImagePolicy?): Validator {
        if (policy == null) return Validator(false, UNKNOWN_ERROR)

        val path = file.path
        val maxRes = policy.maximumRes
        val minRes = policy.minimumRes

        if (file.exists().not()) return Validator(false, FILE_NOT_FOUND)
        if (file.isMaxFileSize(policy.maxFileSize)) return Validator(false, maxFileSizeMessage(policy.maxFileSize))
        if (extensionsAllowed(path, policy.extension).not()) return Validator(false, formatNotAllowedMessage(policy.extension))
        if (path.isMaxBitmapResolution(maxRes.width, maxRes.height)) return Validator(false, maxResBitmapMessage(maxRes.width, maxRes.height))
        if (path.isMinBitmapResolution(minRes.width, minRes.height)) return Validator(false, minResBitmapMessage(minRes.width, minRes.height))

        return Validator(true, "")
    }
}
