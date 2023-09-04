package com.tokopedia.picker.common

import android.annotation.SuppressLint
import android.os.Parcelable
import com.tokopedia.picker.common.types.EditorToolType
import kotlinx.parcelize.Parcelize

@SuppressLint("ParamFieldAnnotation")
@Parcelize
data class EditorParam(
    private var ratioList: ArrayList<ImageRatioType> = createDefaultRatioList(),
    private var editorToolsList: ArrayList<Int> = createDefaultEditorTools(),
    private var autoCropRatio: ImageRatioType? = null
) : Parcelable {
    private var addLogoIndex = 0

    // getter
    fun ratioList() = ratioList
    fun editorToolsList() = editorToolsList
    fun autoCropRatio() = autoCropRatio

    // setter
    fun withWatermark() = editorToolsList.add(EditorToolType.WATERMARK)

    fun withRemoveBackground() {
        editorToolsList.add(0, EditorToolType.REMOVE_BACKGROUND)
        addLogoIndex = 1
    }

    fun withAddLogo() = editorToolsList.add(addLogoIndex, EditorToolType.ADD_LOGO)

    fun withAddText() = editorToolsList.add(0, EditorToolType.ADD_TEXT)

    fun autoCrop1to1() = apply {
        setAutoCropRatio(ImageRatioType.RATIO_1_1)
    }

    fun autoCrop3to4() = apply {
        setAutoCropRatio(ImageRatioType.RATIO_3_4)
    }

    fun autoCrop2to1() = apply {
        setAutoCropRatio(ImageRatioType.RATIO_2_1)
    }

    fun resetRatioList() = ratioList.clear()
    fun ratioListAdd1to1() = ratioList.add(ImageRatioType.RATIO_1_1)
    fun ratioListAdd3to4() = ratioList.add(ImageRatioType.RATIO_3_4)
    fun ratioListAdd2to1() = ratioList.add(ImageRatioType.RATIO_2_1)

    private fun setAutoCropRatio(ratio: ImageRatioType) {
        autoCropRatio = ratio
    }
}

fun createDefaultEditorTools() = arrayListOf(
    EditorToolType.BRIGHTNESS,
    EditorToolType.CONTRAST,
    EditorToolType.CROP,
    EditorToolType.ROTATE
)

fun createDefaultRatioList() = arrayListOf(
    ImageRatioType.RATIO_1_1,
    ImageRatioType.RATIO_3_4,
    ImageRatioType.RATIO_2_1
)

@Parcelize
enum class ImageRatioType(val id: Int, val ratio: Pair<Int, Int>) : Parcelable {
    RATIO_1_1(1, 1 to 1),
    RATIO_3_4(2, 3 to 4),
    RATIO_2_1(3, 2 to 1);

    fun getRatioX() = ratio.first
    fun getRatioY() = ratio.second
    fun getRatio() = ratio.first.toFloat() / ratio.second
}
