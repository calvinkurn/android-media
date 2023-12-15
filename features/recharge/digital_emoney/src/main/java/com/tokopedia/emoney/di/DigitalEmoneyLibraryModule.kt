package com.tokopedia.emoney.di

import com.tokopedia.emoney.integration.BCALibraryIntegration
import com.tokopedia.emoney.integration.BCALibraryIntegrationImpl
import dagger.Binds
import dagger.Module

@Module
abstract class DigitalEmoneyLibraryModule {

    @Binds
    @DigitalEmoneyScope
    abstract fun libraryBCA(library: BCALibraryIntegrationImpl): BCALibraryIntegration
}
