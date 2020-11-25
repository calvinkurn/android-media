package com.tokopedia.play.view.contract

import com.tokopedia.play.view.type.ScreenOrientation

/**
 * Created by jegul on 29/04/20
 */
interface PlayOrientationListener {

    fun onOrientationChanged(screenOrientation: ScreenOrientation, isTilting: Boolean)
}