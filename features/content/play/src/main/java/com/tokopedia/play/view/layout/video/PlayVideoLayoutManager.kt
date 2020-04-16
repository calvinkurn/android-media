package com.tokopedia.play.view.layout.video

import android.view.View
import com.tokopedia.play.view.layout.PlayLayoutManager

/**
 * Created by jegul on 16/04/20
 */
interface PlayVideoLayoutManager : PlayLayoutManager {

    fun onVideoTopBoundsChanged(view: View, topBounds: Int)
}