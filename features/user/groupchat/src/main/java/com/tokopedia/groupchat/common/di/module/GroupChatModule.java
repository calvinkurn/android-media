package com.tokopedia.groupchat.common.di.module;


import android.content.Context;

import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.interceptor.AccountsAuthorizationInterceptor;
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.groupchat.common.di.scope.GroupChatScope;
import com.tokopedia.vote.data.VoteApi;
import com.tokopedia.vote.data.VoteUrl;
import com.tokopedia.vote.di.VoteModule;
import com.tokopedia.vote.di.VoteQualifier;
import com.tokopedia.vote.domain.mapper.SendVoteMapper;
import com.tokopedia.vote.domain.source.VotingSource;
import com.tokopedia.vote.domain.usecase.SendVoteUseCase;
import com.tokopedia.vote.network.VoteErrorInterceptor;
import com.tokopedia.vote.network.VoteErrorResponse;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * @author by nisie on 2/1/18.
 */

@GroupChatScope
@Module(includes = {GroupChatNetModule.class, VoteModule.class})
public class GroupChatModule {

    @GroupChatScope
    @Provides
    public AnalyticTracker provideAnalyticTracker(@ApplicationContext Context context) {
        if (context instanceof AbstractionRouter) {
            return ((AbstractionRouter) context).getAnalyticTracker();
        }
        throw new RuntimeException("App should implement " + AbstractionRouter.class.getSimpleName());
    }

}
