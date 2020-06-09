package com.tokopedia.play.broadcaster.view.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.play.broadcaster.dispatcher.PlayBroadcastDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

/**
 * @author by furqan on 09/06/2020
 */
class PlayBroadcastCoverTitleViewModel @Inject constructor(
        @Named(PlayBroadcastDispatcher.MAIN) dispatcher: CoroutineDispatcher,
        @Named(PlayBroadcastDispatcher.IO) private val ioDispatcher: CoroutineDispatcher)
    : BaseViewModel(dispatcher) {


}