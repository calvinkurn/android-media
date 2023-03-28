package com.tokopedia.mvc.di.module

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.mvc.di.scope.MerchantVoucherCreationScope
import com.tokopedia.mvc.util.constant.CommonConstant.SELLER_MVC_SHARED_PREF_NAME
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.permission.PermissionCheckerHelper
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
class MerchantVoucherCreationModule {
    @MerchantVoucherCreationScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @MerchantVoucherCreationScope
    @Provides
    fun provideGraphqlRepository() = GraphqlInteractor.getInstance().graphqlRepository

    @MerchantVoucherCreationScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @MerchantVoucherCreationScope
    @Provides
    fun providePermissionCheckerHelper(): PermissionCheckerHelper = PermissionCheckerHelper()

    @MerchantVoucherCreationScope
    @Provides
    internal fun provideMvcSharedPrefs(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(SELLER_MVC_SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }
}
