package com.tokopedia.events.domain.model.scanticket;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UrlParams {

@SerializedName("app_url")
@Expose
private String appUrl;
@SerializedName("web_url")
@Expose
private String webUrl;
@SerializedName("method")
@Expose
private String method;

public String getAppUrl() {
return appUrl;
}

public void setAppUrl(String appUrl) {
this.appUrl = appUrl;
}

public String getWebUrl() {
return webUrl;
}

public void setWebUrl(String webUrl) {
this.webUrl = webUrl;
}

public String getMethod() {
return method;
}

public void setMethod(String method) {
this.method = method;
}

}