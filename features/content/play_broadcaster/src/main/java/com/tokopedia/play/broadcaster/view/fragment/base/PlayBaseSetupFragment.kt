package com.tokopedia.play.broadcaster.view.fragment.base

/**
 * Created by jegul on 26/05/20
 */
abstract class PlayBaseSetupFragment : PlayBaseBroadcastFragment() {

    abstract fun getTitle(): String

    abstract fun isRootFragment(): Boolean
}