package com.tokopedia.creation.common.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.creation.common.analytics.ContentCreationAnalytics
import com.tokopedia.creation.common.upload.di.uploader.CreationUploaderComponent
import dagger.BindsInstance
import dagger.Component

/**
 * Created By : Muhammad Furqan on 04/10/23
 */
@ContentCreationScope
@Component(modules = [ContentCreationModule::class], dependencies = [BaseAppComponent::class])
interface ContentCreationComponent {
    fun contentCreationFactory(): ViewModelProvider.Factory
    fun contentCreationAnalytics(): ContentCreationAnalytics

    @Component.Factory
    interface Factory {
        fun create(
            baseAppComponent: BaseAppComponent,
            @BindsInstance context: Context
        ): ContentCreationComponent
    }
}
