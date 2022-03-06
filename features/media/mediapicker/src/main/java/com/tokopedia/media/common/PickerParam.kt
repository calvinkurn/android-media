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
    private var maxVideoSize: Int = 250, // mb
    private var minVideoDuration: Int = 3000, // equals 3 sec
    private var maxVideoDuration: Int = 30000, // equals 30 sec
    private var maxImageSize: Int = 10, // mb
    private var minImageResolution: Int = 300, // px
    private var maxImageResolution: Int = 20000, // px
    private var isIncludeVideo: Boolean = true,
    private var isOnlyVideo: Boolean = false,
    private var isIncludeAnimation: Boolean = false,
    private var isMultipleSelection: Boolean = true,
    private var excludedMedias: List<File> = emptyList(),
    private var pageType: Int = PickerPageType.COMMON,
    private var cameraRatio: CameraRatio = CameraRatio.Full
) : Parcelable {

    fun isMultipleSelectionType() = isMultipleSelection

    fun isIncludeVideoFile() = isIncludeVideo

    fun isOnlyVideoFile() = isOnlyVideo

    fun isIncludeGifFile() = isIncludeAnimation

    fun excludeMedias() = excludedMedias

    fun maxVideoCount() = maxVideo

    fun limitOfMedia() = maxMediaSize + maxVideo

    fun maxVideoDuration() = maxVideoDuration

    fun minVideoDuration() = minVideoDuration

    fun isCommonPageType() = pageType == PickerPageType.COMMON

    fun ratioIsSquare() = cameraRatio == CameraRatio.Square

}

enum class CameraRatio(val value: Int) {
    Square(1),
    Full(2)
}