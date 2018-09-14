package com.tokopedia.payment.setting.add.di

import android.content.Context
import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.payment.setting.add.domain.AddCreditCardUseCase
import com.tokopedia.payment.setting.add.view.presenter.AddCreditCardPresenter
import com.tokopedia.payment.setting.authenticate.di.AuthenticateCCScope
import com.tokopedia.user.session.UserSession
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor

@Module
class AddCreditCardModule {

    @AddCreditCardScope
    @Provides
    fun providePresenter(addCreditCardUseCase: AddCreditCardUseCase) : AddCreditCardPresenter {
        return AddCreditCardPresenter(addCreditCardUseCase)
    }

    @AddCreditCardScope
    @Provides
    fun productAddCreditCardUseCase(tkpdAuthInterceptor: ArrayList<Interceptor>, @ApplicationContext context: Context, userSession: UserSession) : AddCreditCardUseCase{
        return AddCreditCardUseCase(tkpdAuthInterceptor, context, userSession)
    }


    @AddCreditCardScope
    @Provides
    fun provideTkpdAuthInterceptor(@ApplicationContext context: Context, networkRouter: NetworkRouter, userSession: UserSession) : ArrayList<Interceptor>{
        val  listInterceptor =  ArrayList<Interceptor>()
        listInterceptor.add(TkpdAuthInterceptor(context, networkRouter, userSession))
        if(GlobalConfig.DEBUG){
            listInterceptor.add(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        }
        return listInterceptor
    }

    @AddCreditCardScope
    @Provides
    fun provideNetworkRouter(@ApplicationContext context: Context) : NetworkRouter{
        return (context as NetworkRouter)
    }

    @AddCreditCardScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context) : UserSession{
        val userSession = UserSession(context)
        return userSession
    }
}
