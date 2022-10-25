package com.tokopedia.kol.feature.postdetail.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.feedcomponent.di.FeedComponentViewModelModule
import com.tokopedia.kol.feature.postdetail.di.module.ContentDetailModule
import com.tokopedia.kol.feature.postdetail.di.module.ContentDetailRepositoryModule
import com.tokopedia.kol.feature.postdetail.di.module.ContentDetailViewModelModule
import com.tokopedia.kol.feature.postdetail.view.fragment.ContentDetailFragment
import dagger.Component

/**
 * Created by meyta.taliti on 02/08/22.
 */
@ContentDetailScope
@Component(
    modules = [
        ContentDetailModule::class,
        ContentDetailViewModelModule::class,
        ContentDetailRepositoryModule::class,
        FeedComponentViewModelModule::class,
    ],
    dependencies = [BaseAppComponent::class]
)
interface ContentDetailComponent {

    fun inject(fragment: ContentDetailFragment)

}
