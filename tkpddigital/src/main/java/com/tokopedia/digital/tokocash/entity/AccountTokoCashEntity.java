package com.tokopedia.digital.tokocash.entity;

/**
 * Created by nabillasabbaha on 10/27/17.
 */

public class AccountTokoCashEntity {

    private String client_id;
    private String identifier;
    private String img_url;
    private String auth_date_fmt;

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getAuth_date_fmt() {
        return auth_date_fmt;
    }

    public void setAuth_date_fmt(String auth_date_fmt) {
        this.auth_date_fmt = auth_date_fmt;
    }
}
