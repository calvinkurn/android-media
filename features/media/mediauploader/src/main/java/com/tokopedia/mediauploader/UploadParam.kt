@file:SuppressLint("ParamFieldAnnotation")

package com.tokopedia.mediauploader

import android.annotation.SuppressLint
import com.tokopedia.mediauploader.common.data.entity.SourcePolicy
import com.tokopedia.mediauploader.common.state.ProgressUploader
import java.io.File

sealed interface InternalUploadParam

open class BaseParam(
    val file: File,
    val sourceId: String,
    val progress: ProgressUploader?,
    val policy: SourcePolicy,
) : InternalUploadParam

data class VideoParam(
    val withTranscode: Boolean,
    val shouldCompress: Boolean,
    val ableToRetry: Boolean,
    val base: InternalUploadParam,
) : InternalUploadParam by base

data class ImageParam(
    val isSecure: Boolean,
    val extraHeader: Map<String, String>,
    val extraBody: Map<String, String>,
    val base: InternalUploadParam,
) : InternalUploadParam by base
