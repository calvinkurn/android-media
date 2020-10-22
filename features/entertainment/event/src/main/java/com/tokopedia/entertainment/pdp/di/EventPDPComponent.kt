package com.tokopedia.entertainment.pdp.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.entertainment.pdp.fragment.*
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import dagger.Component
import kotlinx.coroutines.CoroutineDispatcher

/**
 * Author firman 06-04-20
 */
@EventPDPScope
@Component(modules = [EventPDPModule::class], dependencies = [BaseAppComponent::class])
interface EventPDPComponent {
    @ApplicationContext
    fun context(): Context


    fun dispatcher(): CoroutineDispatcher

    fun graphQlRepository(): GraphqlRepository

    fun inject(eventPDPFragment: EventPDPFragment)
    fun inject(eventPDPTicketFragment: EventPDPTicketFragment)
    fun inject(eventCheckoutFragment: EventCheckoutFragment)
    fun inject(eventPDPFormFragment: EventPDPFormFragment)
    fun inject(eventRedeemFragment: EventRedeemFragment)
}