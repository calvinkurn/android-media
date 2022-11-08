package com.tokopedia.play.broadcaster.shorts.di

import com.tokopedia.play.broadcaster.shorts.data.PlayShortsRepositoryImpl
import com.tokopedia.play.broadcaster.shorts.domain.PlayShortsRepository
import com.tokopedia.play.broadcaster.shorts.ui.mapper.PlayShortsMapper
import com.tokopedia.play.broadcaster.shorts.ui.mapper.PlayShortsUiMapper
import com.tokopedia.play.broadcaster.util.preference.HydraSharedPreferences
import com.tokopedia.play.broadcaster.util.preference.PermissionSharedPreferences
import dagger.Binds
import dagger.Module

/**
 * Created By : Jonathan Darwin on November 08, 2022
 */
@Module
abstract class PlayShortsBindModule {

    @Binds
    @PlayShortsScope
    abstract fun bindPlayShortsRepository(repository: PlayShortsRepositoryImpl): PlayShortsRepository

    @Binds
    @PlayShortsScope
    abstract fun bindPlayShortsUiMapper(mapper: PlayShortsUiMapper): PlayShortsMapper

    @Binds
    @PlayShortsScope
    abstract fun bindPermissionSharedPrefs(sharedPref: HydraSharedPreferences): PermissionSharedPreferences
}
