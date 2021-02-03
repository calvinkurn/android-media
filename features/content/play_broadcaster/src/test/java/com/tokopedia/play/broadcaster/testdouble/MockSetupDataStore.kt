package com.tokopedia.play.broadcaster.testdouble

import com.tokopedia.play.broadcaster.data.datastore.BroadcastScheduleDataStore
import com.tokopedia.play.broadcaster.data.datastore.CoverDataStore
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastSetupDataStore
import com.tokopedia.play.broadcaster.data.datastore.ProductDataStore
import com.tokopedia.play.broadcaster.data.type.OverwriteMode

/**
 * Created by jegul on 25/09/20
 */
class MockSetupDataStore(
        private val mProductDataStore: ProductDataStore,
        private val mCoverDataStore: CoverDataStore,
        private val mScheduleDataStore: BroadcastScheduleDataStore
) : PlayBroadcastSetupDataStore, ProductDataStore by mProductDataStore, CoverDataStore by mCoverDataStore, BroadcastScheduleDataStore by mScheduleDataStore {

    var isOverwritten: Boolean = false

    override fun overwrite(dataStore: PlayBroadcastSetupDataStore, modeExclusion: List<OverwriteMode>) {
        isOverwritten = true
    }

    override fun getProductDataStore(): ProductDataStore {
        return mProductDataStore
    }

    override fun getCoverDataStore(): CoverDataStore {
        return mCoverDataStore
    }

    override fun getBroadcastScheduleDataStore(): BroadcastScheduleDataStore {
        return mScheduleDataStore
    }
}