package com.tokopedia.play.broadcaster.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment


/**
 * Created by mzennis on 25/05/20.
 */
class PlayBroadcastInteractionFragment : BaseDaggerFragment() {

    override fun getScreenName(): String = "Play Broadcast Interaction"

    override fun initInjector() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}