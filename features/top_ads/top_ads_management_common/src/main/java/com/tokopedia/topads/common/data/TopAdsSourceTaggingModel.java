package com.tokopedia.topads.common.data;

import com.tokopedia.topads.common.constant.TopAdsConstant;
import com.tokopedia.topads.common.constant.TopAdsSourceOption;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * Created by hadi.putra on 17/04/18.
 */

public class TopAdsSourceTaggingModel {
    private @TopAdsSourceOption String source;
    private Date timestamp;
    private DateFormat dateFormat = DateFormat.getDateTimeInstance();

    public TopAdsSourceTaggingModel(String source, Date timestamp) {
        this.source = source;
        this.timestamp = timestamp;
    }

    public TopAdsSourceTaggingModel(String source, String timestamp) {
        this.source = source;
        try {
            this.timestamp = dateFormat.parse(timestamp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public TopAdsSourceTaggingModel(String formatedSource) {
        if (!formatedSource.contains(TopAdsConstant.SEPARATOR)) {
            throw new IllegalArgumentException("Wrong format in parameter. \""
                    +TopAdsConstant.SEPARATOR+"\" not present in parameter");
        }

        String[] tmp = formatedSource.split(TopAdsConstant.SEPARATOR);
        source = tmp[0];
        try {
            timestamp = dateFormat.parse(tmp[1]);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return source+ TopAdsConstant.SEPARATOR+dateFormat.format(timestamp);
    }
}
