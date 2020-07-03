package com.tokopedia.play.broadcaster.view.contract

import android.view.View
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastSetupDataStore

/**
 * Created by jegul on 23/06/20
 */
interface ProductSetupListener {

    suspend fun onProductSetupFinished(sharedElements: List<View>, dataStore: PlayBroadcastSetupDataStore) : Throwable?
}