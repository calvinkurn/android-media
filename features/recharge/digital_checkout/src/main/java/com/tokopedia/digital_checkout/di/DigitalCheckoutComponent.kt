package com.tokopedia.digital_checkout.di

import com.tokopedia.common_digital.common.di.DigitalCommonComponent
import com.tokopedia.digital_checkout.presentation.fragment.DigitalCartFragment
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import dagger.Component
import kotlinx.coroutines.CoroutineDispatcher

/**
 * @author by jessica on 07/01/21
 */

@DigitalCheckoutScope
@Component(modules = [DigitalCheckoutModule::class, DigitalCheckoutViewModelModule::class],
        dependencies = [DigitalCommonComponent::class])
interface DigitalCheckoutComponent {

    fun inject(digitalCartFragment: DigitalCartFragment)

    fun graphQlRepository(): GraphqlRepository

    fun coroutineDispatcher(): CoroutineDispatcher
}