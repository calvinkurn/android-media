package com.tokopedia.picker.common

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.tokopedia.picker.common.types.ModeType
import com.tokopedia.picker.common.types.PageType
import kotlinx.parcelize.Parcelize
import java.io.File

@Parcelize
data class PickerParam(
    @SerializedName("pageType") private var pageType: Int = PageType.COMMON,
    @SerializedName("modeType") private var modeType: Int = ModeType.COMMON,
    @SerializedName("isMultipleSelection") private var isMultipleSelection: Boolean = true,
    @SerializedName("cameraRatio") private var cameraRatio: CameraRatio = CameraRatio.Full,
    @SerializedName("maxMediaItem") private var maxMediaItem: Int = 4,
    @SerializedName("maxVideoItem") private var maxVideoItem: Int = 2,
    @SerializedName("maxVideoFileSize") private var maxVideoFileSize: Long = 250_000_000, // 250 mb
    @SerializedName("minVideoDuration") private var minVideoDuration: Int = 3000, // equals 3 sec
    @SerializedName("maxVideoDuration") private var maxVideoDuration: Int = 30_000, // equals 30 sec
    @SerializedName("maxImageFileSize") private var maxImageFileSize: Long = 10_000_000, // 10 mb
    @SerializedName("minImageResolution") private var minImageResolution: Int = 300, // px
    @SerializedName("maxImageResolution") private var maxImageResolution: Int = 20000, // px
    @SerializedName("minStorageThreshold") private var minStorageThreshold: Long = 150_000_000, // 150 mb
    @SerializedName("isIncludeAnimation") private var isIncludeAnimation: Boolean = false,
    @SerializedName("withEditor") private var withEditor: Boolean = false,
    @SerializedName("pageSource") private var pageSource: PageSource = PageSource.Unknown,
    @SerializedName("includeMedias") private var includeMedias: List<String> = emptyList(),
    @SerializedName("excludedMedias") private var excludedMedias: List<File> = emptyList(),
    @SerializedName("previewActionText") private var previewActionText: String = "",
    @SerializedName("editorParam")private var editorParam: EditorParam? = null
) : Parcelable {

    // getter
    fun pageType() = pageType
    fun modeType() = modeType
    fun pageSourceName() = pageSource.value
    fun isImageModeOnly() = modeType == ModeType.IMAGE_ONLY
    fun isVideoModeOnly() = modeType == ModeType.VIDEO_ONLY
    fun isCommonPageType() = pageType == PageType.COMMON
    fun isGalleryPageType() = pageType == PageType.GALLERY
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
    fun isEditorEnabled() = withEditor
    fun getEditorParam() = editorParam
    fun previewActionText(): String {
        return if (previewActionText.length > CUSTOM_ACTION_TEXT_LIMIT) {
            previewActionText.substring(IntRange(
                SUBSTRING_START_INDEX,
                SUBSTRING_END_INDEX
            )) + SUBSTRING_ELLIPSIZE_APPEND
        } else {
            previewActionText
        }
    }

    // setter
    fun pageSource(value: PageSource) = apply { pageSource = value }
    fun cameraRatio(value: CameraRatio) = apply { cameraRatio = value }
    fun maxMediaItem(value: Int) = apply { maxMediaItem = value }
    fun maxVideoItem(value: Int) = apply { maxVideoItem = value }
    fun maxVideoFileSize(value: Long) = apply { maxVideoFileSize = value }
    fun minVideoDuration(value: Int) = apply { minVideoDuration = value }
    fun maxVideoDuration(value: Int) = apply { maxVideoDuration = value }
    fun maxImageFileSize(value: Long) = apply { maxImageFileSize = value }
    fun minImageResolution(value: Int) = apply { minImageResolution = value }
    fun maxImageResolution(value: Int) = apply { maxImageResolution = value }
    fun minStorageThreshold(value: Long) = apply { minStorageThreshold = value }
    fun withEditor(param: EditorParam.() -> Unit = {}) = apply {
        withEditor = true
        editorParam = EditorParam().apply(param)
    }
    fun withoutEditor() = apply { withEditor = false }
    fun includeAnimationGif(value: Boolean) = apply { isIncludeAnimation = value }
    fun includeMedias(value: List<String>) = apply { includeMedias = value }
    fun excludeMedias(value: List<File>) = apply { excludedMedias = value }
    fun pageType(@PageType value: Int) = apply { pageType = value }
    fun modeType(@ModeType value: Int) = apply { modeType = value }
    fun multipleSelectionMode() = apply { isMultipleSelection = true }
    fun singleSelectionMode() = apply { isMultipleSelection = false }
    fun previewActionText(value: String) = apply { previewActionText = value }

    companion object {
        private const val CUSTOM_ACTION_TEXT_LIMIT = 10
        private const val SUBSTRING_START_INDEX = 0
        private const val SUBSTRING_END_INDEX = 9
        private const val SUBSTRING_ELLIPSIZE_APPEND = "..."
    }
}

enum class CameraRatio(val value: Int) {
    Square(1),
    Full(2)
}
