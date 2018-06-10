package com.tokopedia.groupchat.chatroom.di;

import com.tokopedia.groupchat.chatroom.data.ChatroomApi;
import com.tokopedia.groupchat.common.data.VoteApi;
import com.tokopedia.groupchat.common.di.qualifier.GroupChatQualifier;
import com.tokopedia.groupchat.common.di.qualifier.VoteQualifier;

import com.tokopedia.groupchat.common.data.GroupChatUrl;

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
    @VoteQualifier
    public Retrofit provideVoteRetrofit(Retrofit.Builder retrofitBuilder,
                                        OkHttpClient okHttpClient) {
        return retrofitBuilder.baseUrl(GroupChatUrl.BASE_URL).client(okHttpClient).build();
    }

    @ChatroomScope
    @Provides
    public VoteApi provideVoteApi(@VoteQualifier Retrofit retrofit) {
        return retrofit.create(VoteApi.class);
    }
}
