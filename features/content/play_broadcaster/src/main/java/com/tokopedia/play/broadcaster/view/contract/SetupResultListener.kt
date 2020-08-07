package com.tokopedia.play.broadcaster.view.contract

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastSetupDataStore

/**
 * Created by jegul on 22/06/20
 */
interface SetupResultListener {

    fun onSetupCanceled()

    suspend fun onSetupCompletedWithData(bottomSheet: BottomSheetDialogFragment, dataStore: PlayBroadcastSetupDataStore): Throwable?
}