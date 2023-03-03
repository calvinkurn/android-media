package com.tokopedia.search.mps

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.search.di.module.CartLocalCacheHandlerModule
import com.tokopedia.search.di.module.UserSessionModule
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.presentation.view.activity.SearchComponent
import dagger.Component

@SearchScope
@Component(
    modules = [
        FakeMPSViewModelModule::class,
        UserSessionModule::class,
        CartLocalCacheHandlerModule::class,
    ],
    dependencies = [BaseAppComponent::class]
)
internal interface MPSFragmentTestComponent: SearchComponent {

}
