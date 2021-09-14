package com.tokopedia.play.view.uimodel.recom.multiplelikes

/**
 * Created by jegul on 08/09/21
 */
/**
 * The key of the map is Icon
 * The value of the map is List of Colors associated to the Icon
 */
data class PlayMultipleLikesConfig(
    val bubbleConfig: Map<String, List<String>> = emptyMap(),
)