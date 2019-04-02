package com.tokopedia.tokocash.common.di;

import android.content.Context;
import android.text.InputFilter;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.tokocash.TokoCashRouter;
import com.tokopedia.tokocash.WalletUserSession;
import com.tokopedia.tokocash.accountsetting.data.AccountSettingRepository;
import com.tokopedia.tokocash.accountsetting.domain.GetOAuthInfoTokoCashUseCase;
import com.tokopedia.tokocash.accountsetting.domain.PostUnlinkTokoCashUseCase;
import com.tokopedia.tokocash.activation.data.ActivateRepository;
import com.tokopedia.tokocash.activation.domain.GetRefreshWalletTokenUseCase;
import com.tokopedia.tokocash.activation.domain.LinkedTokoCashUseCase;
import com.tokopedia.tokocash.activation.domain.RequestOtpTokoCashUseCase;
import com.tokopedia.tokocash.autosweepmf.data.source.cloud.api.AutoSweepApi;
import com.tokopedia.tokocash.autosweepmf.view.presenter.SetAutoSweepLimitPresenter;
import com.tokopedia.tokocash.autosweepmf.view.util.InputFilterMinMax;
import com.tokopedia.tokocash.balance.data.repository.BalanceRepository;
import com.tokopedia.tokocash.balance.domain.GetBalanceTokoCashUseCase;
import com.tokopedia.tokocash.common.WalletProvider;
import com.tokopedia.tokocash.common.WalletScheduler;
import com.tokopedia.tokocash.historytokocash.data.repository.WalletRepository;
import com.tokopedia.tokocash.historytokocash.domain.GetHistoryDataUseCase;
import com.tokopedia.tokocash.historytokocash.domain.MoveToSaldoUseCase;
import com.tokopedia.tokocash.network.WalletTokenRefresh;
import com.tokopedia.tokocash.network.api.TokoCashApi;
import com.tokopedia.tokocash.network.api.WalletApi;
import com.tokopedia.tokocash.network.api.WalletBalanceApi;
import com.tokopedia.tokocash.network.api.WalletUrl;
import com.tokopedia.tokocash.network.interceptor.TokoCashAuthInterceptor;
import com.tokopedia.tokocash.network.interceptor.TokoCashErrorResponseInterceptor;
import com.tokopedia.tokocash.network.interceptor.WalletAuthInterceptor;
import com.tokopedia.tokocash.network.interceptor.WalletErrorResponseInterceptor;
import com.tokopedia.tokocash.network.interceptor.WalletGqlAuthInterceptor;
import com.tokopedia.tokocash.network.model.ActivateTokoCashErrorResponse;
import com.tokopedia.tokocash.network.model.TokoCashErrorResponse;
import com.tokopedia.tokocash.network.model.WalletErrorResponse;
import com.tokopedia.tokocash.pendingcashback.domain.GetPendingCasbackUseCase;
import com.tokopedia.tokocash.qrpayment.data.repository.QrPaymentRepository;
import com.tokopedia.tokocash.qrpayment.domain.GetInfoQrTokoCashUseCase;
import com.tokopedia.tokocash.qrpayment.domain.PostQrPaymentUseCase;
import com.tokopedia.tokocash.tracker.WalletAnalytics;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by nabillasabbaha on 12/27/17.
 */
@Module
public class TokoCashModule {

    public TokoCashModule() {

    }

    @Provides
    WalletUserSession provideTokoCashSession(@ApplicationContext Context context) {
        if (context instanceof TokoCashRouter) {
            return ((TokoCashRouter) context).getTokoCashSession();
        }
        return null;
    }

    @Provides
    public TokoCashRouter provideTokoCashRouter(@ApplicationContext Context context) {
        if (context instanceof TokoCashRouter) {
            return ((TokoCashRouter) context);
        }
        throw new RuntimeException("App should implement " + TokoCashRouter.class.getSimpleName());
    }

    @Provides
    @TokoCashChuckQualifier
    public Interceptor provideChuckInterceptor(TokoCashRouter tokoCashRouter) {
        return tokoCashRouter.getChuckInterceptor();
    }

    @Provides
    @OkHttpTokoCashQualifier
    OkHttpClient provideOkHttpClient(TokoCashAuthInterceptor tokoCashAuthInterceptor, Gson gson,
                                     @TokoCashChuckQualifier Interceptor chuckIntereptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(tokoCashAuthInterceptor)
                .addInterceptor(new TokoCashErrorResponseInterceptor(TokoCashErrorResponse.class, gson))
                .addInterceptor(new TokoCashErrorResponseInterceptor(ActivateTokoCashErrorResponse.class, gson))
                .addInterceptor(chuckIntereptor).build();
    }

    @Provides
    @RetrofitTokoCashQualifier
    Retrofit provideRetrofit(Retrofit.Builder retrofitBuilder, @OkHttpTokoCashQualifier OkHttpClient okHttpClient) {
        return retrofitBuilder.baseUrl(WalletUrl.BaseUrl.ACCOUNTS_DOMAIN)
                .client(okHttpClient)
                .build();
    }

    @Provides
    TokoCashApi provideTokoCashApi(@RetrofitTokoCashQualifier Retrofit retrofit) {
        return retrofit.create(TokoCashApi.class);
    }

    @Provides
    WalletTokenRefresh provideWalletTokenRefresh(WalletUserSession walletUserSession, @RetrofitTokoCashQualifier Retrofit retrofit) {
        return new WalletTokenRefresh(walletUserSession, retrofit);
    }

    @Provides
    @OkHttpWalletQualifier
    OkHttpClient provideOkHttpClientWallet(WalletAuthInterceptor walletAuthInterceptor, Gson gson,
                                           WalletTokenRefresh walletTokenRefresh, WalletUserSession walletUserSession,
                                           @TokoCashChuckQualifier Interceptor chuckIntereptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(walletAuthInterceptor)
                .addInterceptor(new WalletErrorResponseInterceptor(WalletErrorResponse.class, gson,
                        walletTokenRefresh, walletUserSession))
                .addInterceptor(chuckIntereptor).build();
    }

    @Provides
    @RetrofitWalletQualifier
    Retrofit provideRetrofitWallet(Retrofit.Builder retrofitBuilder,
                                   @OkHttpWalletQualifier OkHttpClient okHttpClient, Gson gson) {
        return retrofitBuilder.baseUrl(WalletUrl.BaseUrl.WALLET_DOMAIN)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @Provides
    WalletApi provideWalletApi(@RetrofitWalletQualifier Retrofit retrofit) {
        return retrofit.create(WalletApi.class);
    }

    @Provides
    GetHistoryDataUseCase provideGetHistoryDataUseCase(WalletRepository walletRepository) {
        return new GetHistoryDataUseCase(walletRepository);
    }

    @Provides
    GetInfoQrTokoCashUseCase provideGetInfoQrTokoCashUseCase(QrPaymentRepository qrPaymentRepository) {
        return new GetInfoQrTokoCashUseCase(qrPaymentRepository);
    }

    @Provides
    PostQrPaymentUseCase providePostQrPaymentUseCase(QrPaymentRepository qrPaymentRepository) {
        return new PostQrPaymentUseCase(qrPaymentRepository);
    }

    @Provides
    GetBalanceTokoCashUseCase provideGetBalanceTokoCashUseCase(BalanceRepository tokoCashBalanceRepository) {
        return new GetBalanceTokoCashUseCase(tokoCashBalanceRepository);
    }

    @Provides
    @TokoCashScope
    LinkedTokoCashUseCase provideActivateTokoCashUseCase(ActivateRepository activateRepository) {
        return new LinkedTokoCashUseCase(activateRepository);
    }

    @Provides
    @TokoCashScope
    RequestOtpTokoCashUseCase provideRequestOtpTokoCashUseCase(ActivateRepository activateRepository) {
        return new RequestOtpTokoCashUseCase(activateRepository);
    }

    @Provides
    @TokoCashScope
    MoveToSaldoUseCase provideMoveToSaldoUseCase(WalletRepository walletRepository) {
        return new MoveToSaldoUseCase(walletRepository);
    }

    @Provides
    @TokoCashScope
    GetOAuthInfoTokoCashUseCase provideGetOAuthInfoTokoCashUseCase(AccountSettingRepository accountSettingRepository) {
        return new GetOAuthInfoTokoCashUseCase(accountSettingRepository);
    }

    @Provides
    @TokoCashScope
    PostUnlinkTokoCashUseCase providePostUnlinkTokoCashUseCase(AccountSettingRepository accountSettingRepository) {
        return new PostUnlinkTokoCashUseCase(accountSettingRepository);
    }

    @Provides
    UserSession provideUserSession(@ApplicationContext Context context) {
        return new UserSession(context);
    }

    @Provides
    public NetworkRouter provideNetworkRouter(@ApplicationContext Context context) {
        if (context instanceof NetworkRouter) {
            return ((NetworkRouter) context);
        }
        throw new RuntimeException("Application must implement " + NetworkRouter.class.getCanonicalName());
    }

    @Provides
    public WalletGqlAuthInterceptor provideWalletGqlAuthInterceptor(@ApplicationContext Context context,
                                                                    NetworkRouter networkRouter,
                                                                    UserSession userSession) {
        return new WalletGqlAuthInterceptor(context, networkRouter, userSession);
    }

    @Provides
    @OkHttpGqlTokocashQualifier
    OkHttpClient provideOkHttpGqlClientWallet(@TokoCashChuckQualifier Interceptor chuckIntereptor,
                                              WalletGqlAuthInterceptor walletGqlAuthInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(walletGqlAuthInterceptor)
                .addInterceptor(chuckIntereptor).build();
    }

    @Provides
    @RetrofitGqlTokocashQualifier
    Retrofit provideRetrofitGqlWallet(Retrofit.Builder retrofitBuilder, Gson gson,
                                      @OkHttpGqlTokocashQualifier OkHttpClient okHttpClient) {
        return retrofitBuilder.baseUrl(WalletUrl.BaseUrl.GQL_TOKOCASH_DOMAIN)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @Provides
    WalletBalanceApi provideWalletBalanceApi(@RetrofitGqlTokocashQualifier Retrofit retrofit) {
        return retrofit.create(WalletBalanceApi.class);
    }

    @Provides
    Context provideContext(@ApplicationContext Context context) {
        return context;
    }

    @Provides
    AutoSweepApi provideAutoSweepApi(@RetrofitGqlTokocashQualifier Retrofit retrofit) {
        return retrofit.create(AutoSweepApi.class);
    }

    @Provides
    InputFilter[] provideInputFilterForAutoSweepLimit(SetAutoSweepLimitPresenter presenter) {
        return new InputFilter[]{new InputFilterMinMax(0, (int) presenter.getAutoSweepMaxLimit())};
    }

    @TokoCashScope
    @Provides
    GetRefreshWalletTokenUseCase provideGetRefreshWalletTokenUseCase(ActivateRepository repository) {
        return new GetRefreshWalletTokenUseCase(repository);
    }

    @TokoCashScope
    @Provides
    WalletProvider provideWalletScheduler() {
        return new WalletScheduler();
    }

    @Provides
    WalletAnalytics provideWalletAnalytics(@ApplicationContext Context context) {
        if (context instanceof TokoCashRouter) {
            AnalyticTracker analyticTracker = ((TokoCashRouter) context).getAnalyticTracker();
            return new WalletAnalytics(analyticTracker);
        }
        throw new RuntimeException("App should implement " + TokoCashRouter.class.getSimpleName());
    }

    @TokoCashScope
    @Provides
    public UserSessionInterface provideUserSessionInterface(@ApplicationContext Context context) {
        return new UserSession(context);
    }
}