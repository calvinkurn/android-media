package com.tokopedia.shareexperience.data.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.shareexperience.ui.ShareExBottomSheet
import com.tokopedia.shareexperience.ui.ShareExLoadingActivity
import com.tokopedia.shareexperience.ui.util.ShareExInitializer
import dagger.Component

@ActivityScope
@Component(
    modules = [
        ShareExViewModelModule::class,
        ShareExUseCaseModule::class,
        ShareExRepositoryModule::class,
        ShareExModule::class
    ],
    dependencies = [BaseAppComponent::class]
)
interface ShareExComponent {
    fun inject(activity: ShareExLoadingActivity)
    fun inject(bottomSheet: ShareExBottomSheet)
    fun inject(initializer: ShareExInitializer)
}
