package com.tokopedia.imagepicker.common

import android.content.Context
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ImagePickerBuilder(
        var title: String,
        var imagePickerTab: Array<ImagePickerTab>,
        var galleryType: GalleryType,
        var imageRatioType: ImageRatioType = ImageRatioType.ORIGINAL,
        var minResolution: Int = DEFAULT_MIN_RESOLUTION,
        var maxFileSizeInKB: Int = DEFAULT_MAX_IMAGE_SIZE_IN_KB,
        var moveImageResultToLocal: Boolean = true,
        var imagePickerEditorBuilder: ImagePickerEditorBuilder? = null,
        var imagePickerMultipleSelectionBuilder: ImagePickerMultipleSelectionBuilder? = null
) : Parcelable {
    fun supportMultipleSelection() = imagePickerMultipleSelectionBuilder != null
    fun getBelowMinResolutionErrorMessage() = imagePickerEditorBuilder?.belowMinResolutionErrorMessage ?: ""

    fun getImageTooLargeErrorMessage() = imagePickerEditorBuilder?.imageTooLargeErrorMessage ?: ""
    fun getInitialSelectedImagePathList(): ArrayList<String> {
        return imagePickerMultipleSelectionBuilder?.initialSelectedImagePathList ?: arrayListOf()
    }

    fun isContinueToEditAfterPick() = imagePickerEditorBuilder != null
    fun getMaximumNoPick() = imagePickerMultipleSelectionBuilder?.maximumNoPick ?: 1
    fun getCameraIndex() = imagePickerTab.indexOf(ImagePickerTab.TYPE_CAMERA)
    fun getGalleryIndex() = imagePickerTab.indexOf(ImagePickerTab.TYPE_GALLERY)
    fun getRecorderIndex() = imagePickerTab.indexOf(ImagePickerTab.TYPE_RECORDER)
    fun getRatioX() = imageRatioType.getRatioX()
    fun getRatioY() = imageRatioType.getRatioY()
    fun isRecheckSizeAfterResize() = imagePickerEditorBuilder?.recheckSizeAfterResize ?: false
    fun getImageEditActionType(): Array<ImageEditActionType> = imagePickerEditorBuilder?.imageEditActionType
            ?: arrayOf()

    fun isCirclePreview() = imagePickerEditorBuilder?.circlePreview ?: false
    fun getRatioOptionList() = imagePickerEditorBuilder?.imageRatioTypeList ?: arrayListOf()
    fun getConvertToWebp() = imagePickerEditorBuilder?.convertToWebp ?: false

    fun withSimpleEditor():ImagePickerBuilder {
        imagePickerEditorBuilder = ImagePickerEditorBuilder.getSimpleEditBuilder()
        return this
    }

    fun withSimpleMultipleSelection(initialImagePathList: ArrayList<String> = arrayListOf(),
                                    maxPick:Int = DEFAULT_MAXIMUM_NO_PICK)
            :ImagePickerBuilder {
        imagePickerMultipleSelectionBuilder = ImagePickerMultipleSelectionBuilder(
                initialSelectedImagePathList = initialImagePathList,
                maximumNoPick = maxPick)
        return this
    }

    companion object {
        @JvmStatic
        fun getSquareImageBuilder(context: Context): ImagePickerBuilder {
            return ImagePickerBuilder(
                    title = context.getString(R.string.choose_image),
                    imagePickerTab = arrayOf(ImagePickerTab.TYPE_GALLERY, ImagePickerTab.TYPE_CAMERA),
                    galleryType = GalleryType.IMAGE_ONLY,
                    imageRatioType = ImageRatioType.RATIO_1_1
            )
        }

        @JvmStatic
        fun getOriginalImageBuilder(context: Context): ImagePickerBuilder {
            return ImagePickerBuilder(
                    title = context.getString(R.string.choose_image),
                    imagePickerTab = arrayOf(ImagePickerTab.TYPE_GALLERY, ImagePickerTab.TYPE_CAMERA),
                    galleryType = GalleryType.IMAGE_ONLY,
                    imageRatioType = ImageRatioType.ORIGINAL
            )
        }
    }
}

@Parcelize
data class ImagePickerEditorBuilder(
        var imageEditActionType: Array<ImageEditActionType>,
        var circlePreview: Boolean = false,
        var imageRatioTypeList: ArrayList<ImageRatioType>? = null,
        var belowMinResolutionErrorMessage: String = "",
        var imageTooLargeErrorMessage: String = "",
        var recheckSizeAfterResize: Boolean = false,
        var convertToWebp: Boolean = false
) : Parcelable {
    companion object {
        fun getSimpleEditBuilder(): ImagePickerEditorBuilder {
            return ImagePickerEditorBuilder(
                    getDefaultEditor(),
                    false,
                    null
            )
        }

        fun getDefaultEditor() = arrayOf(ImageEditActionType.ACTION_BRIGHTNESS,
                ImageEditActionType.ACTION_CONTRAST,
                ImageEditActionType.ACTION_CROP,
                ImageEditActionType.ACTION_ROTATE)
    }
}

@Parcelize
data class ImagePickerMultipleSelectionBuilder(
        var usePrimaryImageString: Boolean = false,
        var maximumNoPick: Int = DEFAULT_MAXIMUM_NO_PICK,
        var canReorder: Boolean = false,
        var initialSelectedImagePathList: ArrayList<String> = arrayListOf(),
        var placeholderImagePathResList: ArrayList<Int> = arrayListOf(),
        var previewExtension: PreviewExtension? = null
) : Parcelable

@Parcelize
data class PreviewExtension(
        val hideThumbnailListPreview: Boolean = false,
        val showCounterAtSelectedImage: Boolean = false,
        val showBiggerPreviewWhenThumbnailHidden: Boolean = true,
        val appendInitialSelectedImageInGallery: Boolean = false
) : Parcelable

@Parcelize
enum class ImagePickerTab(val value: Int) : Parcelable {
    TYPE_GALLERY(1),
    TYPE_CAMERA(2),
    TYPE_RECORDER(3);
}

@Parcelize
enum class GalleryType(val value: Int) : Parcelable {
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

@Parcelize
enum class ImageRatioType(val id: Int, val ratio: Pair<Int, Int>) : Parcelable {
    ORIGINAL(-1, -1 to -1),
    RATIO_1_1(1, 1 to 1),
    RATIO_3_4(2, 3 to 4),
    RATIO_4_3(3, 4 to 3),
    RATIO_16_9(4, 16 to 9),
    RATIO_9_16(5, 9 to 16);

    fun getRatioX() = ratio.first
    fun getRatioY() = ratio.second
}

@Parcelize
enum class ImageEditActionType(val action: Int) : Parcelable {
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