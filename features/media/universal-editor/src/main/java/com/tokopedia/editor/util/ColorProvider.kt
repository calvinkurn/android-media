package com.tokopedia.editor.util

import android.content.Context
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.editor.ui.model.ColorModel
import javax.inject.Inject
import com.tokopedia.unifyprinciples.R.color as colorR

interface ColorProvider {
    fun getColorMap(): Map<Int, ColorModel>
    fun findColorName(colorInt: Int): String
}

class ColorProviderImp @Inject constructor(
    @ApplicationContext context: Context
): ColorProvider {

    // key = color Int
    // pair = color name & text alt color for background
    private var colorMap: MutableMap<Int, ColorModel> = mutableMapOf()

    // map color for text & background & color name (TODO, wait for tracker detail)
    private val colorResList = listOf(
        Triple(colorR.Unify_NN0, colorR.Unify_NN900, "white"),
        Triple(colorR.Unify_NN900, colorR.Unify_NN0, "black"),
        Triple(colorR.Unify_YN500, colorR.Unify_NN0, "yellow"),
        Triple(colorR.Unify_RN500, colorR.Unify_NN0, "red"),
        Triple(colorR.Unify_PN500, colorR.Unify_NN0, "purple"),
        Triple(colorR.Unify_BN500, colorR.Unify_NN0, "blue"),
        Triple(colorR.Unify_TN500, colorR.Unify_NN0, "teal"),
        Triple(colorR.Unify_GN500, colorR.Unify_NN0, "green")
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
