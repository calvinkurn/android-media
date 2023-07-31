package com.tokopedia.editor.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.editor.di.module.EditorModule
import com.tokopedia.editor.di.module.EditorViewModelModule
import com.tokopedia.editor.di.module.FragmentEditorModule
import com.tokopedia.editor.ui.main.MainEditorActivity
import dagger.Component

@ActivityScope
@Component(
    modules = [
        EditorModule::class,
        EditorViewModelModule::class,
        FragmentEditorModule::class,
    ], dependencies = [
        BaseAppComponent::class
    ]
)
interface UniversalEditorComponent {
    fun inject(activity: MainEditorActivity)
}
