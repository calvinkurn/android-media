package com.tokopedia.usercomponents.explicit.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.usercomponents.common.stub.di.FakeAppModule
import com.tokopedia.usercomponents.explicit.fake_view.ExplicitDebugFragment
import com.tokopedia.usercomponents.explicit.view.ExplicitView
import com.tokopedia.usercomponents.explicit.view.interactor.ExplicitViewContract
import com.tokopedia.usercomponents.stickylogin.view.StickyLoginView
import dagger.Component
import dagger.Provides
import javax.inject.Singleton

@ActivityScope
@Component(modules = [
    FakeExplicitModule::class,
], dependencies = [FakeAppComponent::class])
interface FakeExplicitComponent {

    fun inject(fragment: ExplicitDebugFragment)
}

@ApplicationScope
@Component(
    modules = [FakeAppModule::class]
)
interface FakeAppComponent : BaseAppComponent