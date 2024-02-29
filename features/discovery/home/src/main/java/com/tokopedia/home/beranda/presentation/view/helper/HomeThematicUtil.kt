package com.tokopedia.home.beranda.presentation.view.helper

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.tokopedia.home_component.usecase.thematic.ThematicModel
import com.tokopedia.unifyprinciples.ColorMode
import com.tokopedia.unifyprinciples.modeAware
import javax.inject.Inject
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * Created by frenzel
 */
class HomeThematicUtil @Inject constructor(context: Context) {
    companion object {
        const val PAYLOAD_APPLY_THEMATIC_COLOR = "payloadApplyThematicColor"
    }

    var thematicModel: ThematicModel = ThematicModel()
    var isBackgroundLoaded: Boolean = false

    private val thematicColorMap = mapOf<@receiver:ColorRes Int, HomeThematicColorToken>(
        unifyprinciplesR.color.Unify_NN1000 to HomeThematicColorToken(
            unifyprinciplesR.color.Unify_Static_Black,
            unifyprinciplesR.color.Unify_Static_White
        )
    )

    private fun getColorMode() = if (thematicModel.isShown && isBackgroundLoaded) {
        when (thematicModel.colorMode) {
            ThematicModel.COLOR_LIGHT -> ColorMode.LIGHT_MODE
            ThematicModel.COLOR_DARK -> ColorMode.DARK_MODE
            else -> ColorMode.DEFAULT
        }
    } else {
        ColorMode.DEFAULT
    }

    internal fun getThematicColor(actualColorToken: Int, context: Context): Int {
        val ctx = context.modeAware(getColorMode()) ?: context
        return ContextCompat.getColor(ctx, actualColorToken)
    }

    internal fun getThematicColorToken(actualColorToken: Int): Int {
        val colorToken = thematicColorMap[actualColorToken] ?: return actualColorToken
        return when (getColorMode()) {
            ColorMode.LIGHT_MODE -> colorToken.lightColorToken
            ColorMode.DARK_MODE -> colorToken.darkColorToken
            else -> actualColorToken
        }
    }

    internal fun getThematicDrawable(actualColorToken: Int, context: Context): Drawable? {
        val ctx = context.modeAware(getColorMode()) ?: context
        return ContextCompat.getDrawable(ctx, actualColorToken)
    }

    fun isDarkMode() = getColorMode() == ColorMode.DARK_MODE

    fun isLightMode() = getColorMode() == ColorMode.LIGHT_MODE

    fun isDefault() = getColorMode() == ColorMode.DEFAULT
}

/**
 * Created by frenzel
 */
internal data class HomeThematicColorToken(
    @ColorRes val lightColorToken: Int,
    @ColorRes val darkColorToken: Int
)
