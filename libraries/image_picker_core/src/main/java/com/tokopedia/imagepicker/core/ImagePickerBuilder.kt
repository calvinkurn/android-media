package com.tokopedia.imagepicker.core

import android.content.Context
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ImagePickerBuilder(
        val title: String,
        val imagePickerTab: Array<ImagePickerTab>,
        val galleryType: GalleryType,
        val minResolution: Int,
        val maxFileSizeInKB: Long,
        val imageRatioType: ImageRatioType,
        val moveImageResultToLocal: Boolean = false,
        val imagePickerEditorBuilder: ImagePickerEditorBuilder? = null,
        val imagePickerMultipleSelectionBuilder: ImagePickerMultipleSelectionBuilder? = null
) : Parcelable {
    fun supportMultipleSelection() = imagePickerMultipleSelectionBuilder!= null
    fun getBelowMinResolutionErrorMessage(): String? {
        return imagePickerEditorBuilder?.belowMinResolutionErrorMessage
    }

    fun getImageTooLargeErrorMessage(): String? {
        return imagePickerEditorBuilder?.imageTooLargeErrorMessage
    }
    companion object {
        fun getDefaultBuilder(context: Context): ImagePickerBuilder {
            return ImagePickerBuilder(
                    context.getString(R.string.choose_image),
                    arrayOf(ImagePickerTab.TYPE_GALLERY, ImagePickerTab.TYPE_CAMERA),
                    GalleryType.IMAGE_ONLY,
                    DEFAULT_MIN_RESOLUTION,
                    DEFAULT_MAX_IMAGE_SIZE_IN_KB,
                    ImageRatioType.RATIO_1_1,
                    true,
                    ImagePickerEditorBuilder.getDefaultBuilder(),
                    ImagePickerMultipleSelectionBuilder(),
            )
        }
    }
}

@Parcelize
data class ImagePickerEditorBuilder(
        val imageEditActionType: Array<ImageEditActionType>,
        val circlePreview: Boolean = false,
        val imageRatioTypeList: ArrayList<ImageRatioType>? = null,
        val belowMinResolutionErrorMessage: String = "",
        val imageTooLargeErrorMessage: String = "",
        val recheckSizeAfterResize: Boolean = false,
) : Parcelable {
    companion object {
        fun getDefaultBuilder(): ImagePickerEditorBuilder {
            return ImagePickerEditorBuilder(
                    arrayOf(ImageEditActionType.ACTION_BRIGHTNESS,
                            ImageEditActionType.ACTION_CONTRAST,
                            ImageEditActionType.ACTION_CROP,
                            ImageEditActionType.ACTION_ROTATE),
                    false,
                    null
            )
        }
    }
}

@Parcelize
data class ImagePickerMultipleSelectionBuilder(
        val primaryImageStringRes: Int = 0,
        val maximumNoPick: Int = DEFAULT_MAXIMUM_NO_PICK,
        val canReorder: Boolean = false,
        val initialSelectedImagePathList: ArrayList<String> = arrayListOf(),
        val placeholderImagePathResList: ArrayList<Int> = arrayListOf(),
        val previewExtension: PreviewExtension? = null
) : Parcelable

@Parcelize
data class PreviewExtension(
        val hideThumbnailListPreview: Boolean = false,
        val showCounterAtSelectedImage: Boolean = false,
        val showBiggerPreviewWhenThumbnailHidden: Boolean = true,
        val appendInitialSelectedImageInGallery: Boolean = false
) : Parcelable

enum class ImagePickerTab(val value: Int) {
    TYPE_GALLERY(1),
    TYPE_CAMERA(2),
    TYPE_INSTAGRAM(3),
    TYPE_RECORDER(4);
    companion object {
        private val map = values().associateBy(ImagePickerTab::value)
        @JvmStatic
        fun fromInt(type: Int) = map[type]
    }
}

enum class GalleryType(val value: Int) {
    ALL(1),
    IMAGE_ONLY(2),
    VIDEO_ONLY(3),
    GIF_ONLY(4);
    companion object {
        private val map = values().associateBy(GalleryType::value)
        @JvmStatic
        fun fromInt(type: Int) = map[type]
    }
}

enum class ImageRatioType(val ratio: Pair<Int, Int>) {
    ORIGINAL(-1 to -1),
    RATIO_1_1(1 to 1),
    RATIO_3_4(3 to 4),
    RATIO_4_3(4 to 3),
    RATIO_16_9(16 to 9),
    RATIO_9_16(9 to 16);

    fun getRatioX() = ratio.first
    fun getRatioY() = ratio.second

    companion object {
        private val map = values().associateBy(ImageRatioType::ratio)
        @JvmStatic
        fun fromInt(type: Pair<Int, Int>) = map[type]
    }
}

enum class ImageEditActionType(val action: Int) {
    ACTION_CROP(1),
    ACTION_ROTATE(2),
    ACTION_WATERMARK(3),
    ACTION_CROP_ROTATE(4),
    ACTION_BRIGHTNESS(5),
    ACTION_CONTRAST(6);
    companion object {
        private val map = values().associateBy(ImageEditActionType::action)
        @JvmStatic
        fun fromInt(type: Int) = map[type]
    }
}