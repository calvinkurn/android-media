package com.tokopedia.ordermanagement.orderhistory.purchase.detail.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.ordermanagement.orderhistory.purchase.detail.presentation.activity.OrderHistoryActivity
import com.tokopedia.user.session.UserSessionInterface
import dagger.Component

/**
 * Created by kris on 11/17/17. Tokopedia
 */
@OrderHistoryScope
@Component(modules = [OrderHistoryModule::class, OrderHistoryViewModelModule::class], dependencies = [BaseAppComponent::class])
interface OrderHistoryComponent {
    fun inject(activity: OrderHistoryActivity)
    fun userSession(): UserSessionInterface
    fun coroutineDispatchers(): CoroutineDispatchers
}