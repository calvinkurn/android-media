package com.tokopedia.groupchat.channel.view.model;

import java.util.List;

/**
 * @author by nisie on 2/3/18.
 */

public class ChannelListViewModel {

    private final String cursor;
    List<ChannelViewModel> channelViewModelList;
    boolean hasNextPage;

    public ChannelListViewModel(List<ChannelViewModel> list, boolean hasNextPage, String cursor) {
        this.channelViewModelList = list;
        this.hasNextPage = hasNextPage;
        this.cursor = cursor;
    }

    public List<ChannelViewModel> getChannelViewModelList() {
        return channelViewModelList;
    }

    public void setChannelViewModelList(List<ChannelViewModel> channelViewModelList) {
        this.channelViewModelList = channelViewModelList;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public String getCursor() {
        return cursor;
    }
}
