package com.tokopedia.tkpd.beranda.data.source.pojo;

import com.google.gson.annotations.Expose;

/**
 * Created by henrypriyono on 26/01/18.
 */

public class Slides {
    @Expose
    private String redirect_url;

    @Expose
    private String id;

    @Expose
    private String title;

    @Expose
    private String image_url;

    @Expose
    private String applink;

    public String getRedirect_url() {
        return redirect_url;
    }

    public void setRedirect_url(String redirect_url) {
        this.redirect_url = redirect_url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getApplink() {
        return applink;
    }

    public void setApplink(String applink) {
        this.applink = applink;
    }
}
