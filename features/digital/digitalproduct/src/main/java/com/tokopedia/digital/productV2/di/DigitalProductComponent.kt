package com.tokopedia.digital.productV2.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.digital.productV2.presentation.fragment.DigitalProductFragment
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.user.session.UserSessionInterface
import dagger.Component
import kotlinx.coroutines.CoroutineDispatcher

@DigitalProductScope
@Component(modules = [DigitalProductModule::class, DigitalProductViewModelModule::class], dependencies = [BaseAppComponent::class])
interface DigitalProductComponent {

    @ApplicationContext
    fun context(): Context

    fun userSessionInterface(): UserSessionInterface

    fun dispatcher(): CoroutineDispatcher

    fun graphQlRepository(): GraphqlRepository

    fun inject(digitalProductFragment: DigitalProductFragment)
}