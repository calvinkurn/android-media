package com.tokopedia.usercomponents.explicit.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.usercomponents.explicit.di.module.ExplicitViewModelModule
import com.tokopedia.usercomponents.explicit.view.ExplicitView
import dagger.Component

@ActivityScope
@Component(
    modules = [
        ExplicitViewModelModule::class
    ], dependencies = [BaseAppComponent::class]
)
interface ExplicitComponent {
    fun inject(view: ExplicitView)
}