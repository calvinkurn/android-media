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
    private var maxMediaItem: Int = 4,
    private var maxVideoItem: Int = 2,
    private var maxVideoFileSize: Long = 250_000_000, // 250 mb
    private var minVideoDuration: Int = 3000, // equals 3 sec
    private var maxVideoDuration: Long = 30_000, // equals 30 sec
    private var maxImageFileSize: Long = 10_000_000, // 10 mb
    private var minImageResolution: Int = 300, // px
    private var maxImageResolution: Int = 20000, // px
    private var minStorageThreshold: Long = 150_000_000, // 150 mb
    private var isIncludeAnimation: Boolean = false,
    private var withEditor: Boolean = false,
    private var pageSource: PageSource = PageSource.Unknown,
    private var includeMedias: List<File> = emptyList(),
    private var excludedMedias: List<File> = emptyList(),
) : Parcelable {

    // getter
    fun pageType() = pageType
    fun pageSourceName() = pageSource.value
    fun isImageModeOnly() = modeType == ModeType.IMAGE_ONLY
    fun isCommonPageType() = pageType == PageType.COMMON
    fun ratioIsSquare() = cameraRatio == CameraRatio.Square
    fun isMultipleSelectionType() = isMultipleSelection
    fun isIncludeVideoFile() = modeType == ModeType.COMMON
    fun isOnlyVideoFile() = modeType == ModeType.VIDEO_ONLY
    fun isIncludeGifFile() = isIncludeAnimation
    fun excludeMedias() = excludedMedias
    fun includeMedias() = includeMedias
    fun maxMediaTotal() = maxMediaItem + maxVideoItem
    fun maxVideoCount() = maxVideoItem
    fun maxVideoDuration() = maxVideoDuration
    fun minVideoDuration() = minVideoDuration
    fun maxVideoFileSize() = maxVideoFileSize
    fun maxImageResolution() = maxImageResolution
    fun minImageResolution() = minImageResolution
    fun maxImageFileSize() = maxImageFileSize
    fun minStorageThreshold() = minStorageThreshold
    fun withEditor() = withEditor

    // setter
    fun pageSource(value: PageSource) = apply { pageSource = value }
    fun cameraRatio(value: CameraRatio) = apply { cameraRatio = value }
    fun maxMediaItem(value: Int) = apply { maxMediaItem = value }
    fun maxVideoItem(value: Int) = apply { maxVideoItem = value }
    fun maxVideoFileSize(value: Long) = apply { maxVideoFileSize = value }
    fun minVideoDuration(value: Int) = apply { minVideoDuration = value }
    fun maxVideoDuration(value: Long) = apply { maxVideoDuration = value }
    fun maxImageFileSize(value: Long) = apply { maxImageFileSize = value }
    fun minImageResolution(value: Int) = apply { minImageResolution = value }
    fun maxImageResolution(value: Int) = apply { maxImageResolution = value }
    fun minStorageThreshold(value: Long) = apply { minStorageThreshold = value }
    fun withEditor(value: Boolean) = apply { withEditor = value }
    fun includeAnimationGif(value: Boolean) = apply { isIncludeAnimation = value }
    fun includeMedias(value: List<File>) = apply { includeMedias = value }
    fun excludeMedias(value: List<File>) = apply { excludedMedias = value }
    fun pageType(@PageType value: Int) = apply { pageType = value }
    fun modeType(@ModeType value: Int) = apply { modeType = value }
    fun multipleSelectionMode() = apply { isMultipleSelection = true }
    fun singleSelectionMode() = apply { isMultipleSelection = false }
}

enum class CameraRatio(val value: Int) {
    Square(1),
    Full(2)
}