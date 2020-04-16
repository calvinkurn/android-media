package com.tokopedia.vouchercreation.di.module

import com.tokopedia.vouchercreation.di.scope.VoucherCreationScope
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

@Module
class VoucherCreationModule {
    @Named("Main")
    @VoucherCreationScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main
}