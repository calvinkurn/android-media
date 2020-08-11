package com.tokopedia.play.broadcaster.di.provider

import com.tokopedia.play.broadcaster.di.broadcast.PlayBroadcastComponent

/**
 * Created by jegul on 22/06/20
 */
interface PlayBroadcastComponentProvider {

    fun getBroadcastComponent(): PlayBroadcastComponent
}