package com.tokopedia.settingbank.banklist.v2.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.settingbank.banklist.v2.data.SettingBankApi
import com.tokopedia.settingbank.banklist.v2.data.SettingBankUrl
import com.tokopedia.settingbank.banklist.v2.view.adapter.BankAccountListAdapter
import com.tokopedia.settingbank.banklist.v2.view.adapter.BankListAdapter
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@SettingBankScope
@Module
class SettingBankModule {

    @SettingBankScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @SettingBankScope
    @Provides
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @SettingBankScope
    @Provides
    fun provideBankAccountAdapter(): BankAccountListAdapter = BankAccountListAdapter(arrayListOf())
    @SettingBankScope
    @Provides
    fun provideBankAdapter(): BankListAdapter = BankListAdapter(arrayListOf())


    @SettingBankScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @SettingBankScope
    @Provides
    fun provideSettingBankRetrofit(retrofitBuilder: Retrofit.Builder,
                                   okHttpClient: OkHttpClient): Retrofit {
        return retrofitBuilder.baseUrl(SettingBankUrl.BASE_URL).client(okHttpClient).build()
    }

    @SettingBankScope
    @Provides
    fun provideSettingBankApi(retrofit: Retrofit): SettingBankApi {
        return retrofit.create(SettingBankApi::class.java)
    }

}
