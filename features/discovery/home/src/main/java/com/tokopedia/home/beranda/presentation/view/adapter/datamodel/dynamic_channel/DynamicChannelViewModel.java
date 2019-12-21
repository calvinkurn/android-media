package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel;

import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel;
import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable;
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory;

import java.util.List;
import java.util.Map;

/**
 * Created by henrypriyono on 31/01/18.
 */

public class DynamicChannelViewModel implements HomeVisitable {

    private DynamicHomeChannel.Channels channel;

    private long serverTimeOffset;
    private boolean isCache;
    private Map<String, Object> trackingData;
    private List<Object> trackingDataForCombination;
    private boolean isCombined;

    @Override
    public boolean isCache() {
        return isCache;
    }

    @Override
    public String visitableId() {
        return channel.getId();
    }

    public void setCache(boolean cache) {
        isCache = cache;
    }

    public long getServerTimeOffset() {
        return serverTimeOffset;
    }

    public void setServerTimeOffset(long serverTimeOffset) {
        this.serverTimeOffset = serverTimeOffset;
    }

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

    @Override
    public void setTrackingData(Map<String, Object> trackingData) {
        this.trackingData = trackingData;
    }

    @Override
    public Map<String, Object> getTrackingData() {
        return trackingData;
    }

    @Override
    public List<Object> getTrackingDataForCombination() {
        return trackingDataForCombination;
    }

    @Override
    public void setTrackingDataForCombination(List<Object> trackingDataForCombination) {
        this.trackingDataForCombination = trackingDataForCombination;
    }

    @Override
    public boolean isTrackingCombined() {
        return isCombined;
    }

    @Override
    public void setTrackingCombined(boolean isCombined) {
        this.isCombined = isCombined;
    }
}
