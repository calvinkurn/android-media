package com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.beranda.data.source.pojo.DynamicHomeChannel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.factory.HomeTypeFactory;

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
