package com.tokopedia.promotionstarget.data.di.components

import com.tokopedia.promotionstarget.data.di.modules.AppModule
import com.tokopedia.promotionstarget.data.di.modules.DispatcherModule
import com.tokopedia.promotionstarget.data.di.modules.GqlModule
import com.tokopedia.promotionstarget.data.di.modules.GratiffPresenterModule
import com.tokopedia.promotionstarget.data.di.scopes.CmGratifPresnterScope
import com.tokopedia.promotionstarget.domain.presenter.GratifThankYouUseCase
import com.tokopedia.promotionstarget.domain.presenter.GratificationPresenter
import dagger.Component

@CmGratifPresnterScope
@Component(modules = [GratiffPresenterModule::class, AppModule::class, GqlModule::class, DispatcherModule::class])
interface CmGratificationPresenterComponent {

    fun inject(presenter: GratificationPresenter)
    fun inject(usecase: GratifThankYouUseCase)
}