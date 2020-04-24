package com.tokopedia.browse.common.di

import com.tokopedia.browse.common.data.DigitalBrowseRepositoryImpl
import com.tokopedia.browse.common.domain.DigitalBrowseRepository
import dagger.Module
import dagger.Provides

/**
 * @author by furqan on 30/08/18.
 */

@Module
class DigitalBrowseModule {

    @DigitalBrowseScope
    @Provides
    fun provideDigitalBrowseRepository(): DigitalBrowseRepository {
        return DigitalBrowseRepositoryImpl()
    }
}
