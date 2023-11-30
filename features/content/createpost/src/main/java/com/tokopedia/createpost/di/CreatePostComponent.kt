package com.tokopedia.createpost.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.affiliatecommon.analytics.AffiliateAnalytics
import com.tokopedia.content.common.di.ContentFragmentFactoryModule
import com.tokopedia.createpost.common.analyics.CreatePostAnalytics
import com.tokopedia.createpost.common.di.CreatePostScope
import com.tokopedia.content.common.producttag.di.module.ContentCreationProductTagBindModule
import com.tokopedia.content.common.producttag.di.module.ContentCreationProductTagModule
import com.tokopedia.createpost.common.di.ActivityContext
import com.tokopedia.createpost.view.activity.CreatePostActivityNew
import com.tokopedia.createpost.view.activity.ProductTagActivity
import com.tokopedia.createpost.view.fragment.BaseCreatePostFragmentNew
import com.tokopedia.creation.common.upload.di.uploader.CreationUploaderComponent
import dagger.BindsInstance
import dagger.Component

/**
 * @author by milhamj on 9/26/18.
 */
@CreatePostScope
@Component(
    modules = [
        CreatePostModule::class,
        ViewModelModule::class,
        ContentCreationProductTagBindModule::class,
        ContentCreationProductTagModule::class,
        ContentFragmentFactoryModule::class,
    ],
    dependencies = [
        BaseAppComponent::class,
        CreationUploaderComponent::class,
    ]
)
interface CreatePostComponent {

    fun provideAffiliateAnalytics(): AffiliateAnalytics

    fun provideCreatePostAnalytics(): CreatePostAnalytics

    fun inject(baseCreatePostFragmentNew: BaseCreatePostFragmentNew)
    fun inject(createPostActivityNew: CreatePostActivityNew)
    fun inject(productTagActivity: ProductTagActivity)

    @Component.Factory
    interface Factory {
        fun create(
            baseAppComponent: BaseAppComponent,
            creationUploaderComponent: CreationUploaderComponent,
            @BindsInstance @ActivityContext context: Context
        ): CreatePostComponent
    }
}
