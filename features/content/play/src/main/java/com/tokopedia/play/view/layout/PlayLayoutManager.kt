package com.tokopedia.play.view.layout

import android.view.View
import androidx.core.view.WindowInsetsCompat
import com.tokopedia.play.view.type.ScreenOrientation
import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play.view.uimodel.VideoPlayerUiModel

/**
 * Created by jegul on 13/04/20
 */
interface PlayLayoutManager {

    fun layoutView(view: View)

    fun setupInsets(view: View, insets: WindowInsetsCompat)

    fun onOrientationChanged(view: View, orientation: ScreenOrientation, videoOrientation: VideoOrientation, videoPlayer: VideoPlayerUiModel)

    fun onDestroy()
}