package com.tokopedia.media.editor.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.media.editor.data.AddTextColorProvider
import com.tokopedia.media.editor.data.AddTextColorProviderImpl
import dagger.Module
import dagger.Provides

@Module
class EditorColorProviderModule constructor(private val context: Context) {
    @Provides
    @ActivityScope
    internal fun provideAddTextFilterRepository(): AddTextColorProvider {
        return AddTextColorProviderImpl(
            context
        )
    }
}
