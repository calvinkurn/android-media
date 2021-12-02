package com.tokopedia.play.view.uimodel.mapper

import android.graphics.Color
import com.tokopedia.play.data.multiplelikes.MultipleLikeConfig
import com.tokopedia.play.di.PlayScope
import com.tokopedia.play.view.uimodel.recom.PlayLikeBubbleConfig
import javax.inject.Inject

/**
 * Created by jegul on 15/09/21
 */
@PlayScope
class PlayMultipleLikesMapper @Inject constructor() {

    fun mapMultipleLikeConfig(configs: List<MultipleLikeConfig>): PlayLikeBubbleConfig {
        val bubbleMap = mutableMapOf<String, List<Int>>()
        configs.forEach { config ->
            val color = try {
                if (config.bgColor.isNotBlank()) {
                    Color.parseColor(config.bgColor)
                } else Color.BLACK
            } catch (e: IllegalArgumentException) { null } ?: return@forEach

            val savedColorList = bubbleMap[config.icon] ?: emptyList()
            bubbleMap[config.icon] = savedColorList + color
        }
        return PlayLikeBubbleConfig(bubbleMap = bubbleMap)
    }
}