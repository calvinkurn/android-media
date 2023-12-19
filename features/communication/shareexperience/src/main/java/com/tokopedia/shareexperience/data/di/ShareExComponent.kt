package com.tokopedia.shareexperience.data.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.shareexperience.ui.ShareExBottomSheet
import dagger.Component

@ActivityScope
@Component(
    modules = [ShareExViewModelModule::class],
    dependencies = [BaseAppComponent::class]
)
interface ShareExComponent {
    fun inject(bottomSheet: ShareExBottomSheet)
}
