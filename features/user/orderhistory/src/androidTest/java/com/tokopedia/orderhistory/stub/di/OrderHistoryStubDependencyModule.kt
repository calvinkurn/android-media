package com.tokopedia.orderhistory.stub.di

import com.tokopedia.orderhistory.OrderHistoryAndroidTestCoroutineContextDispatcher
import com.tokopedia.orderhistory.di.OrderHistoryScope
import com.tokopedia.orderhistory.view.viewmodel.OrderHistoryCoroutineContextProvider
import dagger.Module
import dagger.Provides


@Module
class OrderHistoryStubDependencyModule {

    @OrderHistoryScope
    @Provides
    fun provideOrderHistoryContextProvider(): OrderHistoryCoroutineContextProvider {
        return OrderHistoryAndroidTestCoroutineContextDispatcher()
    }

}