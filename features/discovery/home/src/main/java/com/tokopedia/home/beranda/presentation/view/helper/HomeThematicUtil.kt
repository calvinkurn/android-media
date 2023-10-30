package com.tokopedia.home.beranda.presentation.view.helper

import androidx.annotation.ColorRes
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeThematicModel
import javax.inject.Inject
import com.tokopedia.unifyprinciples.R as unifyprinciplesR
import com.tokopedia.home.R as homeR

/**
 * Created by frenzel
 */
class HomeThematicUtil @Inject constructor() {
    companion object {
        const val COLOR_DARK = "dark"
        const val COLOR_LIGHT = "light"
        const val COLOR_DEFAULT = "default"
        const val PAYLOAD_APPLY_THEMATIC_COLOR = "payloadApplyThematicColor"
    }

    var thematicModel: HomeThematicModel = HomeThematicModel()
    var isBackgroundLoaded: Boolean = false

    private fun getColorMode() = if(thematicModel.isShown && isBackgroundLoaded) thematicModel.colorMode else COLOR_DEFAULT

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
        unifyprinciplesR.color.Unify_NN100 to HomeThematicColorToken(
            homeR.color.home_dms_Unify_NN100_force_light,
            homeR.color.home_dms_Unify_NN100_force_dark,
        ),
        unifyprinciplesR.color.Unify_NN50 to HomeThematicColorToken(
            homeR.color.home_dms_Unify_NN50_force_light,
            homeR.color.home_dms_Unify_NN50_force_dark,
        ),
    )

    @ColorRes
    internal fun asThematicColor(actualColorToken: Int): Int {
        val colorToken = thematicColorMap[actualColorToken] ?: return actualColorToken
        return when(getColorMode()) {
            COLOR_LIGHT -> colorToken.lightColorToken
            COLOR_DARK -> colorToken.darkColorToken
            else -> actualColorToken
        }
    }

    fun isDarkMode() = getColorMode() == COLOR_DARK

    fun isLightMode() = getColorMode() == COLOR_LIGHT

    fun isDefault() = getColorMode() != COLOR_DARK && getColorMode() != COLOR_LIGHT
}

/**
 * Created by frenzel
 */
internal data class HomeThematicColorToken(
    @ColorRes val lightColorToken: Int,
    @ColorRes val darkColorToken: Int,
)
