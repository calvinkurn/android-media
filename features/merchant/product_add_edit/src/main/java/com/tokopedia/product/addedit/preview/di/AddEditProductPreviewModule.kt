package com.tokopedia.product.addedit.preview.di

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module(includes = [AddEditProductPreviewViewModelModule::class])
@AddEditProductPreviewScope
class AddEditProductPreviewModule {
    @AddEditProductPreviewScope
    @Provides
    fun provideCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.Main
}
