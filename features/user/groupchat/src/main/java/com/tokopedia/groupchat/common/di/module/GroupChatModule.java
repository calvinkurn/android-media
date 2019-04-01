package com.tokopedia.groupchat.common.di.module;


import com.tokopedia.groupchat.common.di.scope.GroupChatScope;
import com.tokopedia.vote.di.VoteModule;

import dagger.Module;

/**
 * @author by nisie on 2/1/18.
 */

@GroupChatScope
@Module(includes = {GroupChatNetModule.class, VoteModule.class})
public class GroupChatModule {

}
