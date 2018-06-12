package com.tokopedia.vote.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.vote.data.VoteApi;
import com.tokopedia.vote.domain.source.VotingSource;
import com.tokopedia.vote.domain.usecase.SendVoteUseCase;

import dagger.Component;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * @author by nisie on 5/7/18.
 */
@Component(modules = VoteModule.class)
public interface VoteComponent {

//    @ApplicationContext
//    Context getApplicationContext();
//
//    OkHttpClient provideOkHttpClient();
//
//    Retrofit.Builder retrofitBuilder();
//
//    VoteApi provideVoteApi();
//
//    VotingSource provideVotingSource();
//
//    SendVoteUseCase provideSendVoteUseCase();

}
