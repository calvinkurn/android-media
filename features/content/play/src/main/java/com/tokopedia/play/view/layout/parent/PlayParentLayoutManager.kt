package com.tokopedia.play.view.layout.parent

import android.view.View
import com.tokopedia.play.view.layout.PlayLayoutManager
import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play_common.state.PlayVideoState

/**
 * Created by jegul on 17/04/20
 */
interface PlayParentLayoutManager : PlayLayoutManager {

    fun onVideoStreamChanged(view: View)

    fun onVideoStateChanged(view: View, videoState: PlayVideoState, videoOrientation: VideoOrientation)

    fun onVideoTopBoundsChanged(view: View, topBounds: Int)

    fun onBottomInsetsShown(view: View, bottomMostBounds: Int)

    fun onBottomInsetsHidden(view: View)
}