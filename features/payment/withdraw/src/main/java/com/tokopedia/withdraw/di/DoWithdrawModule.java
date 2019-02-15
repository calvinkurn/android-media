//package com.tokopedia.withdraw.di;
//
//import android.content.Context;
//
//import com.tokopedia.abstraction.AbstractionRouter;
//import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
//import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
//import com.tokopedia.user.session.UserSession;
//import com.tokopedia.withdraw.data.DepositActionApi;
//import com.tokopedia.withdraw.data.WithdrawUrl;
//import com.tokopedia.withdraw.domain.source.DoWithdrawSource;
//import com.tokopedia.withdraw.domain.usecase.DoWithdrawUseCase;
//
//import dagger.Module;
//import dagger.Provides;
//import okhttp3.OkHttpClient;
//import retrofit2.Retrofit;
//
///**
// * @author by StevenFredian on 30/07/18.
// */
//
//@DoWithdrawScope
//@Module
//public class DoWithdrawModule {
//
//    @DoWithdrawScope
//    @WithdrawQualifier
//    @Provides
//    public Retrofit provideRetrofit(Retrofit.Builder retrofitBuilder,
//                                        @WithdrawQualifier OkHttpClient okHttpClient) {
//        return retrofitBuilder.baseUrl(WithdrawUrl.URL_DEPOSIT_ACTION).client(okHttpClient).build();
//    }
//
//    @DoWithdrawScope
//    @Provides
//    public DepositActionApi provideDeposiActiontApi(@WithdrawQualifier Retrofit retrofit){
//        return retrofit.create(DepositActionApi.class);
//    }
//
//    @Provides
//    public DoWithdrawUseCase provideDoWithdrawUseCase(DoWithdrawSource source){
//        return new DoWithdrawUseCase(source);
//    }
//
//
//    @Provides
//    public UserSession provideUserSession(@ApplicationContext Context context){
//        return new UserSession(context);
//    }
//
//
//    @DoWithdrawScope
//    @Provides
//    public AnalyticTracker provideAnalyticTracker(@ApplicationContext Context context) {
//        if (context instanceof AbstractionRouter) {
//            return ((AbstractionRouter) context).getAnalyticTracker();
//        }
//        throw new RuntimeException("App should implement " + AbstractionRouter.class.getSimpleName());
//    }
//}
