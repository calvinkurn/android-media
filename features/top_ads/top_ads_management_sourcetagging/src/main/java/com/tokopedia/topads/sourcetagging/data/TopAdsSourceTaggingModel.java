package com.tokopedia.topads.sourcetagging.data;

import com.tokopedia.topads.sourcetagging.constant.TopAdsSourceOption;

/**
 * Created by hadi.putra on 17/04/18.
 */

public class TopAdsSourceTaggingModel {
    private @TopAdsSourceOption String source;
    private long timestamp;

    public TopAdsSourceTaggingModel(String source, long timestamp) {
        this.source = source;
        this.timestamp = timestamp;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
