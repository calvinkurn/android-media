package com.tokopedia.play.broadcaster.view.viewmodel

import androidx.lifecycle.ViewModel
import com.tokopedia.play.broadcaster.data.config.HydraConfigStore
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastSetupDataStore
import com.tokopedia.play_common.util.coroutine.CoroutineDispatcherProvider
import javax.inject.Inject


/**
 * Created by mzennis on 01/12/20.
 */
class SetupBroadcastScheduleViewModel @Inject constructor(
        private val hydraConfigStore: HydraConfigStore,
        private val dispatcher: CoroutineDispatcherProvider,
        private val setupDataStore: PlayBroadcastSetupDataStore
) : ViewModel() {


}