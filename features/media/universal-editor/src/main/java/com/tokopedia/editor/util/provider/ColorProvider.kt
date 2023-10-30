package com.tokopedia.editor.util.provider

import android.content.Context
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.editor.ui.model.ColorModel
import javax.inject.Inject
import com.tokopedia.editor.R.color as colorR

interface ColorProvider {
    fun getColorMap(): Map<Int, ColorModel>
    fun findColorName(colorInt: Int): String
}

class ColorProviderImpl @Inject constructor(
    @ApplicationContext context: Context
): ColorProvider {

    // key = color Int
    // pair = color name & text alt color for background
    private var colorMap: MutableMap<Int, ColorModel> = mutableMapOf()

    // map color for text & background & color name (TODO, wait for tracker detail)
    private val colorResList = listOf(
        Triple(colorR.dms_universal_editor_nn_0, colorR.dms_universal_editor_nn_900, "white"),
        Triple(colorR.dms_universal_editor_nn_900, colorR.dms_universal_editor_nn_0, "black"),
        Triple(colorR.dms_universal_editor_yn_500, colorR.dms_universal_editor_nn_0, "yellow"),
        Triple(colorR.dms_universal_editor_rn_500, colorR.dms_universal_editor_nn_0, "red"),
        Triple(colorR.dms_universal_editor_pn_500, colorR.dms_universal_editor_nn_0, "purple"),
        Triple(colorR.dms_universal_editor_bn_500, colorR.dms_universal_editor_nn_0, "blue"),
        Triple(colorR.dms_universal_editor_tn_500, colorR.dms_universal_editor_nn_0, "teal"),
        Triple(colorR.dms_universal_editor_gn_500, colorR.dms_universal_editor_nn_0, "green")
    )

    init {
        colorResList.forEachIndexed { index, (mainColor, altColor, colorName) ->
            val textColor = ContextCompat.getColor(context, mainColor)
            val textColorOnBackground = ContextCompat.getColor(context, altColor)

            colorMap[textColor] = ColorModel(
                colorInt = textColor,
                colorName = colorName,
                textColorAlternate = textColorOnBackground
            )
        }
    }

    override fun getColorMap(): Map<Int, ColorModel> {
        return colorMap
    }

    override fun findColorName(colorInt: Int): String {
        return colorMap[colorInt]?.colorName ?: ""
    }
}
