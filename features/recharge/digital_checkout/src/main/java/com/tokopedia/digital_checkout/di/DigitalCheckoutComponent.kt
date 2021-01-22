package com.tokopedia.digital_checkout.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.digital_checkout.presentation.fragment.DigitalCartFragment
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.user.session.UserSessionInterface
import dagger.Component
import kotlinx.coroutines.CoroutineDispatcher

/**
 * @author by jessica on 07/01/21
 */

@DigitalCheckoutScope
@Component(modules = [DigitalCheckoutModule::class, DigitalCheckoutViewModelModule::class],
        dependencies = [BaseAppComponent::class])
interface DigitalCheckoutComponent {

    fun inject(digitalCartFragment: DigitalCartFragment)

    fun userSessionInterface(): UserSessionInterface

    fun graphQlRepository(): GraphqlRepository

    fun coroutineDispatcher(): CoroutineDispatcher
}