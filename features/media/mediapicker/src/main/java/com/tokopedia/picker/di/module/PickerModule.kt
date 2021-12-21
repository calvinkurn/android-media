package com.tokopedia.picker.di.module

import android.content.Context
import com.tokopedia.picker.data.repository.FileLoaderRepository
import com.tokopedia.picker.data.repository.FileLoaderRepositoryImpl
import com.tokopedia.picker.di.scope.PickerScope
import dagger.Module
import dagger.Provides

@Module
class PickerModule constructor(
    private val context: Context
) {

    @Provides
    @PickerScope
    fun provideFilesLoaderRepository(): FileLoaderRepository {
        return FileLoaderRepositoryImpl(context)
    }

}