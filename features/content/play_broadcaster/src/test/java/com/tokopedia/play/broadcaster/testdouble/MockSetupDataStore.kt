package com.tokopedia.play.broadcaster.testdouble

import com.tokopedia.play.broadcaster.data.datastore.*
import com.tokopedia.play.broadcaster.data.type.OverwriteMode

/**
 * Created by jegul on 25/09/20
 */
class MockSetupDataStore(
        private val mProductDataStore: ProductDataStore,
        private val mCoverDataStore: CoverDataStore,
        private val mScheduleDataStore: BroadcastScheduleDataStore,
        private val mTitleDataStore: TitleDataStore,
        private val mTagsDataStore: TagsDataStore,
        private val mInteractiveDataStore: InteractiveDataStore
) : PlayBroadcastSetupDataStore, ProductDataStore by mProductDataStore, CoverDataStore by mCoverDataStore, BroadcastScheduleDataStore by mScheduleDataStore, TitleDataStore by mTitleDataStore, TagsDataStore by mTagsDataStore , InteractiveDataStore by mInteractiveDataStore {

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

    override fun getInteractiveDataStore(): InteractiveDataStore {
        return mInteractiveDataStore
    }

    override fun getTitleDataStore(): TitleDataStore {
        return mTitleDataStore
    }
}