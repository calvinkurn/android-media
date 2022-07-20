package com.tokopedia.play.broadcaster.data.datastore

import com.tokopedia.play.broadcaster.data.type.OverwriteMode

interface PlayBroadcastSetupDataStore : CoverDataStore, TitleDataStore, TagsDataStore, BroadcastScheduleDataStore, InteractiveDataStore {

    fun overwrite(dataStore: PlayBroadcastSetupDataStore, modeExclusion: List<OverwriteMode> = emptyList())

    fun getCoverDataStore(): CoverDataStore

    fun getTitleDataStore(): TitleDataStore

    fun getBroadcastScheduleDataStore(): BroadcastScheduleDataStore

    fun getInteractiveDataStore(): InteractiveDataStore
}