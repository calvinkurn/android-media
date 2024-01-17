package com.tokopedia.shareexperience.data.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.shareexperience.ui.ShareExBottomSheet
import com.tokopedia.shareexperience.ui.util.ShareExInitializer
import com.tokopedia.shareexperience.ui.view.ShareExLoadingDialog
import dagger.Component

@ActivityScope
@Component(
    modules = [
        ShareExViewModelModule::class,
        ShareExUseCaseModule::class,
        ShareExModule::class
    ],
    dependencies = [BaseAppComponent::class]
)
interface ShareExComponent {
    fun inject(bottomSheet: ShareExBottomSheet)
    fun inject(initializer: ShareExInitializer)
    fun inject(dialog: ShareExLoadingDialog)
}
