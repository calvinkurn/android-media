package com.tokopedia.groupchat.channel.di;

import com.tokopedia.groupchat.channel.view.fragment.ChannelFragment;
import com.tokopedia.groupchat.common.di.component.GroupChatComponent;

import dagger.Component;

/**
 * @author by nisie on 2/3/18.
 */
@ChannelScope
@Component(modules = {ChannelModule.class}, dependencies = GroupChatComponent.class)
public interface ChannelComponent {

    void inject(ChannelFragment fragment);

}
