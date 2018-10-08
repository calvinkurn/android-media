package com.tokopedia.home.beranda.presentation.view.adapter.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel;
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory;

/**
 * Created by henrypriyono on 31/01/18.
 */

public class DynamicChannelViewModel implements Visitable<HomeTypeFactory> {

    private DynamicHomeChannel.Channels channel;

    public DynamicHomeChannel.Channels getChannel() {
        return channel;
    }

    public void setChannel(DynamicHomeChannel.Channels channel) {
        this.channel = channel;
    }

    @Override
    public int type(HomeTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
