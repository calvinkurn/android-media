package com.tokopedia.groupchat.chatroom.di;

import com.tokopedia.groupchat.chatroom.data.ChatroomApi;
import com.tokopedia.groupchat.common.data.GroupChatUrl;
import com.tokopedia.groupchat.common.di.qualifier.GcpQualifier;
import com.tokopedia.groupchat.common.di.qualifier.GroupChatQualifier;
import com.tokopedia.vote.domain.source.VotingSource;
import com.tokopedia.vote.domain.usecase.SendVoteUseCase;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * @author by nisie on 2/15/18.
 */

@ChatroomScope
@Module
public class ChatroomModule {

    @ChatroomScope
    @Provides
    @GroupChatQualifier
    public Retrofit provideChatroomRetrofit(Retrofit.Builder retrofitBuilder,
                                            OkHttpClient okHttpClient) {
        return retrofitBuilder.baseUrl(GroupChatUrl.BASE_URL).client(okHttpClient).build();
    }

    @ChatroomScope
    @Provides
    public ChatroomApi provideChatroomApi(@GroupChatQualifier Retrofit retrofit) {
        return retrofit.create(ChatroomApi.class);
    }

    @ChatroomScope
    @Provides
    @GcpQualifier
    public Retrofit provideChatroomGCPRetrofit(Retrofit.Builder retrofitBuilder,
                                            OkHttpClient okHttpClient) {
        return retrofitBuilder.baseUrl(GroupChatUrl.BASE_GCP_URL).client(okHttpClient).build();
    }

    @ChatroomScope
    @Provides
    @GcpQualifier
    public ChatroomApi provideChatroomGCPApi(@GcpQualifier Retrofit retrofit) {
        return retrofit.create(ChatroomApi.class);
    }


    @ChatroomScope
    @Provides
    public SendVoteUseCase provideSendVoteUseCase(VotingSource votingSource){
        return new SendVoteUseCase(votingSource);
    }
}
