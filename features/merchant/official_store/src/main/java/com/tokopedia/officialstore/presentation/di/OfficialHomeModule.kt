package com.tokopedia.officialstore.presentation.di

import com.tokopedia.officialstore.presentation.OfficialHomePresenter
import dagger.Module
import dagger.Provides

@Module
@OfficialHomeScope
class OfficialHomeModule {

    @OfficialHomeScope
    @Provides
    fun provideOfficialHomePresenter(): OfficialHomePresenter {
        return OfficialHomePresenter()
    }
}