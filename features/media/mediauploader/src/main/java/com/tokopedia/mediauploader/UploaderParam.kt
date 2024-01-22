@file:SuppressLint("ParamFieldAnnotation")

package com.tokopedia.mediauploader

import android.annotation.SuppressLint
import com.tokopedia.mediauploader.common.state.ProgressUploader
import com.tokopedia.mediauploader.common.util.isVideoFormat
import java.io.File

sealed interface BaseUploaderParam

// a common properties for both Image and Video
// common param: file, sourceId, and progress
open class BaseParam(
    val file: File,
    val sourceId: String,
    val progress: ProgressUploader?,
) : BaseUploaderParam {
    
    companion object {
        // only for logger
        fun create(file: File, sourceId: String): BaseParam {
            return BaseParam(file, sourceId, null)
        }
    }
}

// parameter builder for [UploaderUseCase]
open class UseCaseParam(
    val image: BaseUploaderParam,
    val video: BaseUploaderParam,
    val base: BaseUploaderParam
) : BaseUploaderParam by base {

    fun isVideo(): Boolean {
        val param = base as BaseParam
        val path = param.file.path
        return isVideoFormat(path)
    }
}

// param-specific for Video
data class VideoParam(
    val withTranscode: Boolean,
    val shouldCompress: Boolean,
    val ableToRetry: Boolean,
    val base: BaseUploaderParam,
) : BaseUploaderParam by base

// param-specific for Image
data class ImageParam(
    val isSecure: Boolean,
    val extraHeader: Map<String, String>,
    val extraBody: Map<String, String>,
    val base: BaseUploaderParam,
) : BaseUploaderParam by base
