package com.tokopedia.stories.creation.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.content.common.di.ContentCoachMarkSharedPrefModule
import com.tokopedia.content.common.di.ContentFragmentFactoryModule
import com.tokopedia.content.product.picker.seller.di.ProductPickerBindModule
import com.tokopedia.content.product.picker.seller.di.ProductPickerFragmentModule
import com.tokopedia.creation.common.upload.di.uploader.CreationUploaderComponent
import com.tokopedia.creation.common.upload.di.uploader.CreationUploaderModule
import com.tokopedia.stories.creation.view.activity.StoriesCreationActivity
import dagger.BindsInstance
import dagger.Component

/**
 * Created By : Jonathan Darwin on September 05, 2023
 */
@Component(
    modules = [
        StoriesCreationModule::class,
        StoriesCreationBindModule::class,
        StoriesCreationViewModelModule::class,
        ProductPickerFragmentModule::class,
        ProductPickerBindModule::class,
        ContentFragmentFactoryModule::class,
        ContentCoachMarkSharedPrefModule::class,
    ],
    dependencies = [
        BaseAppComponent::class,
        CreationUploaderComponent::class,
    ]
)
@StoriesCreationScope
interface StoriesCreationComponent {

    fun inject(activity: StoriesCreationActivity)

    @Component.Factory
    interface Factory {
        fun create(
            baseAppComponent: BaseAppComponent,
            creationUploaderComponent: CreationUploaderComponent,
            @BindsInstance context: Context
        ): StoriesCreationComponent
    }
}
