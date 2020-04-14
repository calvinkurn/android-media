package com.tokopedia.play.view.layout.interaction

import com.tokopedia.play.view.layout.PlayLayoutManager

/**
 * Created by jegul on 14/04/20
 */
interface PlayInteractionLayoutManager : PlayLayoutManager {

    /**
     * @return systemUiVisibility in Int
     */
    fun onEnterImmersive(): Int

    /**
     * @return systemUiVisibility in Int
     */
    fun onExitImmersive(): Int
}