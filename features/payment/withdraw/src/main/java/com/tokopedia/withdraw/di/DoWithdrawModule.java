package com.tokopedia.withdraw.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.withdraw.data.WithdrawUrl;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * @author by StevenFredian on 30/07/18.
 */

@DoWithdrawScope
@Module
public class DoWithdrawModule {

    @DoWithdrawScope
    @WithdrawQualifier
    @Provides
    public Retrofit provideRetrofit(Retrofit.Builder retrofitBuilder,
                                        @WithdrawQualifier OkHttpClient okHttpClient) {
        return retrofitBuilder.baseUrl(WithdrawUrl.URL_DEPOSIT_ACTION).client(okHttpClient).build();
    }
//
//    @DoWithdrawScope
//    @Provides
//    public DepositApi provideDepositApi(@WithdrawQualifier Retrofit retrofit){
//        return retrofit.create(DepositApi.class);
//    }
//
//    @Provides
//    public DepositUseCase provideDepositUseCase(DepositSource depositSource){
//        return new DepositUseCase(depositSource);
//    }


    @Provides
    public UserSession provideUserSession(@ApplicationContext Context context){
        return new UserSession(context);
    }
}
