@file:Suppress("ResponseFieldAnnotation")
package com.tokopedia.media.common

import android.os.Parcelable
import com.tokopedia.media.common.types.PickerPageType
import kotlinx.parcelize.Parcelize
import java.io.File

@Parcelize
data class PickerParam(
    private var maxMediaSize: Int = 4,
    private var maxVideo: Int = 2,
    private var maxVideoSize: Long = 250_000_000, // 250 mb
    private var minVideoDuration: Int = 3000, // equals 3 sec
    private var maxVideoDuration: Int = 30000, // equals 30 sec
    private var maxImageSize: Long = 1_000_000, // 10 mb
    private var minImageResolution: Int = 300, // px
    private var maxImageResolution: Int = 20000, // px
    private var isIncludeVideo: Boolean = true, // has handled by appLink
    private var isOnlyVideo: Boolean = false, // has handled by appLink
    private var isMultipleSelection: Boolean = true, // has handled by appLink
    private var isIncludeAnimation: Boolean = false,
    private var withEditor: Boolean = false,
    private var includeMedias: List<File> = emptyList(),
    private var excludedMedias: List<File> = emptyList(),
    private var pageType: Int = PickerPageType.COMMON,
    private var cameraRatio: CameraRatio = CameraRatio.Full
) : Parcelable {

    fun isCommonPageType() = pageType == PickerPageType.COMMON

    fun ratioIsSquare() = cameraRatio == CameraRatio.Square

    fun isMultipleSelectionType() = isMultipleSelection

    fun isIncludeVideoFile() = isIncludeVideo

    fun isOnlyVideoFile() = isOnlyVideo

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

    fun pageType() = pageType

}

enum class CameraRatio(val value: Int) {
    Square(1),
    Full(2)
}