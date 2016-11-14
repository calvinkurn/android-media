package com.tokopedia.core.home.favorite.model.params;

/**
 * @author Kulomady on 11/11/16.
 */

public class TopAddParams {

    private String userId;
    private String tkpdSessionId;
    private String xDevice;
    private String item;
    private String src;
    private String page;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTkpdSessionId() {
        return tkpdSessionId;
    }

    public void setTkpdSessionId(String tkpdSessionId) {
        this.tkpdSessionId = tkpdSessionId;
    }

    public String getxDevice() {
        return xDevice;
    }

    public void setxDevice(String xDevice) {
        this.xDevice = xDevice;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }
}
