package com.tokopedia.topads.common.data;

import com.tokopedia.topads.common.constant.TopAdsConstant;
import com.tokopedia.topads.common.constant.TopAdsSourceOption;

import java.util.Date;

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

    public TopAdsSourceTaggingModel(String formatedSource) {
        if (!formatedSource.contains(TopAdsConstant.SEPARATOR)) {
            throw new IllegalArgumentException("Wrong format in parameter. \""
                    +TopAdsConstant.SEPARATOR+"\" not present in parameter");
        }

        String[] tmp = formatedSource.split(TopAdsConstant.SEPARATOR);
        source = tmp[0];
        try {
            timestamp = Long.parseLong(tmp[1]);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

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

    @Override
    public String toString() {
        return source+ TopAdsConstant.SEPARATOR+timestamp;
    }
}
