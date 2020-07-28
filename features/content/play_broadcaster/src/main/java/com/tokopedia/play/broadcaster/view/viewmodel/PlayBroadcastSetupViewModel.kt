package com.tokopedia.play.broadcaster.view.viewmodel

import androidx.lifecycle.ViewModel
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastSetupDataStore
import javax.inject.Inject

/**
 * Created by jegul on 19/06/20
 */
class PlayBroadcastSetupViewModel @Inject constructor(
        val setupDataStore: PlayBroadcastSetupDataStore
) : ViewModel()