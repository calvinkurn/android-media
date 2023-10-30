package com.tokopedia.centralizedpromo.di.module

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.CENTRALIZED_PROMO_PREF
import com.tokopedia.centralizedpromo.di.scope.CentralizedPromoScope
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class CentralizedPromoModule {
    @CentralizedPromoScope
    @Named(CENTRALIZED_PROMO_PREF)
    @Provides
    fun provideCentralizePromoSharedPreference(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return context.getSharedPreferences(CENTRALIZED_PROMO_PREF, Context.MODE_PRIVATE)
    }

    @CentralizedPromoScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @CentralizedPromoScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @CentralizedPromoScope
    @Provides
    fun provideVoucherCreationSharedPref(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(VOUCHER_CREATION_PREF_NAME, Context.MODE_PRIVATE)
    }

    companion object {
        private const val VOUCHER_CREATION_PREF_NAME = "voucher_creation"
    }

}
