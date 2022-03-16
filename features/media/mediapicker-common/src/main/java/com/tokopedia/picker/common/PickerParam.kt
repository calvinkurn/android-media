@file:Suppress("ResponseFieldAnnotation")
package com.tokopedia.picker.common

import android.os.Parcelable
import com.tokopedia.picker.common.types.ModeType
import com.tokopedia.picker.common.types.PageType
import kotlinx.parcelize.Parcelize
import java.io.File

@Parcelize
data class PickerParam(
    private var pageType: Int = PageType.COMMON,
    private var modeType: Int = ModeType.COMMON,
    private var isMultipleSelection: Boolean = true,
    private var cameraRatio: CameraRatio = CameraRatio.Full,
    private var maxMediaSize: Int = 4,
    private var maxVideo: Int = 2,
    private var maxVideoSize: Long = 250_000_000, // 250 mb
    private var minVideoDuration: Int = 3000, // equals 3 sec
    private var maxVideoDuration: Int = 30000, // equals 30 sec
    private var maxImageSize: Long = 1_000_000, // 10 mb
    private var minImageResolution: Int = 300, // px
    private var maxImageResolution: Int = 20000, // px
    private var isIncludeAnimation: Boolean = false,
    private var withEditor: Boolean = false,
    private var includeMedias: List<File> = emptyList(),
    private var excludedMedias: List<File> = emptyList(),
) : Parcelable {

    // getter
    fun pageType() = pageType
    fun isImageModeOnly() = modeType == ModeType.IMAGE_ONLY
    fun isCommonPageType() = pageType == PageType.COMMON
    fun ratioIsSquare() = cameraRatio == CameraRatio.Square
    fun isMultipleSelectionType() = isMultipleSelection
    fun isIncludeVideoFile() = modeType == ModeType.COMMON
    fun isOnlyVideoFile() = modeType == ModeType.VIDEO_ONLY
    fun isIncludeGifFile() = isIncludeAnimation
    fun excludeMedias() = excludedMedias
    fun includeMedias() = includeMedias
    fun maxMediaAmount() = maxMediaSize + maxVideo
    fun maxVideoCount() = maxVideo
    fun maxVideoDuration() = maxVideoDuration
    fun minVideoDuration() = minVideoDuration
    fun maxVideoSize() = maxVideoSize
    fun maxImageResolution() = maxImageResolution
    fun minImageResolution() = minImageResolution
    fun maxImageSize() = maxImageSize
    fun withEditor() = withEditor

    // setter
    fun cameraRatio(value: CameraRatio) = apply { cameraRatio = value }
    fun maxMediaSize(value: Int) = apply { maxMediaSize = value }
    fun maxVideoSize(value: Int) = apply { maxVideo = value }
    fun maxVideoFileSize(value: Long) = apply { maxVideoSize = value }
    fun minVideoDuration(value: Int) = apply { minVideoDuration = value }
    fun maxVideoDuration(value: Int) = apply { maxVideoDuration = value }
    fun maxImageFileSize(value: Long) = apply { maxImageSize = value }
    fun minImageResolution(value: Int) = apply { minImageResolution = value }
    fun maxImageResolution(value: Int) = apply { maxImageResolution = value }
    fun withEditor(value: Boolean) = apply { withEditor = value }
    fun includeAnimationGif(value: Boolean) = apply { isIncludeAnimation = value }
    fun includeMedias(value: List<File>) = apply { includeMedias = value }
    fun excludeMedias(value: List<File>) = apply { excludedMedias = value }
    fun pageType(@PageType value: Int) = apply { pageType = value }
    fun modeType(@ModeType value: Int) = apply { modeType = value }
    fun asMultipleSelectionMode(mode: Boolean) = apply { isMultipleSelection = mode }
}

enum class CameraRatio(val value: Int) {
    Square(1),
    Full(2)
}