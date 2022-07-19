package com.tokopedia.picker.common

import android.annotation.SuppressLint
import android.os.Parcelable
import com.tokopedia.picker.common.types.EditorToolType
import kotlinx.parcelize.Parcelize

@SuppressLint("ParamFieldAnnotation")
@Parcelize
data class EditorParam(
    var ratioDefault: ImageRatioType = ImageRatioType.RATIO_1_1,
    var editorTools: ArrayList<Int> = createDefaultEditorTools(),
    var pickerParam: PickerParam = PickerParam()
) : Parcelable {
    fun ratio(ratio: ImageRatioType) = apply {
        ratioDefault = ratio
    }

    fun withWatermark() = editorTools.add(EditorToolType.WATERMARK)

    fun withRemoveBackground() = editorTools.add(EditorToolType.REMOVE_BACKGROUND)

}

fun createDefaultEditorTools() = arrayListOf(
    EditorToolType.BRIGHTNESS,
    EditorToolType.CONTRAST,
    EditorToolType.CROP,
    EditorToolType.ROTATE,
    EditorToolType.WATERMARK
)

@Parcelize
enum class ImageRatioType(val id: Int, private val ratio: Pair<Int, Int>) : Parcelable {
    RATIO_1_1(1, 1 to 1),
    RATIO_3_4(2, 3 to 4),
    RATIO_4_3(3, 4 to 3),
    RATIO_16_9(4, 16 to 9),
    RATIO_9_16(5, 9 to 16);

    fun getRatioX() = ratio.first
    fun getRatioY() = ratio.second
}