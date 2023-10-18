package com.tokopedia.shop.settings.common.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.imageuploader.di.ImageUploaderModule
import com.tokopedia.shop.common.di.ShopCommonModule
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

/**
 * @author by furqan on 21/03/18.
 */
@Module(includes = [ImageUploaderModule::class, ShopSettingsInfoViewModelModule::class, ShopCommonModule::class])
class ShopSettingsModule {

    @Provides
    @ShopSettingsScope
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    @ShopSettingsScope
    fun provideMultiRequestGraphqlUseCase() = GraphqlInteractor.getInstance().multiRequestGraphqlUseCase

    @Provides
    @ShopSettingsScope
    fun provideGqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }
}
