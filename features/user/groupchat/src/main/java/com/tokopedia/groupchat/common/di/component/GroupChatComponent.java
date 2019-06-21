package com.tokopedia.groupchat.common.di.component;

import android.content.Context;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.groupchat.common.di.module.GroupChatModule;
import com.tokopedia.groupchat.common.di.scope.GroupChatScope;
import com.tokopedia.vote.data.VoteApi;

import dagger.Component;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * @author by nisie on 2/1/18.
 */

@GroupChatScope
@Component(modules = GroupChatModule.class, dependencies = BaseAppComponent.class)
public interface GroupChatComponent {
    @ApplicationContext
    Context getApplicationContext();

    OkHttpClient provideOkHttpClient();

    Retrofit.Builder retrofitBuilder();

    void inject(BaseDaggerFragment baseChatActivity);

    VoteApi provideVoteApi();
}
