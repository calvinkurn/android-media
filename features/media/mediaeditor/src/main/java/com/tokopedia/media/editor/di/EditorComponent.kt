package com.tokopedia.media.editor.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.media.editor.di.module.*
import com.tokopedia.media.editor.ui.activity.addtext.AddTextActivity
import com.tokopedia.media.editor.ui.activity.detail.DetailEditorActivity
import com.tokopedia.media.editor.ui.activity.main.EditorActivity
import com.tokopedia.media.editor.ui.fragment.bottomsheet.AddTextBackgroundBottomSheet
import dagger.Component

@ActivityScope
@Component(
    modules = [
        EditorModule::class,
        EditorColorProviderModule::class,
        EditorCommonModule::class,
        EditorNetworkModule::class,
        EditorFragmentModule::class,
        EditorViewModelModule::class
    ], dependencies = [
        BaseAppComponent::class
    ]
)
interface EditorComponent {
    fun inject(activity: EditorActivity)
    fun inject(activity: DetailEditorActivity)
    fun inject(activity: AddTextActivity)
    fun inject(fragment: AddTextBackgroundBottomSheet)
}
