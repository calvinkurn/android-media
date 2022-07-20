package com.tokopedia.shop_widget.mvc_locked_to_product.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor.Companion.getInstance
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.shop.common.di.ShopCommonModule
import com.tokopedia.shop_widget.mvc_locked_to_product.di.scope.MvcLockedToProductScope
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module(includes = [ShopCommonModule::class, MvcLockedToProductViewModelModule::class])
class MvcLockedToProductModule {

    @MvcLockedToProductScope
    @Provides
    fun provideGqlRepository(): GraphqlRepository {
        return getInstance().graphqlRepository
    }

    @MvcLockedToProductScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

}