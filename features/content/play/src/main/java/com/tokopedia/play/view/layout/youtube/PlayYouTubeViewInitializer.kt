package com.tokopedia.play.view.layout.youtube

import android.view.ViewGroup
import androidx.annotation.IdRes

/**
 * Created by jegul on 28/04/20
 */
interface PlayYouTubeViewInitializer {

    @IdRes fun onInitYouTube(container: ViewGroup): Int
}