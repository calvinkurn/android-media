package com.tokopedia.play.di

import com.tokopedia.play.domain.repository.PlayViewerRepository
import dagger.Module
import dagger.Provides

/**
 * Created by kenny.hadisaputra on 21/03/22
 */
@Module
class PlayTestRepositoryModule(val repo: PlayViewerRepository) {

    @Provides
    fun provideViewerRepository(): PlayViewerRepository {
        return repo
    }
}