package com.tokopedia.vote.di;

import android.content.Context;

import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.interceptor.AccountsAuthorizationInterceptor;
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.vote.data.VoteApi;
import com.tokopedia.vote.data.VoteUrl;
import com.tokopedia.vote.domain.source.VotingSource;
import com.tokopedia.vote.domain.usecase.SendVoteUseCase;
import com.tokopedia.vote.network.VoteErrorInterceptor;
import com.tokopedia.vote.network.VoteErrorResponse;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * @author by nisie on 5/7/18.
 */

@Module
public class VoteModule {

    @Provides
    public VoteErrorInterceptor provideVoteErrorInterceptor() {
        return new VoteErrorInterceptor(VoteErrorResponse.class);
    }

    @VoteQualifier
    @Provides
    public AccountsAuthorizationInterceptor provideAccountsAuthorizationInterceptor(@ApplicationContext Context context) {
        return new AccountsAuthorizationInterceptor(context);
    }

    @VoteQualifier
    @Provides
    public ChuckInterceptor provideChuckInterceptor(@ApplicationContext Context context) {
        return new ChuckInterceptor(context).showNotification(GlobalConfig.isAllowDebuggingTools());
    }

    @VoteQualifier
    @Provides
    public OkHttpClient provideOkHttpClient(@VoteQualifier ChuckInterceptor chuckInterceptor,
                                            HttpLoggingInterceptor httpLoggingInterceptor,
                                            TkpdAuthInterceptor tkpdAuthInterceptor,
                                            @VoteQualifier AccountsAuthorizationInterceptor accountsAuthorizationInterceptor) {

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(new ErrorResponseInterceptor(VoteErrorResponse.class))
                .addInterceptor(tkpdAuthInterceptor)
                .addInterceptor(accountsAuthorizationInterceptor);

        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(chuckInterceptor)
                    .addInterceptor(httpLoggingInterceptor);
        }
        return builder.build();
    }

    @VoteQualifier
    @Provides
    public Retrofit provideVoteRetrofit(Retrofit.Builder retrofitBuilder,
                                        @VoteQualifier OkHttpClient okHttpClient) {
        return retrofitBuilder.baseUrl(VoteUrl.BASE_URL).client(okHttpClient).build();
    }

    @Provides
    public VoteApi provideVoteApi(@VoteQualifier Retrofit retrofit) {
        return retrofit.create(VoteApi.class);
    }

    @Provides
    public SendVoteUseCase provideSendVoteUseCase(VotingSource votingSource) {
        return new SendVoteUseCase(votingSource);
    }
}
