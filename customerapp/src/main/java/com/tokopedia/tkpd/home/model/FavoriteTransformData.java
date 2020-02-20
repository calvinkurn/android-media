package com.tokopedia.tkpd.home.model;

import java.util.Map;

/**
 * Created by m.normansyah on 27/11/2015.
 */
public class FavoriteTransformData {
    Map<String, String> header;
    Map<String, String> content;

    public Map<String, String> getHeader() {
        return header;
    }

    public void setHeader(Map<String, String> header) {
        this.header = header;
    }

    public Map<String, String> getContent() {
        return content;
    }

    public void setContent(Map<String, String> content) {
        this.content = content;
    }

}
