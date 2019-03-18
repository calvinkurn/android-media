package com.tokopedia.groupchat.common.di.module;


import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.groupchat.common.di.scope.GroupChatScope;
import com.tokopedia.vote.di.VoteModule;

import dagger.Module;
import dagger.Provides;

/**
 * @author by nisie on 2/1/18.
 */

@GroupChatScope
@Module(includes = {GroupChatNetModule.class, VoteModule.class})
public class GroupChatModule {

}
