package com.tokopedia.digital.home.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.digital.home.presentation.activity.DigitalHomePageActivity
import com.tokopedia.digital.home.presentation.fragment.DigitalHomePageFragment
import com.tokopedia.digital.home.presentation.fragment.DigitalHomePageSearchFragment
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.user.session.UserSessionInterface
import dagger.Component
import kotlinx.coroutines.CoroutineDispatcher

@DigitalHomePageScope
@Component(modules = [DigitalHomePageModule::class, DigitalHomePageViewModelModule::class], dependencies = [BaseAppComponent::class])
interface DigitalHomePageComponent {

    @ApplicationContext
    fun context(): Context

    fun userSessionInterface(): UserSessionInterface

    fun dispatcher(): CoroutineDispatcher

    fun graphQlRepository(): GraphqlRepository

    fun inject(digitalHomePageFragment: DigitalHomePageFragment)

    fun inject(digitalHomePageSearchFragment: DigitalHomePageSearchFragment)
}