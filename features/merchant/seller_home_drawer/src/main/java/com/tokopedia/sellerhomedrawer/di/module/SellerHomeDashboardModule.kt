package com.tokopedia.sellerhomedrawer.di.module

import android.content.Context
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.sellerhomedrawer.di.SellerHomeDashboardScope
import com.tokopedia.sellerhomedrawer.domain.usecase.FlashSaleGetSellerStatusUseCase
import com.tokopedia.sellerhomedrawer.domain.usecase.GetShopStatusUseCase
import com.tokopedia.sellerhomedrawer.presentation.view.presenter.SellerHomeDashboardDrawerPresenter
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@SellerHomeDashboardScope
@Module(includes = [SellerHomeDashboardQueryModule::class])
class SellerHomeDashboardModule(val context: Context) {

    @Provides
    fun providePresenter(getShopStatusUseCase: GetShopStatusUseCase,
                         flashSaleGetSellerStatusUseCase: FlashSaleGetSellerStatusUseCase,
                         userSession: UserSessionInterface): SellerHomeDashboardDrawerPresenter =
            SellerHomeDashboardDrawerPresenter(getShopStatusUseCase, flashSaleGetSellerStatusUseCase, userSession)

    @Provides
    fun provideGraphQlUseCase(): GraphqlUseCase = GraphqlUseCase()

    @Provides
    fun provideUserSession(): UserSessionInterface = UserSession(context)

    @Provides
    fun provideContext(): Context = context
}
