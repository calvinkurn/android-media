package com.tokopedia.instantloan.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.instantloan.di.scope.InstantLoanScope
import com.tokopedia.instantloan.domain.interactor.GetFilterDataUseCase
import com.tokopedia.instantloan.domain.interactor.GetLendingDataUseCase
import com.tokopedia.instantloan.domain.interactor.GetLoanProfileStatusUseCase
import com.tokopedia.instantloan.domain.interactor.PostPhoneDataUseCase
import com.tokopedia.instantloan.network.InstantLoanAuthInterceptor
import com.tokopedia.user.session.UserSession
import dagger.Module
import dagger.Provides

@InstantLoanScope
@Module
class InstantLoanModule {

    @Provides
    fun provideGetLoanProfileStatusUseCase(instantLoanAuthInterceptor: InstantLoanAuthInterceptor, @ApplicationContext context: Context): GetLoanProfileStatusUseCase {
        return GetLoanProfileStatusUseCase(instantLoanAuthInterceptor, context)
    }

    @Provides
    fun provideGetLendingDataUseCase(@ApplicationContext context: Context) : GetLendingDataUseCase {
        return GetLendingDataUseCase(context)
    }

    @Provides
    fun provideGetFilterDataUseCase(@ApplicationContext context: Context) : GetFilterDataUseCase {
        return GetFilterDataUseCase(context)
    }

    @Provides
    fun providePostPhoneDataUseCase(instantLoanAuthInterceptor: InstantLoanAuthInterceptor, @ApplicationContext context: Context): PostPhoneDataUseCase {
        return PostPhoneDataUseCase(instantLoanAuthInterceptor, context)
    }

    @InstantLoanScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSession {
        return UserSession(context)
    }
}

