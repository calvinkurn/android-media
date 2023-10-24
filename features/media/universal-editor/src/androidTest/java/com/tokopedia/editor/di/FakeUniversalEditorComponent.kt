package com.tokopedia.editor.di

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.editor.di.module.EditorModule
import com.tokopedia.editor.di.module.EditorRepositoryModule
import com.tokopedia.editor.di.module.EditorViewModelModule
import com.tokopedia.editor.di.module.FragmentEditorModule
import dagger.Component

@ActivityScope
@Component(
    modules = [
        EditorModule::class,
        EditorRepositoryModule::class,
        EditorViewModelModule::class,
        FragmentEditorModule::class,
    ], dependencies = [
        FakeBaseAppComponent::class
    ]
)
interface FakeUniversalEditorComponent : UniversalEditorComponent
