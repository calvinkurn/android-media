package com.tokopedia.mediauploader

import com.tokopedia.mediauploader.common.util.fileExtension
import java.io.File

typealias Validator = UploaderValidator.Validator

interface UploaderValidator<T> {

    operator fun invoke(file: File, policy: T?): Validator

    fun extensionsAllowed(filePath: String, allowedExtensions: String): Boolean {
        val fileExt = filePath
            .fileExtension()
            .lowercase()

        val allowed = allowedExtensions
            .split(",")
            .map { it.drop(1) }

        return allowed.contains(fileExt)
    }

    data class Validator(
        val isValid: Boolean,
        val message: String
    )
}
