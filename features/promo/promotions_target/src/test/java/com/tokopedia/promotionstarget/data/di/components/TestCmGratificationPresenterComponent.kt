package com.tokopedia.promotionstarget.data.di.components

import com.tokopedia.promotionstarget.data.di.modules.TestAppModule
import com.tokopedia.promotionstarget.data.di.modules.TestDispatcherModule
import com.tokopedia.promotionstarget.data.di.modules.TestGqlModule
import com.tokopedia.promotionstarget.data.di.modules.TestGratiffPresenterModule
import com.tokopedia.promotionstarget.data.di.scopes.CmGratifPresnterScope
import dagger.Component

@CmGratifPresnterScope
@Component(modules = [TestGratiffPresenterModule::class, TestAppModule::class, TestGqlModule::class, TestDispatcherModule::class])
interface TestCmGratificationPresenterComponent : CmGratificationPresenterComponent {
}