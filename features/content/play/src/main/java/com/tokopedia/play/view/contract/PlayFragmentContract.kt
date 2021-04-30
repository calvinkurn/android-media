package com.tokopedia.play.view.contract

import com.tokopedia.play.view.type.ScreenOrientation
import com.tokopedia.play.view.type.PiPMode
import com.tokopedia.play.view.type.PiPState

/**
 * Created by jegul on 29/04/20
 */
interface PlayFragmentContract {

    fun onInterceptOrientationChangedEvent(newOrientation: ScreenOrientation): Boolean

    fun onEnterPiPState(pipState: PiPState) {}
}