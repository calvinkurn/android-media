package com.tokopedia.picker.common

import android.annotation.SuppressLint
import android.os.Parcelable
import com.tokopedia.picker.common.types.EditorToolType
import kotlinx.parcelize.Parcelize

@SuppressLint("ParamFieldAnnotation")
@Parcelize
data class EditorParam(
    var ratioList: ArrayList<ImageRatioType> = createDefaultRatioList(),
    var editorToolsList: ArrayList<Int> = createDefaultEditorTools(),
    var autoCropRatio: ImageRatioType? = null
) : Parcelable {
    fun withWatermark() = editorToolsList.add(EditorToolType.WATERMARK)
    fun withRemoveBackground() = editorToolsList.add(EditorToolType.REMOVE_BACKGROUND)
}

fun createDefaultEditorTools() = arrayListOf(
    EditorToolType.BRIGHTNESS,
    EditorToolType.CONTRAST,
    EditorToolType.CROP,
    EditorToolType.ROTATE
)

fun createDefaultRatioList() = arrayListOf(
    ImageRatioType.RATIO_1_1,
    ImageRatioType.RATIO_2_1,
    ImageRatioType.RATIO_3_4
)

@Parcelize
enum class ImageRatioType(val id: Int, private val ratio: Pair<Int, Int>) : Parcelable {
    RATIO_1_1(1, 1 to 1),
    RATIO_3_4(2, 3 to 4),
    RATIO_2_1(3, 2 to 1);

    fun getRatioX() = ratio.first
    fun getRatioY() = ratio.second
    fun getRatio() = ratio.first.toFloat() / ratio.second
}