package com.tokopedia.media.editor.di.module

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.media.editor.data.tool.ColorFilterManager
import com.tokopedia.media.editor.data.tool.ColorFilterManagerImpl
import dagger.Binds
import dagger.Module

@Module
abstract class EditorModule {

    @Binds
    @ActivityScope
    internal abstract fun bindColorFilterManager(colorFilter: ColorFilterManagerImpl): ColorFilterManager

}