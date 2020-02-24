package com.tokopedia.sellerhomedrawer.di.module

import android.app.Activity
import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.sellerhomedrawer.data.constant.SellerHomeParamConstant
import com.tokopedia.sellerhomedrawer.di.SellerHomeDashboardScope
import com.tokopedia.sellerhomedrawer.domain.datamanager.SellerDrawerDataManager
import com.tokopedia.sellerhomedrawer.domain.datamanager.SellerDrawerDataManagerImpl
import com.tokopedia.sellerhomedrawer.domain.usecase.FlashSaleGetSellerStatusUseCase
import com.tokopedia.sellerhomedrawer.domain.usecase.GetSellerHomeUserAttributesUseCase
import com.tokopedia.sellerhomedrawer.domain.usecase.GetShopStatusUseCase
import com.tokopedia.sellerhomedrawer.domain.usecase.SellerTokoCashUseCase
import com.tokopedia.sellerhomedrawer.presentation.listener.SellerDrawerDataListener
import com.tokopedia.sellerhomedrawer.presentation.view.drawer.SellerDrawerPresenterActivity
import com.tokopedia.sellerhomedrawer.presentation.view.helper.SellerDrawerHelper
import com.tokopedia.sellerhomedrawer.presentation.view.presenter.SellerHomeDashboardDrawerPresenter
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import javax.inject.Named

@SellerHomeDashboardScope
@Module(includes = [SellerHomeDashboardQueryModule::class, SellerHomeDashboardUseCaseModule::class])
class SellerHomeDashboardModule(private val activity: SellerDrawerPresenterActivity) {

    @SellerHomeDashboardScope
    @Provides
    fun providePresenter(getShopStatusUseCase: GetShopStatusUseCase,
                         flashSaleGetSellerStatusUseCase: FlashSaleGetSellerStatusUseCase,
                         userSession: UserSessionInterface,
                         @ApplicationContext context: Context): SellerHomeDashboardDrawerPresenter =
            SellerHomeDashboardDrawerPresenter(getShopStatusUseCase, flashSaleGetSellerStatusUseCase, userSession, context)

    @SellerHomeDashboardScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @SellerHomeDashboardScope
    @Provides
    fun provideDrawerCache(@ApplicationContext context: Context): LocalCacheHandler =
            LocalCacheHandler(context, SellerDrawerHelper.DRAWER_CACHE)

    @SellerHomeDashboardScope
    @Provides
    fun provideRemoteConfig(@ApplicationContext context: Context): FirebaseRemoteConfigImpl =
            FirebaseRemoteConfigImpl(context)

    @SellerHomeDashboardScope
    @Provides
    fun provideSellerDrawerDataManagerImpl(
            @ApplicationContext context: Context,
            sellerTokoCashUseCase: SellerTokoCashUseCase,
            getSellerHomeUserAttributesUseCase: GetSellerHomeUserAttributesUseCase,
            @Named(SellerHomeParamConstant.SELLER_DRAWER_DATA_QUERY) query: String
    ): SellerDrawerDataManager =
            SellerDrawerDataManagerImpl(
                    context,
                    activity,
                    sellerTokoCashUseCase,
                    getSellerHomeUserAttributesUseCase,
                    query)

    @SellerHomeDashboardScope
    @Provides
    @ApplicationContext
    fun provideContext(): Context = activity.applicationContext

    @SellerHomeDashboardScope
    @Provides
    fun provideActivity(): Activity = activity

}
