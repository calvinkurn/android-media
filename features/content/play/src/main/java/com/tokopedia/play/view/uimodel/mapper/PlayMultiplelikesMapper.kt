package com.tokopedia.play.view.uimodel.mapper

import android.graphics.Color
import com.tokopedia.play.data.multiplelikes.MultipleLikeConfig
import com.tokopedia.play.di.PlayScope
import com.tokopedia.play.view.uimodel.recom.PlayLikeBubbleConfig
import com.tokopedia.play.view.uimodel.recom.PlayMultipleLikesConfig
import javax.inject.Inject

/**
 * Created by jegul on 15/09/21
 */
@PlayScope
class PlayMultiplelikesMapper @Inject constructor() {

    fun mapMultipleLikeConfig(configs: List<MultipleLikeConfig>): PlayMultipleLikesConfig {
        val bubbleMap = mutableMapOf<String, List<Int>>()
        configs.forEach { config ->
            val color = try {
                Color.parseColor(config.bgColor)
            } catch (e: IllegalArgumentException) { null } ?: return@forEach

            val savedColorList = bubbleMap[config.icon] ?: emptyList()
            bubbleMap[config.icon] = savedColorList + color
        }
//        val bubbleMap = mapOf(
//            "https://images.tokopedia.net/img/playassets/join.png" to listOf("#F94D63", "#00AA5B").map {
//                Color.parseColor(it)
//            }
//        )
        val bubbleConfig = PlayLikeBubbleConfig(bubbleMap = bubbleMap)
        return PlayMultipleLikesConfig(
            self = bubbleConfig,
            other = bubbleConfig,
        )
    }
}