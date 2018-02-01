package com.tokopedia.tkpdchat.common.di.module;

import com.tokopedia.tkpdchat.common.di.ChatScope;

import dagger.Module;

/**
 * @author by nisie on 2/1/18.
 */

@ChatScope
@Module(includes = {NetChatModule.class})
public class ChatModule {
}
