package com.tokopedia.play.broadcaster.util.transition

import androidx.transition.Transition

/**
 * Created by jegul on 04/06/20
 */
open class SimpleTransitionListener : Transition.TransitionListener {

    override fun onTransitionEnd(transition: Transition) {}

    override fun onTransitionResume(transition: Transition) {}

    override fun onTransitionPause(transition: Transition) {}

    override fun onTransitionCancel(transition: Transition) {}

    override fun onTransitionStart(transition: Transition) {}
}