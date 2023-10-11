package com.tokopedia.home.beranda.presentation.view.helper

import androidx.annotation.ColorRes
import com.tokopedia.unifyprinciples.R as unifyprinciplesR
import com.tokopedia.home.R as homeR

/**
 * Created by frenzel
 */
internal object HomeThematicUtil {
    const val COLOR_DARK = "dark"
    const val COLOR_LIGHT = "light"
    const val COLOR_DEFAULT = "default"

    var colorMode: String = COLOR_DEFAULT

    private val thematicColorMap = mapOf<@receiver:ColorRes Int, HomeThematicColorToken>(
        unifyprinciplesR.color.Unify_NN1000 to HomeThematicColorToken(
            unifyprinciplesR.color.Unify_Static_Black,
            unifyprinciplesR.color.Unify_Static_White,
        ),
        unifyprinciplesR.color.Unify_GN500 to HomeThematicColorToken(
            homeR.color.home_dms_Unify_GN500_force_light,
            homeR.color.home_dms_Unify_GN500_force_dark,
        ),
        unifyprinciplesR.color.Unify_NN600 to HomeThematicColorToken(
            homeR.color.home_dms_Unify_NN600_force_light,
            homeR.color.home_dms_Unify_NN600_force_dark,
        ),
        unifyprinciplesR.color.Unify_NN950 to HomeThematicColorToken(
            homeR.color.home_dms_Unify_NN950_force_light,
            homeR.color.home_dms_Unify_NN950_force_dark,
        ),
        unifyprinciplesR.color.Unify_NN950 to HomeThematicColorToken(
            homeR.color.home_dms_Unify_NN950_force_light,
            homeR.color.home_dms_Unify_NN950_force_dark,
        ),
    )

    @ColorRes
    internal fun @receiver:ColorRes Int.asThematicColor(colorMode: String): Int {
        val colorToken = thematicColorMap[this] ?: return this
        return when(colorMode) {
            COLOR_LIGHT -> colorToken.lightColorToken
            COLOR_DARK -> colorToken.darkColorToken
            else -> this
        }
    }

    fun isDarkMode() = colorMode == COLOR_DARK

    fun isLightMode() = colorMode == COLOR_LIGHT

    fun isDefault() = colorMode != COLOR_DARK || colorMode != COLOR_LIGHT
}

/**
 * Created by frenzel
 */
internal data class HomeThematicColorToken(
    @ColorRes val lightColorToken: Int,
    @ColorRes val darkColorToken: Int,
)
