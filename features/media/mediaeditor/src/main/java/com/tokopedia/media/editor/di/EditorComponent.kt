package com.tokopedia.media.editor.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.media.editor.di.module.EditorFragmentModule
import com.tokopedia.media.editor.ui.activity.detail.DetailEditorActivity
import com.tokopedia.media.editor.ui.activity.main.EditorActivity
import dagger.Component

@ActivityScope
@Component(modules = [
    EditorFragmentModule::class,
], dependencies = [
    BaseAppComponent::class
])
interface EditorComponent {
    fun inject(activity: EditorActivity)
    fun inject(activity: DetailEditorActivity)
}